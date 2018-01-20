package app.analysis;

import app.structures.ClassificationDTO;
import app.structures.CommitDetails;
import app.structures.FileDiffs;
import app.structures.GUIDetails;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.joda.time.DateTime;

import java.net.MalformedURLException;
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
    public Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException {
        ObservableList<ClassificationDTO> classificationDTOS = FXCollections.observableArrayList(createDataSets(commitDetails, guiDetails.getFrom(), guiDetails.getTo()));
        TableView node = new TableView(classificationDTOS);
        node.setPrefWidth(width);
        node.setPrefHeight(height);

        TableColumn<ClassificationDTO, String> committerName = new TableColumn<>();
        committerName.setPrefWidth(400);
        committerName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAuthorName()));
        committerName.setText("Committer name");

        TableColumn<ClassificationDTO, Integer> commits = new TableColumn<>();
        commits.setPrefWidth(185);
        commits.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCommits()).asObject());
        commits.setText("Commits");

        TableColumn<ClassificationDTO, Integer> insertions = new TableColumn<>();
        insertions.setPrefWidth(185);
        insertions.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getInsertions()).asObject());
        insertions.setText("Insertions");

        TableColumn<ClassificationDTO, Integer> deletions = new TableColumn<>();
        deletions.setPrefWidth(185);
        deletions.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getDeletions()).asObject());
        deletions.setText("Deletions");

        node.getColumns().addAll(committerName, commits, insertions, deletions);
        return node;
    }

    public List<ClassificationDTO> createDataSets(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
        List<ClassificationDTO> classificationDTOS = new ArrayList<>();

        getAuthors(commitDetails, from, to)
                .forEach(author -> {
                    List<CommitDetails> authorsCommits = getAuthorsCommits(commitDetails, author, from, to);
                    classificationDTOS.add(new ClassificationDTO(author, authorsCommits.size(),
                            authorsCommits.stream().mapToInt(cd -> cd.getFiles().stream()
                                    .mapToInt(FileDiffs::getInsertions).sum()).sum(),
                            authorsCommits.stream().mapToInt(cd -> cd.getFiles().stream()
                                    .mapToInt(FileDiffs::getDeletions).sum()).sum()
                            ));
                });

        return classificationDTOS;
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