/**
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 * Classification level: Public
 */
package com.griddynamics.cd.selenium.monitoring;

public interface SeleniumSessionsMBean {
    int getNumberOfUsedSessions();

    int getNumberOfMaxSessions();
}
