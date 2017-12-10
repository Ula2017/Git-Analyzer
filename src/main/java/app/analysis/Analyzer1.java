package app.analysis;

import java.util.Arrays;
import java.util.List;

public class Analyzer1 implements IAnalyzer {
    private List<IAnalyzerModule> modules;

    public Analyzer1(){
        modules = Arrays.asList(
                new RepoCommitsAnalyzerModule()
        );
    }

    @Override
    public List<IAnalyzerModule> getModules(){
        return modules;
    }
}