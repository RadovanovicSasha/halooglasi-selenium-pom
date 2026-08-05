package framework.support;

/**
 * Thrown when execution is blocked by an external anti-bot challenge (e.g.
 * Cloudflare Managed Challenge, Turnstile, "Verify you are human") before
 * the application ever became testable.
 *
 * Deliberately a distinct type from TimeoutException/NoSuchElementException -
 * this is an infrastructure condition outside the framework's control, not a
 * broken locator or an ordinary timeout, and the test report should make
 * that distinction clear.
 */
public class AntiBotChallengeException extends RuntimeException {
    public AntiBotChallengeException(String message) {
        super(message);
    }
}
