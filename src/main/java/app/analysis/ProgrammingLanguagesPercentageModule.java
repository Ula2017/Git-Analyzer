package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.File;
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
	public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
		return createFileWithChart(commitDetails);
	}

    private File createFileWithChart(List<CommitDetails> commitDetails) throws Exception {
        String outputPath = getPathForOutput();

        JFreeChart chart = ChartFactory.createPieChart("Lines of code", // chart title
                createDataset(getLinesForLanguages(commitDetails)),
                true,
                true,
                false);

        File outputFile = new File(outputPath);
        try {
            ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
        }
        catch (Exception e) {
            throw new Exception("Problem occurred creating chart.");
        }

        return outputFile;
    }

	private DefaultPieDataset createDataset(Map<String, Integer> languages) throws Exception {
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