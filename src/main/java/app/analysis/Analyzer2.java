package app.analysis;

import java.util.Arrays;
import java.util.List;

public class Analyzer2 implements IAnalyzer {
    private List<IAnalyzerModule> modules;

    public Analyzer2(){
        modules = Arrays.asList(
                new AnalyzerModule1()
        );
    }

    @Override
    public List<IAnalyzerModule> getModules(){
        return modules;
    }
}