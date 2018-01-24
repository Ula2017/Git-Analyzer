package app.fetch;

import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.Git;

import java.util.List;

public interface RepoDownloader {

    List<Git> getRepository(String repoUrl, SimpleDoubleProperty progress) throws Exception;

}
