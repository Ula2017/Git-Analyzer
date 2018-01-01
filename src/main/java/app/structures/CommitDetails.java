package app.structures;

import org.joda.time.DateTime;

public class CommitDetails {
    private final DateTime commitDate;
    private final String authorName;
    private final String commitMessage;

    public CommitDetails(DateTime commitDate, String authorName, String commitMessage){
        this.commitDate = commitDate;
        this.authorName = authorName;
        this.commitMessage = commitMessage;
    }

    public DateTime getCommitDate() { return commitDate; }
    public String getAuthorName(){ return authorName; }
    public String getCommitMessage() { return commitMessage; }

}