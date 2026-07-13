package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * AdDetailsPage predstavlja stranicu sa detaljima pojedinačnog oglasa.
 *
 * Namerno izlaže samo stanje (da li je stranica otvorena, da li su cena i
 * kategorija vidljive) - bez ijedne asertacije, u skladu sa POM pravilima.
 *
 * Namerno NE sadrži lokatore za telefon, kontakt, poruku, favorite, deljenje,
 * prijavu nepravilnosti niti bilo koju akciju koja menja stanje naloga ili
 * oglasa - ova stranica služi isključivo za pregled informacija.
 */
public class AdDetailsPage extends BasePage {

    // Glavni kontejner stranice sa detaljima - koristim ga kao strukturni
    // pokazatelj da je stranica detalja oglasa zaista otvorena
    private final By detailsContainer = By.cssSelector("article.product-details-container");
    // Naslov oglasa
    private final By title = By.cssSelector("h1.product-details-title");
    // Cena oglasa (tekst varira - "Pozovite", broj i sl. - zato proveravam
    // samo vidljivost, nikad sadržaj)
    private final By price = By.cssSelector(".price-product-detail");
    // Breadcrumb sa kategorijom oglasa
    private final By categoryBreadcrumb = By.cssSelector(".widget-breadcrumb");

    public AdDetailsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Proveravam da li je stranica detalja oglasa otvorena - kombinujem
     * glavni kontejner i naslov kao dva nezavisna strukturna signala.
     */
    public boolean isDetailsPageOpened() {
        return isVisible(detailsContainer, 10) && isVisible(title, 10);
    }

    public boolean isPriceVisible() {
        return isVisible(price, 10);
    }

    public boolean isCategoryBreadcrumbVisible() {
        return isVisible(categoryBreadcrumb, 10);
    }
}
