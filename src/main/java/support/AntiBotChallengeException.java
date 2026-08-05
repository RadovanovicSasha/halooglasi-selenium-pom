package support;

/**
 * Baca se kada je izvršavanje blokirano spoljnom anti-bot zaštitom (npr.
 * Cloudflare Managed Challenge, Turnstile, "Verify you are human") pre nego
 * što je aplikacija uopšte postala testabilna.
 *
 * Namerno je odvojen tip od TimeoutException/NoSuchElementException - ovo
 * je infrastrukturni uslov van naše kontrole, a ne pokvaren lokator niti
 * običan timeout, i test izveštaj treba to jasno da razlikuje.
 */
public class AntiBotChallengeException extends RuntimeException {
    public AntiBotChallengeException(String message) {
        super(message);
    }
}
