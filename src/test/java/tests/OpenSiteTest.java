package tests;

import org.junit.jupiter.api.Test;
import pages.HomePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC001 – Otvaranje početne stranice sajta.
 *
 * Koraci:
 * 1. Otvoriti početnu stranicu aplikacije
 *
 * Očekivano:
 * Početna stranica se učitava i vidljivo je polje za pretragu u headeru.
 */
public class OpenSiteTest extends BaseTest {

    @Test
    public void TC001_openSite_headerSearchVisible() {

        // Kreiram instancu HomePage
        HomePage homePage = new HomePage(driver);

        // Otvaram početnu stranicu sajta
        homePage.open();

        // Proveravam da li je polje za pretragu vidljivo u headeru
        assertTrue(homePage.isSearchVisible(), "Search input nije vidljiv na home strani.");
    }
}