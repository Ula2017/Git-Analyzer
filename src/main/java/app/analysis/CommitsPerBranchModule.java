package app.analysis;

import app.structures.CommitDetails;
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

public class CommitsPerBranchModule extends AbstractAnalyzerModule {
    @Override
    public String toString() {
        return "Number of commits per branch";
    }

    @Override
    public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
        return createFileWithTable(commitDetails, guiDetails.getFrom(), guiDetails.getTo());
    }

    private File createFileWithTable(List<CommitDetails> commitDetails, DateTime from, DateTime to) throws IOException{
        String outputPath = getPathForOutput();
        File outputFile = new File(outputPath);

        BufferedImage bi = createImageFromText(createDataSet(commitDetails, from, to));
        ImageIO.write(bi, "png", outputFile);

        return outputFile;
    }

    public List<String> createDataSet(List<CommitDetails> commitDetails, DateTime from, DateTime to) throws IOException {
        Map<String, Integer> commits = getCommitsPerBranch(commitDetails, from, to);
        List<String> dataSet = new ArrayList<>();

        dataSet.add("Number od commits per branch");
        commits.entrySet().stream().sorted(reverseOrder(Map.Entry.comparingByValue())).forEach(c -> dataSet.add(String.format("%s %d", c.getKey(), c.getValue())));

        return dataSet;
    }

    private Map<String,Integer> getCommitsPerBranch(List<CommitDetails> commitDetails, DateTime from, DateTime to){
        Map<String, Integer> branches = new HashMap<>();

        getBranches(commitDetails, from, to)
                .forEach(a -> { System.out.println(a); branches.put(a, getNumberOfCommitsPerBranch(commitDetails,from,to,a));});

        return branches;
    }

    private Set<String> getBranches(List<CommitDetails> commitDetails, DateTime from, DateTime to){
        return commitDetails.stream()
                .map(CommitDetails::getBranch)
                .collect(Collectors.toSet());
    }

    private int getNumberOfCommitsPerBranch(List<CommitDetails> commitDetails, DateTime from, DateTime to, String name){
        List<CommitDetails> commits = commitDetails.stream()
                                        .filter(cd -> Objects.equals(cd.getBranch(), name))
                                        .collect(Collectors.toList());
        return commits.size();
    }

    private static BufferedImage createImageFromText(List<String> dataSet){
        BufferedImage bufferedImage = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        final int vertPadding = 25;
        final int horPadding = 100;
        g.setFont(new Font("Arial", Font.PLAIN, vertPadding - 5));

        AtomicInteger start = new AtomicInteger(30);
        dataSet.forEach(s -> g.drawString(s, horPadding, start.getAndAdd(vertPadding)));

        return bufferedImage;
    }
}