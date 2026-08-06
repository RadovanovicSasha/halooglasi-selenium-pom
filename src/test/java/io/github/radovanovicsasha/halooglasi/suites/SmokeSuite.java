package io.github.radovanovicsasha.halooglasi.suites;

import io.github.radovanovicsasha.halooglasi.tests.smoke.AcceptCookiesTest;
import io.github.radovanovicsasha.halooglasi.tests.smoke.OpenLoginPageTest;
import io.github.radovanovicsasha.halooglasi.tests.smoke.OpenSiteTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        OpenSiteTest.class,
        AcceptCookiesTest.class,
        OpenLoginPageTest.class
})
public class SmokeSuite {
}
