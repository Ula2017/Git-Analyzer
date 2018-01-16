package app.analysis.Test;

import app.analysis.ClassificationModule;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
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
public class ClassificationTest {
    private ClassificationModule cm;
    private List<CommitDetails> commitDetails;

    @Before
    public void setUp(){
        cm = new ClassificationModule();
        commitDetails = new ArrayList<>();
    }

    @Test
    public void test1() throws Exception {
        //Prepare
        CommitDetails cm1 = new CommitDetails();
        cm1.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(11).withDayOfMonth(1), "xyz", "Commit message 1.1", "master");
        FileDiffs fd11 = new FileDiffs();
        fd11.setInformation("file1.java", 20, 2);
        cm1.addFile(fd11);
        FileDiffs fd12 = new FileDiffs();
        fd12.setInformation("file2.java", 30, 3);
        cm1.addFile(fd12);

        CommitDetails cm2 = new CommitDetails();
        cm2.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "xyz", "Commit message 2.1", "master");
        FileDiffs fd21 = new FileDiffs();
        fd21.setInformation("file3.java", 10, 8);
        cm2.addFile(fd21);
        FileDiffs fd22 = new FileDiffs();
        fd22.setInformation("file4.java", 25, 8);
        cm2.addFile(fd22);
        FileDiffs fd23 = new FileDiffs();
        fd23.setInformation("file5.java", 12, 1);
        cm2.addFile(fd23);

        CommitDetails cm3 = new CommitDetails();
        cm3.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "abc", "Commit message 1.1", "branch");
        FileDiffs fd31 = new FileDiffs();
        fd31.setInformation("file6.java", 10, 3);
        cm3.addFile(fd31);
        FileDiffs fd32 = new FileDiffs();
        fd32.setInformation("file7.java", 19, 17);
        cm3.addFile(fd32);
        FileDiffs fd33 = new FileDiffs();
        fd33.setInformation("file8.java", 14, 13);
        cm3.addFile(fd33);

        commitDetails.addAll(Arrays.asList(cm1, cm2, cm3));

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("Rank of commits");
        expectedResult.add("1. xyz 2");
        expectedResult.add("2. abc 1");
        expectedResult.add("Rank of insertions");
        expectedResult.add("1. xyz 97");
        expectedResult.add("2. abc 43");
        expectedResult.add("Rank of deletions");
        expectedResult.add("1. abc 33");
        expectedResult.add("2. xyz 22");

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
