package io.github.radovanovicsasha.halooglasi.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Represents the cookie consent banner shown on first site visit.
 *
 * The banner can block interaction with other page elements, so it's
 * modeled as its own page object that checks for its presence and accepts
 * cookies when needed.
 */
public class CookiesBannerPage extends BasePage {

    private final By acceptBtn = By.id("onetrust-accept-btn-handler");

    public CookiesBannerPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Accepts cookies if the banner is present. Uses findElements instead of
     * findElement to avoid an exception when the banner isn't shown.
     */
    public void acceptCookiesIfPresent() {
        List<WebElement> els = driver.findElements(acceptBtn);
        if (!els.isEmpty() && els.get(0).isDisplayed()) {
            els.get(0).click();
        }
    }

    public boolean isBannerPresent() {
        return !driver.findElements(acceptBtn).isEmpty();
    }
}
