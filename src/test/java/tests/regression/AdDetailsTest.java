package tests.regression;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
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
 * TC008 - View ad details.
 *
 * Precondition:
 * User is logged in.
 *
 * Steps:
 * 1. Search for ads
 * 2. Select the first search result
 * 3. Verify the ad details page is open and structural elements (price,
 *    category) are visible
 *
 * Expected:
 * The ad details page opens, with price and category visible.
 * The test stops at viewing - no contact, message, favorites, or purchase.
 *
 * Doesn't assert specific content (title/price/advertiser) since site
 * inventory changes constantly - only checks that structural elements are visible.
 *
 * Authenticates against the live test account.
 */
@Severity(SeverityLevel.NORMAL)
@Feature("Ad Details")
@Story("View ad details")
@Tag("regression")
@Tag("login")
public class AdDetailsTest extends BaseTest {

    @Test
    public void TC008_viewAdvertisementDetails_showsStructuralInfo() {

        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();
        loginSteps.submitValidCredentials(loginPage);

        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();
        searchPage.search(TestData.SEARCH_TERM);

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.hasAtLeastOneResult(),
                "Search did not return any results to select from.");

        AdDetailsPage adDetailsPage = searchResultsPage.openFirstResult();

        assertTrue(adDetailsPage.isDetailsPageOpened(),
                "Ad details page did not open.");
        assertTrue(adDetailsPage.isPriceVisible(),
                "Price is not visible on the ad details page.");
        assertTrue(adDetailsPage.isCategoryBreadcrumbVisible(),
                "Category breadcrumb is not visible on the ad details page.");
    }
}
