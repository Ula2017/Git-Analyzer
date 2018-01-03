package app.analysis;

import app.structures.FileDiffs;
import app.structures.ModuleNames;
import com.google.common.io.Files;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.joda.time.DateTime;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgrammingLanguagesPercentageAnalyzer extends AbstractAnalyzerModule {
	private List<List<FileDiffs>> fileDiffs;
	private DateTime from = DateTime.now().minusYears(1), to = DateTime.now();

	@Override
	public ModuleNames getModuleName() {
		return ModuleNames.MODULE3;
	}

	public String generateFile(List<List<FileDiffs>> fileDiffs) throws Exception {
		this.fileDiffs = fileDiffs;
        return "file:"+createFileWithChart();
	}

    private String createFileWithChart() throws Exception {
        String outputPath = ModuleNames.getPathForOutput(ModuleNames.MODULE3);
        int height = 480;
        int width = 640;

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

        return outputPath;
    }

	private DefaultPieDataset createDataset() throws Exception {
		Map<String, Integer> types = new HashMap<String, Integer>();
		int lines;

		for (List<FileDiffs> resultList : fileDiffs) {
			for (FileDiffs fileDiffs : resultList) {
				String path = fileDiffs.getFilePath();
				String ext = Files.getFileExtension(path);
				lines = countLines(path);

				if (!types.containsKey(ext)) {
					types.put(ext, lines);
				} else {
					int i = types.get(ext);
					types.put(ext, i + lines);
				}
			}
		}

		DefaultPieDataset dataset = new DefaultPieDataset();

		for (String key : types.keySet()) {
			dataset.setValue(key, types.get(key));
		}

		return dataset;
	}



	/*
	 * private boolean existCommiter(String commiter, int year, int month) {
	 * 
	 * List<CommitDetails> commitsForYearAndMonth = commits.stream() .filter(x ->
	 * x.getCommitDate().getYear() == year && x.getCommitDate().getMonthOfYear() ==
	 * month) .collect(Collectors.toList());
	 * 
	 * 
	 * for(CommitDetails comDetails: commitsForYearAndMonth){ String name =
	 * comDetails.getAuthorName(); if(name == commiter) return true; } return false;
	 * }
	 

	private void countCode() throws IOException {
		Fetcher fetcher = new Fetcher(null);
		Map<String, Integer> types = new HashMap<String, Integer>();
		int lines;
		DateTimeFormatter f = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
		DateTime fromDateTime = f.parseDateTime(from.getYear() + "-" + from.getMonthOfYear() + "-01 00:00:00");
		DateTime toDateTime = f.parseDateTime(to.getYear() + "-" + to.getMonthOfYear() + "-01 00:00:00");

		List<List<FileDiffs>> resultsDiffList = fetcher.getDiffsFromTimeRange(fromDateTime, toDateTime);
		for (List<FileDiffs> resultList : resultsDiffList) {
			for (FileDiffs fileDiffs : resultList) {
				String path = fileDiffs.getFilePath();
				String ext = Files.getFileExtension(path);
				lines = countLines(path);

				if (types.containsKey(ext)) {
					types.put(ext, lines);
				} else {
					int i = types.get(ext);
					types.put(ext, add(i, lines));
				}
			}
		}
	}
*/
	private static int countLines(String filename) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} finally {
			is.close();
		}
	}
}