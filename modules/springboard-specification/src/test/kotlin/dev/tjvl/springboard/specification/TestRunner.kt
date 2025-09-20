package dev.tjvl.springboard.specification

import io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME
import io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.Suite

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "pretty,html:build/reports/cucumber,json:build/reports/cucumber/cucumber.json",
)
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "dev.tjvl.springboard.specification",
)
class TestRunner
