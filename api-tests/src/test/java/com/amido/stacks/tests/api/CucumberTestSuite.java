package com.amido.stacks.tests.api;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

/**
 * Runs Cucumber features via JUnit 5 Platform Suite Engine.
 * Specifies features location to enable test discovery.
 * Additional configuration (glue, tags, plugins) is loaded from cucumber.properties.
 * This resolves: https://github.com/cucumber/cucumber-jvm/pull/2498
 */
@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "classpath:cucumber/features")
public class CucumberTestSuite {
}
