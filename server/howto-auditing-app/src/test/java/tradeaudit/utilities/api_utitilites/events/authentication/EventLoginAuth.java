package tradeaudit.utilities.api_utitilites.events.authentication;

import tradeaudit.utilities.api_utitilites.event_base.EventBase;

import java.nio.file.Path;

import static tradeaudit.utilities.json_utilities.JsonModifier.modifyJSON;

public class EventLoginAuth extends EventBase {
    private static final Path path = Path.of(payloadPath + "/P_login.json");

    public EventLoginAuth(String username, String password) {
        super(path);
        modifyJSON(path, "USER_NAME", username);
        modifyJSON(path, "PASSWORD", password);
        super.setEndPoint("EVENT_LOGIN_AUTH");
    }
}