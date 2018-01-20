package app.fetch;

import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GitDownloader implements RepoDownloader {
    public GitDownloader(){}

    @Override
    public List<Git> getRepository(String repoUrl, SimpleDoubleProperty progress) throws Exception {
        Git git;
        List<String> branches = getBranchesToClone(repoUrl);
        final int branchesCount = branches.size();
        List<Git> gits = new ArrayList<>();
        try{
            for(int i = 0; i < branchesCount; i++){
                File file = Files.createTempDir();
                git = Git.cloneRepository()
                        .setURI(repoUrl)
                        .setDirectory(file)
                        .setBranch(branches.get(i))
                        .call();
                git.getRepository().close();
                git.close();
                gits.add(git);
                final int ind = i;
                Platform.runLater(()-> progress.set(1.0*(ind+1)/branchesCount));
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