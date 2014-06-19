/*
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 */
package com.griddynamics.cd.selenium

import com.griddynamics.cd.selenium.entity.SeNodes
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration("classpath:/com/griddynamics/cd/selenium/app-context.xml")
class SeHubSmokeTest extends Specification {
    @Value('${SELENIUM_URL}')
    String wdRootUrl
    @Value('${EXPECTED_NUMBER_OF_NODES}')
    int expectedNumberOfNodes
    @Value('${EXPECTED_NUMBER_OF_BROWSERS}')
    int expectedNumberOfBrowsers

    def "number of nodes should match the expected number"() {
        given: "Selenium Grid is running on: $wdRootUrl"
            new URL(wdRootUrl).text
        when: "Accessing Grid Console"
            SeNodes nodes = new HubPageParser().parseHubConsolePage(new URL("${wdRootUrl}/grid/console"))
        then: "$expectedNumberOfNodes of nodes should be found"
            expectedNumberOfNodes == nodes.size()
    }

    def "number of browsers should match the expected number"() {
        given: "Selenium Grid is running on: $wdRootUrl"
            new URL(wdRootUrl).text
        when: "Accessing Grid Console"
            SeNodes nodes = new HubPageParser().parseHubConsolePage(new URL("${wdRootUrl}/grid/console"))
        then: "$expectedNumberOfBrowsers of browsers should be found"
            expectedNumberOfBrowsers == nodes.allBrowsers.size()
    }
}
