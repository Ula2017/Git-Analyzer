package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import javafx.scene.Node;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public abstract class AbstractAnalyzerModule {
    protected int height = 720;
    protected int width = 960;
    public abstract Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException;
    protected String getPathForOutput() {
        if(!new File("images").exists())
            new File("images").mkdir();
        return String.format("images/%s.png", toString());
    }
}