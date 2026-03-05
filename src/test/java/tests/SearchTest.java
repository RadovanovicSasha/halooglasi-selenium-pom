package tests;

import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.SearchPage;
import utils.TestData;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC006 – Provera globalne pretrage oglasa.
 *
 * Preuslov:
 * Korisnik je ulogovan u sistem.
 *
 * Koraci:
 * 1. Otvoriti login stranicu
 * 2. Uneti validne kredencijale
 * 3. Ulogovati se u sistem
 * 4. Uneti termin pretrage u search polje
 *
 * Očekivano:
 * Prikazuju se rezultati pretrage za uneti pojam.
 */
public class SearchTest extends BaseTest {

    @Test
    public void TC006_search_returnsResults() {

        // Otvaram login stranicu
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Proveravam da li je login forma učitana
        assertTrue(loginPage.isLoginFormVisible(), "Login forma nije vidljiva.");

        // Vršim login sa validnim kredencijalima
        loginPage.login(TestData.VALID_EMAIL, TestData.VALID_PASSWORD);

        // Otvaram stranicu sa globalnom pretragom
        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();

        // Pokrećem pretragu za zadati termin
        searchPage.search(TestData.SEARCH_TERM);

        // Proveravam da li su rezultati pretrage prikazani
        assertTrue(searchPage.hasResults(), "Pretraga nije vratila rezultate.");
    }
}