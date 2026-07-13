package testdata;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestData {

    private static final String LOCAL_CONFIG_FILE = "testdata-local.properties";

    // termin za search test
    public static final String SEARCH_TERM = "stan";

    // kredencijali za login (čitaju se iz src/test/resources/testdata-local.properties)
    public static final String haloEmail;
    public static final String haloPass;

    static {
        Properties localProperties = loadLocalProperties();
        haloEmail = requireNonBlank(localProperties, "HALO_EMAIL");
        haloPass = requireNonBlank(localProperties, "HALO_PASS");
    }

    private TestData() {
    }

    private static Properties loadLocalProperties() {
        Properties properties = new Properties();
        try (InputStream input = TestData.class.getClassLoader().getResourceAsStream(LOCAL_CONFIG_FILE)) {
            if (input == null) {
                throw new IllegalStateException(
                        "Missing src/test/resources/" + LOCAL_CONFIG_FILE + ". Copy "
                                + "src/test/resources/testdata-local.example.properties to "
                                + "src/test/resources/" + LOCAL_CONFIG_FILE
                                + " and fill in HALO_EMAIL and HALO_PASS with your local credentials.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read src/test/resources/" + LOCAL_CONFIG_FILE, e);
        }
        return properties;
    }

    private static String requireNonBlank(Properties properties, String key) {
        String value = properties.getProperty(key, "").trim();
        if (value.isEmpty()) {
            throw new IllegalStateException(
                    "Missing value for " + key + " in src/test/resources/" + LOCAL_CONFIG_FILE
                            + ". Open that file and enter your real Halo Oglasi " + key + ".");
        }
        return value;
    }
}
