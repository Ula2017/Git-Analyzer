package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class MonthlyAmmountOfCommitersModule extends AbstractAnalyzerModule {
    @Override
    public String toString(){
        return "Monthly ammount of commiters";
    }

    @Override
    public Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException {
        ImageView node = new ImageView();
        node.setFitWidth(width);
        node.setImage(new Image(createFileWithChart(commitDetails, guiDetails.getFrom(), guiDetails.getTo(), getPathForOutput()).toURI().toURL().toString()));
        return node;
    }

    public File createFileWithChart(List<CommitDetails> commitDetails, DateTime from, DateTime to, String outputPath) throws CreateImageException {
        List<String> symbolAxis = new LinkedList<>();
        XYSeriesCollection dataset = createDataset(commitDetails, from, to, symbolAxis);

        SymbolAxis sa = new SymbolAxis("AxisLabel", symbolAxis.toArray(new String[0]));

        JFreeChart chart = ChartFactory.createScatterPlot("Number of authors each month",
                "Month/Year",
                "Authors",
                dataset);
        chart.getXYPlot().setDomainAxis(sa);

        File outputFile = new File(outputPath);
        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
        }
        catch (NullPointerException e){
            throw new CreateImageException("Output path for image is not correct");
        }
        catch (IOException e) {
            throw new CreateImageException("Problem creating image with chart for MonthlyAmmountOfCommitersModule");
        }

        return outputFile;
    }

    public XYSeriesCollection createDataset(List<CommitDetails> commitDetails, DateTime from, DateTime to, List<String> symbolAxis){
        List<Integer> authors = countAuthors(commitDetails, from, to, symbolAxis);
        XYSeries series = new XYSeries("Number of authors");
        for(int i = 0;i < authors.size(); i++)
            series.add(i, authors.get(i));

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    public List<Integer> countAuthors(List<CommitDetails> commitDetails, DateTime from, DateTime to, List<String> symbolAxis){
        List<Integer> res = new ArrayList<>();
        for(int year = from.getYear(); year <= to.getYear(); year++){
            for(int month = year != from.getYear() ? 1 : from.getMonthOfYear();
                month <= (year != to.getYear() ? 12 : to.getMonthOfYear()); month++){
                res.add(countAuthorsInMonth(commitDetails, year, month));
                symbolAxis.add(String.format("%d/%d", month, year%100));
            }
        }

        return res;
    }

    private int countAuthorsInMonth(List<CommitDetails> commitDetails, int year, int month){
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