package app.structures;

public class FileDiffs {

    private String fileName;
    private int insertions;
    private int deletions;
    private int linesNumber;

    public FileDiffs(){
    }

    public void setInformation(String fileName, int insertions, int deletions){
        this.fileName = fileName;
        this.insertions = insertions;
        this.deletions = deletions;

    }
    public void setLinesNumber(int linesNumber){
        this.linesNumber = linesNumber;
    }
    public void setInsertions(int insertions){
        this.insertions = insertions;
    }
    public int getLinesNumber(){return linesNumber;}
    public String getFileName(){return fileName;}
    public int getInsertions(){
        return insertions;
    }
    public int getDeletions(){
        return deletions;
    }

}
