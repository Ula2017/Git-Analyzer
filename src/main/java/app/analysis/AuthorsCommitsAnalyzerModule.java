package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AuthorsCommitsAnalyzerModule extends AbstractAnalyzerModule {
    private List<DateTime> authorsCommitDateTimeList;
    private List<DateTime> commitsInPeriodOfTimeList;
    private DateTime projectStartDate;
    private DateTime projectEndDate;
    private static int numberOfIntervals = 3;

    @Override
    public String toString(){
        return "Authors Commits Analyzer";
    }
    
	@Override
	public Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException {
        ImageView node = new ImageView();
        node.setFitWidth(width);
        node.setImage(new Image(createFileWithChart(commitDetails, guiDetails.getFrom(), guiDetails.getTo(), guiDetails.getCommitterName()).toURI().toURL().toString()));
		return node;
	}

    private int createListWithCommits(List<CommitDetails> commitDetails, String committerName) {
    	authorsCommitDateTimeList = new ArrayList<DateTime>();
        List<CommitDetails> commitsForAuthor = commitDetails.stream()
                .filter(x -> x.getAuthorName().equals(committerName))	
                .collect(Collectors.toList());
        
        for(CommitDetails comDetails: commitsForAuthor){
        	authorsCommitDateTimeList.add(comDetails.getCommitDate());
        }
        
    	return authorsCommitDateTimeList.size();
    }
    
    private int findCommitsInPeriodOfTime(DateTime startDate, DateTime endDate) {
    	commitsInPeriodOfTimeList = new ArrayList<DateTime>();
        List<DateTime> commitsForAuthorInPeriodOfTime = authorsCommitDateTimeList.stream()
        		.filter(x -> x.compareTo(startDate) >= 0 && x.compareTo(endDate) < 0) //x >= startDate && x < endDate <- musi sie miescic w przedziale czasu
                .collect(Collectors.toList());

        for (int i = 0; i < commitsForAuthorInPeriodOfTime.size(); i++)
        	commitsInPeriodOfTimeList.add(commitsForAuthorInPeriodOfTime.get(i));

    	return commitsInPeriodOfTimeList.size();
    }
    
	private File createFileWithChart(List<CommitDetails> commitDetails, DateTime from, DateTime to, String committerName) throws CreateImageException {
		String outputPath = getPathForOutput();
		List<String> symbolAxis = new LinkedList<>();
        int width = 640;
        int height = 480;
		projectStartDate = commitDetails.get(commitDetails.size() - 1).getCommitDate();
        projectEndDate = commitDetails.get(0).getCommitDate();
		
        createListWithCommits(commitDetails, committerName);
        DefaultCategoryDataset dataset = createDataset(committerName, symbolAxis);
                
        JFreeChart chart = ChartFactory.createBarChart(
                "Number of commits of a chosen user.",
                "Date",
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
	
	private DefaultCategoryDataset createDataset(String committerName, List<String> symbolAxis){
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int interval = (Hours.hoursBetween(projectStartDate, projectEndDate).getHours() ) / numberOfIntervals;
        
        DateTime periodStartDate = projectStartDate;
        Integer numberOfCommitsInPeriod = 0;
        
        while (periodStartDate.compareTo(projectEndDate) < 0){		
        	numberOfCommitsInPeriod = findCommitsInPeriodOfTime(periodStartDate, periodStartDate.plusHours(interval));   	
        	symbolAxis.add(String.format("%d/%d/%d", periodStartDate.getDayOfMonth(), periodStartDate.getMonthOfYear()%100, periodStartDate.getYear()%10000));
        	dataset.addValue(numberOfCommitsInPeriod, committerName, String.format("%d/%d/%d", periodStartDate.getDayOfMonth(), periodStartDate.getMonthOfYear()%100, periodStartDate.getYear()%10000));
        	periodStartDate = periodStartDate.plusHours(interval);
        }

        return dataset;
    }
}
