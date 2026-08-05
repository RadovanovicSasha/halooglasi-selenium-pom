package tests.smoke;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.LoginPage;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC003 - Open the login page.
 *
 * Steps:
 * 1. Open the application's login page
 *
 * Expected:
 * The login form is displayed, with email, password, and login button fields visible.
 *
 * Does not submit credentials - see the login-tagged tests for authenticated flows.
 */
@Tag("smoke")
public class OpenLoginPageTest extends BaseTest {

    @Test
    public void TC003_openLoginPage_loginFormVisible() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        assertTrue(loginPage.isLoginFormVisible(),
                "Login form is not displayed (email/password/button).");
    }
}
