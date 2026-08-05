package tests.regression;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.HomePage;
import framework.pages.LoginPage;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC010 - Failed login with invalid credentials.
 *
 * Steps:
 * 1. Open the login page
 * 2. Enter a non-existent email and wrong password
 * 3. Click the login button
 *
 * Expected:
 * The user is NOT logged in ("Moj profil" indicator is not visible in the
 * header) and the login form remains visible - the application doesn't
 * grant account access on invalid credentials. Negative counterpart to TC004.
 *
 * Uses fabricated credentials rather than the real test account, so that
 * repeated failed attempts can't risk a lockout or extra anti-bot scrutiny
 * on the real account. Still submits to the live login endpoint, though,
 * so it's excluded from routine CI along with the other login flows.
 */
@Severity(SeverityLevel.NORMAL)
@Feature("Authentication")
@Story("Failed login with invalid credentials")
@Tag("regression")
@Tag("login")
public class FailedLoginTest extends BaseTest {

    @Test
    public void TC010_failedLogin_invalidCredentials_userNotLoggedIn() {

        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        assertTrue(loginPage.isLoginFormVisible(),
                "Login form is not visible (email/password/button).");

        loginPage.login("nonexistent.qa.user@example.com", "WrongPassword123!");

        HomePage homePage = new HomePage(driver);
        assertFalse(homePage.isUserLoggedIn(),
                "User is logged in despite invalid credentials.");

        assertTrue(loginPage.isLoginFormVisible(),
                "Login form is not visible after the failed login attempt.");
    }
}
