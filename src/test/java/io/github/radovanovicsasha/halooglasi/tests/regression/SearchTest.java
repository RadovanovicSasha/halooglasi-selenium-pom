package io.github.radovanovicsasha.halooglasi.tests.regression;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.github.radovanovicsasha.halooglasi.framework.pages.SearchPage;
import io.github.radovanovicsasha.halooglasi.testdata.TestData;
import io.github.radovanovicsasha.halooglasi.tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC006 - Global ad search.
 *
 * Precondition:
 * None - global search is public functionality and requires no login.
 *
 * Steps:
 * 1. Open the home page
 * 2. Enter a search term into the search field
 *
 * Expected:
 * Search results are shown for the entered term.
 */
@Severity(SeverityLevel.CRITICAL)
@Feature("Search")
@Story("Search returns results")
@Tag("regression")
public class SearchTest extends BaseTest {

    @Test
    public void TC006_search_returnsResults() {

        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();

        searchPage.search(TestData.SEARCH_TERM);

        assertTrue(searchPage.hasResults(), "Search did not return any results.");
    }
}
