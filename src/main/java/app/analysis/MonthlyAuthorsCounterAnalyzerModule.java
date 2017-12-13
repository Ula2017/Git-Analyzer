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
import java.util.HashSet;
import java.util.List;

public class MonthlyAuthorsCounterAnalyzerModule implements IAnalyzerModule{
    private static int year = 2017; //TODO
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
        HashSet<String> namesSet = new HashSet<>();
        List<CommitDetails> commitDetailsList = fetcher.getMonthlyRaport(month, year);

        for(CommitDetails comDetails: commitDetailsList){
            String name = comDetails.getAuthorName();
            namesSet.add(name);
        }

        return namesSet.size();
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