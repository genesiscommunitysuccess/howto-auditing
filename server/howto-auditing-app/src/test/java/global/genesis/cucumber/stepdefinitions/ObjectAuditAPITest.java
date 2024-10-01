package global.genesis.cucumber.stepdefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import tradeaudit.utilities.api_utitilites.events.authentication.EventLoginAuth;
import tradeaudit.utilities.api_utitilites.events.event.Event;
import tradeaudit.utilities.api_utitilites.reqrep.ReqRep;
import tradeaudit.utilities.config_utilities.ConfigReader;

import java.nio.file.Path;

import static tradeaudit.utilities.api_utitilites.constants.Authentication.SESSION_AUTH_TOKEN;
import static tradeaudit.utilities.json_utilities.CompareJSON.compareJSONFiles;
import static tradeaudit.utilities.json_utilities.JsonModifier.modifyJSON;
import static tradeaudit.utilities.json_utilities.WriteReadJSON.writeResponseJSON;

public class ObjectAuditAPITest {
    private static final ConfigReader objectAuditProperties = new ConfigReader("src/test/resources/4-config/TradeAudit.properties");
    private static final ConfigReader configProperties = new ConfigReader("src/test/resources/4-config/config.properties");
    private String objectKey;
    private String objectValue;
    private Response response;
    private String endpoint;
    private String queryParam;

    @Given("User connect with username {string} and password {string}")
    public void user_connect_with_username_and_password(String username, String password) {
        EventLoginAuth eventLoginAuth = new EventLoginAuth(username, password);
        Response response = eventLoginAuth.post();
        SESSION_AUTH_TOKEN = response.jsonPath().get("SESSION_AUTH_TOKEN");
    }

    @When("User insert a {string} with the {string} using the {string}")
    public void user_insert_a_with_the_using_the(String object, String payload, String object_event) {
        Event event = new Event(object_event, Path.of(configProperties.readProperty("payloadPath") + payload));
        event.post();
    }

    @And("User find the {string} with {string} as {string} and extract {string}")
    public void user_find_the_with_and_and_extract(String object, String key, String value, String object_id) {
        endpoint = objectAuditProperties.getValue(object, configKey -> configKey.equals(object.toUpperCase() + "_REQ_REP"));
        queryParam = key + "=" + value;

        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();

        this.objectKey = object_id;
        this.objectValue = response.jsonPath().getString("REPLY[0]." + object_id);
        queryParam += "&" + objectKey + "=" + objectValue;
    }

    @Then("User verifies the {string} successfully created comparing to the {string} with {string} and {string}")
    public void user_verifies_the_successfully_created_comparing_to_the(String object, String expected_result, String primary_key, String keys_to_ignore) {
        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();
        Path expectedPath = Path.of(configProperties.readProperty("expectedPath") + expected_result);
        String fileName = expectedPath.getFileName().toString().split("\\.")[0];
        Path actualPath = writeResponseJSON(response, fileName, "actual");
        compareJSONFiles(expectedPath, actualPath, primary_key, keys_to_ignore);
    }

    @Then("User modifies the {string} with the {string} and {string}")
    public void user_modifies_the_with_the_and(String object, String payload, String object_event) {
        Path jsonFile = Path.of(configProperties.readProperty("payloadPath") + payload);
        modifyJSON(jsonFile, objectKey, objectValue);
        Event event = new Event(object_event, jsonFile);
        event.post();
    }

    @And("User verifies the {string} successfully modified comparing to the {string} with {string} and {string}")
    public void user_verifies_the_successfully_modified_comparing_to_the_and(String object, String expected_result, String primary_key, String keys_to_ignore) {
        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();
        Path expectedPath = Path.of(configProperties.readProperty("expectedPath") + expected_result);
        String fileName = expectedPath.getFileName().toString().split("\\.")[0];
        Path actualPath = writeResponseJSON(response, fileName, "actual");
        compareJSONFiles(expectedPath, actualPath, primary_key, keys_to_ignore);
    }

    @Then("User deletes the {string} with the {string} using {string}")
    public void user_deletes_the_using(String object, String payload, String object_event) {
        Path jsonFile = Path.of(configProperties.readProperty("payloadPath") + payload);
        modifyJSON(jsonFile, objectKey, objectValue);
        Event event = new Event(object_event, jsonFile);
        event.post();
    }

    @And("User verifies the {string} successfully deleted and comparing to the {string} with {string} and {string}")
    public void user_verifies_the_successfully_deleted_and_compare_to_the(String object, String expected_result, String primary_key, String keys_to_ignore) {
        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();
        Path expectedPath = Path.of(configProperties.readProperty("expectedPath") + expected_result);
        String fileName = expectedPath.getFileName().toString().split("\\.")[0];
        Path actualPath = writeResponseJSON(response, fileName, "actual");
        compareJSONFiles(expectedPath, actualPath, primary_key, keys_to_ignore);
    }

    @Given("User verifies the {string} {string} audit was created using the {string} and compares to {string} with {string} and {string}")
    public void user_verifies_the_audit_was_created_using_the_and_compares_to_with_and(String object, String crud, String object_event, String expected_audit_result, String primary_key, String keys_to_ignore) {
        String queryParam = primary_key + "=" + object_event + "&" + objectKey + "=" + objectValue;
        endpoint = objectAuditProperties.getValue(object, configKey -> configKey.equals(object.toUpperCase() + "_AUDIT_REQ_REP"));

        ReqRep reqRep = new ReqRep(endpoint, queryParam);
        response = reqRep.get();

        Path expectedPath = Path.of(configProperties.readProperty("expectedPath") + expected_audit_result);
        String fileName = expectedPath.getFileName().toString().split("\\.")[0];
        Path actualPath = writeResponseJSON(response, fileName, "actual");
        compareJSONFiles(expectedPath, actualPath, primary_key, keys_to_ignore);
    }
}
