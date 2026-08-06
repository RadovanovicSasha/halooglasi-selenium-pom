package io.github.radovanovicsasha.halooglasi.framework.pages;

import io.github.radovanovicsasha.halooglasi.framework.config.EnvConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

/**
 * Represents the site's global ad search.
 */
public class SearchPage extends BasePage {

    private final By searchInput = By.cssSelector("input[name='query'], input[placeholder*='Šta tražite'], input[type='search']");
    private final By firstResult = By.cssSelector(".product-item, .product, .product-list .product-item");

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens the home page to access the global search field from there.
     */
    public void open() {
        driver.get(EnvConfig.getBaseUrl());
    }

    /**
     * Enters a search term and submits by pressing ENTER. Uses Ctrl+A +
     * Delete instead of clear(), which is unreliable in CI.
     */
    public void search(String term) {
        var input = driver.findElement(searchInput);

        // Click first so the input has focus (matters in CI/headless).
        input.click();

        input.sendKeys(Keys.CONTROL + "a");
        input.sendKeys(Keys.DELETE);

        input.sendKeys(term);
        input.sendKeys(Keys.ENTER);
    }

    public boolean hasResults() {
        return isVisible(firstResult, 10);
    }
}
