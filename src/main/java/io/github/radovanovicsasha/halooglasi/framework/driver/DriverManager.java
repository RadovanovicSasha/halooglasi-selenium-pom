package io.github.radovanovicsasha.halooglasi.framework.driver;

import org.openqa.selenium.WebDriver;

/**
 * Holds the active WebDriver per thread. Each test thread gets its own
 * driver instance rather than sharing a single static reference, which is
 * what makes running tests in parallel (see junit-platform.properties)
 * safe: two concurrently-executing tests never see or quit each other's
 * browser session.
 */
public final class DriverManager {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void setDriver(WebDriver driver) {
        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    /**
     * Quits the current thread's driver, if any, and clears the ThreadLocal
     * slot so the thread doesn't retain a reference to a dead session (e.g.
     * when JUnit reuses worker threads across tests in parallel runs).
     */
    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }
}
