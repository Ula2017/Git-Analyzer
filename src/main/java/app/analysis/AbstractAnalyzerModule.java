package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;

import java.io.File;
import java.util.List;

public abstract class AbstractAnalyzerModule {
    protected int height = 480;
    protected int width = 640;
    public abstract File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException;
    protected String getPathForOutput() {
        if(!new File("images").exists())
            new File("images").mkdir();
        return String.format("images/%s.png", toString());
    }
}