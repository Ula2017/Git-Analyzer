package app.analysis;

import app.fetch.Fetcher;
import app.structures.GUIDetails;
import com.google.inject.Inject;
import javafx.scene.Node;
import org.joda.time.DateTime;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Karol on 2018-01-15.
 */
public class AnalysisFactory {
    private final Fetcher fetcher;

    @Inject
    public AnalysisFactory(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    public Node generateNode(AbstractAnalyzerModule module, GUIDetails guiDetails) throws Exception {
        DateTime from = guiDetails.getFrom(), to = guiDetails.getTo();
        String committerName = guiDetails.getCommitterName();

        return module.generateNode(fetcher.getCommitsFromDateRange(from, to).stream()
                        .filter(cd -> (committerName == null || committerName.isEmpty()) ||
                                Objects.equals(cd.getAuthorName(), committerName)).collect(Collectors.toList()),
                guiDetails);
    }
}
