package app.analysis;

import app.structures.CommitDetails;
import app.structures.ModuleNames;
import org.joda.time.DateTime;

import java.util.List;

public abstract class AbstractAnalyzerModule {
    protected DateTime from = DateTime.now().minusYears(1), to = DateTime.now();

    public void setFromDate(DateTime from){
        this.from = from;
    }

    public void setToDate(DateTime to){ this.to = to; }

    public abstract ModuleNames getModuleName();
    public abstract String generateFile(List<CommitDetails> commitDetails) throws Exception;
}