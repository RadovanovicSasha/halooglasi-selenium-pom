package tests.smoke;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import framework.pages.CookiesBannerPage;
import framework.pages.HomePage;
import tests.base.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * TC002 - Accept the cookie consent banner.
 *
 * Steps:
 * 1. Open the site's home page
 * 2. Accept cookies if the banner is shown
 *
 * Expected:
 * The cookie banner disappears and no longer blocks page elements.
 */
@Tag("smoke")
public class AcceptCookiesTest extends BaseTest {

    @Test
    public void TC002_acceptCookies_bannerDisappears() {

        HomePage home = new HomePage(driver);
        home.open();

        CookiesBannerPage cookies = new CookiesBannerPage(driver);
        cookies.acceptCookiesIfPresent();

        assertFalse(cookies.isBannerPresent(),
                "Cookie banner is still present after accepting.");
    }
}
