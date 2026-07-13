package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Predstavlja bezbednosno obaveštenje (Bootstrap modal) koje se ponekad
 * pojavljuje nakon otvaranja sajta, odvojeno od cookie bannera.
 *
 * Ovaj modal može da blokira interakciju sa poljima na stranici (npr.
 * login formom), pa ga proveravam i zatvaram klikom na dugme za potvrdu
 * pre nego što nastavim sa testom.
 */
public class SecurityNotificationModal extends BasePage {

    // Dugme za potvrdu bezbednosnog obaveštenja (jedinstvena klasa u DOM-u)
    private final By confirmBtn = By.cssSelector(".system-notification-confirm");
    // Bootstrap backdrop koji prekriva stranicu dok je modal otvoren
    private final By backdrop = By.cssSelector(".modal-backdrop");

    public SecurityNotificationModal(WebDriver driver) {
        super(driver);
    }

    /**
     * Ako je bezbednosno obaveštenje prisutno, zatvaram ga klikom na dugme
     * za potvrdu i čekam da modal i backdrop nestanu sa stranice.
     *
     * Ako dugme ne postane klikabilno u zadatom vremenu, nastavljam bez
     * greške (TimeoutException ovde samo znači da dugme nije postalo
     * klikabilno u tom roku, ne dokazuje da modal nikada nije postojao).
     */
    public void dismissIfPresent() {
        WebElement confirmButton;
        try {
            confirmButton = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(confirmBtn));
        } catch (TimeoutException e) {
            return;
        }

        confirmButton.click();

        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(confirmBtn));
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(backdrop));
    }
}
