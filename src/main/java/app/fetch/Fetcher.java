package app.fetch;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fetcher {

    private static Git git;

    public Fetcher(Git git){
        this.git = git;
    }

    public List<CommitDetails> getAllCommits(){
        List<CommitDetails> commitDetailsList = new ArrayList<CommitDetails>();
        try{
            Iterable<RevCommit> logs = git.log().call();
            for(RevCommit rev : logs){
                commitDetailsList.add(new CommitDetails(new DateTime(rev.getAuthorIdent().getWhen()), rev.getAuthorIdent().getName(), rev.getShortMessage()));
            }
        }
        catch (GitAPIException e){
            e.printStackTrace();
        }
        return commitDetailsList;
    }

    public List<CommitDetails> getCommitsFiltered(DateTime startDate, DateTime endDate){

        List<CommitDetails> commitDetailsList = getAllCommits();
        return commitDetailsList.stream().filter(d->d.getCommitDate().isAfter(startDate)).filter(d->d.getCommitDate().isBefore(endDate)).collect(Collectors.toList());
    }


    public List<DateTime> getCommitsDates(){
        List<CommitDetails> commitDetailsList = getAllCommits();
        return commitDetailsList.stream().map(d->d.getCommitDate()).collect(Collectors.toList());
    }


}
