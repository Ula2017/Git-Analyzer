package app.fetch;

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

    @Inject
    public Fetcher( RepoDownloader repoOpener){
        this.gitDownloader =repoOpener;
        this.git = repoOpener.getRepository();
    }

    public RepoDownloader getGitDownloader(){
        return gitDownloader;
    }

    public List<IDTO> getAllCommits(){
        List<IDTO> commitDetailsList = new ArrayList<>();
        try{
            for(RevCommit rev : git.log().call()){
                commitDetailsList.add(new CommitDetails(
                        new DateTime(rev.getAuthorIdent().getWhen()),
                        rev.getAuthorIdent().getName(),
                        rev.getShortMessage()));
            }
        }
        catch (GitAPIException e){
            e.printStackTrace();
        }

        return commitDetailsList;
    }
}