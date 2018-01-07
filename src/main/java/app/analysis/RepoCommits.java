package app.analysis;

import app.fetch.Fetcher;
import app.structures.CommitDetails;
import app.structures.ModuleNames;
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

//    private List<DateTime> createDemoData(){
//        DateTime tmpStartDate = new DateTime(1996, 7, 14, 0, 0);
//        List<DateTime> trialList = new ArrayList<DateTime>();
//        for (int i = 0; i < 10; i++){
//            trialList.add(tmpStartDate);
//            tmpStartDate = tmpStartDate.plusDays(1);
//        }
//        return trialList;
//    }

    @Override
    public ModuleNames getModuleName() {
        return ModuleNames.MODULE2;
    }

    public String generateFile(List<CommitDetails> commitDetails) throws Exception {
       // this.dateTimeList = data.stream().map(CommitDetails::getCommitDate).collect(Collectors.toList());
        commits = commitDetails;

        return "file:"+createDiagram();
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

    private String createDiagram(){
        String path = ModuleNames.getPathForOutput(ModuleNames.MODULE2);
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

        return path;
    }
}
