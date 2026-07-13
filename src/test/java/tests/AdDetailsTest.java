package tests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.AdDetailsPage;
import pages.LoginPage;
import pages.SearchPage;
import pages.SearchResultsPage;
import testdata.TestData;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC008 – Pregled detalja oglasa.
 *
 * Preuslov:
 * Korisnik je ulogovan u sistem.
 *
 * Koraci:
 * 1. Pretražiti oglase
 * 2. Izabrati prvi rezultat pretrage
 * 3. Proveriti da je stranica detalja oglasa otvorena i da su strukturni
 *    elementi (cena, kategorija) vidljivi
 *
 * Očekivano:
 * Stranica detalja oglasa je otvorena, cena i kategorija su vidljivi.
 * Test se zaustavlja na pregledu - bez kontakta, poruke, favorita ili kupovine.
 *
 * Ne asertujem konkretan sadržaj (naslov/cenu/oglašivača) jer se inventar
 * na sajtu stalno menja - proveravam samo da su strukturni elementi vidljivi.
 */
@Tag("regression")
public class AdDetailsTest extends BaseTest {

    @Test
    public void TC008_viewAdvertisementDetails_showsStructuralInfo() {

        // Prijavljujem se validnim kredencijalima
        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();
        loginSteps.submitValidCredentials(loginPage);

        // Pretražujem oglase
        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();
        searchPage.search(TestData.SEARCH_TERM);

        // Proveravam da pretraga ima bar jedan rezultat pre izbora
        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.hasAtLeastOneResult(),
                "Pretraga nije vratila nijedan rezultat za selekciju.");

        // Biram prvi rezultat i otvaram stranicu detalja oglasa
        AdDetailsPage adDetailsPage = searchResultsPage.openFirstResult();

        // Proveravam strukturne elemente stranice detalja oglasa
        assertTrue(adDetailsPage.isDetailsPageOpened(),
                "Stranica sa detaljima oglasa nije otvorena.");
        assertTrue(adDetailsPage.isPriceVisible(),
                "Cena nije vidljiva na stranici oglasa.");
        assertTrue(adDetailsPage.isCategoryBreadcrumbVisible(),
                "Kategorija (breadcrumb) nije vidljiva na stranici oglasa.");
    }
}
