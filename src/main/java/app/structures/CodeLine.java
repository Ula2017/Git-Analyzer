package app.structures;

public class CodeLine {
    private String fileName;
    private int lineNumber;
    private String text;


    public CodeLine(String fileName, int lineNumber ) {
        this.fileName = fileName;
        this.lineNumber = lineNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getText(){
        return text;
    }

    public void setFileName(String fn){this.fileName = fn;}

    public void setLineNumber(int number){
        this.lineNumber = number;
    }

    public void setText(String t){
        this.text = t;
    }
}
