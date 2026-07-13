package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * SearchResultsPage predstavlja stranicu sa rezultatima globalne pretrage
 * (URL /pretraga?...), odvojenu od SearchPage koja predstavlja početnu
 * stranicu i samu akciju pretrage.
 *
 * Namerno izlaže samo stanje i akcije, bez ijedne asertacije - test klasa
 * odlučuje šta i kada da proveri.
 */
public class SearchResultsPage extends BasePage {

    // Kartica pojedinačnog rezultata pretrage
    private final By resultItem = By.cssSelector(".product-item");
    // Naslovni link unutar kartice - jedini pouzdan način da se izabere
    // pravi link ka oglasu (kartica sadrži i link ka slici i nepovezan link)
    private final By titleLinkInCard = By.cssSelector("h3.product-title a");

    public SearchResultsPage(WebDriver driver) {
        super(driver);
    }

    public int resultCount() {
        return driver.findElements(resultItem).size();
    }

    public boolean hasAtLeastOneResult() {
        return isVisible(resultItem, 10);
    }

    /**
     * Bira prvi rezultat pretrage (po redosledu u DOM-u, bez filtriranja po
     * promovisanom statusu - trenutno svi rezultati nose oznaku "Premium",
     * pa bi filtriranje ostavilo praznu listu) i otvara njegovu stranicu
     * detalja.
     *
     * Ne vraća AdDetailsPage dok se ne potvrdi da je stranica detalja zaista
     * otvorena - klik sam po sebi nije dovoljan dokaz uspešne navigacije.
     */
    public AdDetailsPage openFirstResult() {
        if (!isVisible(resultItem, 10)) {
            throw new IllegalStateException("Nema rezultata pretrage (.product-item) za izbor.");
        }

        List<WebElement> items = driver.findElements(resultItem);
        WebElement firstCard = items.get(0);
        WebElement titleLink = firstCard.findElement(titleLinkInCard);
        clickWhenClickable(titleLink, 10);

        AdDetailsPage adDetailsPage = new AdDetailsPage(driver);
        if (!adDetailsPage.isDetailsPageOpened()) {
            throw new IllegalStateException("Kliknuto je na oglas, ali stranica detalja oglasa nije otvorena.");
        }
        return adDetailsPage;
    }
}
