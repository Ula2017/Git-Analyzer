package app.analysis;

import app.structures.ModuleNames;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Analyzer {
    public Analyzer(){ }

    public List<AbstractAnalyzerModule> getModules(){
        return Arrays.asList(
            new RepoCommits(),
            new MonthlyAuthorsCounter(),
            new ProgrammingLanguagesPercentageAnalyzer());
    }

    public List<ModuleNames> getModulesNames() {
        return getModules().stream()
                .map(AbstractAnalyzerModule::getModuleName)
                .collect(Collectors.toList());
    }

    public AbstractAnalyzerModule getModule(ModuleNames moduleName){
        return getModules().stream()
                .filter(a -> Objects.equals(a.getModuleName(), moduleName))
                .findFirst()
                .get();
    }
}