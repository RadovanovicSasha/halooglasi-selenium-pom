package framework.pages;

import io.github.radovanovicsasha.halooglasi.framework.config.EnvConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

/**
 * Represents the user's profile page, opened after a successful login.
 */
public class ProfilePage extends BasePage {

    private final String profileUrl = EnvConfig.getBaseUrl() + "profil";
    // "Moj profil" heading, used as the signal the page loaded successfully.
    private final By profileHeader =
            By.cssSelector("a[data-url='/profil/moji-oglasi']");
    // Header toggle that opens the user dropdown menu.
    private final By userMenuToggle =
            By.cssSelector(".logged-in-wrapper a");
    private final By logoutLink =
            By.xpath("//a[contains(.,'Izloguj se')]");

    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get(profileUrl);
    }

    public boolean isProfilePageOpened() {
        return isVisible(profileHeader, 10);
    }

    /**
     * Logout flow: opens the user menu in the header, then clicks "Izloguj
     * se". Falls back to a JS click if the standard click doesn't register,
     * in case the UI changes.
     */
    public void logoutSession() {
        try {
            hover(userMenuToggle, 5);
        } catch (TimeoutException e) {
            hover(profileHeader, 5);
        }

        try {
            clickWhenClickable(logoutLink, 10);
        } catch (TimeoutException e) {
            jsClick(logoutLink, 10);
        }
    }
}
