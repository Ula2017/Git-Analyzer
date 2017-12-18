
package app.fetch;

import com.google.inject.Inject;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class GitDownloader implements RepoDownloader {
    private Git git;
    private String repoUrl;


    @Inject
    public GitDownloader(@RepoUrl String url){
        this.repoUrl = url;
    }


    public Git getGit(){
        return git;
    }
    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }


    @Override
    public Git getRepository() {
        File file = new File("C:\\localRepo");
        if(file.exists())
            delete(file);

        try{
            this.git=Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(new File("C:\\localRepo"))
                    .call();

        }
        catch (JGitInternalException e){ System.err.println("Already cloned"); }
        catch (GitAPIException e) { e.printStackTrace(); }

        return this.git;
    }

    @Override
    public boolean checkIfExistsRemote(){
        boolean result;
        InputStream ins = null;
        try {
            URLConnection conn = new URL(repoUrl).openConnection();
            ins = conn.getInputStream();
            result = true;
        }
        catch (Exception e) {
            result = false;
        }
        finally {
            try {
                if(ins != null)
                    ins.close();
            }
            catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }

        return result;
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