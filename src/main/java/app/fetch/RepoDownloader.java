package app.fetch;

import org.eclipse.jgit.api.Git;

public interface RepoDownloader {

    public Git getRepository();
    public boolean checkIfExistsRemote();
    //takie pytanie czy getRepository ma miec strinnga i czy moze check metoda tez?, czy get git tu tez?
    public Git getGit();
    public void setRepoUrl(String url);
}
