package app.analysis;

import app.fetch.Fetcher;
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

public class RepoCommitsAnalyzerModule implements IAnalyzerModule{
    private static int numberOfIntervals = 5;

    private List<DateTime> dateTimeList;
    private DateTime projectStartDate;
    private DateTime projectEndDate;
    private Fetcher fetcher;

    public RepoCommitsAnalyzerModule(){
        fetcher = new Fetcher();
        this.dateTimeList = createDemoData();
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
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String generateFile() {
        String path = createDiagram();
        return "file:"+path;
    }

    private XYDataset createDataset(){
        XYSeries series = new XYSeries("Number of commits in whole project.");

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

    private String createDiagram(){
        String path = "images/repoCommits.jpg";
        int width = 640;
        int height = 480;

        XYDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Number of commits in a project dependent on hour of the project existance.",
                "Hour of project existance",
                "Number of commits",
                dataset);

        try {
            ChartUtilities.saveChartAsJPEG(new File(path), chart, width, height);
        }
        catch (Exception e) {
            System.err.println("Problem occurred creating chart.");
        }

        return path;
    }
}