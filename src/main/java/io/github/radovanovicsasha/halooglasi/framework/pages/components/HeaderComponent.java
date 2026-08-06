package io.github.radovanovicsasha.halooglasi.framework.pages.components;

import io.github.radovanovicsasha.halooglasi.framework.pages.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the part of the header shared across multiple pages (Home,
 * Login, Profile) - currently just the "logged in" indicator.
 *
 * Extracted as a component because this same state was previously
 * duplicated (with different locators and implementations) in HomePage and
 * ProfilePage.
 */
public class HeaderComponent extends BasePage {

    private final By loggedInIndicator = By.cssSelector(".logged-in-wrapper a");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public boolean isUserLoggedIn() {
        return isVisible(loggedInIndicator, 10);
    }
}
