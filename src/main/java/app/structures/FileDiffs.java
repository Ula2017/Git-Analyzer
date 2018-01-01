package app.structures;

import org.eclipse.jgit.lib.ObjectId;
import org.joda.time.DateTime;

public class FileDiffs {

    private int insertions;
    private int deletions;
    private String commitMessage;
    private String filePath;

    public FileDiffs(String filePath, String commitMessage, int insertions, int deletions){
        this.filePath = filePath;
        this.commitMessage = commitMessage;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public String getFilePath(){
        return filePath;
    }
    public int getInsertions(){
        return insertions;
    }
    public int getDeletions(){
        return deletions;
    }
    public String getCommitMessage(){
        return commitMessage;
    }

}
