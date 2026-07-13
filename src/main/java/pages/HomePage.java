package pages;

import config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * HomePage predstavlja početnu stranicu sajta HaloOglasi.
 *
 * Ova klasa sadrži osnovne elemente koji su dostupni odmah nakon
 * otvaranja sajta, kao što su globalna pretraga i indikator da je
 * korisnik uspešno ulogovan.
 */
public class HomePage extends BasePage {

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
        driver.get(FrameworkConfig.BASE_URL);
    }

    /**
     * Proveravam da li je polje za pretragu vidljivo.
     * Ovo koristim kao osnovnu validaciju da se Home stranica uspešno učitala.
     * Timeout je duži u CI okruženju zbog sporijeg učitavanja stranice.
     */
    public boolean isSearchVisible() {
        int timeout = FrameworkConfig.isCi() ? 20 : 10;
        return isVisible(searchInput, timeout);
    }
    /**
     * Proveravam da li je korisnik ulogovan.
     * Ako je element "Moj profil" vidljiv u headeru, smatram da je login uspešan.
     * Koristim postojeći isVisible helper iz BasePage umesto sopstvenog
     * WebDriverWait-a (ranije je ovde bila duplirana wait logika).
     */
    public boolean isUserLoggedIn() {
        return isVisible(mojProfilLink, 10);
    }
}