package app.fetch.Test;

import app.fetch.GitDownloader;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.Repository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.*;


@RunWith(MockitoJUnitRunner.class)
public class GitDownloaderTest {

    private GitDownloader gitDownloader;
    @Mock
    private File file;
    @Mock
    private Git git;
    @Mock
    private Git git2;

    @Before
    public void setup() {
        gitDownloader = new GitDownloader();
    }
///(expected = Exception.class)
    @Test
    public void getRepository() throws Exception {

//            Mockito.when(Git.cloneRepository()
//                    .call()).thenReturn(git2);
//
//        assertEquals(gitDownloader.getRepository("someUrl"), git);



//
//        Mockito.when(gitDownloader.getRepository("someUrl")).thenThrow(
//                new Exception("Problem with cloning remote repository."));
//        assertEquals(gitDownloader.getRepository("someUrl"),
//                "Problem with cloning remote repository.");
//
//        Mockito.when(file.delete()).thenThrow(
//                new Exception("Problem with deleting directory"));
//        assertEquals(gitDownloader.getRepository("mam"),
//                "Problem with deleting directory");

    }

    //        Mockito.when(Git.cloneRepository().call()).thenThrow(
//                new JGitInternalException("mistake"));
//        System.out.println(gitDownloader.getRepository("someUrl"));
//    assertEquals(gitDownloader.getRepository("someUrl"),
//                true );

}