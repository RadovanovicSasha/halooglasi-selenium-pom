package config;

/**
 * FrameworkConfig sadrži deljene, nesenzitivne vrednosti okruženja koje
 * koriste i Page klase i testovi: osnovni URL sajta i CI detekcija.
 *
 * Ovde ne stoje kredencijali niti bilo kakva Selenium logika - samo
 * konfiguracija okruženja. Kredencijali ostaju u utils.TestData
 * (src/test/resources/testdata-local.properties), jer su test-scoped i
 * osetljivi, za razliku od ovih vrednosti.
 */
public class FrameworkConfig {

    // Osnovni URL sajta - jedini izvor istine umesto dupliranih literala
    public static final String BASE_URL = "https://www.halooglasi.com/";

    private FrameworkConfig() {
    }

    /**
     * Proveravam da li se testovi izvršavaju u CI okruženju (GitHub Actions).
     * Ranije je ova provera bila duplirana u DriverFactory i HomePage.
     */
    public static boolean isCi() {
        return "true".equalsIgnoreCase(System.getenv("CI"));
    }
}
