package app.structures;

public class FileDiffs {
    private String commitMessage;
    private String committerName;
    private String filePath;
    private int insertions;
    private int deletions;

    public FileDiffs(String filePath, String commitMessage, String committerName, int insertions, int deletions){
        this.filePath = filePath;
        this.commitMessage = commitMessage;
        this.committerName = committerName;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public String getFilePath(){
        return filePath;
    }
    public String getCommitMessage(){
        return commitMessage;
    }
    public String getCommitterName(){
        return committerName;
    }
    public int getInsertions(){
        return insertions;
    }
    public int getDeletions(){
        return deletions;
    }

}
