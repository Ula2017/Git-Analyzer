package app.analysis;

import app.fetch.CommitDetails;
import app.fetch.IDTO;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthlyAuthorsCounterAnalyzerModule extends IAnalyzerModule{
    private List<CommitDetails> commits;

    @Override
    public String getName() {
        return "Monthly ammount of commiters";
    }

    @Override
    public String generateFile(List<IDTO> data) {
        commits = (List<CommitDetails>)(List<?>) data;
        String path = createChart();
        return "file:"+path;
    }

    private String createChart() {
        String path = "images/authorsMonthly.jpg";
        int height = 480;
        int width = 640;

        List<String> symbolAxis = new LinkedList<>();
        XYDataset dataset = createDataset(symbolAxis);

        SymbolAxis sa = new SymbolAxis("AxisLabel", symbolAxis.toArray(new String[0]));

        JFreeChart chart = ChartFactory.createScatterPlot("Number of authors each month",
                "Month/Year",
                "Authors",
                dataset);
        chart.getXYPlot().setDomainAxis(sa);
        try {
            ChartUtilities.saveChartAsJPEG(new File(path), chart, width, height);
        }
        catch (Exception e) {
            System.err.println("Problem occurred creating chart.");
        }

        return path;
    }

    private XYDataset createDataset(List<String> symbolAxis){
        XYSeries series = new XYSeries("Number of authors");

        for(int i=0, year = from.getYear();year <= to.getYear(); year++){
            for(int month = year != from.getYear() ? 1 : from.getMonthOfYear();
                month <= (year != to.getYear() ? 12 : to.getMonthOfYear());
                month++, i++){
                series.add(i, countAuthors(year, month));
                symbolAxis.add(String.format("%d/%d", month, year%100));
            }
        }

        XYSeriesCollection dataSet = new XYSeriesCollection();
        dataSet.addSeries(series);
        return dataSet;
    }

    private int countAuthors(int year, int month){
        HashSet<String> namesSet = new HashSet<>();
        List<CommitDetails> commitsForYearAndMonth = commits.stream()
                .filter(x -> x.getCommitDate().getYear() == year && x.getCommitDate().getMonthOfYear() == month)
                .collect(Collectors.toList());

        for(CommitDetails comDetails: commitsForYearAndMonth){
            String name = comDetails.getAuthorName();
            namesSet.add(name);
        }

        return namesSet.size();
    }
}