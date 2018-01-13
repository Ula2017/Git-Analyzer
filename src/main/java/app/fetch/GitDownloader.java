package app.fetch;

import com.google.common.io.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.File;


public class GitDownloader implements RepoDownloader {

    public GitDownloader(){}

    @Override
    public Git getRepository(String repoUrl) throws Exception {
        Git git;
        File file = Files.createTempDir();

        try{
            git=Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(file)
                    .call();
            git.getRepository().close();
            git.close();

        }
        catch (GitAPIException e) {
            throw new Exception("Problem with cloning remote repository.");
        }
        return git;
    }
}