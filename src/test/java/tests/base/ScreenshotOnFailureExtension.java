package tests.base;

import io.github.radovanovicsasha.halooglasi.framework.driver.DriverManager;
import framework.utils.ScreenshotUtils;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Captures a screenshot when a test fails.
 *
 * Uses AfterTestExecutionCallback rather than TestWatcher, because
 * TestWatcher callbacks (testFailed/testSuccessful) run only AFTER the
 * @AfterEach method (BaseTest.tearDown(), which calls DriverManager.quitDriver()).
 * AfterTestExecutionCallback runs immediately after the @Test method and
 * before @AfterEach, while the driver session is still alive.
 *
 * Registered once on BaseTest (@ExtendWith), so it applies to every test
 * class without per-test repetition. Reads the driver from DriverManager
 * (the current thread's session) rather than the test instance field, so
 * it works the same whether or not tests are running in parallel.
 */
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotOnFailureExtension.class);

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isEmpty()) {
            return;
        }

        String className = context.getRequiredTestClass().getSimpleName();
        String methodName = context.getRequiredTestMethod().getName();

        WebDriver driver = DriverManager.getDriver();
        if (driver == null) {
            log.warn("Skipping screenshot for {}#{}: driver is null", className, methodName);
            return;
        }

        ScreenshotUtils.captureScreenshot(driver, className, methodName);
    }
}
