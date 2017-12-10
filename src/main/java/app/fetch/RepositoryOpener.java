package app.fetch;

import com.sun.org.apache.regexp.internal.RE;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LsRemoteCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

public class RepositoryOpener {

    private Git git;
    private String repoUrl;

    private RepositoryOpener(){}

    private static RepositoryOpener INSTANCE;

    public static RepositoryOpener getInstance(){
        if(INSTANCE == null)
            INSTANCE = new RepositoryOpener();
        return INSTANCE;
    }

    public Git getRepo() {

        //TODO-check if repo is already cloned
        try{
            git = Git.cloneRepository()
                    .setURI(repoUrl)
                    .call();
        }
        catch (JGitInternalException e){
            System.out.println("Already cloned");
        }
        catch (GitAPIException e) {
            e.printStackTrace();
        }

        return git;
    }

    private  boolean hasAtLeastOneReference(Repository repo) {
        for (Ref ref : repo.getAllRefs().values()) {
            if (ref.getObjectId() == null)
                continue;
            return true;
        }
        return false;
    }

    public boolean checkIfExists(){
        LsRemoteCommand lsCmd = new LsRemoteCommand(null);
        lsCmd.setRemote(repoUrl);
        try
        {
            System.out.println(lsCmd.call().toString());
        }catch (GitAPIException e){
            return false;
        }
        return true;
    }

    public void setRepoUrl(String repoUrl){
        this.repoUrl = repoUrl;
    }

}
