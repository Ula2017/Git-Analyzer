package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgrammingLanguagesPercentageModule extends AbstractAnalyzerModule {
    private List<String> extensions = Arrays.asList(".java", ".cpp", ".cs", ".py",
            ".c", ".h",
            ".js", ".html", ".css", ".php",
            ".sql",
            ".json", ".xml");

	@Override
	public String toString() {
		return "Percentage of lines in different programming languages";
	}

    @Override
    public Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException {
        ImageView node = new ImageView();
        node.setFitWidth(width);
        node.setImage(new Image(createFileWithChart(commitDetails, getPathForOutput()).toURI().toURL().toString()));
        return node;
    }

    public File createFileWithChart(List<CommitDetails> commitDetails, String outputPath) throws CreateImageException {
        JFreeChart chart = ChartFactory.createPieChart("Lines of code", // chart title
                createDataset(commitDetails),
                true,
                true,
                false);

        File outputFile = new File(outputPath);
        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
        }
        catch (NullPointerException e){
            throw new CreateImageException("Output path for image is not correct");
        }
        catch (IOException e) {
            throw new CreateImageException("Problem creating image with chart for ProgrammingLanguagesPercentageModule");
        }

        return outputFile;
    }

	public DefaultPieDataset createDataset(List<CommitDetails> commitDetails) {
        Map<String, Integer> languages = getLinesForLanguages(commitDetails);
		DefaultPieDataset dataset = new DefaultPieDataset();
        languages.keySet().forEach(key -> {
            int value = languages.get(key);
            if(value > 0)
                dataset.setValue(key, value);
        });

		return dataset;
	}

	public Map<String, Integer> getLinesForLanguages(List<CommitDetails> commitDetails){
        Map<String, Integer> languages = new HashMap<String, Integer>();
        extensions.forEach(e -> languages.put(e, 0));

        commitDetails.forEach(cd -> cd.getFiles().forEach(fd -> {
            int indexOfLastDot = fd.getFileName().lastIndexOf(".");
            if(indexOfLastDot != -1){
                String extension = fd.getFileName().substring(indexOfLastDot);
                if(extensions.contains(extension))
                    languages.replace(extension, languages.get(extension)+fd.getInsertions()-fd.getDeletions());
            }
        }));
        return languages;
    }
}