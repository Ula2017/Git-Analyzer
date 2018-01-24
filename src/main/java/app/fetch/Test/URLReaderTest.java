package app.fetch.Test;

import app.fetch.URLReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

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
    }

}