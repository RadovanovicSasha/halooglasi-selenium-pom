package support;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pages.CookiesBannerPage;
import pages.SecurityNotificationModal;

import java.util.List;

/**
 * Centralizuje rešavanje poznatih, uklonjivih UI prepreka (cookie banner,
 * bezbednosno obaveštenje) i eksplicitnu detekciju spoljne anti-bot zaštite
 * (Cloudflare, Turnstile).
 *
 * Namerno pokriva SAMO potvrđene, uklonjive elemente sa Halo Oglasi sajta -
 * nema AI-ja, nema keša, nema slojeva heuristika. Ako prepreka nije jedna
 * od poznatih (cookie banner, bezbednosno obaveštenje), handler je ne dira.
 *
 * Tok za akciju koja padne zbog moguće prepreke:
 * 1. originalna akcija se prvo pokuša direktno (poziva se izvan ove klase)
 * 2. na pad: proveri da li je u pitanju spoljna anti-bot zaštita - ako jeste,
 *    NE pokušavaj da je zaobiđeš, baci AntiBotChallengeException odmah
 * 3. u suprotnom, ukloni poznate prepreke ako su prisutne
 * 4. ponovi originalnu akciju tačno jednom
 * 5. ako i dalje ne uspe, neka izuzetak iz retry-ja normalno probije (obična
 *    Selenium greška - pokvaren lokator ili običan timeout, bez izmene)
 */
public final class ObstacleHandler {

    private static final Logger log = LoggerFactory.getLogger(ObstacleHandler.class);

    // Poznata Cloudflare challenge markup - iframe sa Turnstile/challenge
    // izazovom ili wrapper koji Cloudflare ubacuje umesto prave stranice.
    private static final By ANTI_BOT_MARKUP = By.cssSelector(
            "#challenge-running, #cf-wrapper, iframe[src*='challenges.cloudflare.com'], iframe[title*='hallenge']");

    // Tekstualni markeri koje Cloudflare (i slične zaštite) ispisuju na
    // interstitial stranici, umesto stvarnog sadržaja sajta.
    private static final List<String> ANTI_BOT_TEXT_MARKERS = List.of(
            "verify you are human",
            "performing security verification",
            "checking your browser",
            "attention required",
            "just a moment");

    private ObstacleHandler() {
    }

    /**
     * Baca AntiBotChallengeException ako stranica trenutno prikazuje poznatu
     * anti-bot proveru umesto aplikacije. Ne klikće, ne pokušava da je reši -
     * samo prijavljuje da je sajt nedostupan iz spoljnog razloga.
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
     * Uklanja poznate prepreke ako su trenutno prisutne (cookie banner,
     * bezbednosno obaveštenje). Ne radi ništa ako nijedna nije prisutna.
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
     * Poziva se kada je originalna akcija (npr. klik) već pukla. Prvo
     * proverava da nije u pitanju anti-bot zaštita (u kom slučaju odmah
     * odustaje umesto da pokuša bilo šta dalje), zatim uklanja poznate
     * prepreke i ponavlja originalnu akciju tačno jednom. Ako i taj pokušaj
     * padne, izuzetak se ne hvata ovde - probija normalno ka pozivaocu.
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
            // Ako telo stranice nije dostupno (npr. HTTP 403 challenge page
            // bez standardnog <body> sadržaja), ne dozvoli da provera padne -
            // markup provera (ANTI_BOT_MARKUP) i title i dalje rade.
            return "";
        }
    }

    private static String safeLower(String value) {
        return value == null ? "" : value.toLowerCase();
    }
}
