package io.github.radovanovicsasha.halooglasi.tests.regression;

import io.github.radovanovicsasha.halooglasi.framework.pages.HomePage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.github.radovanovicsasha.halooglasi.framework.pages.LoginPage;
import io.github.radovanovicsasha.halooglasi.tests.base.BaseTest;
import io.github.radovanovicsasha.halooglasi.tests.steps.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC004 - Successful login with valid credentials.
 *
 * Steps:
 * 1. Open the login page
 * 2. Enter valid credentials
 * 3. Click the login button
 *
 * Expected:
 * The user is successfully logged in and "Moj profil" is visible in the header.
 *
 * Authenticates against the live test account.
 */
@Severity(SeverityLevel.CRITICAL)
@Feature("Authentication")
@Story("Successful login")
@Tag("regression")
@Tag("login")
public class SuccessfulLoginTest extends BaseTest {

    @Test
    public void TC004_successfulLogin_redirectsToProfile() {

        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();

        assertTrue(loginPage.isLoginFormVisible(),
                "Login form is not visible (email/password/button).");

        loginSteps.submitValidCredentials(loginPage);

        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isUserLoggedIn(),
                "User was not successfully logged in.");
    }
}
