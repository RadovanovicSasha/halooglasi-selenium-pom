package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * ProfilePage predstavlja korisničku stranicu koja se otvara nakon
 * uspešnog logovanja.
 *
 * Na ovoj stranici proveravam da li je korisnik ulogovan i omogućavam
 * akcije kao što je otvaranje korisničkog menija i logout.
 */
public class ProfilePage extends BasePage {

    // URL stranice korisničkog profila
    private final String profileUrl = "https://www.halooglasi.com/profil";
    // Naslov "Moj profil" koji koristim kao indikator da je stranica uspešno učitana
    private final By profileHeader =
            By.cssSelector("a[data-url='/profil/moji-oglasi']");
    // Toggle u headeru koji otvara korisnički dropdown meni
    private final By userMenuToggle =
            By.cssSelector(".logged-in-wrapper a"); // locator za otvaranje user menija
    // Link za logout koji se nalazi u dropdown meniju
    private final By logoutLink =
            By.xpath("//a[contains(.,'Izloguj se')]");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Otvaram direktno stranicu korisničkog profila.
     */
    public void open() {
        driver.get(profileUrl);
    }

    /**
     * Proveravam da li je stranica profila uspešno otvorena.
     * Ako je naslov "Moj profil" vidljiv smatram da je korisnik ulogovan
     * i da je profil stranica učitana.
     */
    public boolean isProfilePageOpened() {
        return isVisible(profileHeader, 10);
    }

    /**
     * Logout scenarij:
     * 1. Otvaram korisnički meni u headeru
     * 2. Klikćem na opciju "Izloguj se"
     *
     * Koristim try/catch blok kao fallback u slučaju da prvi locator
     * ne pogodi element zbog promena u UI.
     */
    public void logoutSession() {

        // pokušavam da otvorim korisnički meni
        try {
            hover(userMenuToggle, 5);
        } catch (Exception e) {
            hover(profileHeader, 5);
        }

        // klik na logout opciju
        try {
            clickWhenClickable(logoutLink, 10);
        } catch (Exception e) {
            jsClick(logoutLink, 10);
        }
    }
}