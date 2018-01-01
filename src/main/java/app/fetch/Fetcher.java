package app.fetch;

import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.org.apache.regexp.internal.RE;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Singleton
public class Fetcher {
    private Git git;
    private RepoDownloader gitDownloader;
    private List<CommitDetails> commitDetailsList;

    @Inject
    public Fetcher(RepoDownloader repoOpener) {
        this.gitDownloader = repoOpener;
        this.git = repoOpener.getRepository();
        this.commitDetailsList = new ArrayList<>();
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

    private List<CommitDetails> generateCommitDetailList() {

        try {
            for (RevCommit rev : git.log().call()) {
                this.commitDetailsList.add(new CommitDetails(
                        new DateTime(rev.getAuthorIdent().getWhen()),
                        rev.getAuthorIdent().getName(),
                        rev.getShortMessage()));
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

        return commitDetailsList;
    }

    private List<RevCommit> getRevCommits() throws GitAPIException {
        Iterable<RevCommit> revCommitsIterable = git.log().call();
        List<RevCommit> revCommits = new ArrayList<>();
        revCommitsIterable.forEach(revCommits::add);
        return revCommits;
    }

    public List<List<FileDiffs>> getDiffsFromTimeRange(DateTime startDate, DateTime endDate) {

        List<List<FileDiffs>> results = new ArrayList<>();
        try {
            List<RevCommit> filteredCommits = getRevCommits()
                    .stream()
                    .filter(d -> new DateTime(d.getAuthorIdent().getWhen()).isAfter(startDate) && new DateTime(d.getAuthorIdent().getWhen()).isBefore(endDate))
                    .collect(Collectors.toList());

            for (RevCommit newCommit : filteredCommits) {
                if (newCommit.getParentCount() != 0) {
                    RevCommit oldCommit = newCommit.getParent(0);
                    results.add(getDiff(oldCommit, newCommit));
                }
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return results;
    }

    private List<FileDiffs> getDiff(RevCommit newCommit, RevCommit oldCommit) throws GitAPIException, IOException {

        List<DiffEntry> diffEntries = git.diff()
                .setOldTree(getCanonicalTreeParser(oldCommit))
                .setNewTree(getCanonicalTreeParser(newCommit))
                .call();

        List<FileDiffs> results = new ArrayList<>();

        for (DiffEntry diffEntry : diffEntries) {
            int deletions = 0;
            int insertions = 0;
            DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
            diffFormatter.setRepository(git.getRepository());
            diffFormatter.setContext(0);
            EditList edits = diffFormatter.toFileHeader(diffEntry).toEditList();

            deletions = edits.stream().mapToInt(e -> e.getLengthA()).sum();
            insertions = edits.stream().mapToInt(e -> e.getLengthB()).sum();

            results.add(new FileDiffs(diffEntry.getNewPath(), newCommit.getShortMessage(), insertions, deletions));
        }
        return results;

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


