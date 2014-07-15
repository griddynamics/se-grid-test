/**
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 * Classification level: Public
 */
package com.griddynamics.cd.selenium;

import com.griddynamics.cd.selenium.entity.Browser;
import com.griddynamics.cd.selenium.entity.SeNode;
import com.griddynamics.cd.selenium.entity.SeNodes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;

public class HubPageParser {
    public SeNodes parseHubConsolePage(URL hubConsolePage) throws IOException {
        return getSeNodes(Jsoup.parse(hubConsolePage, 2000));
    }

    public SeNodes parseHubConsolePage(String html) {
        return getSeNodes(Jsoup.parse(html));
    }

    private SeNodes getSeNodes(Document page) {
        SeNodes nodes = new SeNodes();
        for (Element node : page.select(".content")) {
            SeNode seNode = new SeNode();
            for (Element browserElement : node.select("img")) {
                seNode.addBrowser(new Browser());
            }
            nodes.add(seNode);
        }
        return nodes;
    }


}
