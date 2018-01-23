package app.structures;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class CommitDetails {
    private DateTime commitDate;
    private String authorName;
    private String commitMessage;
    private final List<FileDiffs> files = new ArrayList<>();
    private String branch;

    public CommitDetails(){
    }

    public void setMessage(String message){
        this.commitMessage = message;
    }

    public void addFile(FileDiffs file){
        this.files.add(file);
    }

    public List<FileDiffs> getFiles() {
        return files;
    }

    public DateTime getCommitDate() { return commitDate; }
    public String getAuthorName(){ return authorName; }
    public String getCommitMessage() { return commitMessage; }
    public String getBranch() { return branch; }

    public void setPrimaryInformation(DateTime date, String authorName, String commitMessage, String branch){
        this.commitDate=date;
        this.authorName=authorName;
        this.commitMessage= commitMessage;
        this.branch = branch;

    }
}