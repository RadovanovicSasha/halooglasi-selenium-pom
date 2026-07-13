package tests;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Snima screenshot kada test ne prođe.
 *
 * Koristi AfterTestExecutionCallback, a ne TestWatcher, jer TestWatcher
 * callback-ovi (testFailed/testSuccessful) izvršavaju se TEK NAKON
 * @AfterEach metode (BaseTest.tearDown(), koja poziva driver.quit()).
 * AfterTestExecutionCallback se izvršava odmah nakon @Test metode, a pre
 * @AfterEach, pa je driver sesija tada još uvek aktivna.
 *
 * Registruje se jednom na BaseTest (@ExtendWith), pa važi za sve test
 * klase bez ponavljanja koda po testu.
 */
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotOnFailureExtension.class);
    private static final Path SCREENSHOTS_DIR = Path.of("screenshots");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

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
        if (!(driver instanceof TakesScreenshot)) {
            log.warn("Skipping screenshot for {}#{}: driver does not support TakesScreenshot", className, methodName);
            return;
        }

        // Screenshot capture je "best effort" dijagnostika - ne sme da prekrije
        // originalni razlog pada testa, zato hvatam bilo koji izuzetak ovde
        // (npr. sesija je u međuvremenu postala nedostupna) i samo logujem.
        try {
            Files.createDirectories(SCREENSHOTS_DIR);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path target = SCREENSHOTS_DIR.resolve(buildFileName(className, methodName));
            Files.copy(source.toPath(), target);
            log.info("Screenshot captured: {}", target);
        } catch (Exception e) {
            log.warn("Failed to capture screenshot for {}#{}: {}: {}",
                    className, methodName, e.getClass().getSimpleName(), e.getMessage());
        }
    }

    private String buildFileName(String className, String methodName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        return sanitize(className) + "_" + sanitize(methodName) + "_" + timestamp + ".png";
    }

    private String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
