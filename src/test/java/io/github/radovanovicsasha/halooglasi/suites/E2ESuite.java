package io.github.radovanovicsasha.halooglasi.suites;

import io.github.radovanovicsasha.halooglasi.tests.e2e.BrowseListingEndToEndTest;
import io.github.radovanovicsasha.halooglasi.tests.e2e.LogoutTest;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        LogoutTest.class,
        BrowseListingEndToEndTest.class
})
public class E2ESuite {
}
