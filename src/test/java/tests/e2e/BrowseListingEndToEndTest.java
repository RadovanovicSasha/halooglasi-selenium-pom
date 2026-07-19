package tests.e2e;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.AdDetailsPage;
import pages.LoginPage;
import pages.SearchPage;
import pages.SearchResultsPage;
import testdata.TestData;
import tests.BaseTest;
import tests.LoginSteps;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * TC009 – Bezbedan end-to-end flow: pretraga → izbor oglasa → pregled detalja.
 *
 * Preuslov:
 * Korisnik je ulogovan u sistem.
 *
 * Koraci:
 * 1. Pretražiti oglase
 * 2. Izabrati prvi rezultat pretrage
 * 3. Otvoriti stranicu detalja oglasa
 *
 * Očekivano:
 * Ceo lanac koraka (pretraga -> izbor -> pregled) uspešno završava na
 * stranici detalja oglasa.
 *
 * Flow se NAMERNO zaustavlja na pregledu informacija - test ne kontaktira
 * oglašivača, ne šalje poruku, ne otkriva/klika broj telefona, ne dodaje
 * oglas u izabrane, ne kreira porudžbinu i ne vrši nikakvu kupovinu, uplatu
 * ili trajnu promenu naloga. AdDetailsPage namerno ne izlaže nijednu takvu
 * akciju (vidi AdDetailsPage - nema lokatora za te elemente).
 */
@Tag("e2e")
public class BrowseListingEndToEndTest extends BaseTest {

    @Test
    public void TC009_searchSelectViewDetails_nonDestructiveBrowseFlow() {

        // Prijavljujem se validnim kredencijalima
        LoginSteps loginSteps = new LoginSteps(driver);
        LoginPage loginPage = loginSteps.openLoginPage();
        loginSteps.submitValidCredentials(loginPage);

        // Pretražujem oglase
        SearchPage searchPage = new SearchPage(driver);
        searchPage.open();
        searchPage.search(TestData.SEARCH_TERM);

        SearchResultsPage searchResultsPage = new SearchResultsPage(driver);
        assertTrue(searchResultsPage.hasAtLeastOneResult(),
                "Pretraga nije vratila nijedan rezultat.");

        // Biram oglas i otvaram njegovu stranicu detalja - flow se ovde zaustavlja
        AdDetailsPage adDetailsPage = searchResultsPage.openFirstResult();

        assertTrue(adDetailsPage.isDetailsPageOpened(),
                "Flow pretraga->izbor->pregled detalja nije uspeo - stranica detalja oglasa nije otvorena.");
    }
}
