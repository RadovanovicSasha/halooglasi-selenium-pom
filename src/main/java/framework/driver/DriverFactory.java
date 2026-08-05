package framework.driver;

import framework.config.EnvConfig;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Creates configured WebDriver instances. Headless/CI-safe arguments are
 * applied automatically when running under CI so local runs stay visible
 * (useful for debugging) while CI runs stay headless.
 */
public class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createChromeDriver() {
        ChromeOptions options = new ChromeOptions();

        if (EnvConfig.isCi()) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
        }

        return new ChromeDriver(options);
    }
}
