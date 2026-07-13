package pages;

import config.FrameworkConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.Keys; // generalno za CI

/**
 * SearchPage predstavlja funkcionalnost globalne pretrage oglasa na sajtu.
 *
 * Ovde sam izdvojio elemente za polje pretrage i rezultate kako bih mogao
 * da automatizujem scenarije vezane za pretragu oglasa.
 */
public class SearchPage extends BasePage {

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
        driver.get(FrameworkConfig.BASE_URL);
    }

    /**
     * Unosim termin pretrage i pokrećem pretragu pritiskom na ENTER.
     * Umesto clear() (koji zna da pukne u CI), radim Ctrl+A + Delete.
     */
    public void search(String term) {
        var input = driver.findElement(searchInput);

        // klik da input dobije fokus (u CI/headless je ovo bitno)
        input.click();

        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);

        input.sendKeys(term);
        input.sendKeys(Keys.ENTER);
    }

    /**
     * Proveravam da li postoje rezultati pretrage.
     * Ako je prvi rezultat vidljiv smatram da je pretraga uspešno izvršena.
     */
    public boolean hasResults() {
        return isVisible(firstResult, 10);
    }
}