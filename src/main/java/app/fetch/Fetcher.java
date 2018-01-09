package app.fetch;

import app.gui.AbstractController;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.apache.commons.io.IOUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Singleton
public class Fetcher {

    private Git git;
    private Repository repository;
    private RepoDownloader gitDownloader;
    private List<CommitDetails> commitDetailsList;

    @Inject
    public Fetcher(RepoDownloader repoDownloader) {
        this.gitDownloader = repoDownloader;
        this.commitDetailsList = new ArrayList<>();
    }

    public void prepareDownloader(String url) {
        this.git = gitDownloader.getRepository(url);
        this.repository = git.getRepository();

    }

    public List<CommitDetails> getAllCommits() {
        if (commitDetailsList.isEmpty()) {
            this.commitDetailsList = generateCommitDetailList();
        }
        return this.commitDetailsList;
    }

    public List<CommitDetails> getCommitsFromDateRange(DateTime startDate, DateTime endDate) {
        return getAllCommits().stream().filter(d-> d.getCommitDate().isAfter(startDate)
                && d.getCommitDate().isBefore(endDate)).collect(Collectors.toList());
    }

    private List<CommitDetails> generateCommitDetailList() {
        try {
            for (RevCommit rev : git.log().call()) {
                Injector injector = AbstractController.injector;

                CommitDetails commit = injector.getInstance(CommitDetails.class);
                commit.setPrimaryInformation(new DateTime(rev.getAuthorIdent().getWhen()),
                        rev.getAuthorIdent().getName(),
                        rev.getShortMessage(), getCommitBranch(rev));

                if (rev.getParentCount() != 0) {
                    List<DiffEntry> diffEntries = git.diff()
                            .setOldTree(getCanonicalTreeParser(rev.getParent(0)))
                            .setNewTree(getCanonicalTreeParser(rev))
                            .call();

                    for (DiffEntry diffEntry : diffEntries) {
                        int deletions;
                        int insertions;
                        DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
                        diffFormatter.setRepository(repository);
                        diffFormatter.setContext(0);
                        EditList edits = diffFormatter.toFileHeader(diffEntry).toEditList();

                        deletions = edits.stream().mapToInt(Edit::getLengthA).sum();
                        insertions = edits.stream().mapToInt(Edit::getLengthB).sum();

                        FileDiffs fileDiffs = injector.getInstance(FileDiffs.class);
                        fileDiffs.setInformation(diffEntry.getNewPath(), insertions, deletions);
                        commit.addFile(fileDiffs);

                    }

                } else {
                    TreeWalk treeWalk = new TreeWalk(repository);
                    treeWalk.addTree(rev.getTree());
                    treeWalk.setRecursive(true);

                    while (treeWalk.next()) {
                        String path = treeWalk.getPathString();

                        ObjectId objectId = treeWalk.getObjectId(0);
                        ObjectLoader loader = repository.open(objectId);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        loader.copyTo(stream);

                        int insertions = IOUtils.readLines(new ByteArrayInputStream(stream.toByteArray()), "UTF-8").size();

                        FileDiffs fileDiffs = injector.getInstance(FileDiffs.class);
                        fileDiffs.setInformation(path, insertions, 0);
                        commit.addFile(fileDiffs);

                    }
                    treeWalk.close();
                }

                this.commitDetailsList.add(commit);
            }
        } catch (IOException | GitAPIException e) {
            System.err.println("Error during creating Commits Details.");
            //return?
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return commitDetailsList;
    }

    private CanonicalTreeParser getCanonicalTreeParser(RevCommit revCommit) throws IOException {
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

    private String getCommitBranch(RevCommit co) throws Exception {
        List<Ref> call;
        String branchName;
        try {

            call = git.branchList().call();
            for (Ref ref : call) {
                branchName = ref.getName();
                for (RevCommit commit : git.log().add(repository.resolve(branchName)).call()) {
                    if (commit.getName().equals(co.getName())) {
                        return branchName;

                    }
                }
            }
        } catch (GitAPIException e) {
            throw new Exception("Problem occurred getting branch list from Git.");
        } catch (IOException e) {
            throw new Exception("Problem occurred getting commit list from branch.");
        }

        return null;
    }

    public Map<String, Integer> getBranchCommit(){
        List<Ref> call;
        Map<String, Integer> map = new HashMap<>();
        try {
            call = git.branchList().call();
            for (Ref ref : call) {
                int i =0;
                for (RevCommit commit : git.log().add(repository.resolve(ref.getName())).call()) {
                    i++;
                }
                map.put(ref.getName(), i);
            }

        } catch (GitAPIException | IOException e) {
            System.err.println("Problem occured getting amount of commit per branch. ");
        }
        return map;
    }


}


