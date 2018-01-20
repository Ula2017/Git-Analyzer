package app.structures;

/**
 * Created by Karol on 2018-01-18.
 */
public class ClassificationDTO {
    private String authorName;
    private int commits;
    private int insertions;
    private int deletions;

    public ClassificationDTO(String authorName, int commits, int insertions, int deletions) {
        this.authorName = authorName;
        this.commits = commits;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getCommits() {
        return commits;
    }

    public int getInsertions() {
        return insertions;
    }

    public int getDeletions() {
        return deletions;
    }
}
