package app.analysis;

import app.structures.CommitDetails;
import app.structures.CommitsPerBranchDTO;
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
import java.util.stream.Collectors;

import static java.util.Collections.reverseOrder;

public class CommitsPerBranchModule extends AbstractAnalyzerModule {
    @Override
    public Node generateNode(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws CreateImageException, MalformedURLException {
        ObservableList<CommitsPerBranchDTO> commitsPerBranchDTOS = FXCollections.observableArrayList(createDataSets(commitDetails, guiDetails.getFrom(), guiDetails.getTo()));
        TableView node = new TableView(commitsPerBranchDTOS);
        node.setPrefWidth(width);
        node.setPrefHeight(height);

        TableColumn<CommitsPerBranchDTO, String> branchName = new TableColumn<>();
        branchName.setPrefWidth(600);
        branchName.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getBranchName()));
        branchName.setText("Name");

        TableColumn<CommitsPerBranchDTO, Integer> commits = new TableColumn<>();
        commits.setPrefWidth(350);
        commits.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getValue()).asObject());
        commits.setText("Commits");

        node.getColumns().addAll(branchName, commits);
        return node;
    }

    @Override
    public String toString() {
        return "Number of commits per branch";
    }

    private List<CommitsPerBranchDTO> createDataSets(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
        Map<String, Integer> commits = getCommitsPerBranch(commitDetails, from, to);
        return commits.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .map(c -> new CommitsPerBranchDTO(c.getKey(), c.getValue())).collect(Collectors.toList());
    }

    public List<String> createDataSet(List<CommitDetails> commitDetails, DateTime from, DateTime to) {
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
}