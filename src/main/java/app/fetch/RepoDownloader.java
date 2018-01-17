package app.fetch;

import org.eclipse.jgit.api.Git;

import java.util.List;

public interface RepoDownloader {

    public List<Git> getRepository(String repoUrl) throws Exception;

}
