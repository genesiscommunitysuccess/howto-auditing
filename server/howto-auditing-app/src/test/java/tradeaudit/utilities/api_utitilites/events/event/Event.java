package tradeaudit.utilities.api_utitilites.events.event;

import tradeaudit.utilities.api_utitilites.event_base.EventBase;

import java.nio.file.Path;

public class Event extends EventBase {
    public Event(String endpoint, Path bodyPath) {
        super(bodyPath);
        super.setEndPoint(endpoint);
    }
}