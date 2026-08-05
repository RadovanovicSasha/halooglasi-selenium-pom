package framework.config;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * Typed, application-facing configuration: non-sensitive settings from
 * ConfigReader (config-{env}.properties) plus credentials sourced from the
 * environment.
 *
 * Credentials are read from TEST_USERNAME/TEST_PASSWORD environment
 * variables first - how CI injects them from GitHub Secrets - falling back
 * to a local .env file at the repo root (see .env.example) so the same
 * code path works unchanged locally and in CI.
 */
public final class EnvConfig {

    private static final Dotenv DOTENV = Dotenv.configure().ignoreIfMissing().load();

    private EnvConfig() {
    }

    public static String getBaseUrl() {
        return ConfigReader.get("baseUrl");
    }

    /**
     * The -Dbrowser system property takes precedence over the active
     * environment's config file, so a run can switch browsers without
     * switching environments.
     */
    public static String getBrowser() {
        String override = System.getProperty("browser");
        return override != null ? override : ConfigReader.get("browser");
    }

    public static int getTimeoutSeconds() {
        return Integer.parseInt(ConfigReader.get("timeout"));
    }

    public static String getUsername() {
        return requireCredential("TEST_USERNAME");
    }

    public static String getPassword() {
        return requireCredential("TEST_PASSWORD");
    }

    /**
     * Detects whether tests are running in CI (GitHub Actions), so driver
     * creation and timeouts can adapt without duplicating this check per class.
     */
    public static boolean isCi() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }

    private static String requireCredential(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            value = DOTENV.get(key);
        }
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing " + key + ". Set it as an environment variable, or copy .env.example to .env "
                            + "at the repo root and fill in a real value.");
        }
        return value;
    }
}
