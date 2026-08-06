package io.github.radovanovicsasha.halooglasi.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Represents a security notification (Bootstrap modal) that occasionally
 * appears after the site loads, separate from the cookie banner.
 *
 * This modal can block interaction with page fields (e.g. the login form),
 * so it's checked and dismissed before the test proceeds.
 */
public class SecurityNotificationModal extends BasePage {

    private final By confirmBtn = By.cssSelector(".system-notification-confirm");
    // Bootstrap backdrop covering the page while the modal is open.
    private final By backdrop = By.cssSelector(".modal-backdrop");

    public SecurityNotificationModal(WebDriver driver) {
        super(driver);
    }

    /**
     * Quick check (no waiting) for whether the confirm button is currently
     * in the DOM. Uses findElements instead of findElement to avoid an
     * exception when the modal isn't shown.
     */
    public boolean isPresent() {
        return !driver.findElements(confirmBtn).isEmpty();
    }

    /**
     * If the security notification is present, dismisses it by clicking the
     * confirm button and waits for the modal and backdrop to disappear.
     *
     * If the button doesn't become clickable in time, proceeds without
     * error (a TimeoutException here only means the button didn't become
     * clickable in that window, not that the modal never existed).
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
