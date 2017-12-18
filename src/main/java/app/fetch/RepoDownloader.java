package app.fetch;

import org.eclipse.jgit.api.Git;

public interface RepoDownloader {

    public Git getRepository();
    public boolean checkIfExistsRemote();
    public void setRepoUrl(String url);
}
