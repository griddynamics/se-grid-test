/*
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.cd.selenium

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration("classpath:/com/griddynamics/cd/selenium/app-context.xml")
class SeNodesSmokeTest extends Specification {
    @Value('${SELENIUM_URL}') String wdRootUrl
    @Value('${SELENIUM_URL_TO_ACCESS_FROM_TESTS}') String wdRootUrlAccessibleFromTests
    @Value('${EXPECTED_NUMBER_OF_NODES}') int numberOfNodes
    @Shared List<WebDriver> webDrivers

    def "one browser on every node should open a page of Se Grid Console"() {
        given: "$numberOfNodes sessions are established with Grid: $wdRootUrl"
            webDrivers = createWds(numberOfNodes)
        when: "Accessing Grid Console from the test"
            webDrivers.each { it.get("$wdRootUrlAccessibleFromTests/grid/console") }
        then: "Some standard elements like nodes are visible on the grid console page"
            webDrivers.every { !it.findElements(By.cssSelector(".content")).empty }
    }

    def cleanupSpec() {
        webDrivers.each {
            try {
                it.quit()
            } catch (any) {//do nothing
            }
        }
    }

    private List<WebDriver> createWds(int numberOfBrowsers) {
        return (1..numberOfBrowsers).collect {
            println "Starting #$it browser of $numberOfBrowsers"
            new RemoteWebDriver(new URL("$wdRootUrl/wd/hub"), new DesiredCapabilities(browserName: 'firefox'))
        }
    }
}
