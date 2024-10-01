package tradeaudit.pages.loginpage;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import tradeaudit.utilities.config_utilities.ConfigReader;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage {
    private static final ConfigReader configReader = new ConfigReader("src/test/resources/4-config/config.properties");
    private final Page page;
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;

    public LoginPage(Page page) {
        this.page = page;
        this.usernameField = getLocatorByRole(AriaRole.TEXTBOX, "Username");
        this.passwordField = getLocatorByRole(AriaRole.TEXTBOX, "Password");
        this.loginButton = getLocatorByRole(AriaRole.BUTTON, "Login");
    }

    private Locator getLocatorByRole(AriaRole ariaRole, String fieldName) {
        return page.getByRole(ariaRole, new Page.GetByRoleOptions().setName(fieldName).setExact(true));
    }

    public void validateURL(String url) {
        assertThat(page).hasURL(configReader.readProperty("defaultHost") + url);
    }

    public void login(String username, String password) {
        usernameField.fill(username);
        passwordField.fill(password);
        loginButton.click();
    }
}
