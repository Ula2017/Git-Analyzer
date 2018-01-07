package app.analysis;

import app.fetch.Fetcher;
import app.structures.CommitDetails;
import app.structures.GUIDetails;
//import app.structures.ModuleNames;
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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class RepoCommits extends AbstractAnalyzerModule {
    private static int numberOfIntervals = 5;

    private List<CommitDetails> commits;
    private DateTime projectStartDate = DateTime.now().minusYears(1), projectEndDate = DateTime.now();

        private List<DateTime> dateTimeList;
//    private DateTime projectStartDate;
//    private DateTime projectEndDate;
//    private Fetcher fetcher;

    private int RepoCommits(int year, int month) {

        HashSet<DateTime> dateSet = new HashSet<>();
        List<CommitDetails> commitsForYearAndMonth =  commits.stream()
               .filter(x -> x.getCommitDate().getYear() == year && x.getCommitDate().getMonthOfYear() == month)
               .collect(Collectors.toList());
        for(CommitDetails comDetails: commitsForYearAndMonth){
            DateTime date = comDetails.getCommitDate();
            dateSet.add(date);
        }

        return dateSet.size();

    }

    @Override
    public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
        this.commitDetails = commitDetails;
        this.from = guiDetails.getFrom();
        this.to = guiDetails.getTo();
        return createDiagram();
    }

    public String generateFile(List<CommitDetails> commitDetails) throws Exception {
       // this.dateTimeList = data.stream().map(CommitDetails::getCommitDate).collect(Collectors.toList());
        
    }

    
    private XYDataset createDataset(){
        XYSeries series = new XYSeries("Number of commits in whole project.");

        int interval = (Hours.hoursBetween(projectStartDate, projectEndDate).getHours() ) / numberOfIntervals;
        DateTime nextInterval = projectStartDate.plusHours(interval);

        int numberOfCommits;
        int hourOfProjectExistance = 0;

        for(int i=0, year = projectStartDate.getYear();year <= projectEndDate.getYear(); year++){
            for(int month = year != projectStartDate.getYear() ? 1 : projectStartDate.getMonthOfYear();
                month <= (year != projectEndDate.getYear() ? 12 : projectEndDate.getMonthOfYear());
                month++, i++){
                hourOfProjectExistance+=interval;
                numberOfCommits=RepoCommits(year,month);
                series.add(hourOfProjectExistance, numberOfCommits);
                nextInterval = nextInterval.plusHours(interval);
            }
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private File createDiagram(){
        String path = getPathForOutput();
        int width = 640;
        int height = 480;

        XYDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Number of commits in a project dependent on hour of the project existance.",
                "Hour of project existance",
                "Number of commits",
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
