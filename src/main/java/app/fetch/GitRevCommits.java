package app.fetch;

import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Inject;
import com.google.inject.Provider;
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
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitRevCommits {

    private Provider<CommitDetails> commitDetailsProvider;
    private Provider<FileDiffs> fileDiffsProvider;


    @Inject
    public GitRevCommits(Provider<CommitDetails> commitDetailsProvider, Provider<FileDiffs> fileDiffsProvider){
        this.commitDetailsProvider = commitDetailsProvider;
        this.fileDiffsProvider = fileDiffsProvider;
    }

    public Iterable<RevCommit> revCommitList(Git git) throws GitAPIException {
        return git.log().call();
    }


    public CommitDetails addLinesForAllFiles(RevCommit rev, CommitDetails commit, Git git ) throws Exception {

        Repository repository = git.getRepository();
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
                FileDiffs fileDiffs = fileDiffsProvider.get();

                List<FileDiffs> fileDiffsList = commit.getFiles();
                boolean flag = false;
                for (FileDiffs f : fileDiffsList) {
                    if (f.getFileName().equals(path)) {
                        f.setLinesNumber(linesNumber);
                        if (rev.getParentCount() == 0)
                            f.setInsertions(linesNumber);
                        flag = true;
                    }
                }
                if (!flag) {
                    if (rev.getParentCount() == 0)
                        fileDiffs.setInformation(path, linesNumber, 0);
                    else fileDiffs.setInformation(path, 0, 0);
                    fileDiffs.setLinesNumber(linesNumber);
                    fileDiffs.setFileContent(stream);
                    commit.addFile(fileDiffs);
                }
                treeWalk.close();
            }
        } catch (IOException e) {
            throw new Exception("Problem occured during adding lines for all files.");
        }

        return commit;

    }

    public CommitDetails addDiffsToCommit(RevCommit rev, CommitDetails commit, Git g) throws Exception {
        Repository repository = g.getRepository();
        if (rev.getParentCount() != 0) {
            List<DiffEntry> diffEntries = null;

            try {

                diffEntries = g.diff()
                        .setOldTree(getCanonicalTreeParser(rev.getParent(0), repository))
                        .setNewTree(getCanonicalTreeParser(rev, repository))
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

                    FileDiffs fileDiffs = fileDiffsProvider.get();
                    fileDiffs.setInformation(diffEntry.getNewPath(), insertions, deletions);
                    commit.addFile(fileDiffs);

                }
            } catch (IOException e) {
                throw new Exception("Problem occured during adding diffs.");
            }
        }
        return commit;
    }

    public CanonicalTreeParser getCanonicalTreeParser(ObjectId commitId, Repository repository) throws IOException {
        try( RevWalk walk = new RevWalk(repository)) {
            RevCommit commit = walk.parseCommit( commitId );
            ObjectId treeId = commit.getTree().getId();
            try( ObjectReader reader = repository.newObjectReader()) {
                return new CanonicalTreeParser( null, reader, treeId );
            }
        }
    }


//    public Map<String, Integer> getAmountOfBranchCommits() throws Exception {
//        List<Ref> call;
//        Map<String, Integer> map = new HashMap<>();
//        try {
//            for(Git g: git) {
//                call = g.branchList().call();
//                for (Ref ref : call) {
//                    int i = 0;
//
//                    for (RevCommit commit : g.log().add(g.getRepository().resolve(ref.getName())).call()) {
//                        i++;
//                    }
//                    map.put(ref.getName(), i);
//                }
//            }
//
//        } catch (GitAPIException | IOException e) {
//            throw new Exception("Problem occured getting amount of commit per branch. ");
//        }
//        return map;
//    }
}
