package app.analysis;
import app.fetch.CommitDetails;
import app.fetch.Fetcher;
import app.fetch.RepositoryOpener;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jfree.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;
import org.jfree.ui.*;
import org.joda.time.DateTime;
import org.joda.time.Hours;



public class MonthlyAuthorsCounterAnalyzerModule implements IAnalyzerModule{
	
	
	private int year = 2017;
	private static List<CommitDetails> commitDetailsList;
	private static String names ;
	private static List<String> namesList = new ArrayList<String>();
	static Fetcher provider;
	
	private static int countAuthors(int month, int year){
		provider = new Fetcher();
		commitDetailsList = provider.getMonthlyRaport(month, year);
		for(int i = 0; i<commitDetailsList.size() -1; i++ ){
			names = (commitDetailsList).get(i).getAuthorName();
			namesList.add(names);
		}
		Set<String> uniqueNames = new HashSet<String>(namesList);
		
		return uniqueNames.size();
	}
		
	private XYDataset createDataset(int year){
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series = new XYSeries("Number of authors");
		
		for(int i = 1; i<=12; i++){
			int counter = countAuthors(i,year);
			series.add(i, counter);
		}
		
		dataset.addSeries(series);
		return dataset;
		
	}
	 private String createChart() {
		 
		 	String path = "images/authorsMonthly.jpg";
			int height = 480;
			int width = 640;
			
			XYDataset dataset = createDataset(year);
	        JFreeChart chart = ChartFactory.createScatterPlot(
	            "Number of authors each month",
	            "Month", 
	            "Authors", 
	            dataset
	        );
	        
	        try {
				ChartUtilities.saveChartAsJPEG(new File(path), chart, width, height);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Problem occurred creating chart.");
			}
	        
	        
	        return path;           
	 }

    @Override
    public String getName() {
        return this.getClass().getName();
    }


	@Override
	public String generateFile() {
		String path = createChart();		 
		return "file:"+path;
	}


}
