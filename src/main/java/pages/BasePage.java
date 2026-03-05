package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;

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
    // Osnovni WebDriverWait koji koristim za čekanje elemenata
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        // Prosleđujem driver iz test klase i inicijalizujem wait
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Proveravam da li je element vidljiv na stranici u zadatom vremenskom periodu.
     *
     * Ovu metodu koristim u validacijama da proverim da li se stranica ili
     * određeni element uspešno učitao.
     */
    protected boolean isVisible(By locator, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
        // Ako element nije postao vidljiv u zadatom vremenu vraćam false
            return false;
        }
    }

    /**
     * Čekam da element postane klikabilan pa tek onda izvršavam klik.
     *
     * Ovo koristim da izbegnem probleme kada se elementi još učitavaju
     * ili nisu spremni za interakciju.
     */
    protected void clickWhenClickable(By locator, int seconds) {
        WebElement el = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
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