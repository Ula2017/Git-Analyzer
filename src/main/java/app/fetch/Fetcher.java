package app.fetch;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Fetcher {
    private Git git;
    private RepositoryOpener repositoryOpener;

    public Fetcher(){
        this.repositoryOpener=RepositoryOpener.getInstance();
        this.git = repositoryOpener.getGit();
    }

    public Git getGit() {
        return git;
    }
    public RepositoryOpener getRepositoryOpener() {
        return repositoryOpener;
    }

    public List<CommitDetails> getAllCommits(){
        List<CommitDetails> commitDetailsList = new ArrayList<>();
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

    public List<CommitDetails> getCommitsFiltered(DateTime startDate, DateTime endDate){
        return getAllCommits().stream()
                .filter(d->d.getCommitDate().isAfter(startDate))
                .filter(d->d.getCommitDate().isBefore(endDate))
                .collect(Collectors.toList());
    }

    public List<CommitDetails> getMonthlyRaport(int month, int year){
        return getCommitsFiltered(new DateTime(year, month, 1, 0, 0),new DateTime(year, month, 28,23,59));
    }

    public List<DateTime> getCommitsDates(){
        return getAllCommits().stream().map(CommitDetails::getCommitDate).collect(Collectors.toList());
    }
}