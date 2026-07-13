package pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.BasePage;

/**
 * HeaderComponent predstavlja deo headera koji je prisutan na više
 * stranica (Home, Login, Profile) - trenutno samo indikator da je
 * korisnik ulogovan.
 *
 * Izdvojen je kao komponenta jer je ovo isto stanje ranije bilo
 * duplirano (različit locator i različita implementacija) u HomePage
 * i ProfilePage.
 */
public class HeaderComponent extends BasePage {

    // Element u headeru koji označava da je korisnik ulogovan ("Moj profil")
    private final By loggedInIndicator = By.cssSelector(".logged-in-wrapper a");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    /**
     * Proveravam da li je korisnik ulogovan na osnovu vidljivosti
     * indikatora u headeru.
     */
    public boolean isUserLoggedIn() {
        return isVisible(loggedInIndicator, 10);
    }
}
