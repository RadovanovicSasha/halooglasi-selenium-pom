package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.List;
import org.openqa.selenium.WebElement;

/**
 * Ova klasa predstavlja cookie banner koji se pojavljuje prilikom prvog
 * otvaranja sajta.
 *
 * Banner može da blokira interakciju sa elementima na stranici, pa sam
 * izdvojio posebnu Page klasu koja proverava da li je banner prisutan
 * i po potrebi prihvata cookies.
 */
public class CookiesBannerPage extends BasePage {

    // Dugme za prihvatanje cookies-a na sajtu
    private final By acceptBtn = By.id("onetrust-accept-btn-handler");

    public CookiesBannerPage(WebDriver driver) {
        super(driver);
    }

    /**
     * Proveravam da li je cookie banner prisutan i ako jeste klikćem
     * na dugme za prihvatanje.
     *
     * Koristim findElements umesto findElement da izbegnem exception
     * u slučaju da banner nije prikazan.
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