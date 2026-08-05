package tests.regression;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.LoginPage;
import framework.pages.SearchPage;
import tests.testdata.TestData;
import tests.base.BaseTest;
import tests.steps.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC006 - Global ad search.
 *
 * Precondition:
 * User is logged in.
 *
 * Steps:
 * 1. Open the login page
 * 2. Enter valid credentials
 * 3. Log in
 * 4. Enter a search term into the search field
 *
 * Expected:
 * Search results are shown for the entered term.
 *
 * Authenticates against the live test account.
 */
@Severity(SeverityLevel.CRITICAL)
@Feature("Search")
@Story("Search returns results")
@Tag("regression")
@Tag("login")
public class SearchTest extends BaseTest {

    @Test
    public void TC006_search_returnsResults() {

        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();

        assertTrue(loginPage.isLoginFormVisible(), "Login form is not visible.");

        loginSteps.submitValidCredentials(loginPage);

        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();

        searchPage.search(TestData.SEARCH_TERM);

        assertTrue(searchPage.hasResults(), "Search did not return any results.");
    }
}
