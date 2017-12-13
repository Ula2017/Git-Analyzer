package app.analysis;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnalyzerProvider {
    private static List<IAnalyzer> analyzers;
    private static List<IAnalyzerModule> modules;

    public static List<IAnalyzerModule> getModules(){
        if(modules == null)
            modules = getAnalyzers().stream()
                    .flatMap(analyzer -> analyzer.getModules().stream())
                    .collect(Collectors.toList());

        return modules;
    }

    public static List<String> getModulesNames() {
        return getModules().stream()
                .map(IAnalyzerModule::getName)
                .collect(Collectors.toList());
    }

    public static IAnalyzerModule getModule(String moduleName){
        return getModules().stream()
                .filter(a -> Objects.equals(a.getName(), moduleName))
                .findFirst()
                .get();
    }

    private static List<IAnalyzer> getAnalyzers() {
        if(analyzers == null)
            analyzers = Arrays.asList(new Analyzer1(), new Analyzer2());

        return analyzers;
    }
}