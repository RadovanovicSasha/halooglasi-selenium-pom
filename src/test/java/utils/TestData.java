package utils;

public class TestData {

    // kredencijali za login (čitaju se iz environment variables)
    public static final String VALID_EMAIL =
            System.getenv().getOrDefault("HALO_EMAIL", "radovanovicsasha@yahoo.com");

    public static final String VALID_PASSWORD =
            System.getenv().getOrDefault("HALO_PASS", "skolskihalo");

    // termin za search test
    public static final String SEARCH_TERM = "stan";
}