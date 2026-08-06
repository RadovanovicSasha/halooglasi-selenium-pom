package tests.regression;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.github.radovanovicsasha.halooglasi.framework.pages.ProfilePage;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * TC011 - Profile page access without a prior login.
 *
 * Precondition:
 * User is NOT logged in (this test deliberately does not use LoginSteps).
 *
 * Steps:
 * 1. Open the profile page URL (/profil) directly, without logging in
 *
 * Expected:
 * Profile content is not shown to an unauthenticated user - verifies actual
 * application authorization behavior, not just that an element exists on the page.
 *
 * Requires no credentials and never touches the login endpoint, so it's
 * safe to run automatically on every push.
 */
@Severity(SeverityLevel.CRITICAL)
@Feature("Authorization")
@Story("Profile access requires login")
@Tag("regression")
public class ProfileAccessRequiresLoginTest extends BaseTest {

    @Test
    public void TC011_profileAccessWithoutLogin_profileNotOpened() {

        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.open();

        assertFalse(profilePage.isProfilePageOpened(),
                "Profile page opened without the user being logged in.");
    }
}
