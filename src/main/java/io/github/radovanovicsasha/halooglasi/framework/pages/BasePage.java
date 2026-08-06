package io.github.radovanovicsasha.halooglasi.framework.pages;

import io.github.radovanovicsasha.halooglasi.framework.support.ObstacleHandler;
import framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Base class for all page objects. Centralizes the wait/click/hover helpers
 * every page object needs, so common interaction logic isn't duplicated
 * across pages.
 */
public class BasePage {

    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Checks whether an element becomes visible within the given timeout.
     *
     * A first timeout doesn't immediately mean false - that would make an
     * ordinary timeout, a broken locator, and "the page was intercepted by
     * Cloudflare" indistinguishable (all three look identical: the element
     * never appeared). So on timeout, first check whether it's an anti-bot
     * challenge (in which case throw a clear error instead of a silent
     * false), then dismiss known obstacles and retry the check exactly
     * once. If the element still isn't visible after that, return false as
     * before - a normal outcome (broken locator or a genuinely slow/absent
     * element), unchanged.
     */
    protected boolean isVisible(By locator, int seconds) {
        try {
            WaitUtils.waitForVisible(driver, locator, seconds);
            return true;
        } catch (TimeoutException e) {
            ObstacleHandler.failFastIfAntiBotChallenge(driver);
            ObstacleHandler.dismissKnownObstacles(driver);
            try {
                WaitUtils.waitForVisible(driver, locator, seconds);
                return true;
            } catch (TimeoutException retryTimeout) {
                return false;
            }
        }
    }

    /**
     * Waits for an element to become clickable, then clicks it.
     *
     * If the first attempt times out, it may be due to a known, dismissible
     * obstacle (e.g. a notification that appeared after the initial
     * dismissal in BaseTest). ObstacleHandler first checks it isn't an
     * anti-bot challenge (in which case it doesn't attempt anything
     * further), then dismisses known obstacles and retries the click
     * exactly once. If that attempt also fails, the original
     * TimeoutException propagates normally - indistinguishable from a
     * broken locator or ordinary slow loading.
     */
    protected void clickWhenClickable(By locator, int seconds) {
        try {
            WaitUtils.waitForClickable(driver, locator, seconds).click();
        } catch (TimeoutException e) {
            ObstacleHandler.retryAfterDismissingObstacles(driver,
                    () -> WaitUtils.waitForClickable(driver, locator, seconds).click());
        }
    }

    /**
     * Same as clickWhenClickable(By, int), but for an element that's already
     * been located within a specific context (e.g. one particular result
     * card) and shouldn't be re-queried against the whole page.
     */
    protected void clickWhenClickable(WebElement element, int seconds) {
        WaitUtils.waitForClickable(driver, element, seconds).click();
    }

    /**
     * JS click used as a fallback when a standard Selenium click doesn't
     * succeed (e.g. overlay elements or dynamic UI).
     */
    protected void jsClick(By locator, int seconds) {
        WebElement el = WaitUtils.waitForPresence(driver, locator, seconds);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }

    protected void hover(By locator, int seconds) {
        WebElement el = WaitUtils.waitForPresence(driver, locator, seconds);
        new Actions(driver).moveToElement(el).perform();
    }
}
