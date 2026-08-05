package framework.config;

/**
 * Shared, non-sensitive environment configuration consumed by both the page
 * layer and the test layer: the site's base URL and CI detection.
 *
 * Holds no credentials and no Selenium logic - configuration only.
 */
public class EnvConfig {

    // Single source of truth for the base URL, instead of duplicating the literal across pages.
    public static final String BASE_URL = "https://www.halooglasi.com/";

    private EnvConfig() {
    }

    /**
     * Detects whether tests are running in CI (GitHub Actions), so driver
     * creation and timeouts can adapt without duplicating this check per class.
     */
    public static boolean isCi() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }
}
