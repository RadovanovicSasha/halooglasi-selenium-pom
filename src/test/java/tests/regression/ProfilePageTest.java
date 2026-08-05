package tests.regression;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.LoginPage;
import framework.pages.ProfilePage;
import tests.base.BaseTest;
import tests.base.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC005 - Profile page loads successfully after login.
 *
 * Precondition:
 * Valid credentials.
 *
 * Steps:
 * 1. Open the login page
 * 2. Enter valid credentials and log in
 * 3. Open the profile page
 *
 * Expected:
 * The profile page loads and the "Moj profil" heading is visible.
 *
 * Authenticates against the live test account.
 */
@Tag("regression")
public class ProfilePageTest extends BaseTest {

    @Test
    public void TC005_profilePage_headerVisible() {

        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();

        assertTrue(loginPage.isLoginFormVisible(),
                "Login form is not visible (email/password/button).");

        loginSteps.submitValidCredentials(loginPage);

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.open();

        assertTrue(profilePage.isProfilePageOpened(),
                "Profile page did not open.");
    }
}
