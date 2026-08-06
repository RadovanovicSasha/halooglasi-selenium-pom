package io.github.radovanovicsasha.halooglasi.tests.e2e;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.github.radovanovicsasha.halooglasi.framework.pages.LoginPage;
import io.github.radovanovicsasha.halooglasi.framework.pages.ProfilePage;
import io.github.radovanovicsasha.halooglasi.tests.base.BaseTest;
import io.github.radovanovicsasha.halooglasi.tests.steps.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC007 - Successful user logout.
 *
 * Precondition: user has a valid account.
 *
 * Steps:
 * 1. Open the login page
 * 2. Enter valid credentials
 * 3. Open the user menu
 * 4. Click "Izloguj se"
 *
 * Expected:
 * The user is returned to the login page and the login form is visible.
 *
 * Authenticates against the live test account.
 */
@Severity(SeverityLevel.NORMAL)
@Feature("Authentication")
@Story("Logout")
@Tag("e2e")
@Tag("login")
public class LogoutTest extends BaseTest {

    @Test
    public void TC007_logout_userLoggedOut() {

        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();
        loginSteps.submitValidCredentials(loginPage);

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.open();

        profilePage.logoutSession();

        assertTrue(loginPage.isLoginFormVisible(),
                "Logout did not succeed - login form is not visible.");
    }
}
