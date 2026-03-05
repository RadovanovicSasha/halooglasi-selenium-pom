package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * LoginPage predstavlja stranicu za autentikaciju korisnika.
 *
 * Ovde sam definisao elemente login forme i metode koje koristim
 * za proveru da li je forma učitana i za unos kredencijala.
 */
public class LoginPage extends BasePage {

    // URL login stranice
    private final String loginUrl = "https://www.halooglasi.com/prijava";
    // Polje za unos e-mail adrese ili korisničkog imena
    private final By emailInput =
            By.xpath("//label[contains(text(),'E-mail') or contains(text(),'korisničko ime')]/following::input[1]");
    // Polje za unos lozinke
    private final By passwordInput =
            By.xpath("//label[contains(text(),'Lozinka')]/following::input[1]");
    // Dugme za prijavu na sistem
    private final By loginButton =
            By.xpath("//button[contains(.,'Uloguj me')]");
    // dugme za prihvatanje cookies bannera
    private final By acceptCookiesBtn = By.id("onetrust-accept-btn-handler");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Otvaram login stranicu aplikacije.
     */
    public void open() {
        driver.get(loginUrl);
    }

    /**
     * Proveravam da li je login forma vidljiva.
     * Ako su polja za e-mail, lozinku i login dugme prisutni,
     * smatram da je stranica uspešno učitana.
     */
    public boolean isLoginFormVisible() {
        return isVisible(emailInput, 10)
                && isVisible(passwordInput, 10)
                && isVisible(loginButton, 10);
    }

    /**
     * Unosim kredencijale i pokrećem login akciju.
     */
    public void login(String user, String pass) {

        // Ako se pojavi cookies banner, prihvati ga
        if (isVisible(acceptCookiesBtn, 3)) {
            clickWhenClickable(acceptCookiesBtn, 3);
        }
        driver.findElement(emailInput).sendKeys(user);
        driver.findElement(passwordInput).sendKeys(pass);
        // stabilniji klik
        clickWhenClickable(loginButton, 5);
        // čekam da se pojavi user meni u headeru (login je uspeo)
        isVisible(By.cssSelector(".logged-in-wrapper a"), 10);
    }
}