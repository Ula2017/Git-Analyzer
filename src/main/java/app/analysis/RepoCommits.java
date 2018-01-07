package app.analysis;

import app.fetch.Fetcher;
import app.structures.CommitDetails;
import app.structures.GUIDetails;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepoCommits extends AbstractAnalyzerModule {
    private static int numberOfIntervals = 5;

    private List<DateTime> dateTimeList;
    private DateTime projectStartDate;
    private DateTime projectEndDate;
    private Fetcher fetcher;

    public RepoCommits(){
     //   fetcher = new Fetcher();
//        this.dateTimeList = createDemoData();
        //jak dziewczyny dodadza swoj modul to tutaj trzeba bedzie dodac import i to odkomentowac, a to na gorze zakomentowac
//        this.dateTimeList = fetcher.getCommitsDates();
    }

    private List<DateTime> createDemoData(){
        DateTime tmpStartDate = new DateTime(1996, 7, 14, 0, 0);
        List<DateTime> trialList = new ArrayList<DateTime>();
        for (int i = 0; i < 10; i++){
            trialList.add(tmpStartDate);
            tmpStartDate = tmpStartDate.plusDays(1);
        }
        return trialList;
    }

    @Override
    public String toString() {
        return "Repository commit details";
    }

    @Override
    public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
        this.commitDetails = commitDetails;
        this.from = guiDetails.getFrom();
        this.to = guiDetails.getTo();
        this.dateTimeList = this.commitDetails.stream().map(CommitDetails::getCommitDate).collect(Collectors.toList());
        return createDiagram();
    }

    private XYDataset createDataset(){
        XYSeries series = new XYSeries("Number of commitDetails in whole project.");

        projectStartDate = dateTimeList.get(0);
        projectEndDate = dateTimeList.get(dateTimeList.size() - 1);

        int interval = (Hours.hoursBetween(projectStartDate, projectEndDate).getHours() ) / numberOfIntervals;
        DateTime nextInterval = projectStartDate.plusHours(interval);

        int numberOfCommits = 0;
        int hourOfProjectExistance = 0;
        for (DateTime dt : dateTimeList) {
            numberOfCommits++;
            if (nextInterval.compareTo(dt) < 0){
                hourOfProjectExistance+=interval;
                series.add(hourOfProjectExistance, numberOfCommits);
                nextInterval = nextInterval.plusHours(interval);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private File createDiagram(){
        String path = "images/repoCommits.jpg";
        int width = 640;
        int height = 480;

        XYDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Number of commitDetails in a project dependent on hour of the project existance.",
                "Hour of project existance",
                "Number of commitDetails",
                dataset);

        File outputFile = new File(path);
        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
        }
        catch (Exception e) {
            System.err.println("Problem occurred creating chart.");
        }

        return outputFile;
    }
}