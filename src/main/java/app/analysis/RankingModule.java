package app.analysis;

import app.structures.CommitDetails;
import app.structures.GUIDetails;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class RankingModule extends AbstractAnalyzerModule {
	@Override
	public String toString() {
		return "Ranking";
	}

	@Override
	public File generateFile(List<CommitDetails> commitDetails, GUIDetails guiDetails) throws Exception {
		//System.out.println(commitDetails.size());
		return printTable(commitDetails, guiDetails.getFrom().getYear());
	}

	public HashMap<String, Integer> countCommits(List<CommitDetails> commitDetails, int year) throws IOException {

		HashSet<String> namesSet = getAuthors(commitDetails, year);
		HashMap<String, Integer> commits = new HashMap<String, Integer>();
			
		for (String name : namesSet) {
			List<CommitDetails> authorsCommits = getAuthorsCommits(commitDetails, name, year);
			commits.put(name, authorsCommits.size());
		}
		
		commits.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue());
		
		return commits;
	}

	public HashMap<String, Integer> countInsertions(List<CommitDetails> commitDetails, int year) {

		HashSet<String> namesSet = getAuthors(commitDetails, year);
		HashMap<String, Integer> insertions = new HashMap<String, Integer>();

		for (String name : namesSet) {
			List<CommitDetails> commitsForAuthor = getAuthorsCommits(commitDetails, name, year);

			commitsForAuthor.forEach(cd -> cd.getFiles().forEach(fd -> {
				insertions.replace(name, insertions.get(name) + fd.getInsertions());
			}));
		}
		insertions.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue());

		return insertions;
	}

	public HashMap<String, Integer> countDeletions(List<CommitDetails> commitDetails, int year) {

		HashSet<String> namesSet = getAuthors(commitDetails, year);
		HashMap<String, Integer> deletions = new HashMap<String, Integer>();

		for (String name : namesSet) {
			List<CommitDetails> commitsForAuthor = getAuthorsCommits(commitDetails, name, year);

			commitsForAuthor.forEach(cd -> cd.getFiles().forEach(fd -> {
				deletions.replace(name, deletions.get(name) + fd.getDeletions());
			}));
		}

		deletions.entrySet().stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue());		
		
		return deletions;
	}

	private List<CommitDetails> getAuthorsCommits(List<CommitDetails> commitDetails, String name, int year) {

		List<CommitDetails> commitsForAuthor = commitDetails.stream().filter(x -> x.getAuthorName() == name && x.getCommitDate().getYear() == year)
				.collect(Collectors.toList());

		return commitsForAuthor;
	}

	private HashSet<String> getAuthors(List<CommitDetails> commitDetails, int year) {
		HashSet<String> namesSet = new HashSet<>();
		List<CommitDetails> commitsForYear = commitDetails.stream().filter(x -> x.getCommitDate().getYear() == year)
				.collect(Collectors.toList());

		for (CommitDetails comDetails : commitsForYear) {
			String name = comDetails.getAuthorName();
			namesSet.add(name);
		}
	
		return namesSet;
	}

	private File printTable(List<CommitDetails> commitDetails, int year) throws IOException{
		
		String outputPath = getPathForOutputTxt();
		File outputFile = new File(outputPath);
				
		FileWriter archivo = new FileWriter(outputFile);
				
		HashMap<String, Integer> commits = countCommits(commitDetails, year);
		HashMap<String, Integer> insertions = countInsertions(commitDetails, year);
		HashMap<String, Integer> deletions = countDeletions(commitDetails, year);
		
		archivo.write(String.format("%20s %20s %20s", "Nr", "Author", "Commits \r\n"));
		int i = 1, j = 1, k = 1;
		for (String s : commits.keySet()) {
			archivo.write(String.format("%20s %20s %20s  \r\n", i, s.toString(), commits.get(s)));
			i++;
		}
		
		archivo.write(String.format("\n\n%20s %20s %20s", "Nr", "Author", "Insertions \r\n"));
		
		for (String s : insertions.keySet()) {
			archivo.write(String.format("%20s %20s %20s  \r\n", j, s.toString(), insertions.get(s)));
			j++;
		}
				
		archivo.write(String.format("\n\n%20s %20s %20s", "Nr", "Author", "Deletions \r\n"));
		
		for (String s : deletions.keySet()) {
			archivo.write(String.format("%20s %20s %20s  \r\n", k, s.toString(), deletions.get(s)));
			k++;
		}

		archivo.flush();
		archivo.close();
		return outputFile;

	}
}
