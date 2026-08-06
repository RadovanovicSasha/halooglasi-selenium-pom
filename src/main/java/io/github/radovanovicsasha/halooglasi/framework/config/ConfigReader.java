package io.github.radovanovicsasha.halooglasi.framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads the properties file for the active environment
 * (config-{env}.properties), selected via the `env` system property
 * (-Denv=..., defaulting to "prod"). Holds only non-sensitive settings -
 * credentials live in EnvConfig, sourced from the environment instead.
 */
public final class ConfigReader {

    private static final String DEFAULT_ENV = "prod";
    private static final Properties PROPERTIES = load(activeEnv());

    private ConfigReader() {
    }

    public static String activeEnv() {
        return System.getProperty("env", DEFAULT_ENV);
    }

    public static String get(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            throw new IllegalStateException(
                    "Missing property '" + key + "' in config-" + activeEnv() + ".properties");
        }
        return value;
    }

    private static Properties load(String env) {
        String fileName = "config-" + env + ".properties";
        Properties properties = new Properties();
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Missing " + fileName + " on the classpath. Add src/test/resources/" + fileName
                                + ", or select an existing environment via -Denv=<name> (e.g. -Denv=prod).");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read " + fileName, e);
        }
        return properties;
    }
}
