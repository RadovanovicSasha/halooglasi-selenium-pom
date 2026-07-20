# Halo Oglasi Selenium POM

A UI test automation framework for [halooglasi.com](https://www.halooglasi.com/), a live production classifieds website, built with Java, Selenium WebDriver, and JUnit 5. It exercises real user journeys — browsing, search, authentication, and profile/account behavior — against the live site, and runs automatically in CI on every push and on a daily schedule.

---

## Notes

This framework automates a public third-party website (`halooglasi.com`).

Because the target website is protected by Cloudflare and other anti-bot mechanisms, GitHub-hosted CI runners may occasionally be blocked before the application becomes reachable.

In such cases, failed CI executions represent an external infrastructure limitation rather than a defect in the Selenium framework or the implemented test suite. Local execution remains the primary validation method.

## Project Overview

This project demonstrates a from-scratch Selenium automation framework following the **Page Object Model (POM)** pattern, with a strong emphasis on:

- Separation of concerns between test logic, page interactions, and test data
- Reusable, composable page objects and step helpers (no copy-pasted login/navigation code across tests)
- Deliberately non-destructive automation against a real, live website (see [Project Limitations](#project-limitations-live-website) below)
- Reliable CI execution (headless Chrome, CI-aware waits, failure diagnostics)

The suite covers 12 test cases (`TC001`–`TC012`) spanning smoke checks, regression coverage (including negative/authorization scenarios), and full end-to-end browsing flows.

---

## Technologies

- **Java 17**
- **Selenium WebDriver 4.23**
- **JUnit 5** (Jupiter) — test engine, tagging, and lifecycle extensions
- **Maven** — build and dependency management (`maven-surefire-plugin`)
- **SLF4J** — logging
- **GitHub Actions** — CI pipeline

---

## Architecture

The framework is split into three layers:

1. **Page Objects** (`src/main/java/pages`) — encapsulate locators and low-level interactions for a single page or UI region. Page objects expose *state and actions only*; they contain no assertions, keeping verification decisions in the test layer.
2. **Tests** (`src/test/java/tests`) — own all assertions and orchestrate page objects to express a scenario.
3. **Support utilities** (`config`, `testdata`, `utils`) — shared, cross-cutting concerns like environment configuration, credentials, and WebDriver creation.

Common behavior is centralized rather than duplicated:

- `BasePage` provides shared wait/interaction helpers (`isVisible`, `clickWhenClickable`, `jsClick`, `hover`) used by every page object.
- `BaseTest` provides the shared JUnit lifecycle (`@BeforeEach`/`@AfterEach`): driver creation, opening the site, and dismissing interstitials (cookie banner, security notification modal) before each test, and quitting the driver after.
- `LoginSteps` centralizes the repeated "open login page + submit valid credentials" flow used by multiple tests, without hiding assertions from the tests that use it.
- `HeaderComponent` models a UI fragment (the "logged in" indicator) shared across multiple pages (Home, Profile), avoiding duplicated locators.
- `ScreenshotOnFailureExtension`, registered once on `BaseTest`, automatically captures a screenshot when a test fails — while the driver session is still alive, before teardown runs.

### Package Structure

```text
src
├── main
│   └── java
│       ├── config
│       │   └── FrameworkConfig.java        # Base URL, CI detection
│       └── pages
│           ├── BasePage.java               # Shared wait/click/hover helpers
│           ├── HomePage.java
│           ├── LoginPage.java
│           ├── ProfilePage.java
│           ├── SearchPage.java             # Search input / action
│           ├── SearchResultsPage.java      # Search results listing
│           ├── AdDetailsPage.java          # Read-only ad detail view
│           ├── CookiesBannerPage.java
│           ├── SecurityNotificationModal.java
│           └── components
│               └── HeaderComponent.java    # Shared "logged in" header fragment
│
└── test
    ├── java
    │   ├── tests
    │   │   ├── BaseTest.java                    # Driver lifecycle, common setup/teardown
    │   │   ├── ScreenshotOnFailureExtension.java
    │   │   ├── LoginSteps.java                  # Reusable login step helper
    │   │   ├── smoke
    │   │   │   ├── OpenSiteTest.java             (TC001, smoke)
    │   │   │   ├── AcceptCookiesTest.java        (TC002, smoke)
    │   │   │   └── OpenLoginPageTest.java        (TC003, smoke)
    │   │   ├── regression
    │   │   │   ├── SuccessfulLoginTest.java              (TC004, regression)
    │   │   │   ├── ProfilePageTest.java                  (TC005, regression)
    │   │   │   ├── SearchTest.java                       (TC006, regression)
    │   │   │   ├── AdDetailsTest.java                    (TC008, regression)
    │   │   │   ├── FailedLoginTest.java                  (TC010, regression)
    │   │   │   ├── ProfileAccessRequiresLoginTest.java   (TC011, regression)
    │   │   │   └── SearchNoResultsTest.java              (TC012, regression)
    │   │   └── e2e
    │   │       ├── LogoutTest.java                       (TC007, e2e)
    │   │       └── BrowseListingEndToEndTest.java        (TC009, e2e)
    │   ├── testdata
    │   │   └── TestData.java               # Loads local credentials + shared test constants
    │   └── utils
    │       └── DriverFactory.java          # Chrome driver setup (headless in CI)
    └── resources
        ├── testdata-local.example.properties
        └── testdata-local.properties       # gitignored, created locally per-developer

pom.xml
README.md
```

The `tests.smoke` / `tests.regression` / `tests.e2e` sub-packages exist **purely for readability and logical grouping** - they make it obvious at a glance where a test case belongs. They have no effect on how tests are selected or executed: `BaseTest`, `LoginSteps`, and `ScreenshotOnFailureExtension` stay in the `tests` root package since they're shared across all three, and every test class is still discovered and filtered exclusively through its JUnit 5 `@Tag` annotation and Maven's `-Dgroups` flag (see [Maven Commands](#maven-commands) below). There are no suite classes in this project - grouping by tag, not by a runner class, is the only execution model in use.

---

## Page Object Model

Each page object extends `BasePage` and exposes only what a test needs to drive or verify a scenario — no assertions live in the page layer. For example, `SearchResultsPage` and `AdDetailsPage` expose state (`hasAtLeastOneResult()`, `isDetailsPageOpened()`, `isPriceVisible()`) and navigation actions (`openFirstResult()`), leaving every `assertTrue`/`assertEquals` call in the test class where it belongs.

---

## Reusable Components

- **`HeaderComponent`** — models the shared header login indicator, currently used by `LoginPage` to verify the logged-in header state after a login attempt.
- **`LoginSteps`** — a step helper (not a page object) that composes `LoginPage` actions for the "log in with valid credentials" flow reused by `SuccessfulLoginTest`, `AdDetailsTest`, and `BrowseListingEndToEndTest`.
- **`CookiesBannerPage`** and **`SecurityNotificationModal`** — handle the two interstitials that can block interaction with the site, both dismissed automatically in `BaseTest.setUp()`.

---

## Credential Handling

Login tests require a real Halo Oglasi account. Credentials are **never hardcoded or committed**:

- `TestData` loads `HALO_EMAIL` and `HALO_PASS` at runtime from `src/test/resources/testdata-local.properties`.
- That file is listed in `.gitignore` and is never pushed to the repository.
- `testdata-local.example.properties` is committed as a template showing the expected keys.
- If the local file is missing or a value is blank, `TestData` fails fast with a clear error message explaining how to fix it.
- The current GitHub Actions workflow does not yet provision this file from secrets (see [Project Limitations](#project-limitations-live-website)), so login-dependent tests are intended to be run locally for now.

### Local Setup

```bash
git clone https://github.com/RadovanovicSasha/halooglasi-selenium-pom.git
cd halooglasi-selenium-pom
cp src/test/resources/testdata-local.example.properties src/test/resources/testdata-local.properties
# then edit testdata-local.properties with your real HALO_EMAIL / HALO_PASS
```

Requirements: JDK 17, Maven, and Chrome installed locally. Selenium 4.23's built-in Selenium Manager resolves the matching ChromeDriver binary automatically — no manual driver setup is required.

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

### Current Test Distribution

| Tag | Test cases | Count |
|---|---|---|
| `smoke` | TC001 Open site, TC002 Accept cookies, TC003 Open login page | 3 |
| `regression` | TC004 Successful login, TC005 Profile page, TC006 Search, TC008 Ad details, TC010 Failed login, TC011 Profile access without login, TC012 Search with no results | 7 |
| `e2e` | TC007 Logout, TC009 Browse-to-details end-to-end flow | 2 |

Negative and boundary coverage was added deliberately, on top of the original happy-path suite:

- **TC010** – login with invalid, non-existent credentials is rejected (user stays logged out, login form remains visible). Uses a fake email/password rather than the real test account, so repeated runs can't trigger a lockout or extra bot scrutiny on the real credentials.
- **TC011** – requesting `/profil` without logging in first does not open the profile page. Unlike the other regression/e2e cases, this test needs no credentials at all, so it's the one test in this tag that can also run where `testdata-local.properties` isn't provisioned (e.g. CI today).
- **TC012** – searching for a term that cannot match any real listing returns no results, as the boundary counterpart to TC006.

---

## Screenshot on Failure

`ScreenshotOnFailureExtension` is registered once on `BaseTest` via `@ExtendWith`, so every test class gets it automatically. It uses JUnit 5's `AfterTestExecutionCallback` (rather than `TestWatcher`) specifically because it needs to run **before** `@AfterEach` tears down the driver — `TestWatcher` callbacks fire after teardown, by which point the browser session is already gone.

On failure, a timestamped PNG is written to `screenshots/` (gitignored) as `ClassName_methodName_yyyyMMdd-HHmmss.png`. Screenshot capture is best-effort: any failure while capturing is logged and swallowed so it never masks the original test failure.

---

## GitHub Actions

CI is defined in [`.github/workflows/selenium-tests.yml`](.github/workflows/selenium-tests.yml) and runs:

- On every push to `main`
- On a daily schedule (08:00 UTC / 09:00 CET)

The workflow checks out the repo, sets up JDK 17 (Temurin), and runs `mvn clean test` on `ubuntu-latest`, executing in headless Chrome (`DriverFactory` detects the `CI` environment variable and adds headless/sandbox flags automatically).

The workflow does not currently inject `testdata-local.properties` from secrets, so tests requiring login will fail in CI until that step is added — see the limitation noted below.

---

## Project Limitations (Live Website)

This suite runs against the real, live `halooglasi.com` production site rather than a mocked or staging environment. That constraint shapes several deliberate design decisions:

- **No destructive actions.** `AdDetailsPage` intentionally exposes no locators for contacting a seller, revealing a phone number, messaging, adding to favorites, or any purchase/payment flow — the browse-to-details flow (`TC009`) stops at viewing information.
- **No brittle content assertions.** Tests verify structural elements (e.g., that a price and category breadcrumb are visible) rather than specific text, since real inventory changes constantly.
- **Interstitials are handled defensively.** Cookie banners and security notification modals are dismissed if present, since their appearance can vary between runs and environments.
- **Real credentials, safely scoped.** Login tests require a real account; credentials are supplied per-environment (see [Credential Handling](#credential-handling)) and never committed.
- **CI credential provisioning is a known gap.** The GitHub Actions workflow currently runs `mvn clean test` without injecting `testdata-local.properties`, so login-dependent test cases (TC004–TC009, TC010, TC012 - i.e. most of the `regression` and `e2e` tags) fail in CI today. TC011 is the exception: it needs no login, so it's unaffected by this gap. Provisioning credentials via GitHub Actions secrets is listed under Future Improvements.
- **Cloudflare bot-verification can intercept any run.** The site is fronted by Cloudflare, which occasionally serves a "Verify you are human" challenge page to automated ChromeDriver sessions instead of the real page. When that happens, every test in the run fails identically (missing search box, missing login form, etc.) regardless of what the test itself checks - this is an anti-bot/environment condition external to the framework, not a test or page-object defect. It's not tied to a specific test case, so no single TC is called out for it here.

---

## Future Improvements

- Provision `testdata-local.properties` in CI from GitHub Actions secrets so login-dependent tests (TC004–TC009, TC010, TC012) run in the pipeline, not just locally
- Cross-browser execution (Firefox/Edge) via `DriverFactory`
- Parallel test execution
- Test reporting integration (e.g. Allure)

---

## Author

Aleksandar Radovanović

GitHub: https://github.com/RadovanovicSasha
