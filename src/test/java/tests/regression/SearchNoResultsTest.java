package tests.regression;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.SearchPage;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * TC012 - Search for a term guaranteed not to exist.
 *
 * Precondition:
 * None - global search is public functionality and requires no login.
 *
 * Steps:
 * 1. Open global search
 * 2. Enter a search term guaranteed not to match any ad
 *
 * Expected:
 * The search executes without error and returns no results - the boundary
 * counterpart to TC006 (search that returns results).
 */
@Severity(SeverityLevel.MINOR)
@Feature("Search")
@Story("Search with no results")
@Tag("regression")
public class SearchNoResultsTest extends BaseTest {

    private static final String NONEXISTENT_TERM = "zzqxvbnmqwertzxxx1234567890nonexistentqaterm";

    @Test
    public void TC012_search_withNonexistentTerm_returnsNoResults() {

        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();

        searchPage.search(NONEXISTENT_TERM);

        assertFalse(searchPage.hasResults(),
                "Search returned results for a term that shouldn't exist.");
    }
}
