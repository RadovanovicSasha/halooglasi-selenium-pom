package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys;

/**
 * SearchPage predstavlja funkcionalnost globalne pretrage oglasa na sajtu.
 *
 * Ovde sam izdvojio elemente za polje pretrage i rezultate kako bih mogao
 * da automatizujem scenarije vezane za pretragu oglasa.
 */
public class SearchPage extends BasePage {

    // Osnovni URL sajta (početna stranica sa pretragom)
    private final String baseUrl = "https://www.halooglasi.com/";
    // Polje za unos pojma pretrage
    private final By searchInput = By.cssSelector("input[name='query'], input[placeholder*='Šta tražite'], input[type='search']");
    // Prvi rezultat pretrage koji koristim kao indikator da postoje rezultati
    private final By firstResult = By.cssSelector(".product-item, .product, .product-list .product-item");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Otvaram početnu stranicu sajta kako bih mogao da koristim globalnu pretragu.
     */
    public void open() {
        driver.get(baseUrl);
    }

    /**
     * Unosim termin pretrage i pokrećem pretragu pritiskom na ENTER.
     */
    public void search(String term) {
        driver.findElement(searchInput).clear();
        driver.findElement(searchInput).sendKeys(term);
        driver.findElement(searchInput).sendKeys(Keys.ENTER);
    }

    /**
     * Proveravam da li postoje rezultati pretrage.
     * Ako je prvi rezultat vidljiv smatram da je pretraga uspešno izvršena.
     */
    public boolean hasResults() {
        return isVisible(firstResult, 10);
    }
}