package app.fetch;

import app.structures.CommitDetails;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

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
        if(commitDetailsList.isEmpty()){
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

}


