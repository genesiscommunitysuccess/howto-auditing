package tradeaudit.utilities.api_utitilites.event_base;

import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static tradeaudit.utilities.api_utitilites.request_response_spec.RequestResponseSpecifications.requestSpecification;
import static tradeaudit.utilities.api_utitilites.request_response_spec.RequestResponseSpecifications.responseSpecification;
import static tradeaudit.utilities.file_utilities.FileConstants.feature;


public abstract class EventBase {
    private static final Logger LOG = LoggerFactory.getLogger(EventBase.class);
    protected static final Path payloadPath = Path.of("src/test/resources/1-payload/" +feature);
    private final Path bodyPath;
    private String endPoint;
    private String sourceRef;

    public EventBase(Path bodyPath) {
        this.bodyPath = bodyPath;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    protected String getSourceRef() {
        if (this.sourceRef == null) {
            this.sourceRef = UUID.randomUUID().toString();
        }
        return this.sourceRef;
    }

    public Response post() {
        try {
            return
                    given()
                            .spec(requestSpecification(this.bodyPath))
                            .header("SOURCE-REF", getSourceRef()).
                     when()
                            .post(getEndPoint()).
                     then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in post - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Response get() {
        try {
            return
                    given()
                            .spec(requestSpecification(this.bodyPath))
                            .header("SOURCE-REF", getSourceRef()).
                     when()
                            .get(this.endPoint).
                     then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in get - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Response put() {
        try {
            return
                    given()
                            .spec(requestSpecification(this.bodyPath))
                            .header("SUBSCRIPTION-REF", getSourceRef()).
                     when()
                            .put(this.endPoint).
                     then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in put - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public Response delete() {
        try {
            return
                    given()
                            .spec(requestSpecification(this.bodyPath))
                            .header("SOURCE-REF", getSourceRef()).
                     when()
                            .delete(this.endPoint).
                     then()
                            .spec(responseSpecification())
                            .extract().response();
        } catch (Exception e) {
            LOG.error("Error in delete - {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}