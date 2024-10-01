package tradeaudit.utilities.api_utitilites.dataserver;

import tradeaudit.utilities.api_utitilites.event_base.EventBase;

import java.nio.file.Path;

import static tradeaudit.utilities.json_utilities.JsonModifier.modifyJSON;

public class DataServer extends EventBase {
    private static final Path path = Path.of(payloadPath + "/P_dataServer.json");

    public DataServer(String endpoint) {
        super(path);
        super.setEndPoint(endpoint);
    }

    public DataServer(String endpoint, String criteriaMatch) {
        super(path);
        super.setEndPoint(endpoint);
        modifyJSON(path, "CRITERIA_MATCH", criteriaMatch);
    }
}