/*
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.cd.selenium

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

@ContextConfiguration("classpath:/com/griddynamics/cd/selenium/app-context.xml")
class SeNodesSmokeTest extends Specification {
    @Value('${SELENIUM_URL}') String wdRootUrl
    @Value('${HOST_WHERE_TESTS_RUNNING}') String testPageUrlToAccess
    @Value('${EXPECTED_NUMBER_OF_NODES}') int numberOfNodes
    @Autowired Server server
    @Shared List<WebDriver> webDrivers

    def "one browser on every node should open a page of Se Grid Console"() {
        given: "$numberOfNodes sessions are established with Grid: $wdRootUrl"
            webDrivers = createWds(numberOfNodes)
        when: "Accessing test HTML page from the test: $testPageAddress"
            webDrivers.each { it.get(testPageAddress) }
        then: "Some basic elements like h1 are visible on the test HTML page"
            webDrivers.every { !it.findElements(By.cssSelector("h1")).empty }
    }

    def cleanupSpec() {
        webDrivers.each {
            try {
                it.quit()
            } catch (any) {//do nothing
            }
        }
    }

    /**
     * Combines hostname and port in order to build a URL to access test page. While hostname is set in the
     * configuration or passed as a parameter, the port of the test page is dynamic and changes from run to run in
     * order to protect from port conflicts.
     * @return a built URL to access test page
     */
    private String getTestPageAddress() {
        return "$testPageUrlToAccess:${((ServerConnector) server.connectors[0]).localPort}"
    }

    private List<WebDriver> createWds(int numberOfBrowsers) {
        return (1..numberOfBrowsers).collect {
            println "Starting #$it browser of $numberOfBrowsers"
            new RemoteWebDriver(new URL("$wdRootUrl/wd/hub"), new DesiredCapabilities(browserName: 'firefox'))
        }
    }
}
