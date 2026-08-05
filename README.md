# Halo Oglasi Selenium Test Automation Framework

![Java](https://img.shields.io/badge/Java-17-blue)
![Selenium](https://img.shields.io/badge/Selenium-4.23-43B02A)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36)
![Allure](https://img.shields.io/badge/Allure-Report-FF6E00)
[![CI](https://github.com/RadovanovicSasha/halooglasi-selenium-pom/actions/workflows/ci.yml/badge.svg)](https://github.com/RadovanovicSasha/halooglasi-selenium-pom/actions/workflows/ci.yml)

A layered UI test automation framework for [halooglasi.com](https://www.halooglasi.com/), a live production classifieds website, built with Java, Selenium WebDriver, and JUnit 5. It exercises real user journeys - browsing, search, authentication, and profile/account behavior - against the live site, with multi-environment configuration, cross-browser execution, Allure reporting, and a CI pipeline scoped to protect the live test account from repeated automated logins.

---

## Project Overview

This framework follows the **Page Object Model (POM)** pattern inside a strict, one-direction layered architecture:

```
tests -> pages -> driver / config -> utils
```

Each layer only depends on the layers below it - `utils` has no knowledge of `pages` or `support`, `pages` never reaches back into `tests`, and test classes are the only place assertions live. That separation, combined with thread-safe driver management, environment-based configuration, and deliberately non-destructive automation against a real, live website (see [Testing Safety Constraints](#testing-safety-constraints)), is what makes this a framework rather than a loose collection of scripts.

The suite covers 12 test cases (`TC001`-`TC012`) spanning smoke checks, regression coverage (including negative/authorization scenarios), and full end-to-end browsing flows.

---

## Technologies

- **Java 17**
- **Selenium WebDriver 4.23** - Chrome and Firefox, driver binaries resolved automatically via WebDriverManager
- **JUnit 5** (Jupiter) - test engine, tagging, and lifecycle extensions
- **Maven** - build and dependency management
- **Allure** - test reporting (`allure-jupiter` + `allure-maven`)
- **dotenv-java** - `.env`-based local credential loading
- **SLF4J** - logging
- **GitHub Actions** - CI pipeline

---

## Architecture

```text
src
├── main
│   └── java
│       └── framework
│           ├── config
│           │   ├── ConfigReader.java       # Loads config-{env}.properties for the active -Denv
│           │   └── EnvConfig.java          # Typed config: baseUrl, browser, timeout, credentials, CI detection
│           ├── driver
│           │   ├── DriverManager.java      # ThreadLocal<WebDriver> - one driver per thread
│           │   └── DriverFactory.java      # Chrome/Firefox creation via WebDriverManager
│           ├── pages
│           │   ├── BasePage.java           # Shared wait/click/hover helpers
│           │   ├── HomePage.java, LoginPage.java, ProfilePage.java
│           │   ├── SearchPage.java, SearchResultsPage.java, AdDetailsPage.java
│           │   ├── CookiesBannerPage.java, SecurityNotificationModal.java
│           │   └── components
│           │       └── HeaderComponent.java
│           ├── support
│           │   ├── ObstacleHandler.java        # Dismisses known obstacles, detects anti-bot challenges
│           │   └── AntiBotChallengeException.java
│           └── utils
│               ├── WaitUtils.java          # Generic WebDriverWait helpers (no page/support knowledge)
│               └── ScreenshotUtils.java    # Screenshot capture
│
└── test
    ├── java
    │   └── tests
    │       ├── base
    │       │   ├── BaseTest.java                    # Driver lifecycle via DriverManager/DriverFactory
    │       │   ├── ScreenshotOnFailureExtension.java
    │       │   └── LoginSteps.java                  # Reusable login step helper
    │       ├── testdata
    │       │   └── TestData.java                    # Search term + credentials (delegates to EnvConfig)
    │       ├── smoke        (OpenSiteTest, AcceptCookiesTest, OpenLoginPageTest)
    │       ├── regression   (SuccessfulLoginTest, ProfilePageTest, SearchTest, AdDetailsTest,
    │       │                 FailedLoginTest, ProfileAccessRequiresLoginTest, SearchNoResultsTest)
    │       └── e2e          (LogoutTest, BrowseListingEndToEndTest)
    └── resources
        ├── junit-platform.properties    # Parallel execution + Allure extension autodetection
        ├── allure.properties            # Allure results directory
        ├── config-prod.properties       # Real halooglasi.com settings
        └── config-staging.properties    # Placeholder values demonstrating multi-env support

.env.example    # Committed credential template
.env            # Local-only, gitignored
pom.xml
README.md
```

Common behavior is centralized rather than duplicated:

- `BasePage` provides shared wait/interaction helpers (`isVisible`, `clickWhenClickable`, `jsClick`, `hover`), delegating the actual `WebDriverWait` calls to `framework.utils.WaitUtils` and obstacle/anti-bot handling to `framework.support.ObstacleHandler`.
- `ObstacleHandler` centralizes handling of known, removable UI obstacles (cookie banner, security notification modal) and explicit detection of external anti-bot challenges (Cloudflare/Turnstile). On a timeout, known obstacles are dismissed and the original wait/click is retried exactly once; a detected anti-bot challenge instead fails immediately with a distinct `AntiBotChallengeException`, so a Cloudflare block is never silently reported as an ordinary assertion/timeout failure. See [CI Scope & Known Limitations](#ci-scope--known-limitations).
- `DriverManager` holds the active `WebDriver` per thread (`ThreadLocal`), so `BaseTest` never shares or races on a driver reference across concurrently-running tests.
- `BaseTest` provides the shared JUnit lifecycle (`@BeforeEach`/`@AfterEach`): driver creation via `DriverFactory`/`DriverManager`, opening the configured environment's base URL, dismissing known interstitials and failing fast on an anti-bot challenge before each test, and quitting the driver after.
- `LoginSteps` centralizes the repeated "open login page + submit valid credentials" flow used by multiple tests, without hiding assertions from the tests that use it.
- `HeaderComponent` models a UI fragment (the "logged in" indicator) shared across multiple pages (Home, Profile), avoiding duplicated locators.
- `ScreenshotOnFailureExtension`, registered once on `BaseTest`, automatically captures a screenshot when a test fails, delegating the actual capture to `framework.utils.ScreenshotUtils`.

The `tests.smoke` / `tests.regression` / `tests.e2e` sub-packages exist purely for readability and logical grouping. Test selection is driven entirely by JUnit 5 `@Tag` and Maven's `-Dgroups` flag (see [Maven Commands](#maven-commands)) - there are no suite classes.

---

## Page Object Model

Each page object extends `BasePage` and exposes only what a test needs to drive or verify a scenario - no assertions live in the page layer. For example, `SearchResultsPage` and `AdDetailsPage` expose state (`hasAtLeastOneResult()`, `isDetailsPageOpened()`, `isPriceVisible()`) and navigation actions (`openFirstResult()`), leaving every `assertTrue`/`assertEquals` call in the test class where it belongs.

---

## Environment & Configuration

Non-sensitive configuration is environment-scoped and selected via the `-Denv` system property (defaults to `prod`):

| Property file | Purpose |
|---|---|
| `config-prod.properties` | Real settings for `halooglasi.com` (`baseUrl`, `browser`, `timeout`) |
| `config-staging.properties` | Placeholder values demonstrating the framework scales to multiple environments - this project targets the public production site only, so there is no real staging deployment behind these values |

```bash
mvn test -Denv=prod       # default
mvn test -Denv=staging    # loads config-staging.properties
```

`framework.config.ConfigReader` loads `config-{env}.properties` from the classpath; `framework.config.EnvConfig` is the typed, application-facing wrapper (`getBaseUrl()`, `getBrowser()`, `getTimeoutSeconds()`, `getUsername()`, `getPassword()`, `isCi()`) that the rest of the framework depends on.

---

## Credential Handling

Login-dependent tests require a real Halo Oglasi account. Credentials are **never hardcoded or committed**:

- `EnvConfig` reads `TEST_USERNAME`/`TEST_PASSWORD` from environment variables first - how CI provides them via GitHub Secrets - falling back to a local `.env` file at the repo root (loaded via `dotenv-java`). The same code path works unchanged locally and in CI.
- `.env.example` is committed with placeholder values, showing the expected keys.
- `.env` is gitignored and never pushed to the repository.
- `tests.testdata.TestData` exposes `haloEmail`/`haloPass` as a thin pass-through to `EnvConfig`, so test classes keep a stable, simple call site.

### Local Setup

```bash
git clone https://github.com/RadovanovicSasha/halooglasi-selenium-pom.git
cd halooglasi-selenium-pom
cp .env.example .env
# then edit .env with your real TEST_USERNAME / TEST_PASSWORD
```

Requirements: JDK 17, Maven, and Chrome and/or Firefox installed locally. Driver binaries are resolved automatically via WebDriverManager - no manual driver setup is required.

---

## Cross-Browser Execution

`DriverFactory.createDriver(browser)` supports Chrome and Firefox, both resolved through WebDriverManager. Browser selection follows `-Dbrowser`, falling back to the active environment's `browser` property:

```bash
mvn test -Dgroups=smoke -Dbrowser=chrome    # default
mvn test -Dgroups=smoke -Dbrowser=firefox
```

---

## Maven Commands

Run the full suite:

```bash
mvn test
```

Run a single tag (tags are assigned per test class with JUnit 5's `@Tag`):

```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=regression
mvn test -Dgroups=e2e
```

Run everything **except** tests that authenticate against the live account (what CI runs automatically on push):

```bash
mvn test "-Dgroups=smoke | (regression & !login)"
```

Run only the tests that log in with the real test account (local/manual use - see [CI Scope & Known Limitations](#ci-scope--known-limitations)):

```bash
mvn test -Dgroups=login
```

`-Denv`, `-Dbrowser`, and `-Dgroups` are independent and composable, e.g.:

```bash
mvn test -Dgroups=login -Dbrowser=firefox -Denv=prod
```

### Current Test Distribution

| Tag | Test cases | Count |
|---|---|---|
| `smoke` | TC001 Open site, TC002 Accept cookies, TC003 Open login page | 3 |
| `regression` | TC004 Successful login, TC005 Profile page, TC006 Search, TC008 Ad details, TC010 Failed login, TC011 Profile access without login, TC012 Search with no results | 7 |
| `e2e` | TC007 Logout, TC009 Browse-to-details end-to-end flow | 2 |
| `login` (orthogonal to the above) | TC004, TC005, TC006, TC008, TC010, TC007, TC009 - every test that authenticates against the live account | 7 |

`TC010` (`FailedLoginTest`) is `login`-tagged despite using fabricated credentials, not the real account - it still submits to the live login endpoint, which is the behavior being scoped out of routine CI. `TC011` (`ProfileAccessRequiresLoginTest`) is the only regression case that is never `login`-tagged: it deliberately opens `/profil` without authenticating, to verify the application itself rejects unauthenticated access.

Negative and boundary coverage was added deliberately, on top of the original happy-path suite:

- **TC010** - login with invalid, non-existent credentials is rejected (user stays logged out, login form remains visible). Uses a fake email/password rather than the real test account, so repeated runs can't trigger a lockout or extra bot scrutiny on the real credentials.
- **TC011** - requesting `/profil` without logging in first does not open the profile page.
- **TC012** - searching for a term that cannot match any real listing returns no results, as the boundary counterpart to TC006.

---

## Parallel Execution

Test execution is single-threaded by default (`mvn test`), which keeps runs deterministic against the shared live test account used by `login`-tagged tests. Parallel execution is available as an opt-in switch, not a separate profile or a `pom.xml` edit:

```bash
mvn test -Dgroups=smoke -Djunit.jupiter.execution.parallel.enabled=true
```

This is controlled by `src/test/resources/junit-platform.properties`, which ships with `junit.jupiter.execution.parallel.enabled=false`. JUnit 5 resolves configuration parameters from system properties before the properties file, so the `-D` flag above overrides the default for that run only. A fixed pool of 2 threads (`config.strategy=fixed`) is used rather than one thread per CPU core, to keep the number of concurrent browser sessions against the live site predictable both locally and in CI.

Thread safety comes from `framework.driver.DriverManager`, a `ThreadLocal<WebDriver>` - each parallel JUnit worker thread gets and quits its own driver instance, with no shared mutable state to race on. `ConfigReader`, `EnvConfig`, and `DriverFactory` only expose immutable/stateless behavior, so they're safe to read concurrently without synchronization.

**Where care is still needed:** `login`-tagged tests authenticate against the same real Halo Oglasi account (`EnvConfig.getUsername()/getPassword()`). Running those tests in parallel means multiple browser sessions authenticate concurrently against one live account, which can surface as intermittent login/session flakiness on the site itself - not a defect in the framework's thread-safety. The `smoke` tag (TC001-TC003) performs no real login and is the safest candidate for parallel execution today.

CI (`.github/workflows/ci.yml`) runs sequentially - the default - and, on push, excludes `login`-tagged tests entirely (see below), so this consideration currently applies to local/manual runs.

---

## Allure Reporting

Test results are captured via `allure-jupiter` (JUnit 5 extension autodetection, enabled in `junit-platform.properties`) and written to `target/allure-results`.

Generate and view the HTML report locally:

```bash
mvn test
mvn allure:report   # writes target/site/allure-maven-plugin
mvn allure:serve     # builds and opens the report directly
```

Every test class carries `@Severity`/`@Feature`/`@Story` annotations matching its existing `@Tag`, so the report groups tests by feature/story rather than just by class name. In CI, the report is generated and uploaded as a build artifact on every run (see below).

### Sample Report

A sample Allure report/screenshots from a successful run of the `login`-tagged suite will be added here (`docs/sample-report/`) as evidence the authenticated flows work end-to-end, without requiring continuous re-execution against the live account. *(Pending - see [CI Scope & Known Limitations](#ci-scope--known-limitations).)*

---

## Testing Safety Constraints

No test - including `regression` and `e2e` flows - performs a real purchase, order, payment, or any irreversible transactional action against `halooglasi.com`:

- `AdDetailsPage` intentionally exposes no locators for contacting a seller, revealing a phone number, messaging, adding to favorites, or any purchase/payment flow.
- `BrowseListingEndToEndTest` (TC009) deliberately stops at viewing ad details - the flow never proceeds toward a contact or transactional action.
- Where a flow would naturally lead toward such an action, the test stops one step before the final action and asserts only that the relevant element is present and functional.

---

## Screenshot on Failure

`ScreenshotOnFailureExtension` is registered once on `BaseTest` via `@ExtendWith`, so every test class gets it automatically. It uses JUnit 5's `AfterTestExecutionCallback` (rather than `TestWatcher`) specifically because it needs to run **before** `@AfterEach` tears down the driver - `TestWatcher` callbacks fire after teardown, by which point the browser session is already gone.

On failure, a timestamped PNG is written to `screenshots/` (gitignored) as `ClassName_methodName_yyyyMMdd-HHmmss.png`. Screenshot capture is best-effort: any failure while capturing is logged and swallowed so it never masks the original test failure.

---

## GitHub Actions (CI)

CI is defined in [`.github/workflows/ci.yml`](.github/workflows/ci.yml):

- **On push to `main`**: runs `smoke | (regression & !login)` - every test that never authenticates against the live account. No scheduled/cron trigger.
- **On manual dispatch** (`workflow_dispatch`): runs whatever tag expression is given via the `groups` input, defaulting to `login` - for deliberately exercising the authenticated flows on demand.
- `TEST_USERNAME`/`TEST_PASSWORD` are injected as environment variables from GitHub Secrets in both cases, so the credential path is identical to local execution.
- After the test step (`if: always()`), the Allure report is generated and uploaded as a build artifact, whether the run passed or failed.

Repository secrets (`TEST_USERNAME`, `TEST_PASSWORD`) must be configured in the GitHub repo settings for the login-dependent path to pass in CI - this is a one-time manual setup step, separate from this codebase.

---

## CI Scope & Known Limitations

`halooglasi.com` is a real, protected, production application - not a mock or staging target. That shapes several deliberate design decisions:

- **Login-tagged tests are excluded from automatic CI runs.** The site's anti-bot protection (Cloudflare) can unpredictably block automated browsers, and repeated automated login attempts risk the shared test account being flagged or restricted. Rather than accept flaky, non-informative CI failures on every push, `login`-tagged tests run on demand only - via `workflow_dispatch` or locally - not on the automatic push trigger. This is a deliberate engineering decision to keep routine CI signal reliable, not a gap in the test coverage itself (all 12 test cases exist and pass when run manually).
- **The anti-bot block observed during development was not login-specific.** While building this pipeline, Cloudflare blocked every automated run attempted from the development environment - including simple, unauthenticated smoke tests like opening the home page - not just login flows. That pattern points to IP-reputation-based blocking of that specific environment's egress address, rather than a reaction to the login endpoint itself. `ObstacleHandler` detects this condition explicitly and fails fast with `AntiBotChallengeException` (see [Architecture](#architecture)) instead of reporting it as an ambiguous element-not-found failure.
- **GitHub Actions runners may behave differently.** GitHub-hosted runners use a different IP range and reputation profile than the environment above, so whether the same block reproduces there is not yet established either way - it should be observed from the first real CI run rather than assumed. If CI runs are also intercepted, that's the same documented infrastructure condition, not a defect in the framework or the application under test.
- **The framework never attempts to bypass anti-bot protection.** `ObstacleHandler` only dismisses confirmed, benign UI elements (cookie banner, security notification modal); on detecting an actual anti-bot challenge, it fails immediately and clearly rather than attempting to click through, solve, or otherwise defeat it.
- **No brittle content assertions.** Tests verify structural elements (e.g., that a price and category breadcrumb are visible) rather than specific text, since real inventory changes constantly.
- **See also** [Testing Safety Constraints](#testing-safety-constraints) for the non-destructive automation guarantees, and [Sample Report](#sample-report) for evidence that the login-dependent flows pass on a successful run.

---

## Future Improvements

- A dedicated test account per parallel worker, to remove the shared-account caveat noted in [Parallel Execution](#parallel-execution) and allow `login`-tagged tests to run concurrently
- Containerized execution (Docker) for fully reproducible local/CI environments

---

## Author

Aleksandar Radovanović

GitHub: https://github.com/RadovanovicSasha
