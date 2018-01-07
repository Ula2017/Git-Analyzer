package app.structures;

public class FileDiffs {

    private String fileName;
    private int insertions;
    private int deletions;
    //private List<Line>linesAdded;
    //private List<Line>linesDeleted;

    public FileDiffs(String fileName, int insertions, int deletions){
        this.fileName = fileName;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public String getFileName(){return fileName;};
    public int getInsertions(){
        return insertions;
    }
    public int getDeletions(){
        return deletions;
    }

}
