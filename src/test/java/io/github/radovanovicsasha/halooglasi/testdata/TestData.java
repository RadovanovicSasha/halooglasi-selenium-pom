package io.github.radovanovicsasha.halooglasi.testdata;

import io.github.radovanovicsasha.halooglasi.framework.config.EnvConfig;

/**
 * Test-specific data: shared constants and login credentials for the live
 * Halo Oglasi account used by login-dependent tests. Credentials are
 * sourced from EnvConfig (environment variables, with a local .env
 * fallback) rather than read here directly, so test classes keep using the
 * familiar TestData.haloEmail/haloPass call sites.
 */
public final class TestData {

    // Search term used by search-related test cases.
    public static final String SEARCH_TERM = "stan";

    public static final String haloEmail = EnvConfig.getUsername();
    public static final String haloPass = EnvConfig.getPassword();

    private TestData() {
    }
}
