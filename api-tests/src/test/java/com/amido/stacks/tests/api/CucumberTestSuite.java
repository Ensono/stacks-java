package com.amido.stacks.tests.api;

import static io.cucumber.junit.platform.engine.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@ConfigurationParameter(
    key = FEATURES_PROPERTY_NAME, value = "classpath:cucumber/features")
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME, value = "com.amido.stacks.tests.api.stepdefinitions")
@ConfigurationParameter(
    key = FILTER_TAGS_PROPERTY_NAME,
    value = "@Functional or @Smoke or @Performance and not @Ignore")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "io.cucumber.core.plugin.SerenityReporterParallel,pretty")
public class CucumberTestSuite {}
