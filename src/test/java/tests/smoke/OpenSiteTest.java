package tests.smoke;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.HomePage;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC001 - Open the site's home page.
 *
 * Steps:
 * 1. Open the application's home page
 *
 * Expected:
 * The home page loads and the header search field is visible.
 */
@Severity(SeverityLevel.NORMAL)
@Feature("Home Page")
@Story("Load home page")
@Tag("smoke")
public class OpenSiteTest extends BaseTest {

    @Test
    public void TC001_openSite_headerSearchVisible() {

        HomePage homePage = new HomePage(driver);
        homePage.open();

        assertTrue(homePage.isSearchVisible(), "Search input is not visible on the home page.");
    }
}
