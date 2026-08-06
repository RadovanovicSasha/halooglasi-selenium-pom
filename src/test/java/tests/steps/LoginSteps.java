package tests.steps;

import io.github.radovanovicsasha.halooglasi.framework.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import tests.testdata.TestData;

/**
 * Centralizes the repeated login steps (opening the login page and
 * submitting valid credentials from TestData) that were previously
 * duplicated across multiple test classes.
 *
 * Lives in tests.steps rather than tests.base: tests.base stays
 * lifecycle-only (BaseTest, ScreenshotOnFailureExtension), while step
 * helpers like this one that compose page actions into a reusable flow
 * belong in their own package.
 *
 * Deliberately contains no assertions - the test class still decides what
 * and when to verify. Also doesn't repeat responsibilities already handled
 * by BaseTest.setUp() (driver creation, cookie banner, security
 * notification) - it assumes that's already been done before LoginSteps is used.
 */
public class LoginSteps {

    private final WebDriver driver;

    public LoginSteps(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Opens the login page. The test decides whether and when to assert its
     * visibility before entering credentials.
     */
    public LoginPage openLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        return loginPage;
    }

    /**
     * Enters valid credentials (from TestData) on an already-open login page.
     */
    public void submitValidCredentials(LoginPage loginPage) {
        loginPage.login(TestData.haloEmail, TestData.haloPass);
    }
}
