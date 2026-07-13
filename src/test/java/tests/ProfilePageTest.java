package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.LoginPage;
import pages.ProfilePage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC005 – Provera da se profil stranica uspešno učitava nakon logovanja.
 *
 * Preuslov:
 * Korisnik ima validne kredencijale.
 *
 * Koraci:
 * 1. Otvoriti login stranicu
 * 2. Uneti validne kredencijale i prijaviti se
 * 3. Otvoriti profil stranicu
 *
 * Očekivano:
 * Stranica profila je učitana i vidljiv je naslov "Moj profil".
 */
@Tag("regression")
public class ProfilePageTest extends BaseTest {

    @Test
    public void TC005_profilePage_headerVisible() {

        // Otvaram login stranicu
        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();

        // Proveravam da li je login forma učitana
        assertTrue(loginPage.isLoginFormVisible(),
                "Login forma nije vidljiva (email/pass/dugme).");

        // Unosim validne kredencijale i vršim login
        loginSteps.submitValidCredentials(loginPage);

        // Otvaram profil stranicu korisnika
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.open();

        // Proveravam da li je profil stranica uspešno učitana
        assertTrue(profilePage.isProfilePageOpened(),
                "Profil stranica nije otvorena.");
    }
}