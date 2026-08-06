package io.github.radovanovicsasha.halooglasi.tests.smoke;

import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.github.radovanovicsasha.halooglasi.framework.pages.CookiesBannerPage;
import io.github.radovanovicsasha.halooglasi.framework.pages.HomePage;
import io.github.radovanovicsasha.halooglasi.tests.base.BaseTest;

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
@Severity(SeverityLevel.NORMAL)
@Feature("Cookie Consent")
@Story("Accept cookie banner")
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
