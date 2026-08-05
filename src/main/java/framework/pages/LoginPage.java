package framework.pages;

import framework.config.EnvConfig;
import framework.pages.components.HeaderComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the user authentication page.
 */
public class LoginPage extends BasePage {

    private final String loginUrl = EnvConfig.BASE_URL + "prijava";
    private final By emailInput =
            By.xpath("//label[contains(text(),'E-mail') or contains(text(),'korisničko ime')]/following::input[1]");
    private final By passwordInput =
            By.xpath("//label[contains(text(),'Lozinka')]/following::input[1]");
    private final By loginButton =
            By.xpath("//button[contains(.,'Uloguj me')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(loginUrl);
    }

    /**
     * Checks whether the login form is visible - the email, password, and
     * login button fields are all present.
     */
    public boolean isLoginFormVisible() {
        return isVisible(emailInput, 10)
                && isVisible(passwordInput, 10)
                && isVisible(loginButton, 10);
    }

    /**
     * Enters credentials and submits the login form.
     *
     * The cookie banner is handled centrally in BaseTest.setUp()
     * (CookiesBannerPage), so there's no duplicated handling here.
     */
    public void login(String user, String pass) {
        driver.findElement(emailInput).sendKeys(user);
        driver.findElement(passwordInput).sendKeys(pass);
        clickWhenClickable(loginButton, 5);
        // Wait for the header user menu to appear, confirming login succeeded.
        new HeaderComponent(driver).isUserLoggedIn();
    }
}
