package app.fetch.Test;

import app.fetch.Fetcher;
import app.fetch.RepoDownloader;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import app.fetch.GitRevCommits;
import com.google.inject.Provider;
import org.eclipse.jgit.api.DiffCommand;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;


@RunWith(MockitoJUnitRunner.class)
public class FetcherTest {
    @Mock
    private RepoDownloader repoDownloader;
    @Mock
    private RevCommit revCommitMock1;

    @Mock
    private RevCommit revCommitMock2;

    private List<RevCommit> revCommits = new ArrayList<>();

    //@Mock
    private List<Git> git = new ArrayList<>();

    @Mock
    private Git gitObject;

    private Fetcher f;
    @Mock
    private List<CommitDetails> commitDetailsList;

    private List<CommitDetails> commitDetailsListNotMocked = new ArrayList<>();
    @Mock
    private Provider<CommitDetails> com;

    @Mock
     private SimpleDoubleProperty progress;

    @Mock
    private Provider<GitRevCommits> gitRevCommitsProvider;

    @Mock
    private CommitDetails commitDetailsObject;

    @Mock
    private Repository repository;

    @Mock
    private GitRevCommits gitRevCommits;

    private CommitDetails commitDetailsNotMock = new CommitDetails();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(FetcherTest.class);
        f = new Fetcher(repoDownloader, com, gitRevCommitsProvider);
    }

    @Test
    public void prepareDownloader() throws Exception {
        Mockito.when(repoDownloader.getRepository("mama", progress)).thenReturn(git);
        f.prepareDownloader("mama", progress);
        assertEquals(f.getGit(), git);
    }


    @Test
    public void testGenerateCommitDetailsList() throws Exception {

        commitDetailsNotMock.setPrimaryInformation(new DateTime(2000, 11, 11, 11, 11, 11, 11),
                "auth1", "commess", "branch1");
        commitDetailsList.add(commitDetailsNotMock);
        commitDetailsListNotMocked.add(commitDetailsNotMock);
        commitDetailsNotMock.setPrimaryInformation(new DateTime(2000, 11, 11, 11, 11, 11, 11),
                "auth2", "commess2", "branch2");
        commitDetailsList.add(commitDetailsNotMock);
        commitDetailsListNotMocked.add(commitDetailsNotMock);
        Mockito.when(com.get()).thenReturn(commitDetailsObject);
        revCommitMock1 = createDetailedCommit("Author1", "ComMess1", new Date(
                2000, 11, 11, 11, 11, 11));
        revCommitMock2 = createDetailedCommit("author2", "ComMess1", new Date(
                2010, 11, 11, 11 ,1));
        revCommits.add(revCommitMock1);
        revCommits.add(revCommitMock2);

        Mockito.when(repository.getBranch()).thenReturn("Branch1");
        Mockito.when(gitObject.getRepository()).thenReturn(repository);
        git.add(gitObject);
        Mockito.when(repoDownloader.getRepository("mama", progress)).thenReturn(git);
        f.prepareDownloader("mama", progress);
        Mockito.when(gitRevCommits.revCommitList(git.get(0))).thenReturn(revCommits);
        Mockito.when(gitRevCommits.addDiffsToCommit(revCommitMock1, commitDetailsObject, gitObject))
                .thenReturn(commitDetailsNotMock);
        Mockito.when(gitRevCommits.addDiffsToCommit(revCommitMock2, commitDetailsObject, gitObject))
                .thenReturn(commitDetailsNotMock);
        Mockito.when(gitRevCommits.addLinesForAllFiles(revCommitMock1, commitDetailsNotMock, gitObject))
                .thenReturn(commitDetailsNotMock);
        Mockito.when(gitRevCommits.addLinesForAllFiles(revCommitMock2, commitDetailsNotMock, gitObject))
                .thenReturn(commitDetailsNotMock);
        Mockito.when(gitRevCommitsProvider.get()).thenReturn(gitRevCommits);

        assertEquals(f.getAllCommits(), commitDetailsListNotMocked);

    }

    private RevCommit createDetailedCommit(String author, String commitMessage, Date date) {
        StringBuilder parents = new StringBuilder();
        int numParents = 1;
        for (; numParents > 0; numParents--) {
            parents.append(String.format("parent %040x\n", new java.util.Random().nextLong()));
        }

        String commitData = String.format("tree %040x\n" +
                parents +
                        "author %s\n" +
                        "committer %s %d +0100\n\n" +
                        "%s",
                new Random().nextLong(),
                author,
                author,
                date.getTime(),
                commitMessage);

        return RevCommit.parse(commitData.getBytes());

    }


}