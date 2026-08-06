package tests.base;

import io.github.radovanovicsasha.halooglasi.framework.config.EnvConfig;
import framework.driver.DriverFactory;
import framework.driver.DriverManager;
import framework.support.ObstacleHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for all tests in the project.
 *
 * Defines WebDriver initialization and the shared setup every test class
 * uses (launching the browser, opening the site, and closing the browser
 * after the test). Driver access goes through DriverManager rather than a
 * shared static field, so tests running on different threads never
 * interfere with each other's session.
 */
@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver driver;

    /**
     * Launches the browser and opens the site's home page before each test.
     */
    @BeforeEach
    public void setUp() {

        String browser = EnvConfig.getBrowser();
        log.info("Starting {} driver (CI={})", browser, EnvConfig.isCi());
        DriverManager.setDriver(DriverFactory.createDriver(browser));
        driver = DriverManager.getDriver();

        driver.manage().window().maximize();
        driver.get(EnvConfig.getBaseUrl());

        // If Cloudflare/Turnstile is blocking the page, report it immediately
        // and clearly, instead of letting every test fail on an ambiguous timeout.
        ObstacleHandler.failFastIfAntiBotChallenge(driver);

        // Dismiss known obstacles (cookie banner, security notification) if
        // present, so they don't block elements later (especially in CI).
        ObstacleHandler.dismissKnownObstacles(driver);
    }

    /**
     * Closes the browser after each test so the next test starts with a
     * clean session.
     */
    @AfterEach
    public void tearDown() {
        log.info("Quitting driver");
        DriverManager.quitDriver();
    }
}
