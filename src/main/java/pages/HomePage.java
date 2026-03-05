package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * HomePage predstavlja početnu stranicu sajta HaloOglasi.
 *
 * Ova klasa sadrži osnovne elemente koji su dostupni odmah nakon
 * otvaranja sajta, kao što su globalna pretraga i indikator da je
 * korisnik uspešno ulogovan.
 */
public class HomePage extends BasePage {

    // Osnovni URL početne stranice
    private final String baseUrl = "https://www.halooglasi.com/";
    // Polje za globalnu pretragu oglasa
    private final By searchInput =
            By.cssSelector("input[type='search'], input[name='query'], input[placeholder*='Pretraga']");
    // Element u headeru koji označava da je korisnik ulogovan ("Moj profil")
    private final By mojProfilLink =
            By.xpath("//p[@class='header-label' and contains(text(),'Moj profil')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }
    /**
     * Otvaram početnu stranicu sajta.
     * Ovu metodu koristim na početku testova koji zahtevaju pristup Home stranici.
     */
    public void open() {
        driver.get(baseUrl);
    }

    /**
     * Proveravam da li je polje za pretragu vidljivo.
     * Ovo koristim kao osnovnu validaciju da se Home stranica uspešno učitala.
     */
    // ukinuto zbog CI (sledeća metoda ispod):
//    public boolean isSearchVisible() {
//        return isVisible(searchInput, 10);
//    }
    // dodato zbog CI:
    public boolean isSearchVisible() {
        int timeout = "true".equalsIgnoreCase(System.getenv("CI")) ? 20 : 10;
        return isVisible(searchInput, timeout);
    }
    /**
     * Proveravam da li je korisnik ulogovan.
     * Ako je element "Moj profil" vidljiv u headeru, smatram da je login uspešan.
     */
    public boolean isUserLoggedIn() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(mojProfilLink));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
}