package app;

import app.analysis.AbstractAnalyzerModule;
import app.analysis.MonthlyAmmountOfCommiters;
import app.analysis.ProgrammingLanguagesPercentageAnalyzer;
import app.analysis.RepoCommits;
import app.fetch.GitDownloader;
import app.fetch.RepoDownloader;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

@Singleton
public class iOCModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<AbstractAnalyzerModule> analyzerModuleMultibinder = Multibinder.newSetBinder(binder(), AbstractAnalyzerModule.class);
        analyzerModuleMultibinder.addBinding().to(MonthlyAmmountOfCommiters.class);
        analyzerModuleMultibinder.addBinding().to(ProgrammingLanguagesPercentageAnalyzer.class);
        analyzerModuleMultibinder.addBinding().to(RepoCommits.class);

        bind(RepoDownloader.class).to(GitDownloader.class);

    }
}
//@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.FIELD, ElementType.PARAMETER})
//@BindingAnnotation
//@interface RepoUrl {}

//    private String url;
//    public iOCModule(String url){
//        this.url=url;
//    }

//        bindConstant().annotatedWith(RepoUrl.class).to(url);
//        AnnotatedBindingBuilder<MainMenuController> bind = bind(MainMenuController.class);