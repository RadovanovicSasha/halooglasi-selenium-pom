package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.SearchPage;
import testdata.TestData;

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
@Tag("regression")
public class SearchTest extends BaseTest {

    @Test
    public void TC006_search_returnsResults() {

        // Otvaram login stranicu
        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();

        // Proveravam da li je login forma učitana
        assertTrue(loginPage.isLoginFormVisible(), "Login forma nije vidljiva.");

        // Vršim login sa validnim kredencijalima
        loginSteps.submitValidCredentials(loginPage);

        // Otvaram stranicu sa globalnom pretragom
        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();

        // Pokrećem pretragu za zadati termin
        searchPage.search(TestData.SEARCH_TERM);

        // Proveravam da li su rezultati pretrage prikazani
        assertTrue(searchPage.hasResults(), "Pretraga nije vratila rezultate.");
    }
}