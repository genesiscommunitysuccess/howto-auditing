package global.genesis.cucumber.stepdefinitions;

import com.microsoft.playwright.Page;
import io.cucumber.java.*;
import tradeaudit.utilities.config_utilities.ConfigReader;

import java.net.Socket;

import static tradeaudit.utilities.allure_utitilities.UITestUtils.addAttachment;
import static tradeaudit.utilities.allure_utitilities.UITestUtils.addScreenshot;
import static tradeaudit.utilities.playwright_driver.PlaywrightDriver.*;

public class Hooks {
    private static final ConfigReader configReader = new ConfigReader("src/test/resources/4-config/config.properties");

    private static boolean isApiScenario(Scenario scenario) {
        return scenario.getSourceTagNames().stream().anyMatch(t -> t.contains("@API"));
    }

    private static boolean isUiAvailable() {
        try (Socket ignored = new Socket("localhost", 6060)) {
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @BeforeAll
    public static void beforeAll() {
        if (isUiAvailable())
            createPlaywright();
    }

    @AfterAll
    public static void afterAll() {
        if (isUiAvailable())
            closePlaywright();

    }

    @Before
    public void setup(Scenario scenario) {
        if (!isApiScenario(scenario)) {
            Page page = getPage();
            page.navigate(configReader.readProperty("defaultHost"));
        }
    }

    @After
    public void teardown(Scenario scenario) {
        if (scenario.isFailed()) {
            addAttachment();
            addScreenshot(scenario);
        }
        if (!isApiScenario(scenario)) closePage();
    }
}