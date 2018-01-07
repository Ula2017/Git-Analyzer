package app.structures;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommitDetails {
    private final DateTime commitDate;
    private final String authorName;
    private final String commitMessage;
    private final List<FileDiffs> files = new ArrayList<>();
    //string branch
//    List<String> files;
//    Map<String, Integer> linesAdded;
//    Map<String, Integer> linesDeleted;
    //List<Line> addedLine
    //List<Line> deletedLine

    public CommitDetails(DateTime commitDate, String authorName, String commitMessage){
        this.commitDate = commitDate;
        this.authorName = authorName;
        this.commitMessage = commitMessage;
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

}