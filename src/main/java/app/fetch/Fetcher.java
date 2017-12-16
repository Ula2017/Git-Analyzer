package app.fetch;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class Fetcher {
    private Git git;
    private RepositoryOpener repositoryOpener;

    public Fetcher(){
        this.repositoryOpener=RepositoryOpener.getInstance();
        this.git = repositoryOpener.getGit();
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