package app.structures;

import org.joda.time.DateTime;

/**
 * Created by Karol on 2018-01-07.
 */
public class GUIDetails {
    private final DateTime from;
    private final DateTime to;
    private final String committerName;

    public GUIDetails(DateTime from, DateTime to, String committerName) {
        this.from = from;
        this.to = to;
        this.committerName = committerName;
    }

    public DateTime getFrom() {
        return from;
    }

    public DateTime getTo() {
        return to;
    }

    public String getCommitterName() {
        return committerName;
    }
}
