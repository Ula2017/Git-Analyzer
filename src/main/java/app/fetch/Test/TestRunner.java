package app.fetch.Test;

import app.fetch.Fetcher;
import app.fetch.Test.FetcherTest;
import app.fetch.URLReader;
import com.sun.org.apache.regexp.internal.RE;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.MockitoAnnotations;

public class TestRunner {
    public static void main(String[] args) {
        MockitoAnnotations.initMocks(FetcherTest.class);
        MockitoAnnotations.initMocks(TestRepository.class);
        MockitoAnnotations.initMocks(TestRepositoryPieces.class);
        MockitoAnnotations.initMocks(URLReaderTest.class);
        Result result = JUnitCore.runClasses(FetcherTest.class);
        Result result2 = JUnitCore.runClasses(TestRepository.class);
        Result result3 = JUnitCore.runClasses(TestRepositoryPieces.class);
        Result result4 = JUnitCore.runClasses(URLReaderTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
        for (Failure failure : result2.getFailures()) {
            System.out.println(failure.toString());
        }
        for (Failure failure : result3.getFailures()) {
            System.out.println(failure.toString());
        }
        for (Failure failure : result4.getFailures()) {
            System.out.println(failure.toString());
        }


        System.out.println(result.wasSuccessful());
        System.out.println(result2.wasSuccessful());
        System.out.println(result3.wasSuccessful());
        System.out.println(result4.wasSuccessful());
    }
}
