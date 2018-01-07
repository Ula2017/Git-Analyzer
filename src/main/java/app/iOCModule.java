package app;

import app.analysis.AbstractAnalyzerModule;
import app.analysis.MonthlyAmmountOfCommitersModule;
import app.analysis.ProgrammingLanguagesPercentageModule;
import app.analysis.RepoCommitsModule;
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
        analyzerModuleMultibinder.addBinding().to(MonthlyAmmountOfCommitersModule.class);
        analyzerModuleMultibinder.addBinding().to(ProgrammingLanguagesPercentageModule.class);
        analyzerModuleMultibinder.addBinding().to(RepoCommitsModule.class);

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