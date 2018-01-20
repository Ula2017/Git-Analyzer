package app.analysis;
import app.structures.CommitDetails;
import app.structures.GUIDetails;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;
import org.joda.time.Hours;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepoCommitsModule extends AbstractAnalyzerModule {
    private static int numberOfIntervals = 10;
    private List<DateTime> commitsDateTimeList;
    private List<DateTime> commitsInPeriodOfTimeList;
    private DateTime projectStartDate = DateTime.now().minusYears(1), projectEndDate = DateTime.now();

	@Override
	public String toString() {
		return "Commits in repository";
	}

    @Override
    public Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException {
        ImageView node = new ImageView();
        node.setFitWidth(width);
        node.setImage(new Image(createDiagram(commitDetails, guiDetails.getFrom(), guiDetails.getTo()).toURI().toURL().toString()));
        return node;
    }

    private int createListWithCommits(List<CommitDetails> commitDetails) {
    	commitsDateTimeList = new ArrayList<DateTime>();
              
        for(CommitDetails comDetails: commitDetails)
        	commitsDateTimeList.add(comDetails.getCommitDate());        

    	return commitsDateTimeList.size();
    }
    
    
    private int findCommitsInPeriodOfTime(DateTime startDate, DateTime endDate) {
    	commitsInPeriodOfTimeList = new ArrayList<DateTime>();			//czy na pewno tworzy sie nowa lista
        List<DateTime> commitsListTmp = commitsDateTimeList.stream()
        		.filter(x -> x.compareTo(startDate) >= 0 && x.compareTo(endDate) < 0) //x >= startDate && x < endDate <- musi sie miescic w przedziale czasu
                .collect(Collectors.toList());        
          
        for (int i = 0; i < commitsListTmp.size(); i++)
        	commitsInPeriodOfTimeList.add(commitsListTmp.get(i));
        
    	return commitsInPeriodOfTimeList.size();
    }
    
    
    private XYDataset createDataset(){
        XYSeries series = new XYSeries("Number of commits in whole project.");

        int interval = (Hours.hoursBetween(projectStartDate, projectEndDate).getHours() ) / numberOfIntervals;
        DateTime periodStartDate = projectStartDate;
        int hourOfProjectExistance = 0;      
        int numberOfCommitsInPeriod = 0;
        
        while (periodStartDate.compareTo(projectEndDate) < 0){				//periodStartDate < projectEndDate
        	numberOfCommitsInPeriod += findCommitsInPeriodOfTime(periodStartDate, periodStartDate.plusHours(interval));
        	series.add(hourOfProjectExistance, numberOfCommitsInPeriod);
        	periodStartDate = periodStartDate.plusHours(interval);
        	hourOfProjectExistance+=interval;
        }
        
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }   
    
    
    private File createDiagram(List<CommitDetails> commitDetails, DateTime from, DateTime to){
        String path = getPathForOutput();
        int width = 640;
        int height = 480;
             
        int res = createListWithCommits(commitDetails);
        if (res < 1){
        	//jesli pusta lista to psuje sie diagram, do poprawy 
        }
      

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
