package framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents an individual ad's details page.
 *
 * Deliberately exposes only state (whether the page is open, whether price
 * and category are visible) - no assertions, per POM conventions.
 *
 * Deliberately does NOT expose locators for phone, contact, messaging,
 * favorites, sharing, reporting, or any action that changes account or ad
 * state - this page exists solely for viewing information.
 */
public class AdDetailsPage extends BasePage {

    // Main details container - used as a structural signal the details page actually opened.
    private final By detailsContainer = By.cssSelector("article.product-details-container");
    private final By title = By.cssSelector("h1.product-details-title");
    // Price text varies ("Pozovite", a number, etc.) - so only visibility is
    // checked, never content.
    private final By price = By.cssSelector(".price-product-detail");
    private final By categoryBreadcrumb = By.cssSelector(".widget-breadcrumb");

    public AdDetailsPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Checks whether the ad details page is open, combining the main
     * container and the title as two independent structural signals.
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
