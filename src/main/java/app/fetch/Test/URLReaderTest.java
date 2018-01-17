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

//    public void checkIfExistsRemote() throws Exception {
//        assertEquals(URLReader.checkIfExistsRemote("mama"), false);
//
//        //final URLConnection mockUrlCon = mock(URLConnection.class);
//
//        String response = "mama";
//
//        //final URLConnection mockCon = mock(URLConnection. class);
//        InputStream inputStrm = IOUtils.toInputStream( response);
//        when(mockCon.getLastModified()).thenReturn((Long)10L, (Long)11L);
//        when(mockCon.getInputStream()).thenReturn( inputStrm);
//
//        //mocking httpconnection by URLStreamHandler since we can not mock URL class.
//        URLStreamHandler stubURLStreamHandler = new URLStreamHandler() {
//            @Override
//            protected URLConnection openConnection(URL u ) throws IOException {
//                return mockCon ;
//            }
//        };
//
//        when(mockCon.getInputStream()).thenReturn(ins);
//
//        url = new URL(null,"mama", stubURLStreamHandler);
//
//        //doReturn(url).when(urlReader.)
//        assertEquals(urlReader.checkIfExistsRemote("mama"), true);
//        //        URLReader urlReader = new URLReader();
////        URLReader urlReader2 = new URLReader();
////        urlReader2 = spy(urlReader);
//
//        //doReturn(mockCon).when(urlReader.)
//
//        //Mockito.when(new URL("mama")).thenReturn(urlMock);
//        //Mockito.when(URLReader.).thenReturn(mockCon);
//        //Mockito.when(conn.getInputStream()).thenReturn(ins);
//        //Mockito.when(new URL("mama")).thenReturn(url);
////        Mockito.when(url.openConnection()).thenReturn(conn);
////        Mockito.when(conn.getInputStream()).thenReturn(ins);
//        // assertEquals(URLReader.checkIfExistsRemote("mama"), true);
//        //przetestowac true i false
//    }



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