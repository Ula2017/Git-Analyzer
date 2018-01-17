package app.analysis;

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

public class AuthorsCommitsAnalyzerModule extends AbstractAnalyzerModule {

    private List<DateTime> authorsCommitDateTimeList;
    private List<DateTime> commitsInPeriodOfTimeList;
    private DateTime projectStartDate;
    private DateTime projectEndDate;
    private static int numberOfIntervals = 10;

    @Override
    public String toString(){
        return "Authors Commits Analyzer";
    }
    
	@Override
	public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException {
		return createFileWithChart(commitDetails, guiDetails.getFrom(), guiDetails.getTo(), guiDetails.getCommitterName());
	}

    
    private int createListWithCommits(List<CommitDetails> commitDetails, String committerName) {
    	authorsCommitDateTimeList = new ArrayList<DateTime>();
        List<CommitDetails> commitsForAuthor = commitDetails.stream()
                .filter(x -> x.getAuthorName().equals(committerName))	
                .collect(Collectors.toList());
        
        for(CommitDetails comDetails: commitsForAuthor){
        	authorsCommitDateTimeList.add(comDetails.getCommitDate());
        }
        
        /*
        System.out.println("Autorzy - daty commitow:");
        for(int i = 0; i < authorsCommitDateTimeList.size(); i++)
        	System.out.println(authorsCommitDateTimeList.get(i));
        */
    	
    	return authorsCommitDateTimeList.size();
    }
    
    private int findCommitsInPeriodOfTime(DateTime startDate, DateTime endDate) {
    	commitsInPeriodOfTimeList = new ArrayList<DateTime>();
        List<DateTime> commitsForAuthorInPeriodOfTime = authorsCommitDateTimeList.stream()
        		.filter(x -> x.compareTo(startDate) >= 0 && x.compareTo(endDate) < 0) //x >= startDate && x < endDate <- musi sie miescic w przedziale czasu
                .collect(Collectors.toList());
        
          
        for (int i = 0; i < commitsForAuthorInPeriodOfTime.size(); i++)
        	commitsInPeriodOfTimeList.add(commitsForAuthorInPeriodOfTime.get(i));
        
        /*
        System.out.println("Daty commitow po przefiltrowaniu: ");
        for(int i = 0; i < commitsInPeriodOfTimeList.size(); i++)
        	System.out.println(commitsInPeriodOfTimeList.get(i));
    	*/
    	return commitsInPeriodOfTimeList.size();
    }
    
    
	private File createFileWithChart(List<CommitDetails> commitDetails, DateTime from, DateTime to, String committerName) throws CreateImageException {
		String outputPath = getPathForOutput();
        int width = 640;
        int height = 480;
		//tutaj powinna from to i guiDetails
		projectStartDate = commitDetails.get(commitDetails.size() - 1).getCommitDate();				
        projectEndDate = commitDetails.get(0).getCommitDate();
		
        int res = createListWithCommits(commitDetails, committerName);
        if (res < 1){
//        	System.out.println("Unko!  " + res);
        }
      
        XYDataset dataset = createDataset(committerName);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Number of commits of a chosen user dependent on hour of the project existance.",
                "Hour of project existance",
                "Number of commits",
                dataset);

        File outputFile = new File(outputPath);
        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
        }
        catch (Exception e) {																//poprawic
            System.err.println("Problem occurred creating chart.");
        }		
		return outputFile;
	}
	
	private XYDataset createDataset(String committerName){
        XYSeries series = new XYSeries("Number of commits for " + committerName);	
        
        
//        System.out.println("projectStartDate: " + projectStartDate + ", projectEndDate: " + projectEndDate);
        //RepoCommits(projectStartDate, projectEndDate);
        
        int interval = (Hours.hoursBetween(projectStartDate, projectEndDate).getHours() ) / numberOfIntervals;
        
        DateTime periodStartDate = projectStartDate;
        int hourOfProjectExistance = 0;      
        int numberOfCommitsInPeriod = 0;
        
        
        while (periodStartDate.compareTo(projectEndDate) < 0){				//periodStartDate < projectEndDate
        	numberOfCommitsInPeriod = findCommitsInPeriodOfTime(periodStartDate, periodStartDate.plusHours(interval));
//        	System.out.println("Number of commits: ");
//        	System.out.println(numberOfCommitsInPeriod);
        	series.add(hourOfProjectExistance, numberOfCommitsInPeriod);
        	periodStartDate = periodStartDate.plusHours(interval);
        	hourOfProjectExistance+=interval;
        }
            
        
        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }
}
