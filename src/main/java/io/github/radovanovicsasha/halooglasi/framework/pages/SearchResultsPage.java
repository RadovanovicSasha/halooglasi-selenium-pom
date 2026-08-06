package io.github.radovanovicsasha.halooglasi.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents the search results page (/pretraga?...), distinct from
 * SearchPage which represents the home page and the search action itself.
 *
 * Deliberately exposes only state and actions, no assertions - the test
 * class decides what and when to verify.
 */
public class SearchResultsPage extends BasePage {

    private final By resultItem = By.cssSelector(".product-item");
    // Title link inside the card - the only reliable way to select the
    // actual ad link (the card also contains an image link and an unrelated link).
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
     * Selects the first search result (DOM order, no filtering by promoted
     * status - currently every result carries a "Premium" badge, so
     * filtering would leave an empty list) and opens its details page.
     *
     * Doesn't return an AdDetailsPage until the details page is confirmed
     * open - a click alone isn't sufficient evidence of successful navigation.
     */
    public AdDetailsPage openFirstResult() {
        if (!isVisible(resultItem, 10)) {
            throw new IllegalStateException("No search results (.product-item) to select.");
        }

        List<WebElement> items = driver.findElements(resultItem);
        WebElement firstCard = items.get(0);
        WebElement titleLink = firstCard.findElement(titleLinkInCard);
        clickWhenClickable(titleLink, 10);

        AdDetailsPage adDetailsPage = new AdDetailsPage(driver);
        if (!adDetailsPage.isDetailsPageOpened()) {
            throw new IllegalStateException("Clicked an ad, but its details page did not open.");
        }
        return adDetailsPage;
    }
}
