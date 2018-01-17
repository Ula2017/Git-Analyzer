package app.analysis.Test;

import app.structures.CommitDetails;
import app.structures.FileDiffs;
import org.joda.time.DateTime;

/**
 * Created by Karol on 2018-01-17.
 */
public class AbstractTest {
    protected FileDiffs createFileDiff(String name, int insertions, int deletions){
        FileDiffs fd = new FileDiffs();
        fd.setInformation(name, insertions, deletions);

        return fd;
    }

    protected CommitDetails createCommitDetails(DateTime commitDate, String authorName, String commitMessage, String branch, FileDiffs... fileDiffs){
        CommitDetails cm = new CommitDetails();
        cm.setPrimaryInformation(commitDate, authorName, commitMessage, branch);
        for (FileDiffs fileDiff : fileDiffs) cm.addFile(fileDiff);

        return cm;
    }
}
