package app.fetch;

import org.eclipse.jgit.api.Git;

public interface RepoDownloader {

    public Git getRepository(String repoUrl);
  //  public boolean checkIfExistsRemote();
}
