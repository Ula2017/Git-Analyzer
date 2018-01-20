package app.analysis.Test;

import app.analysis.AbstractAnalyzerModule;
import app.analysis.AnalysisFactory;
import app.fetch.Fetcher;
import app.structures.CommitDetails;
import app.structures.GUIDetails;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Karol on 2018-01-17.
 */

@RunWith(MockitoJUnitRunner.class)
public class AnalysisFactoryTest extends AbstractTest{
    private Fetcher f;
    private AbstractAnalyzerModule aam;
    private AnalysisFactory af;
    private List<CommitDetails> commitDetails;

    @Before
    public void setUp(){
        f = Mockito.mock(Fetcher.class);
        aam = Mockito.mock(AbstractAnalyzerModule.class);
        af = new AnalysisFactory(f);
        commitDetails = new ArrayList<>();
    }

    @Test
    public void analysisFactoryTest() throws Exception {
        //prepare
        final String commiterName = "osom komiter";
        CommitDetails cm1 = createCommitDetails(new DateTime().withYear(2017).withMonthOfYear(11).withDayOfMonth(1),
                commiterName, "Commit message 1.1", "master",
                createFileDiff("file1.java", 20, 2),
                createFileDiff("file2.java", 30, 3));

        CommitDetails cm2 = createCommitDetails(new DateTime().withYear(2016).withMonthOfYear(12).withDayOfMonth(1),
                commiterName, "Commit message 2.1", "master",
                createFileDiff("file3.java", 10, 8),
                createFileDiff("file4.java", 25, 8),
                createFileDiff("file5.java", 12, 1));

        commitDetails.addAll(Arrays.asList(cm1, cm2));

        final DateTime startDate = new DateTime().withYear(2016).withMonthOfYear(11).withDayOfMonth(1),
                endDate = new DateTime().withYear(2018).withMonthOfYear(1).withDayOfMonth(1);
        final GUIDetails guiDetails = new GUIDetails(startDate, endDate, commiterName);
        final String fileName = "osom file name";
        final Node node = Mockito.mock(ImageView.class);

        Mockito.when(f.getCommitsFromDateRange(startDate, endDate)).thenReturn(commitDetails);
        Mockito.when(aam.generateNode(commitDetails, guiDetails)).thenReturn(node);
        Mockito.when(aam.toString()).thenReturn(fileName);

        //act
        Node resNode = af.generateNode(aam, guiDetails);

        //assert
        assert resNode.equals(node);
    }

    @After
    public void tearUp(){
    }
}