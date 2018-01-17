package app.fetch;

import com.google.common.io.Files;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.lib.Ref;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;


public class GitDownloader implements RepoDownloader {

    public GitDownloader(){}

    @Override
    public List<Git> getRepository(String repoUrl) throws Exception {
        Git git;
        List<String> branches = getBranchesToClone(repoUrl);
        List<Git> gits = new ArrayList<>();
        try{
            for( String branch : branches) {
                File file = Files.createTempDir();
                git = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(file)
                        .setBranch(branch)
                        .call();
                git.getRepository().close();
                git.close();
                gits.add(git);
            }

        }
        catch (GitAPIException e) {
            throw new Exception("Problem with cloning remote repository.");
        }
        return gits;
    }

    private List<String> getBranchesToClone(String url  ) throws Exception{
        List<String> branches = new ArrayList<>();
        Collection<Ref> refs;
        try {
            refs = Git.lsRemoteRepository()
                    .setHeads(true)
                    .setRemote(url)
                    .call();
            for (Ref ref : refs) {
                branches.add(ref.getName().substring(ref.getName().lastIndexOf("/") + 1, ref.getName().length()));
            }
        } catch (Exception e) {
            throw new Exception("Error during getBranchToClone.");
        }
        return branches;
    }

    }