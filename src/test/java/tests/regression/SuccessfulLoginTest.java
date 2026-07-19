package tests.regression;

import pages.HomePage;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import tests.BaseTest;
import tests.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC004 – Uspešan login validnim kredencijalima.
 *
 * Koraci:
 * 1. Otvoriti login stranicu
 * 2. Uneti validne kredencijale
 * 3. Kliknuti na dugme za prijavu
 *
 * Očekivano:
 * Korisnik je uspešno ulogovan i u headeru je vidljivo "Moj profil".
 */
@Tag("regression")
public class SuccessfulLoginTest extends BaseTest {

    @Test
    public void TC004_successfulLogin_redirectsToProfile() {

        // Otvaram login stranicu
        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();

        // Proveravam da li je login forma vidljiva pre unosa kredencijala
        assertTrue(loginPage.isLoginFormVisible(),
                "Login forma nije vidljiva (email/pass/dugme).");

        // Unosim validne kredencijale i pokrećem login
        loginSteps.submitValidCredentials(loginPage);

        // Proveravam da li je korisnik uspešno ulogovan
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isUserLoggedIn(),
                "Korisnik nije uspešno ulogovan.");
    }
}
