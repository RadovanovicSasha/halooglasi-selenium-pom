package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.HomePage;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC010 – Neuspešan login sa nevažećim kredencijalima.
 *
 * Koraci:
 * 1. Otvoriti login stranicu
 * 2. Uneti nepostojeći e-mail i pogrešnu lozinku
 * 3. Kliknuti na dugme za prijavu
 *
 * Očekivano:
 * Korisnik NIJE ulogovan (indikator "Moj profil" nije vidljiv u headeru) i
 * login forma je i dalje vidljiva - aplikacija ne dozvoljava pristup nalogu
 * sa nevažećim kredencijalima. Negativni scenario koji dopunjuje TC004
 * (uspešan login).
 *
 * Koristim izmišljene kredencijale, a ne pravi test nalog iz TestData, kako
 * ponovljeni neuspešni pokušaji ne bi rizikovali zaključavanje ili anti-bot
 * reakciju na pravi nalog.
 */
@Tag("regression")
public class FailedLoginTest extends BaseTest {

    @Test
    public void TC010_failedLogin_invalidCredentials_userNotLoggedIn() {

        // Otvaram login stranicu
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Proveravam da je login forma vidljiva pre unosa kredencijala
        assertTrue(loginPage.isLoginFormVisible(),
                "Login forma nije vidljiva (email/pass/dugme).");

        // Unosim nevažeće, izmišljene kredencijale
        loginPage.login("nonexistent.qa.user@example.com", "WrongPassword123!");

        // Proveravam da korisnik NIJE ulogovan
        HomePage homePage = new HomePage(driver);
        assertFalse(homePage.isUserLoggedIn(),
                "Korisnik je ulogovan iako su kredencijali nevažeći.");

        // Proveravam da je login forma i dalje vidljiva - nema pristupa nalogu
        assertTrue(loginPage.isLoginFormVisible(),
                "Login forma nije vidljiva nakon neuspelog pokušaja prijave.");
    }
}
