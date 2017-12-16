package app.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Analyzer {
    public Analyzer(){ }

    public List<IAnalyzerModule> getModules(){
        return Arrays.asList(
            new RepoCommitsAnalyzerModule(),
            new MonthlyAuthorsCounterAnalyzerModule());
    }

    public List<String> getModulesNames() {
        return getModules().stream()
                .map(IAnalyzerModule::getName)
                .collect(Collectors.toList());
    }

    public IAnalyzerModule getModule(String moduleName){
        return getModules().stream()
                .filter(a -> Objects.equals(a.getName(), moduleName))
                .findFirst()
                .get();
    }
}