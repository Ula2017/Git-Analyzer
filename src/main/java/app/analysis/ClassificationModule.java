package app.analysis;

import app.structures.CommitDetails;
import app.structures.FileDiffs;
import app.structures.GUIDetails;
import org.joda.time.DateTime;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

public class ClassificationModule extends AbstractAnalyzerModule {
    private static final int LIMIT = 5;

	@Override
	public String toString() {
		return "Classification";
	}

	@Override
	public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException {
		return createFileWithTable(commitDetails, guiDetails.getFrom(), guiDetails.getTo(), getPathForOutput());
	}

	public File createFileWithTable(List<CommitDetails> commitDetails, DateTime from, DateTime to, String outputPath) throws CreateImageException {
        File outputFile;
        try {
            outputFile = new File(outputPath);

            BufferedImage bi = createImageFromText(createDataSet(commitDetails, from, to));
            ImageIO.write(bi, "png", outputFile);
        }
        catch (NullPointerException e){
            throw new CreateImageException("Output path for image is not correct");
        }
        catch (IOException e) {
            throw new CreateImageException("Problem creating image with table for ClassificationModule");
        }

        return outputFile;
	}

    public List<String> createDataSet(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
        Map<String, Integer> commits = countCommits(commitDetails, from, to);
        Map<String, Integer> insertions = countInsertions(commitDetails, from, to);
        Map<String, Integer> deletions = countDeletions(commitDetails, from, to);

        List<String> dataSet = new ArrayList<>();

        AtomicInteger i = new AtomicInteger(1);
        dataSet.add("Rank of commits");
        commits.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> dataSet.add(String.format("%d. %s %d", i.getAndIncrement(), c.getKey(), c.getValue())));
        i.set(1);
        dataSet.add("Rank of insertions");
        insertions.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> dataSet.add(String.format("%d. %s %d", i.getAndIncrement(), c.getKey(), c.getValue())));
        i.set(1);
        dataSet.add("Rank of deletions");
        deletions.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> dataSet.add(String.format("%d. %s %d", i.getAndIncrement(), c.getKey(), c.getValue())));

        return dataSet;
    }

    private BufferedImage createImageFromText(List<String> dataSet){
        BufferedImage bufferedImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        final int vertPadding = 25;
        final int horPadding = 100;
        g.setFont(new Font("Arial", Font.PLAIN, vertPadding - 5));

        AtomicInteger start = new AtomicInteger(30);
        dataSet.forEach(s -> g.drawString(s, horPadding, start.getAndAdd(vertPadding)));

        return bufferedImage;
    }

	public Map<String, Integer> countCommits(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
        Map<String, Integer> commits = new HashMap<>();

        getAuthors(commitDetails, from, to)
                .forEach(author -> commits.put(author, getAuthorsCommits(commitDetails, author, from, to).size()));

        return commits.entrySet().stream()
                .limit(LIMIT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, Integer> countInsertions(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
        Map<String, Integer> insertions = new HashMap<>();

        getAuthors(commitDetails, from, to)
            .forEach(a -> insertions.put(a,
                getAuthorsCommits(commitDetails, a, from, to).stream()
                    .mapToInt(cd -> cd.getFiles().stream()
                        .mapToInt(FileDiffs::getInsertions).sum()).sum()));

        return insertions.entrySet().stream()
                .limit(LIMIT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public Map<String, Integer> countDeletions(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
        Map<String, Integer> deletions = new HashMap<>();

        getAuthors(commitDetails, from, to)
                .forEach(a -> deletions.put(a,
                        getAuthorsCommits(commitDetails, a, from, to).stream()
                                .mapToInt(cd -> cd.getFiles().stream()
                                        .mapToInt(FileDiffs::getDeletions).sum()).sum()));

        return deletions.entrySet().stream()
                .limit(LIMIT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	private List<CommitDetails> getAuthorsCommits(List<CommitDetails> commitDetails, String name, DateTime from, DateTime to) {
		return commitDetails.stream()
                .filter(cd -> Objects.equals(cd.getAuthorName(), name) &&
                    cd.getCommitDate().getMillis() >= from.getMillis() && cd.getCommitDate().getMillis() <= to.getMillis())
				.collect(Collectors.toList());
	}

	private Set<String> getAuthors(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
		return commitDetails.stream()
                .filter(cd -> cd.getCommitDate().getMillis() >= from.getMillis() && cd.getCommitDate().getMillis() <= to.getMillis())
				.map(CommitDetails::getAuthorName)
                .collect(Collectors.toSet());
	}
}