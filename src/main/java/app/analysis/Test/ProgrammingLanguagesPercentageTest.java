package app.analysis.Test;

import app.analysis.ProgrammingLanguagesPercentageModule;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import org.jfree.data.general.DefaultPieDataset;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

/**
 * Created by Karol on 2018-01-16.
 */

@RunWith(MockitoJUnitRunner.class)
public class ProgrammingLanguagesPercentageTest {
    private ProgrammingLanguagesPercentageModule plpm;
    private List<CommitDetails> commitDetails;

    @Before
    public void setUp(){
        plpm = new ProgrammingLanguagesPercentageModule();
        commitDetails = new ArrayList<>();
    }

    @Test
    public void createDatasetTest() throws Exception {
        //Prepare
        CommitDetails cm1 = new CommitDetails();
        cm1.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(1), "karolb", "Commit message 1.1", "master");
        FileDiffs fd11 = new FileDiffs();
        fd11.setInformation("lala1.js", 20, 2);
        cm1.addFile(fd11);
        FileDiffs fd12 = new FileDiffs();
        fd12.setInformation("lala2.cs", 30, 3);
        cm1.addFile(fd12);
        FileDiffs fd13 = new FileDiffs();
        fd13.setInformation("lala3.html", 40, 4);
        cm1.addFile(fd13);
        FileDiffs fd14 = new FileDiffs();
        fd14.setInformation("lala4.txt", 1, 1);
        cm1.addFile(fd14);
        FileDiffs fd15 = new FileDiffs();
        fd15.setInformation("lala5.js", 20, 30);
        cm1.addFile(fd15);

        CommitDetails cm2 = new CommitDetails();
        cm2.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1), "chudy1997", "Commit message 1.1", "master");
        FileDiffs fd21 = new FileDiffs();
        fd21.setInformation("lele1.js", 10, 3);
        cm2.addFile(fd21);
        FileDiffs fd22 = new FileDiffs();
        fd22.setInformation("lele2.js", 10, 17);
        cm2.addFile(fd22);
        FileDiffs fd23 = new FileDiffs();
        fd23.setInformation("lele3.cs", 10, 13);
        cm2.addFile(fd23);

        commitDetails.addAll(Arrays.asList(cm1, cm2));
        Map<String, Double> expectedResult = new HashMap<>();
        expectedResult.put(".js", 8.0);
        expectedResult.put(".cs", 24.0);
        expectedResult.put(".html", 36.0);

        //Act
        DefaultPieDataset dataSet = plpm.createDataset(commitDetails);

        //Assert
        for(String key: expectedResult.keySet())
            assert Objects.equals(expectedResult.get(key), dataSet.getValue(key));
    }

    @After
    public void tearUp(){

    }
}
