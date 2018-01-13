package app;

import app.analysis.AbstractAnalyzerModule;
import app.analysis.MonthlyAmmountOfCommitersModule;
import app.analysis.ProgrammingLanguagesPercentageModule;
import app.analysis.ClassificationModule;
import app.analysis.RepoCommitsModule;
import app.analysis.AuthorsCommitsAnalyzerModule;
import app.fetch.GitDownloader;
import app.fetch.RepoDownloader;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class iOCModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<AbstractAnalyzerModule> analyzerModuleMultibinder = Multibinder.newSetBinder(binder(), AbstractAnalyzerModule.class);
        analyzerModuleMultibinder.addBinding().to(MonthlyAmmountOfCommitersModule.class);
        analyzerModuleMultibinder.addBinding().to(ProgrammingLanguagesPercentageModule.class);
        analyzerModuleMultibinder.addBinding().to(RepoCommitsModule.class);
        analyzerModuleMultibinder.addBinding().to(ClassificationModule.class);
        analyzerModuleMultibinder.addBinding().to(AuthorsCommitsAnalyzerModule.class);
        bind(RepoDownloader.class).to(GitDownloader.class);

    }
}
