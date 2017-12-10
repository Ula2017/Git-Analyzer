package app.fetch;

import org.joda.time.DateTime;

public class CommitDetails {

    private DateTime commitDate;
    private String authorName;
    private String commitMessage;

    public CommitDetails(DateTime commitDate, String authorName, String commitMessage){
        this.commitDate = commitDate;
        this.authorName = authorName;
        this.commitMessage = commitMessage;
    }

    public DateTime getCommitDate() {
        return commitDate;
    }

    public String getAuthorName(){
        return authorName;
    }

    public String getCommitMessage() {
        return commitMessage;
    }
}
