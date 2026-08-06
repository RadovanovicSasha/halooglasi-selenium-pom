package io.github.radovanovicsasha.halooglasi.framework.utils;

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
import java.util.Optional;

/**
 * Captures a timestamped screenshot to the screenshots/ directory. Capture
 * is best-effort: any failure is logged and swallowed here so it never
 * masks the original test failure it's diagnosing.
 */
public final class ScreenshotUtils {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final Path SCREENSHOTS_DIR = Path.of("screenshots");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");

    private ScreenshotUtils() {
    }

    public static Optional<Path> captureScreenshot(WebDriver driver, String className, String methodName) {
        if (!(driver instanceof TakesScreenshot)) {
            log.warn("Skipping screenshot for {}#{}: driver does not support TakesScreenshot", className, methodName);
            return Optional.empty();
        }

        try {
            Files.createDirectories(SCREENSHOTS_DIR);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path target = SCREENSHOTS_DIR.resolve(buildFileName(className, methodName));
            Files.copy(source.toPath(), target);
            log.info("Screenshot captured: {}", target);
            return Optional.of(target);
        } catch (Exception e) {
            log.warn("Failed to capture screenshot for {}#{}: {}: {}",
                    className, methodName, e.getClass().getSimpleName(), e.getMessage());
            return Optional.empty();
        }
    }

    private static String buildFileName(String className, String methodName) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        return sanitize(className) + "_" + sanitize(methodName) + "_" + timestamp + ".png";
    }

    private static String sanitize(String value) {
        return value.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
