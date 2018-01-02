package app.analysis;

import app.structures.CommitDetails;
import app.fetch.Fetcher;
import app.structures.FileDiffs;
import app.structures.ModuleNames;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import com.google.common.io.Files;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CodeCounterAnalyzerModule extends AbstractAnalyzerModule {


	@Override
	public ModuleNames getModuleName() {
		return ModuleNames.MODULE3;
	}

	@Override
	public String generateFile(List<CommitDetails> commitDetails) throws Exception {
		return "file:" + createDataset();
	}

	private String createDataset() throws Exception {

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

		DefaultPieDataset dataset = new DefaultPieDataset();

		for (String key : types.keySet()) {
			dataset.setValue(key, types.get(key));
		}

		return createChart(dataset);
	}

	private static String createChart(PieDataset dataset) throws Exception {
		JFreeChart chart = ChartFactory.createPieChart("Lines of code", // chart title
				dataset, // data
				true, // include legend
				true, false);

		String outputPath = ModuleNames.getPathForOutput(ModuleNames.MODULE1);
		int width = 640; /* Width of the image */
		int height = 480; /* Height of the image */

		File outputFile = new File(outputPath);
		try {
			ChartUtilities.saveChartAsJPEG(outputFile, chart, width, height);
		} catch (Exception e) {
			throw new Exception("Problem occurred creating chart.");
		}

		return outputPath;
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
	private int add(int x, int y) {
		return x + y;
	}

	public static int countLines(String filename) throws IOException {
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