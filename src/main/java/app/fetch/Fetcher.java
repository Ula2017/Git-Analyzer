package app.fetch;

import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.joda.time.DateTime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Singleton
public class Fetcher {
    private Git git;
    private RepoDownloader gitDownloader;
    private List<CommitDetails> commitDetailsList;

    @Inject
    public Fetcher(RepoDownloader repoDownloader) {
        this.gitDownloader = repoDownloader;
        this.commitDetailsList = new ArrayList<>();
    }

    public void prepereDownloader(String url) {
        this.git = gitDownloader.getRepository(url);
    }

    public List<Ref> getAllBranch() {
        List<Ref> call = null;
        try {
            call = git.branchList().call();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        for (Ref ref : call) {
            System.out.println("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
        }
        return call;
    }

    public RepoDownloader getGitDownloader() {
        return gitDownloader;
    }

    public List<CommitDetails> getAllCommits() {
        if (commitDetailsList.isEmpty()) {
            this.commitDetailsList = generateCommitDetailList();
        }
        return this.commitDetailsList;
    }

    public List<CommitDetails> getCommitsFromDateRange(DateTime startDate, DateTime endDate) {
        return getAllCommits().stream().filter(d-> d.getCommitDate().isAfter(startDate) && d.getCommitDate().isBefore(endDate)).collect(Collectors.toList());
    }

    private List<CommitDetails> generateCommitDetailList() {
        try {
            for (RevCommit rev : git.log().call()) {
                CommitDetails newCommit = new CommitDetails(
                        new DateTime(rev.getAuthorIdent().getWhen()),
                        rev.getAuthorIdent().getName(),
                        rev.getShortMessage());

                if(rev.getParentCount()!=0){
                    List<DiffEntry> diffEntries = git.diff()
                            .setOldTree(getCanonicalTreeParser(rev.getParent(0)))
                            .setNewTree(getCanonicalTreeParser(rev))
                            .call();

                    for (DiffEntry diffEntry : diffEntries) {
                        int deletions;
                        int insertions;
                        DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
                        diffFormatter.setRepository(git.getRepository());
                        diffFormatter.setContext(0);
                        EditList edits = diffFormatter.toFileHeader(diffEntry).toEditList();

                        deletions = edits.stream().mapToInt(e -> e.getLengthA()).sum();
                        insertions = edits.stream().mapToInt(e -> e.getLengthB()).sum();

                        newCommit.addFile(new FileDiffs(diffEntry.getNewPath(), insertions, deletions));
                    }
                }
                else{
                    TreeWalk treeWalk = new TreeWalk(git.getRepository());
                    treeWalk.addTree(rev.getTree());
                    treeWalk.setRecursive(true);

                    while(treeWalk.next()) {
                        String path = treeWalk.getPathString();

                        ObjectId objectId = treeWalk.getObjectId(0);
                        ObjectLoader loader = git.getRepository().open(objectId);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        loader.copyTo(stream);

                        int insertions = IOUtils.readLines(new ByteArrayInputStream(stream.toByteArray()), "UTF-8").size();

                        newCommit.addFile(new FileDiffs(path, insertions, 0));

                    }
                    treeWalk.close();
                }

                this.commitDetailsList.add(newCommit);
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (CorruptObjectException e) {
            e.printStackTrace();
        } catch (MissingObjectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commitDetailsList;
    }

    private CanonicalTreeParser getCanonicalTreeParser(RevCommit revCommit) throws IOException {
        Repository repository = git.getRepository();
        try (RevWalk revWalk = new RevWalk(repository)) {
            RevTree revTree = revWalk.parseTree(revCommit.getTree().getId());

            CanonicalTreeParser canonicalTreeParser = new CanonicalTreeParser();
            try (ObjectReader objectReader = repository.newObjectReader()) {
                canonicalTreeParser.reset(objectReader, revTree.getId());
            }
            revWalk.dispose();
            return canonicalTreeParser;
        }
    }
}


