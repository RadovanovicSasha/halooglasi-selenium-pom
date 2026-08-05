package tests.e2e;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.AdDetailsPage;
import framework.pages.LoginPage;
import framework.pages.SearchPage;
import framework.pages.SearchResultsPage;
import tests.testdata.TestData;
import tests.base.BaseTest;
import tests.base.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC009 - Non-destructive end-to-end flow: search -> select -> view details.
 *
 * Precondition:
 * User is logged in.
 *
 * Steps:
 * 1. Search for ads
 * 2. Select the first search result
 * 3. Open the ad details page
 *
 * Expected:
 * The full chain of steps (search -> select -> view) completes successfully
 * on the ad details page.
 *
 * The flow DELIBERATELY stops at viewing information - the test does not
 * contact the seller, send a message, reveal/click a phone number, add the
 * ad to favorites, create an order, or perform any purchase, payment, or
 * permanent account change. AdDetailsPage deliberately exposes no locators
 * for any such action (see AdDetailsPage).
 *
 * Authenticates against the live test account.
 */
@Tag("e2e")
public class BrowseListingEndToEndTest extends BaseTest {

    @Test
    public void TC009_searchSelectViewDetails_nonDestructiveBrowseFlow() {

        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();
        loginSteps.submitValidCredentials(loginPage);

        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();
        searchPage.search(TestData.SEARCH_TERM);

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.hasAtLeastOneResult(),
                "Search did not return any results.");

        // Selecting an ad opens its details page - the flow stops here.
        AdDetailsPage adDetailsPage = searchResultsPage.openFirstResult();

        assertTrue(adDetailsPage.isDetailsPageOpened(),
                "Search->select->view flow failed - ad details page did not open.");
    }
}
