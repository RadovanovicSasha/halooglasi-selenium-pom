package tests.base;

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
 * @AfterEach method (BaseTest.tearDown(), which calls driver.quit()).
 * AfterTestExecutionCallback runs immediately after the @Test method and
 * before @AfterEach, while the driver session is still alive.
 *
 * Registered once on BaseTest (@ExtendWith), so it applies to every test
 * class without per-test repetition.
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

        Object testInstance = context.getRequiredTestInstance();
        if (!(testInstance instanceof BaseTest baseTest)) {
            log.warn("Skipping screenshot for {}#{}: test instance is not a BaseTest", className, methodName);
            return;
        }

        WebDriver driver = baseTest.driver;
        if (driver == null) {
            log.warn("Skipping screenshot for {}#{}: driver is null", className, methodName);
            return;
        }

        ScreenshotUtils.captureScreenshot(driver, className, methodName);
    }
}
