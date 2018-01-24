package app.fetch.Test;

import app.fetch.Fetcher;
import app.fetch.GitRevCommits;
import app.fetch.RepoDownloader;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Provider;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class TestRepository{

    private Provider<GitRevCommits> gitRevCommitsProvider = new Provider<GitRevCommits>() {
        @Override
        public GitRevCommits get() {
            return gitRevCommits;
        }
    };
    private Provider<CommitDetails> commitDetailsProvider = new Provider<CommitDetails>() {
        @Override
        public CommitDetails get() {
            return commitDetails;
        }
    };
    private Provider<FileDiffs> fileDiffsProvider = new Provider<FileDiffs>() {
        @Override
        public FileDiffs get() {
            return fileDiffs;
        }
    };
    private GitRevCommits gitRevCommits = new GitRevCommits( fileDiffsProvider);
    private static CommitDetails commitDetails = new CommitDetails();
    private static FileDiffs fileDiffs = new FileDiffs();
    @Mock
    private RepoDownloader repoDownloader;
    @Mock
    private SimpleDoubleProperty progress;
    private Fetcher fetcher;
    private List<Git> gitList = new ArrayList();
    private Git git;
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(FetcherTest.class);
        git = Git.init().setDirectory(new File("C:\\tmpRepo")).call();
        fetcher = new Fetcher(repoDownloader, commitDetailsProvider,gitRevCommitsProvider);
        Mockito.when(repoDownloader.getRepository("mama", progress)).thenReturn(gitList);
        fetcher.prepareDownloader("mama", progress);
        gitList.add(git);
    }
    @After
    public void clean() {
        git.getRepository().close();
        deleteFolder(new File("C:\\tmpRepo"));
    }
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }
    @Test
    @Ignore
    public void testRealRepo() throws Exception {
        Repository repository = git.getRepository();
        File myFile = new File(repository.getDirectory().getParent(), "testFile");
        if(!myFile.createNewFile()) {
            throw new IOException("Could not create file " + myFile);
        }

        git.add().addFilepattern("testFile").call();
        git.commit().setMessage("Commit1-Adding testFile").call();

        Path file = new File("C:\\tmpRepo\\testFile2").toPath();
        byte[] buf = "testLine\n testLine2".getBytes();
        Files.write(file, buf);

        git.add().addFilepattern("testFile2").call();
        git.commit().setMessage("Commit2-Adding testFile2").call();

        buf = "testLine\n testLine2\n adddedLine".getBytes();
        Files.write(file, buf);

        git.add().addFilepattern("testFile2").call();
        git.commit().setMessage("Commit3-Changing testFile2").call();

        List<CommitDetails> result = fetcher.getAllCommits();
        assertEquals(result.size(), 3);

    }



}