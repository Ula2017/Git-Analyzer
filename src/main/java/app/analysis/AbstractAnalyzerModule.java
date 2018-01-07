package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import org.joda.time.DateTime;

import java.io.File;
import java.util.List;

public abstract class AbstractAnalyzerModule {
    protected int height = 480;
    protected int width = 640;
    protected List<CommitDetails> commitDetails;
    protected DateTime from;
    protected DateTime to;
    protected String committerName;
    public abstract File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception;
    protected String getPathForOutput(){
        return String.format("images/%s.jpg", toString());
    }
}