package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.ProfilePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC007 – Uspešan logout korisnika.
 *
 * Preuslov: korisnik ima validan nalog.
 *
 * Koraci:
 * 1. Otvoriti login stranicu
 * 2. Uneti validne kredencijale
 * 3. Otvoriti korisnički meni
 * 4. Kliknuti na "Izloguj se"
 *
 * Očekivano:
 * Korisnik je vraćen na login stranicu i forma za prijavu je vidljiva.
 */
@Tag("e2e")
public class LogoutTest extends BaseTest {

    @Test
    public void TC007_logout_userLoggedOut() {

        // Otvaram login stranicu i vršim prijavu korisnika
        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();
        loginSteps.submitValidCredentials(loginPage);

        // Otvaram profil stranicu korisnika
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.open();

        // Izvršavam logout akciju
        profilePage.logoutSession();

        // Proveravam da li je korisnik vraćen na login stranicu
        assertTrue(loginPage.isLoginFormVisible(),
                "Logout nije uspeo – login forma nije vidljiva.");
    }
}