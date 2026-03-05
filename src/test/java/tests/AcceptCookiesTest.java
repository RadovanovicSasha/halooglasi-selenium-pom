package tests;

import org.junit.jupiter.api.Test;
import pages.CookiesBannerPage;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * TC002 – Prihvatanje cookies bannera.
 *
 * Koraci:
 * 1. Otvoriti početnu stranicu sajta
 * 2. Prihvatiti cookies ako je banner prikazan
 *
 * Očekivano:
 * Cookie banner nestaje i više ne blokira elemente na stranici.
 */
public class AcceptCookiesTest extends BaseTest {

    @Test
    public void TC002_acceptCookies_bannerDisappears() {

        // Otvaram početnu stranicu sajta
        HomePage home = new HomePage(driver);
        home.open();

        // Prihvatam cookies ako je banner prikazan
        CookiesBannerPage cookies = new CookiesBannerPage(driver);
        cookies.acceptCookiesIfPresent();

        // Proveravam da banner više nije prisutan
        assertFalse(cookies.isBannerPresent(),
                "Cookie banner je i dalje prisutan posle prihvatanja.");
    }
}