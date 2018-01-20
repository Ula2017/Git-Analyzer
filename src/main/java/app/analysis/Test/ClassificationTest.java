package app.analysis.Test;

import app.analysis.ClassificationModule;
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
 * Created by Karol on 2018-01-16.
 */

@RunWith(MockitoJUnitRunner.class)
public class ClassificationTest extends AbstractTest{
    private ClassificationModule cm;
    private List<CommitDetails> commitDetails;

    @Before
    public void setUp(){
        cm = new ClassificationModule();
        commitDetails = new ArrayList<>();
    }

    @Test
    public void createDatasetTest() throws Exception {
        //Prepare
        CommitDetails cm1 = createCommitDetails(new DateTime().withYear(2017).withMonthOfYear(11).withDayOfMonth(1),
                "xyz", "Commit message 1.1", "master",
                createFileDiff("file1.java", 20, 2),
                createFileDiff("file2.java", 30, 3));

        CommitDetails cm2 = createCommitDetails(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1),
                "xyz", "Commit message 2.1", "master",
                createFileDiff("file3.java", 10, 8),
                createFileDiff("file4.java", 25, 8),
                createFileDiff("file5.java", 12, 1));

        CommitDetails cm3 = createCommitDetails(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1),
                "abc", "Commit message 1.1", "branch",
                createFileDiff("file6.java", 10, 3),
                createFileDiff("file7.java", 19, 17),
                createFileDiff("file8.java", 14, 13));

        commitDetails.addAll(Arrays.asList(cm1, cm2, cm3));

        List<String> expectedResult = Arrays.asList("Rank of commits", "1. xyz 2", "2. abc 1",
                "Rank of insertions", "1. xyz 97", "2. abc 43",
                "Rank of deletions", "1. abc 33", "2. xyz 22");

        //Act
        List<String> dataSet = cm.createDataSet(commitDetails, new DateTime().withYear(2016).withMonthOfYear(1).withDayOfMonth(1), new DateTime().withYear(2017).withMonthOfYear(12).withDayOfMonth(31));

        //Assert
        for(int i=0;i<dataSet.size();i++)
            assert Objects.equals(dataSet.get(i), expectedResult.get(i));
    }

    @After
    public void tearUp(){

    }
}
