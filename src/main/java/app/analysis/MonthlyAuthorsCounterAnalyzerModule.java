package app.analysis;
import app.fetch.CommitDetails;
import app.fetch.Fetcher;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MonthlyAuthorsCounterAnalyzerModule implements IAnalyzerModule{
    private int year = 2017;
    private List<CommitDetails> commitDetailsList;
    private String names ;
    private List<String> namesList = new ArrayList<>();
    private Fetcher fetcher;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String generateFile() {
        String path = createChart();
        return "file:"+path;
    }

    private int countAuthors(int month, int year){
        fetcher = new Fetcher();
        for(CommitDetails comDetails: fetcher.getMonthlyRaport(month, year)){
            names = comDetails.getAuthorName();
            namesList.add(names);
        }

        return (new HashSet<>(namesList)).size();
    }

    private XYDataset createDataset(int year){
        XYSeries series = new XYSeries("Number of authors");
        for(int i = 1; i<=12; i++)
            series.add(i, countAuthors(i,year));

        XYSeriesCollection dataset = new XYSeriesCollection();
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
        }
        catch (Exception e) {
            System.err.println("Problem occurred creating chart.");
        }

        return path;
    }
}