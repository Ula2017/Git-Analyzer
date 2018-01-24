package app.fetch.Test;

import app.fetch.GitDownloader;
import javafx.beans.property.SimpleDoubleProperty;
import org.eclipse.jgit.api.Git;
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
    @Mock
    private SimpleDoubleProperty progress;

    @Before
    public void setup() {
        gitDownloader = new GitDownloader();
    }
//(expected = Exception.class)
    @Test
    public void getRepository() throws Exception {

//            Mockito.when(Git.cloneRepository()
//                    .call()).thenReturn(git);
//
//        assertEquals(gitDownloader.getRepository("someUrl", progress), git);
        //throw new Exception("Error during getBranchToClone.");

//
//
//
//
        Mockito.when(gitDownloader.getRepository("someUrl", progress)).thenThrow(
                new Exception("Error during getBranchToClone."));
        assertEquals(gitDownloader.getRepository("someUrl", progress),
                "Error during getBranchToClone.");
//
//        Mockito.when(file.delete()).thenThrow(
//                new Exception("Problem with deleting directory"));
//        assertEquals(gitDownloader.getRepository("mam", progress),
//                "Problem with deleting directory");

    }

    //        Mockito.when(Git.cloneRepository().call()).thenThrow(
//                new JGitInternalException("mistake"));
//        System.out.println(gitDownloader.getRepository("someUrl"));
//    assertEquals(gitDownloader.getRepository("someUrl"),
//                true );

}