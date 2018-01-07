package app.structures;

import org.joda.time.DateTime;

/**
 * Created by Karol on 2018-01-07.
 */
public class GUIDetails {
    private final DateTime from;
    private final DateTime to;

    public GUIDetails(DateTime from, DateTime to) {
        this.from = from;
        this.to = to;
    }

    public DateTime getFrom() {
        return from;
    }

    public DateTime getTo() {
        return to;
    }
}
