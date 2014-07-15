/**
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 * Classification level: Public
 */
package com.griddynamics.cd.selenium.entity;

import java.util.ArrayList;
import java.util.List;

public class SeNodes {
    private final List<SeNode> nodes = new ArrayList<SeNode>();

    public SeNodes add(SeNode node) {
        nodes.add(node);
        return this;
    }

    public List<Browser> getAllBrowsers() {
        List<Browser> browsers = new ArrayList<Browser>();
        for (SeNode node : nodes) {
            browsers.addAll(node.getBrowsers());
        }
        return browsers;
    }

    public int size() {
        return nodes.size();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }
}
