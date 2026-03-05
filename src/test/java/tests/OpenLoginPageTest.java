package tests;

import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC003 – Otvaranje login stranice.
 *
 * Koraci:
 * 1. Otvoriti login stranicu aplikacije
 *
 * Očekivano:
 * Login forma je prikazana i vidljiva su polja za e-mail,
 * lozinku i dugme za prijavu.
 */
public class OpenLoginPageTest extends BaseTest {

    @Test
    public void TC003_openLoginPage_loginFormVisible() {

        // Otvaram login stranicu
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        // Proveravam da li je login forma vidljiva
        assertTrue(loginPage.isLoginFormVisible(),
                "Login forma nije prikazana (email/lozinka/dugme).");
    }
}