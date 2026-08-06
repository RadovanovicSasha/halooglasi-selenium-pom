package framework.support;

import io.github.radovanovicsasha.halooglasi.framework.pages.CookiesBannerPage;
import io.github.radovanovicsasha.halooglasi.framework.pages.SecurityNotificationModal;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Centralizes handling of known, dismissible UI obstacles (cookie banner,
 * security notification modal) and explicit detection of an external
 * anti-bot challenge (Cloudflare, Turnstile).
 *
 * Deliberately covers ONLY confirmed, dismissible elements on the Halo
 * Oglasi site - no AI, no caching, no layered heuristics. An obstacle that
 * isn't one of the known ones (cookie banner, security notification) is
 * left untouched.
 *
 * Flow for an action that fails because of a possible obstacle:
 * 1. the original action is attempted directly first (called outside this class)
 * 2. on failure: check whether it's an external anti-bot challenge - if so,
 *    do NOT attempt to bypass it, throw AntiBotChallengeException immediately
 * 3. otherwise, dismiss known obstacles if present
 * 4. retry the original action exactly once
 * 5. if it still fails, let the retry's exception propagate normally (an
 *    ordinary Selenium error - broken locator or plain timeout, unchanged)
 */
public final class ObstacleHandler {

    private static final Logger log = LoggerFactory.getLogger(ObstacleHandler.class);

    // Known Cloudflare challenge markup - the iframe/wrapper Cloudflare
    // injects in place of the real page during a Turnstile/managed challenge.
    private static final By ANTI_BOT_MARKUP = By.cssSelector(
            "#challenge-running, #cf-wrapper, iframe[src*='challenges.cloudflare.com'], iframe[title*='hallenge']");

    // Text markers Cloudflare (and similar protections) render on the
    // interstitial page instead of actual site content.
    private static final List<String> ANTI_BOT_TEXT_MARKERS = List.of(
            "verify you are human",
            "performing security verification",
            "checking your browser",
            "attention required",
            "just a moment");

    private ObstacleHandler() {
    }

    /**
     * Throws AntiBotChallengeException if the page is currently showing a
     * known anti-bot check instead of the application. Never clicks or
     * attempts to solve it - only reports that the site is unavailable for
     * an external reason.
     */
    public static void failFastIfAntiBotChallenge(WebDriver driver) {
        if (!isAntiBotChallengePresent(driver)) {
            return;
        }

        String title = driver.getTitle();
        String titleForMessage = (title == null || title.isBlank()) ? "(unavailable)" : title;

        String message = "Execution aborted before the application became testable: "
                + "an external anti-bot challenge (Cloudflare/Turnstile) intercepted the browser "
                + "instead of the target application. Page title at detection: \"" + titleForMessage + "\". "
                + "This is an infrastructure/environment condition, not a defect in the test framework "
                + "or the application under test. Verify: current Cloudflare challenge status for the "
                + "site, network/IP restrictions on this runner, CI environment configuration, and "
                + "general website availability.";

        log.error(message);
        throw new AntiBotChallengeException(message);
    }

    /**
     * Dismisses known obstacles if currently present (cookie banner, security
     * notification modal). No-op if neither is present.
     */
    public static void dismissKnownObstacles(WebDriver driver) {
        CookiesBannerPage cookiesBanner = new CookiesBannerPage(driver);
        if (cookiesBanner.isBannerPresent()) {
            log.info("Obstacle detected: cookie consent banner - dismissing");
            cookiesBanner.acceptCookiesIfPresent();
        }

        SecurityNotificationModal securityModal = new SecurityNotificationModal(driver);
        if (securityModal.isPresent()) {
            log.info("Obstacle detected: security notification modal - dismissing");
            securityModal.dismissIfPresent();
        }
    }

    /**
     * Called once the original action (e.g. a click) has already failed.
     * First checks whether an anti-bot challenge is responsible (in which
     * case it gives up immediately instead of retrying), then dismisses
     * known obstacles and retries the original action exactly once. If that
     * retry also fails, the exception is not caught here - it propagates
     * normally to the caller.
     */
    public static void retryAfterDismissingObstacles(WebDriver driver, Runnable originalAction) {
        failFastIfAntiBotChallenge(driver);
        dismissKnownObstacles(driver);
        log.info("Retrying original action once after dismissing obstacle(s)");
        originalAction.run();
    }

    private static boolean isAntiBotChallengePresent(WebDriver driver) {
        String title = safeLower(driver.getTitle());
        String body = safeLower(readBodyTextQuietly(driver));

        boolean textMatch = ANTI_BOT_TEXT_MARKERS.stream().anyMatch(marker -> title.contains(marker) || body.contains(marker));
        boolean markupMatch = !driver.findElements(ANTI_BOT_MARKUP).isEmpty();
        return textMatch || markupMatch;
    }

    private static String readBodyTextQuietly(WebDriver driver) {
        try {
            return driver.findElement(By.tagName("body")).getText();
        } catch (Exception e) {
            // If the page body isn't available (e.g. an HTTP 403 challenge
            // page with no standard <body> content), don't let this check
            // fail - the markup check (ANTI_BOT_MARKUP) and title still work.
            return "";
        }
    }

    private static String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }
}
