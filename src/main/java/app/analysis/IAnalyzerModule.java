package app.analysis;

import app.fetch.IDTO;
import org.joda.time.DateTime;

import java.util.List;

public abstract class IAnalyzerModule {
    protected DateTime from = DateTime.now().minusYears(1), to = DateTime.now();

    public void setFromDate(DateTime from){
        this.from = from;
    }

    public void setToDate(DateTime to){ this.to = to; }

    public abstract String getName();
    public abstract String generateFile(List<IDTO> data);
}