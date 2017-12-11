package app.fetch;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.xml.internal.bind.v2.TODO;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RepositoryOpener {


    private Repository repository;
    private Git git;
    private String repoUrl;
    private static RepositoryOpener instance;

    protected RepositoryOpener(){
    }

    public static RepositoryOpener getInstance(){
        if(instance == null) {
            instance = new RepositoryOpener();
        }
        return instance;
    }

    public Git getRepo() {

        Git tmp=null;
        try{
            tmp = Git.cloneRepository()
                    .setURI(repoUrl)
                    .call();
            this.git=tmp;
            this.repository = git.getRepository();
        }
        catch (JGitInternalException e){
            System.out.println("Already cloned");
            tmp = this.git;
        }
        catch (GitAPIException e) {
            e.printStackTrace();
        }

        return tmp;
    }

    public boolean checkIfExistsRemote(){

        boolean result;
        InputStream ins = null;
        try {
            URLConnection conn = new URL(repoUrl).openConnection();
            ins = conn.getInputStream();
            result = true;
        } catch (Exception e) {
            result = false;
        } finally {
            try {
                if(ins != null) {
                    ins.close();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
    //    public boolean checkIfExistsLocal() {
//        boolean result;
//
////        try {
////            System.out.println(new FileRepository("C:\\Users\\Ula\\IdeaProjects\\Git-Analyzer\\Sejmometr").getObjectDatabase().exists());
////            result=false;
////        } catch (IOException e) {
////            result = false;
////        }
////        System.out.println(result);
//        if (RepositoryCache.FileKey.isGitRepository(new File("C:\\Users\\Ula\\IdeaProjects\\Git-Analyzer\\Sejmometr"), FS.DETECTED)) {
//            System.out.println("isntije");
//            return true;
//        }
//        return false;
//    }
    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public String getRepoUrl() {
        return repoUrl;
    }

    public Git getGit(){
        return git;
    }

}
