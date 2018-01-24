package app.fetch.Test;

import app.fetch.Fetcher;
import app.fetch.GitRevCommits;
import app.fetch.RepoDownloader;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Provider;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestRepositoryPieces {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();


    private Provider<GitRevCommits> gitRevCommitsProvider= new Provider<GitRevCommits>() {
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
    private CommitDetails commitDetails = new CommitDetails();
    private FileDiffs fileDiffs = new FileDiffs();
    @Mock
    private RepoDownloader repoDownloader;
    @Mock
    private SimpleDoubleProperty progress;
    private Fetcher fetcher;
    private Repository repository;
    private List<Git> gitList = new ArrayList<>();
    private Git git;
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(FetcherTest.class);
        git = Git.init().setDirectory(tempFolder.getRoot()).call();
        fetcher = new Fetcher(repoDownloader, commitDetailsProvider,gitRevCommitsProvider);
        repository = git.getRepository();
        gitList.add(git);
        Mockito.when(repoDownloader.getRepository("mama", progress)).thenReturn(gitList);
        fetcher.prepareDownloader("mama", progress);
    }

    @After
    public void clean(){
        git.getRepository().close();
    }

    @Test
    public void testSimpleCommit() throws Exception {
        createRevCommit("exampleFile.txt", "Line1\n Line2\n Line3\n", "commit1");
        List<CommitDetails> result = fetcher.getAllCommits();
        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getCommitMessage(), "commit1");
        assertEquals(result.get(0).getBranch(), "master");
        assertEquals(result.get(0).getFiles().get(0).getFileName(), "exampleFile.txt");
        assertEquals(result.get(0).getFiles().get(0).getLinesNumber(),3);
        assertEquals(result.get(0).getFiles().get(0).getDeletions(),0);
        assertEquals(result.get(0).getFiles().get(0).getInsertions(),3);
    }

    @Test
    public void testCountLines() throws Exception {
        RevCommit commit = createRevCommit("exampleFile", "Line1\n Line2\n Line3\n", "commit1");
        CommitDetails commitDetailsNew = new CommitDetails();
        CommitDetails commitDetails = gitRevCommits.addLinesForAllFiles(commit, commitDetailsNew, git);
        assertEquals(commitDetails.getFiles().get(0).getLinesNumber(), 3);

    }

    @Test
    public void testDiffs() throws Exception {
        createRevCommit("exampleFile1", "Line1\n Line2\n Line3\n Line4\n", "commitFirst");
        RevCommit commitNew = createRevCommit("exampleFile1", "Line1\n Line2\n LineCompletlyNew\n LineNew\n LineNew\n", "commitSeconf");
        CommitDetails commitDetailsNew = new CommitDetails();
        CommitDetails commitDetails = gitRevCommits.addDiffsToCommit(commitNew, commitDetailsNew, git);
        assertEquals(commitDetails.getFiles().get(0).getInsertions(), 3);
        assertEquals(commitDetails.getFiles().get(0).getDeletions(), 2);
    }

    private RevCommit createRevCommit(String name, String content, String commitMessage) throws IOException, GitAPIException {
        createFile(name, content);
        git.add().addFilepattern(name).call();
        return git.commit().setMessage(commitMessage).call();

    }

    private void createFile(String name, String content) throws IOException {
        File file = new File(git.getRepository().getWorkTree(), name);
        try(FileOutputStream outputStream = new FileOutputStream(file)){
            outputStream.write(content.getBytes());
        }
    }

}
