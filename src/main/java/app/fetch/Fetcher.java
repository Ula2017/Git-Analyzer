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
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
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
import java.io.OutputStream;
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

    }

    public void prepareDownloader(String url) throws Exception {
        this.commitDetailsList = new ArrayList<>();
        this.git = gitDownloader.getRepository(url);
        this.repository = git.getRepository();

    }
    public void closeGit(){
        this.repository.close();
        this.git.close();
    }

    public Git getGit() {
        return git;
    }

    public List<CommitDetails> getAllCommits() throws Exception {
        if (commitDetailsList.isEmpty()) {
            this.commitDetailsList = generateCommitDetailList();
        }
        return this.commitDetailsList;
    }

    public List<CommitDetails> getCommitsFromDateRange(DateTime startDate, DateTime endDate) throws Exception {
        return getAllCommits().stream().filter(d -> d.getCommitDate().isAfter(startDate)
                && d.getCommitDate().isBefore(endDate)).collect(Collectors.toList());
    }
//throws Exception
    private List<CommitDetails> generateCommitDetailList() throws Exception {

        try {
            for (RevCommit rev : git.log().call()) {
                Injector injector = AbstractController.injector;

                CommitDetails commit = injector.getInstance(CommitDetails.class);
                commit.setPrimaryInformation(new DateTime(rev.getAuthorIdent().getWhen()),
                        rev.getAuthorIdent().getName(),
                        rev.getShortMessage(), getCommitBranch(rev));


                addDiffsToCommit(rev, commit, injector);
                addLinesForAllFiles(rev, commit, injector);

                this.commitDetailsList.add(commit);
            }
        }
        catch (GitAPIException e) {
            throw new Exception("Problem occured during getting RevCommits list.");
        }

        return commitDetailsList;
    }


    private void addLinesForAllFiles(RevCommit rev, CommitDetails commit, Injector injector) throws Exception {

        TreeWalk treeWalk = new TreeWalk(repository);
        try {
            treeWalk.setRecursive(true);
            treeWalk.addTree(rev.getTree());

            while (treeWalk.next()) {
                String path = treeWalk.getPathString();

                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = repository.open(objectId);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                loader.copyTo(stream);

                int linesNumber = IOUtils.readLines(new ByteArrayInputStream(
                        stream.toByteArray()), "UTF-8").size();
                FileDiffs fileDiffs = injector.getInstance(FileDiffs.class);

                List<FileDiffs> fileDiffsList = commit.getFiles();
                boolean flag = false;
                for (FileDiffs f:fileDiffsList) {
                    if(f.getFileName().equals(path)){
                        f.setLinesNumber(linesNumber);
                        if(rev.getParentCount()==0)
                            f.setInsertions(linesNumber);
                        flag = true;
                    }
                }
                if(!flag){
                    if(rev.getParentCount()==0)
                        fileDiffs.setInformation(path, linesNumber, 0);
                    else fileDiffs.setInformation(path,0, 0);
                    fileDiffs.setLinesNumber(linesNumber);
                    commit.addFile(fileDiffs);
                }
                treeWalk.close();
            }
        } catch (IOException e) {
            throw new Exception("Problem occured during adding lines for all files.");
        }
    }

    private void addDiffsToCommit(RevCommit rev, CommitDetails commit, Injector injector) throws Exception {
        if (rev.getParentCount() != 0) {
            List<DiffEntry> diffEntries = null;

            try {
                diffEntries = git.diff()
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
            } catch (IOException e) {
                throw new Exception("Problem occured during adding diffs.");
            }
        }
    }

    private CanonicalTreeParser getCanonicalTreeParser(RevCommit revCommit) throws Exception {
        try (RevWalk revWalk = new RevWalk(repository)) {
            RevTree revTree = revWalk.parseTree(revCommit.getTree().getId());

            CanonicalTreeParser canonicalTreeParser = new CanonicalTreeParser();
            ObjectReader objectReader = repository.newObjectReader();
            canonicalTreeParser.reset(objectReader, revTree.getId());

            revWalk.dispose();
            return canonicalTreeParser;
        }  catch (IOException e) {
            throw new Exception("Error during getting canonicalTreeParser");

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

    public Map<String, Integer> getAmountOfBranchCommits() throws Exception {
        List<Ref> call;
        Map<String, Integer> map = new HashMap<>();
        try {
            call = git.branchList().call();
            for (Ref ref : call) {
                int i = 0;
                for (RevCommit commit : git.log().add(repository.resolve(ref.getName())).call()) {
                    i++;
                }
                map.put(ref.getName(), i);
            }

        } catch (GitAPIException | IOException e) {
            throw new Exception("Problem occured getting amount of commit per branch. ");
        }
        return map;
    }
}


