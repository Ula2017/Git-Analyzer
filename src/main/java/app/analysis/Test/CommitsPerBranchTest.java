package app.analysis.Test;

import app.analysis.CommitsPerBranchModule;
import app.structures.CommitDetails;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Karol on 2018-01-17.
 */

@RunWith(MockitoJUnitRunner.class)
public class CommitsPerBranchTest extends AbstractTest{
    private CommitsPerBranchModule cpbm;
    private List<CommitDetails> commitDetails;

    @Before
    public void setUp(){
        cpbm = new CommitsPerBranchModule();
        commitDetails = new ArrayList<>();
    }

    @Test
    public void createDatasetTest() throws Exception {
        //Prepare
        CommitDetails cm1 = new CommitDetails();
        cm1.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(11).withDayOfMonth(1), "xyz", "Commit message 0.1", "master");

        CommitDetails cm2 = new CommitDetails();
        cm2.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "xyz", "Commit message 0.2", "master");

        CommitDetails cm3 = new CommitDetails();
        cm3.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(1), "abc", "Commit message 1.1", "branch1");

        CommitDetails cm4 = new CommitDetails();
        cm4.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(10).withDayOfMonth(1), "xyz", "Commit message 2.1", "branch2");

        CommitDetails cm5 = new CommitDetails();
        cm5.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(9).withDayOfMonth(25), "abc", "Commit message 1.2", "branch1");

        CommitDetails cm6 = new CommitDetails();
        cm6.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(7).withDayOfMonth(25), "xyz", "Commit message 1.3", "branch1");

        commitDetails.addAll(Arrays.asList(cm1, cm2, cm3, cm4, cm5, cm6));

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Number od commits per branch");
        expectedResult.add("branch1 3");
        expectedResult.add("master 2");
        expectedResult.add("branch2 1");

        //Act
        List<String> dataSet = cpbm.createDataSet(commitDetails, new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1), new DateTime().withYear(2017).withMonthOfYear(12).withDayOfMonth(31));

        //Assert
        for(int i=0;i<dataSet.size();i++)
            assert Objects.equals(dataSet.get(i), expectedResult.get(i));
    }

    @After
    public void tearUp(){

    }
}
