package tests.regression;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.SearchPage;
import tests.BaseTest;
import tests.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC012 – Pretraga oglasa terminom koji sigurno ne postoji.
 *
 * Preuslov:
 * Korisnik je ulogovan u sistem.
 *
 * Koraci:
 * 1. Otvoriti login stranicu, prijaviti se
 * 2. Otvoriti globalnu pretragu
 * 3. Uneti termin pretrage koji sigurno ne postoji ni u jednom oglasu
 *
 * Očekivano:
 * Pretraga se izvršava bez greške i ne prikazuje nijedan rezultat -
 * granični slučaj koji dopunjuje TC006 (pretraga koja vraća rezultate).
 */
@Tag("regression")
public class SearchNoResultsTest extends BaseTest {

    private static final String NONEXISTENT_TERM = "zzqxvbnmqwertzxxx1234567890nonexistentqaterm";

    @Test
    public void TC012_search_withNonexistentTerm_returnsNoResults() {

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

        // Pretražujem termin koji sigurno ne postoji ni u jednom oglasu
        searchPage.search(NONEXISTENT_TERM);

        // Proveravam da pretraga NE vraća nijedan rezultat
        assertFalse(searchPage.hasResults(),
                "Pretraga je vratila rezultate za termin koji ne bi trebalo da postoji.");
    }
}
