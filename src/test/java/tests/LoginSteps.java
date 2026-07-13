package tests;

import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import testdata.TestData;

/**
 * LoginSteps objedinjuje ponovljene korake logovanja koji su ranije bili
 * duplirani u više test klasa (otvaranje login stranice i unos važećih
 * kredencijala iz TestData).
 *
 * Namerno ne sadrži nijednu asertaciju - test klasa i dalje sama odlučuje
 * šta i kada da proveri. Takođe ne ponavlja odgovornosti iz BaseTest.setUp()
 * (kreiranje drajvera, cookie banner, bezbednosno obaveštenje) - pretpostavlja
 * da je to već odrađeno pre nego što se LoginSteps koristi.
 */
public class LoginSteps {

    private final WebDriver driver;

    public LoginSteps(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Otvara login stranicu. Test sam odlučuje da li i kada da asertuje
     * njenu vidljivost pre unosa kredencijala.
     */
    public LoginPage openLoginPage() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        return loginPage;
    }

    /**
     * Unosi važeće kredencijale (iz TestData) na već otvorenoj login stranici.
     */
    public void submitValidCredentials(LoginPage loginPage) {
        loginPage.login(TestData.haloEmail, TestData.haloPass);
    }
}
