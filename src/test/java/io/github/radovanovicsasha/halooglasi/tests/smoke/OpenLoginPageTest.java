package io.github.radovanovicsasha.halooglasi.tests.smoke;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.github.radovanovicsasha.halooglasi.framework.pages.LoginPage;
import io.github.radovanovicsasha.halooglasi.tests.base.BaseTest;

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
@Severity(SeverityLevel.NORMAL)
@Feature("Authentication")
@Story("Open login page")
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
