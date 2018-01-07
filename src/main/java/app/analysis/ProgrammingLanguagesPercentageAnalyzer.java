package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgrammingLanguagesPercentageAnalyzer extends AbstractAnalyzerModule {
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
		this.commitDetails = commitDetails;
		this.from = guiDetails.getFrom();
		this.to = guiDetails.getTo();
		return createFileWithChart();
	}

    private File createFileWithChart() throws Exception {
        String outputPath = getPathForOutput();

        JFreeChart chart = ChartFactory.createPieChart("Lines of code", // chart title
                createDataset(),
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

	private DefaultPieDataset createDataset() throws Exception {
		Map<String, Integer> types = new HashMap<String, Integer>();
		extensions.forEach(e -> types.put(e, 0));

		List<CommitDetails> commitDetailsWithinDate = commitDetails.stream()
                .filter(cm -> Long.compare(cm.getCommitDate().getMillis(), from.getMillis()) > 0
                        && Long.compare(cm.getCommitDate().getMillis(), to.getMillis()) < 0)
                .collect(Collectors.toList());

		commitDetailsWithinDate.forEach(cd -> cd.getFiles().forEach(fd -> {
            int indexOfLastDot = fd.getFileName().lastIndexOf(".");
            if(indexOfLastDot != -1){
                String extension = fd.getFileName().substring(indexOfLastDot);
                if(extensions.contains(extension))
                    types.replace(extension, types.get(extension)+fd.getInsertions()-fd.getDeletions());
            }
        }));

		DefaultPieDataset dataset = new DefaultPieDataset();
        types.keySet().forEach(key -> {
            int value = types.get(key);
            if(value > 0)
                dataset.setValue(key, value);
        });

		return dataset;
	}
}