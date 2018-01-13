package app.fetch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {

    public static boolean checkIfExistsRemote(String repoUrl) throws Exception {
        boolean result;
        InputStream ins = null;
        try {
            URLConnection conn = new URL(repoUrl).openConnection();
            ins = conn.getInputStream();
            result = true;
        }
        catch(MalformedURLException e ){
            result = false;
        }
        catch (IOException e) {
            throw new Exception("Connection problem");
        }
        finally {
            try {
                if(ins != null)
                    ins.close();
            }
            catch (NullPointerException | IOException e) {
                System.err.println("Error during closing connection.");
            }
        }
        return result;
    }
}
