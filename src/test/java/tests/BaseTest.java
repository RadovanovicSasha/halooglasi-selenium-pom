package tests;

import config.FrameworkConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.DriverFactory; // za CI
import support.ObstacleHandler;

/**
 * BaseTest je osnovna klasa za sve testove u projektu.
 *
 * Ovde sam definisao inicijalizaciju WebDriver-a i osnovni setup koji
 * će koristiti sve test klase (pokretanje browsera, otvaranje sajta i
 * zatvaranje browsera nakon testa).
 */
@ExtendWith(ScreenshotOnFailureExtension.class)
public class BaseTest {

    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    // WebDriver koji koriste sve test klase
    protected WebDriver driver;

/**
* Pokrećem browser i otvaram početnu stranicu pre svakog testa.
*/
    @BeforeEach
    public void setUp() {

        log.info("Starting Chrome driver (CI={})", FrameworkConfig.isCi());
        driver = DriverFactory.createChromeDriver(); // za CI

        driver.manage().window().maximize();
        driver.get(FrameworkConfig.BASE_URL);

        // Ako Cloudflare/Turnstile blokira stranicu, prijavljujem to odmah i
        // jasno, umesto da svaki test kasnije padne na nejasnom timeout-u.
        ObstacleHandler.failFastIfAntiBotChallenge(driver);

        // Uklanjam poznate prepreke (cookie banner, bezbednosno obaveštenje)
        // ako su prisutne, da ne blokiraju elemente (posebno u CI).
        ObstacleHandler.dismissKnownObstacles(driver);
    }

/**
* Nakon svakog testa zatvaram browser kako bih osigurao
* da sledeći test počinje sa čistom sesijom.
*/
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            log.info("Quitting driver");
            driver.quit();
        }
    }
}