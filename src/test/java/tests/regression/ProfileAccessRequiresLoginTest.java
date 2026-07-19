package tests.regression;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;
import tests.BaseTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * TC011 – Pristup profil stranici bez prethodne prijave.
 *
 * Preduslov:
 * Korisnik NIJE ulogovan (test namerno ne koristi LoginSteps).
 *
 * Koraci:
 * 1. Direktno otvoriti URL profil stranice (/profil) bez prijave
 *
 * Očekivano:
 * Sadržaj profil stranice se ne prikazuje neulogovanom korisniku -
 * proveravam stvarno autorizaciono ponašanje aplikacije, ne samo da
 * element postoji na stranici.
 *
 * Test ne zahteva kredencijale iz TestData, pa može da se izvrši i u
 * okruženjima bez podešenog testdata-local.properties (npr. CI bez
 * provizionisanih secreta - videti README, Project Limitations).
 */
@Tag("regression")
public class ProfileAccessRequiresLoginTest extends BaseTest {

    @Test
    public void TC011_profileAccessWithoutLogin_profileNotOpened() {

        // Direktno otvaram profil stranicu bez prethodne prijave
        ProfilePage profilePage = new ProfilePage(driver);
        profilePage.open();

        // Proveravam da profil stranica NIJE otvorena neulogovanom korisniku
        assertFalse(profilePage.isProfilePageOpened(),
                "Profil stranica je otvorena bez prijave korisnika.");
    }
}
