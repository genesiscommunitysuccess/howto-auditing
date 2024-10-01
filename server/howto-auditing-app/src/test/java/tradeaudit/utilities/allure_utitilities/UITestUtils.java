package tradeaudit.utilities.allure_utitilities;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.cucumber.java.Scenario;
import io.qameta.allure.Allure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tradeaudit.utilities.config_utilities.ConfigReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import static tradeaudit.utilities.api_utitilites.constants.Authentication.USERNAME;
import static tradeaudit.utilities.file_utilities.FileConstants.feature;
import static tradeaudit.utilities.playwright_driver.PlaywrightDriver.getPage;

/**
 * Utility class for capturing the last state of the GUI and attaching it to the report.
 */
public class UITestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(UITestUtils.class);
    private static final ConfigReader config_properties = new ConfigReader("src/test/resources/4-config/config.properties");

    /**
     * Captures a full page screenshot.
     *
     * @param filePath the path where the screenshot will be saved
     */
    public static void captureFullPage(String filePath) {
        getPage().screenshot(new Page.ScreenshotOptions().setPath(Paths.get(filePath)));
        LOG.info("Finished captureFullPage with filePath: {}", filePath);
    }

    /**
     * Captures a screenshot of an element.
     *
     * @param locator  the locator of the element to capture
     * @param filePath the path where the screenshot will be saved
     */
    public static void captureElement(String locator, String filePath) {
        Locator element = getPage().locator(locator);
        element.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get(filePath)));
        LOG.info("Finished captureElement with locator: {} and filePath: {}", locator, filePath);
    }

    /**
     * Adds a screenshot to the Allure report.
     *
     * @param scenario the Cucumber scenario for which the screenshot is being added
     */
    public static void addScreenshot(Scenario scenario) {
        String screenshotPath = "build/screenshots/" + scenario.getName() + ".png";
        captureFullPage(screenshotPath);
        try (InputStream is = Files.newInputStream(Paths.get(screenshotPath))) {
            Allure.attachment(scenario.getName() + ".png", is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to add the latest file in the actual directory as an attachment
     */
    public static void addAttachment() {
        Path path = Paths.get(config_properties.readProperty("actualPath") + "/" + feature + "/");
        if (Files.exists(path)) {
            try {
                Optional<Path> latestFilePath = Files.list(path)
                        .filter(Files::isRegularFile)
                        .max(Comparator.comparingLong(p -> p.toFile().lastModified()));

                if (latestFilePath.isPresent()) {
                    Path filePath = latestFilePath.get();
                    Allure.attachment(filePath.getFileName().toString(), new String(Files.readAllBytes(filePath)));
                }
            } catch (IOException e) {
                LOG.error("Error adding attachment to allure report", e);
            }
        }
    }
}
