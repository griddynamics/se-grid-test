/**
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 * Classification level: Public
 */
package com.griddynamics.cd.selenium.entity;

import java.util.ArrayList;
import java.util.List;

public class SeNode {
    private final List<Browser> browsers = new ArrayList<Browser>();

    public SeNode addBrowser(Browser browser) {
        browsers.add(browser);
        return this;
    }

    public List<Browser> getBrowsers() {
        return browsers;
    }
}
