package app.fetch.Test;

import app.fetch.Fetcher;
import app.fetch.RepoDownloader;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import com.google.inject.Provider;
import org.eclipse.jgit.api.Git;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;


@RunWith(MockitoJUnitRunner.class)
public class FetcherTest {
    @Mock
    private RepoDownloader repoDownloader;
    @Mock
    private List<Git> git;
    private Fetcher f;
    @Mock
    private List<CommitDetails> commitDetailsList;
    @Mock
    private CommitDetails com;
    @Mock
    private FileDiffs fd;

    @Before
    public void setup() {
        f = new Fetcher(repoDownloader, (Provider<CommitDetails>) com, (Provider<FileDiffs>)fd );
    }

    @Test
    public void prepareDownloader() throws Exception {
        Mockito.when(repoDownloader.getRepository("mama")).thenReturn(git);
        f.prepareDownloader("mama");
        assertEquals(f.getGit(),git);
    }

    @Test(expected = Exception.class)
    public void getBranchCommit() throws Exception {
//        Mockito.when(f.getBranchCommit()).
//        Mockito.when(git.branchList().call()).thenThrow(
//                new Exception("Problem occured getting amount of commit per branch. "));
//        assertEquals(f.getBranchCommit(), "Problem occured getting amount of commit per branch. ");
//        Mockito.when(git.log()).thenThrow(
//                new Exception("Problem occured getting amount of commit per branch. "));
//        assertEquals(f.getAllCommits(), "Problem occured getting amount of commit per branch. ");
    }

    @Test
    public void getGit(){
    }

    @Test
    public void getAllCommits(){
    }

    @Test
    public void getCommitsFromDateRange(){
    }
//    public FetcherTest() {
//        MockitoAnnotations.initMocks(this);
//
//    }



}