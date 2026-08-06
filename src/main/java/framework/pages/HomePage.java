package framework.pages;

import io.github.radovanovicsasha.halooglasi.framework.config.EnvConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Represents the Halo Oglasi home page.
 *
 * Exposes the elements available immediately after the site loads, such as
 * the global search field and the "logged in" indicator.
 */
public class HomePage extends BasePage {

    // Global ad search field.
    private final By searchInput =
            By.cssSelector("input[type='search'], input[name='query'], input[placeholder*='Pretraga']");
    // Header element indicating a logged-in user ("Moj profil").
    private final By mojProfilLink =
            By.xpath("//p[@class='header-label' and contains(text(),'Moj profil')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    /**
     * Opens the site's home page.
     */
    public void open() {
        driver.get(EnvConfig.getBaseUrl());
    }

    /**
     * Checks whether the search field is visible, as a basic signal that
     * the home page loaded successfully. Timeout is longer in CI due to
     * slower page loads there.
     */
    public boolean isSearchVisible() {
        int timeout = EnvConfig.isCi() ? 20 : 10;
        return isVisible(searchInput, timeout);
    }

    /**
     * Checks whether the user is logged in, based on visibility of the
     * "Moj profil" header indicator.
     */
    public boolean isUserLoggedIn() {
        return isVisible(mojProfilLink, 10);
    }
}
