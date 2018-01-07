package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;

import java.io.File;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthlyAmmountOfCommiters extends AbstractAnalyzerModule {
    @Override
    public String toString(){
        return "Monthly ammount of commiters";
    }

    @Override
    public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
        this.commitDetails = commitDetails;
        this.from = guiDetails.getFrom();
        this.to = guiDetails.getTo();
        return createFileWithChart();
    }

    private File createFileWithChart() throws Exception {
        List<String> symbolAxis = new LinkedList<>();
        XYDataset dataSet = createDataset(symbolAxis);
        SymbolAxis sa = new SymbolAxis("AxisLabel", symbolAxis.toArray(new String[0]));

        JFreeChart chart = ChartFactory.createScatterPlot("Number of authors each month",
                "Month/Year",
                "Authors",
                dataSet);
        chart.getXYPlot().setDomainAxis(sa);

        File outputFile = new File(getPathForOutput());
        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
        }
        catch (Exception e) { throw new Exception("Problem occurred creating chart."); }

        return outputFile;
    }

    private XYDataset createDataset(List<String> symbolAxis){
        XYSeries series = new XYSeries("Number of authors");

        DateTime firstDate = commitDetails.stream().map(CommitDetails::getCommitDate)
                .min(Comparator.comparingLong(BaseDateTime::getMillis)).get();
        DateTime lastDate = commitDetails.stream().map(CommitDetails::getCommitDate)
                .max(Comparator.comparingLong(BaseDateTime::getMillis)).get();

        for(int i=0, year = firstDate.getYear();year <= lastDate.getYear(); year++){
            for(int month = year != firstDate.getYear() ? 1 : firstDate.getMonthOfYear();
                month <= (year != lastDate.getYear() ? 12 : lastDate.getMonthOfYear());
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
        List<CommitDetails> commitsForYearAndMonth = commitDetails.stream()
                .filter(x -> x.getCommitDate().getYear() == year && x.getCommitDate().getMonthOfYear() == month)
                .collect(Collectors.toList());

        for(CommitDetails comDetails: commitsForYearAndMonth){
            String name = comDetails.getAuthorName();
            namesSet.add(name);
        }

        return namesSet.size();
    }
}