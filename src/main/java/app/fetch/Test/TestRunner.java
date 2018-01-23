package app.fetch.Test;

import app.fetch.Fetcher;
import app.fetch.Test.FetcherTest;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.MockitoAnnotations;

public class TestRunner {
    public static void main(String[] args) {
        MockitoAnnotations.initMocks(FetcherTest.class);
        Result result = JUnitCore.runClasses(FetcherTest.class);

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(result.wasSuccessful());
    }
}
