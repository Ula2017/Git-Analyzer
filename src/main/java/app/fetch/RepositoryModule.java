package app.fetch;

import app.gui.ModuleController;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.ScopedBindingBuilder;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Singleton;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@BindingAnnotation
@interface RepoUrl {}

public class RepositoryModule extends AbstractModule {

    private String url;

    public RepositoryModule(String url){
        this.url=url;
    }

    @Override
    protected void configure() {

        bindConstant().annotatedWith(RepoUrl.class).to(url);
        bind(RepoDownloader.class).to(GitDownloader.class);

    }
}
