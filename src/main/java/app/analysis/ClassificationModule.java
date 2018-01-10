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
	public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
		return createFileWithTable(commitDetails, guiDetails.getFrom(), guiDetails.getTo());
	}

	private File createFileWithTable(List<CommitDetails> commitDetails, DateTime from, DateTime to) throws IOException{
		String outputPath = getPathForOutput();
		File outputFile = new File(outputPath);

		Map<String, Integer> commits = countCommits(commitDetails, from, to);
		Map<String, Integer> insertions = countInsertions(commitDetails, from, to);
		Map<String, Integer> deletions = countDeletions(commitDetails, from, to);

        BufferedImage bi = createImageFromText(commits, insertions, deletions);
        ImageIO.write(bi, "png", outputFile);

		return outputFile;
	}

    private static BufferedImage createImageFromText(Map<String, Integer> commits, Map<String, Integer> insertions, Map<String, Integer> deletions){
        BufferedImage bufferedImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        final int vertPadding = 25;
        final int horPadding = 100;
        g.setFont(new Font("Arial", Font.PLAIN, vertPadding - 5));

        AtomicInteger start = new AtomicInteger(30), i = new AtomicInteger(1);
        g.drawString("Rank of commits", horPadding, start.get());
        commits.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> g.drawString(String.format("%d. %s %d", i.get(), c.getKey(), c.getValue()), horPadding, start.get() + i.getAndIncrement()*vertPadding));
        i.set(1);
        g.drawString("Rank of insertions", horPadding, start.addAndGet((commits.size() + 1) * vertPadding + 10));
        insertions.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> g.drawString(String.format("%d. %s %d", i.get(), c.getKey(), c.getValue()), horPadding, start.get() + i.getAndIncrement()*vertPadding));
        i.set(1);
        g.drawString("Rank of deletions", horPadding, start.addAndGet((insertions.size() + 1) * vertPadding + 10));
        commits.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> g.drawString(String.format("%d. %s %d", i.get(), c.getKey(), c.getValue()), horPadding, start.get() + i.getAndIncrement()*vertPadding));

        return bufferedImage;
    }

	public Map<String, Integer> countCommits(List<CommitDetails> commitDetails, DateTime from, DateTime to) throws IOException {
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
        try {
            Map<String, Integer> deletions = new HashMap<>();

            getAuthors(commitDetails, from, to)
                    .forEach(a -> deletions.put(a,
                            getAuthorsCommits(commitDetails, a, from, to).stream()
                                    .mapToInt(cd -> cd.getFiles().stream()
                                            .mapToInt(FileDiffs::getDeletions).sum()).sum()));

            Map<String, Integer> x= deletions.entrySet().stream()
                    .limit(LIMIT)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            return x;
        }
        catch(Exception e){
            e = e;
            return null;
        }
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