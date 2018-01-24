package app.fetch;

import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.*;
import javafx.beans.property.SimpleDoubleProperty;
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
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class Fetcher {

    private List<Git> git;
    private RepoDownloader gitDownloader;
    private Provider<CommitDetails> commitDetailsProvider;
    private Provider<GitRevCommits> gitRevCommitsProvider;
    private List<CommitDetails> commitDetailsList;
    private CommitDetails commit;

    @Inject
    public Fetcher(RepoDownloader repoDownloader,
                   Provider<CommitDetails> commitDetailsProvider, Provider<GitRevCommits> gitRevCommitsProvider) {
        this.gitDownloader = repoDownloader;
        this.gitRevCommitsProvider = gitRevCommitsProvider;
        this.commitDetailsProvider = commitDetailsProvider;

    }

    public void prepareDownloader(String url, SimpleDoubleProperty progress) throws Exception {
        this.commitDetailsList = new ArrayList<>();
        this.git = gitDownloader.getRepository(url, progress);
    }

    public List<Git> getGit() {
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

    private List<CommitDetails> generateCommitDetailList() throws Exception {
        try {
            GitRevCommits revTmp = gitRevCommitsProvider.get();
            for (Git g : git) {
                 for (RevCommit rev : revTmp.revCommitList(g)){

                        CommitDetails commit = commitDetailsProvider.get();
                        commit.setPrimaryInformation(new DateTime(rev.getAuthorIdent().getWhen()),
                                rev.getAuthorIdent().getName(),
                                rev.getShortMessage(), g.getRepository().getBranch());

                        commit = revTmp.addDiffsToCommit(rev, commit, g);
                        commit = revTmp.addLinesForAllFiles(rev, commit, g);

                        this.commitDetailsList.add(commit);
                    }
                }
            }
        catch (GitAPIException e) {
            throw new Exception("Problem occured during getting RevCommits list.");
        }

        return commitDetailsList;
    }

}


