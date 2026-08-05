package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import support.ObstacleHandler;

import java.time.Duration;

/**
 * BasePage je osnovna klasa za sve stranice u projektu.
 * Ovde sam smestio zajedničke funkcije koje koristim na svim Page klasama
 * (wait za elemente, klik kada je element klikabilan i JS klik kao fallback).
 * Cilj je da izbegnem dupliranje koda i da svi testovi koriste iste pomoćne metode.
 */

public class BasePage {

    // WebDriver instanca koju nasleđuju sve Page klase
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        // Prosleđujem driver iz test klase
        this.driver = driver;
    }

    /**
     * Proveravam da li je element vidljiv na stranici u zadatom vremenskom periodu.
     *
     * Ovu metodu koristim u validacijama da proverim da li se stranica ili
     * određeni element uspešno učitao.
     *
     * Na prvi timeout ne vraćam odmah false - to bi običan timeout, pokvaren
     * lokator i "stranicu je presrela Cloudflare zaštita" učinilo
     * nerazlučivim (sva tri izgledaju identično: element se nije pojavio).
     * Zato prvo proveravam da nije u pitanju anti-bot zaštita (u kom slučaju
     * bacam jasnu grešku umesto tihog false), zatim uklanjam poznate
     * prepreke i proveru ponavljam tačno jednom. Ako i tada element nije
     * vidljiv, vraćam false kao i pre - to je normalan ishod (pokvaren
     * lokator ili stvarno spor/odsutan element), bez izmene ponašanja.
     */
    protected boolean isVisible(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            ObstacleHandler.failFastIfAntiBotChallenge(driver);
            ObstacleHandler.dismissKnownObstacles(driver);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(seconds))
                        .until(ExpectedConditions.visibilityOfElementLocated(locator));
                return true;
            } catch (TimeoutException retryTimeout) {
                return false;
            }
        }
    }

    /**
     * Čekam da element postane klikabilan pa tek onda izvršavam klik.
     *
     * Ovo koristim da izbegnem probleme kada se elementi još učitavaju
     * ili nisu spremni za interakciju.
     *
     * Ako prvi pokušaj padne na timeout-u, ne odustajem odmah - moguće je da
     * je u pitanju poznata uklonjiva prepreka (npr. obaveštenje koje se
     * pojavilo posle početnog dismissal-a u BaseTest). ObstacleHandler prvo
     * proveri da nije u pitanju anti-bot zaštita (u kom slučaju ne pokušava
     * ništa dalje), zatim ukloni poznate prepreke i ponovi klik tačno
     * jednom. Ako i taj pokušaj padne, originalni TimeoutException probija
     * normalno - ne razlikuje se od pokvarenog lokatora ili običnog sporog
     * učitavanja.
     */
    protected void clickWhenClickable(By locator, int seconds) {
        try {
            WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
        } catch (TimeoutException e) {
            ObstacleHandler.retryAfterDismissingObstacles(driver, () -> {
                WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                        .until(ExpectedConditions.elementToBeClickable(locator));
                el.click();
            });
        }
    }

    /**
     * Isto kao clickWhenClickable(By, int), ali za slučaj kada je element već
     * pronađen unutar određenog konteksta (npr. konkretna kartica rezultata)
     * i ne želim ponovo da ga tražim po celoj stranici.
     */
    protected void clickWhenClickable(WebElement element, int seconds) {
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(element));
        el.click();
    }

    /**
     * JS klik koristim kao fallback u situacijama kada standardni Selenium klik
     * ne uspe (npr. overlay elementi ili dinamički UI).
     */
    protected void jsClick(By locator, int seconds) {
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
    }
    // Hover metoda:
    protected void hover(By locator, int seconds) {
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
        new Actions(driver).moveToElement(el).perform();
    }
}