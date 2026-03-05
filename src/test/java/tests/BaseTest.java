package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions; // za CI

/**
 * BaseTest je osnovna klasa za sve testove u projektu.
 *
 * Ovde sam definisao inicijalizaciju WebDriver-a i osnovni setup koji
 * će koristiti sve test klase (pokretanje browsera, otvaranje sajta i
 * zatvaranje browsera nakon testa).
 */
public class BaseTest {

    // WebDriver koji koriste sve test klase
    protected WebDriver driver;
    // Osnovni URL aplikacije
    protected final String baseUrl = "https://www.halooglasi.com/";

/**
* Pokrećem browser i otvaram početnu stranicu pre svakog testa.
*/
    @BeforeEach
    public void setUp() {

        ChromeOptions options = new ChromeOptions(); // objekat klase ChromeOptions za CI

        // DOPUNA ZA GITHUB ACTIONS
        options.addArguments("--headless=new");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");

        driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get(baseUrl);
    }

/**
* Nakon svakog testa zatvaram browser kako bih osigurao
* da sledeći test počinje sa čistom sesijom.
*/
    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}