package app.fetch.Test;

import app.fetch.URLReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class URLReaderTest {
    @Mock
    private URLConnection conn;
    @Mock
    private InputStream ins;



    @Test
    public void checkIfExistsRemote() throws Exception {
        assertEquals(URLReader.checkIfExistsRemote("mama"), false);
        //Mockito.when(new URL("mama")).thenReturn(url);
//        Mockito.when(url.openConnection()).thenReturn(conn);
//        Mockito.when(conn.getInputStream()).thenReturn(ins);
       // assertEquals(URLReader.checkIfExistsRemote("mama"), true);
        //przetestowac true i false
    }

}