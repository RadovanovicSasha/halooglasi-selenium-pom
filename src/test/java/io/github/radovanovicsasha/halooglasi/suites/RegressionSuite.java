package io.github.radovanovicsasha.halooglasi.suites;

import io.github.radovanovicsasha.halooglasi.tests.negative.FailedLoginTest;
import io.github.radovanovicsasha.halooglasi.tests.regression.AdDetailsTest;
import io.github.radovanovicsasha.halooglasi.tests.regression.ProfileAccessRequiresLoginTest;
import io.github.radovanovicsasha.halooglasi.tests.regression.ProfilePageTest;
import io.github.radovanovicsasha.halooglasi.tests.regression.SearchNoResultsTest;
import io.github.radovanovicsasha.halooglasi.tests.regression.SearchTest;
import io.github.radovanovicsasha.halooglasi.tests.regression.SuccessfulLoginTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        SuccessfulLoginTest.class,
        ProfilePageTest.class,
        SearchTest.class,
        AdDetailsTest.class,
        FailedLoginTest.class,
        ProfileAccessRequiresLoginTest.class,
        SearchNoResultsTest.class
})
public class RegressionSuite {
}
