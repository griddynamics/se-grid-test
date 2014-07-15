/**
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 * Classification level: Public
 */
package com.griddynamics.cd.selenium

import org.junit.Test

class HubPageParserTest {
    private HubPageParser sut = new HubPageParser()

    @Test
    public void 'given no nodes, empty list to be returned'() {
        assert sut.parseHubConsolePage(seConsoleHtml()).empty
    }

    @Test
    public void 'given single node only in left column, 1 element to be returned'() {
        assert 1 == sut.parseHubConsolePage(seConsoleHtml(1)).size()
    }

    @Test
    public void 'given 2 node in left and right columns, 2 elements to be returned'() {
        assert 2 == sut.parseHubConsolePage(seConsoleHtml(2)).size()
    }

    @Test
    public void 'smoke test from real Se Hub'() {
        assert 8 == sut.parseHubConsolePage(getClass().classLoader.getResourceAsStream("console-from-real-grid.html").text).size()
    }

    @Test
    public void 'given no nodes, 0 browsers to be returned'() {
        assert sut.parseHubConsolePage(seConsoleHtml()).allBrowsers.empty
    }

    @Test
    public void 'given nodes with no browsers, 0 browsers to be returned'() {
        assert sut.parseHubConsolePage(seConsoleHtml(5)).allBrowsers.empty
    }

    @Test
    public void 'given 1 browser in 1 node, 1 browsers to be returned'() {
        assert 1 == sut.parseHubConsolePage(seConsoleHtml(1, 1)).allBrowsers.size()
    }

    @Test
    public void 'given many browsers in many nodes, many x many browsers to be returned'() {
        assert 20 == sut.parseHubConsolePage(seConsoleHtml(5, 4)).allBrowsers.size()
    }

    private static String seConsoleHtml(int numberOfNodes = 0, int numberOfBrowsers = 0) {
        int leftColumnNodesNumber = numberOfNodes
        if (numberOfNodes > 1) {
            leftColumnNodesNumber = numberOfNodes / 2
        }
        String leftColumn = nodePartOfHtml(leftColumnNodesNumber, numberOfBrowsers)
        String rightColumn = nodePartOfHtml(numberOfNodes - leftColumnNodesNumber, numberOfBrowsers)
        return """<body>
             <div id='main_content'>
               <div id='leftColumn'> $leftColumn </div>
               <div id='rightColumn'> $rightColumn </div>
             </div>
           </body>"""
    }

    private static String nodePartOfHtml(int numberOfNodes = 0, int numberOfBrowsers = 0) {
        String result = '';
        String browsers = browsersPartOfHtml(numberOfBrowsers)
        for (int i = 0; i < numberOfNodes; i++) {
            result += """<div class='proxy'>
             <p class="proxyname">DefaultRemoteProxy (version : 2.40.0)</p>
             <p class="proxyid">id : http://10.210.209.203:5555, OS : VISTA</p>
             <div class='content'> $browsers </div>
           </div>"""
        }
        return result
    }

    private static String browsersPartOfHtml(int numberOfBrowsers = 0) {
        String browsers = ''
        for (int i = 0; i < numberOfBrowsers; i++) {
            browsers += '<img src="/grid/resources/org/openqa/grid/images/chrome.png" width="16" height="16" title="{platform=VISTA, seleniumProtocol=WebDriver, browserName=chrome, maxInstances=10}">'
        }
        if (browsers) {
            return """<div type='browsers'>
             <p class='protocol'></p>
             <p> 'v:' $browsers </p>
           </div>"""
        }
        return ''
    }
}
