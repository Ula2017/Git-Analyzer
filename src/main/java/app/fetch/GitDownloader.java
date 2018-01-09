
package app.fetch;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import java.io.File;


public class GitDownloader implements RepoDownloader {
    public GitDownloader(){}

    @Override
    public Git getRepository(String repoUrl) {
        Git git =null;
        File file = new File("C:\\localRepo");
        if(file.exists())
            delete(file);

        try{
            git=Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(new File("C:\\localRepo"))
                    .call();

        }
        catch (JGitInternalException e){ System.err.println("Already cloned"); }
        catch (GitAPIException e) {
            System.err.println("Problem with cloning remote repository.");
            System.exit(1);
        }

        return git;
    }

    private void delete(File file){
        for (File childFile : file.listFiles()) {
            if (childFile.isDirectory())
                delete(childFile);

            childFile.delete();
        }

        if (!file.delete())
            System.err.println("Problem with deleting directory");
    }
}