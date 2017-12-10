package app.analysis;

/**
 * Created by Karol on 2017-12-10.
 */
public class AnalyzerModule2 implements IAnalyzerModule{
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String generateFile() {
        return "file:images/wykres1.jpg";
    }
}
