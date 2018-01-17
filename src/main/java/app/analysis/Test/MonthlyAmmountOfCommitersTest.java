package app.analysis.Test;

import app.analysis.CreateImageException;
import app.analysis.MonthlyAmmountOfCommitersModule;
import app.structures.CommitDetails;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Karol on 2018-01-16.
 */

@RunWith(MockitoJUnitRunner.class)
public class MonthlyAmmountOfCommitersTest extends AbstractTest {
    private MonthlyAmmountOfCommitersModule maocm;
    private List<CommitDetails> commitDetails;

    @Before
    public void setUp(){
        maocm = new MonthlyAmmountOfCommitersModule();
        commitDetails = new ArrayList<>();
    }

    @Test
    public void createFileWithChartShouldThrowExceptionWhenWrongPath(){
        try {
            maocm.createFileWithChart(commitDetails, new DateTime().withYear(2017).withMonthOfYear(11).withDayOfMonth(1),
                    new DateTime().withYear(2018).withMonthOfYear(1).withDayOfMonth(1), "someStupidPath\\whichDoesNotExist\\file.jpg");
            fail("Expected an CreateImageException to be thrown");
        } catch (CreateImageException e) {
            assertTrue(Objects.equals(e.getMessage(), "Problem creating image with chart for MonthlyAmmountOfCommitersModule"));
        }
    }

    @Test
    public void createDatasetTest() throws Exception {
        //Prepare
        CommitDetails cm11 = new CommitDetails();
        cm11.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(11), "karolb", "Commit message 1.1", "master");
        commitDetails.add(cm11);
        CommitDetails cm12 = new CommitDetails();
        cm12.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(12), "chudy1997", "Commit message 1.2", "master");
        commitDetails.add(cm12);
        CommitDetails cm13 = new CommitDetails();
        cm13.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(13), "lolek308", "Commit message 1.3", "master");
        commitDetails.add(cm13);
        CommitDetails cm21 = new CommitDetails();
        cm21.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(25), "karolb", "Commit message 2.1", "master");
        commitDetails.add(cm21);
        CommitDetails cm22 = new CommitDetails();
        cm22.setPrimaryInformation(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(26), "chudy1997", "Commit message 2.1", "master");
        commitDetails.add(cm22);
        CommitDetails cm31 = new CommitDetails();
        cm31.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(1).withDayOfMonth(1), "karolb", "Commit message 3.1", "master");
        commitDetails.add(cm31);
        CommitDetails cm32 = new CommitDetails();
        cm32.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(1).withDayOfMonth(31), "karolb", "Commit message 3.2", "master");
        commitDetails.add(cm32);
        CommitDetails cm41 = new CommitDetails();
        cm41.setPrimaryInformation(new DateTime().withYear(2017).withMonthOfYear(5).withDayOfMonth(31), "karolb", "Commit message 4.1", "master");
        commitDetails.add(cm41);

        ArrayList<String> symbolAxis = new ArrayList<>();
        List<Integer> expectedResult = Arrays.asList(3, 2, 1, 0, 0, 0, 1);

        //Act
        XYSeriesCollection dataSet = maocm.createDataset(commitDetails, new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(11),
                new DateTime().withYear(2017).withMonthOfYear(5).withDayOfMonth(31), symbolAxis);
        XYSeries series = dataSet.getSeries("Number of authors");

        //Assert
        for (int i = 0; i < series.getItemCount(); i++) {
            assert Objects.equals(expectedResult.get(i), series.getDataItem(i).getY());
        }
    }

    @After
    public void tearUp(){

    }
}
