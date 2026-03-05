package tests;
import pages.HomePage;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import utils.TestData;

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
public class SuccessfulLoginTest extends BaseTest {

    @Test
    public void TC004_successfulLogin_redirectsToProfile() {

        // Otvaram login stranicu
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Proveravam da li je login forma vidljiva pre unosa kredencijala
        assertTrue(loginPage.isLoginFormVisible(),
                "Login forma nije vidljiva (email/pass/dugme).");

        // Unosim validne kredencijale i pokrećem login
        loginPage.login(TestData.VALID_EMAIL, TestData.VALID_PASSWORD);


        // Proveravam da li je korisnik uspešno ulogovan
        HomePage homePage = new HomePage(driver);
        assertTrue(homePage.isUserLoggedIn(),
                "Korisnik nije uspešno ulogovan.");
    }
}