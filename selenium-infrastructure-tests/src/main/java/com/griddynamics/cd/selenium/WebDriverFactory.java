/**
 * Copyright 2014, Grid Dynamics International, Inc.
 * Licensed under the Apache License, Version 2.0.
 * Classification level: Public
 */
package com.griddynamics.cd.selenium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * Creates WebDrivers with defaults pre-set.
 */
public class WebDriverFactory {
    private final String webDriverUrl;
    private Proxy proxy;
    private boolean proxyEnabled;

    public WebDriverFactory(String webDriverUrl) {
        this.webDriverUrl = webDriverUrl;
    }

    public WebDriver createDriver(Capabilities capabilities) {
        DesiredCapabilities finalCapabilities = new DesiredCapabilities(capabilities);
        finalCapabilities = withProxy(finalCapabilities);
        finalCapabilities = withChromeOptions(finalCapabilities);
        try {
            RemoteWebDriver driver = new RemoteWebDriver(new URL(webDriverUrl), finalCapabilities);
            //otherwise it may fail from time to time because of performance/network glitches on the node
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return driver;
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Couldn't start WebDriver Session with: " + webDriverUrl, e);
        }
    }

    private DesiredCapabilities withChromeOptions(DesiredCapabilities capabilities) {
        if (!"chrome".equals(capabilities.getBrowserName())) {
            return capabilities;
        }
        ChromeOptions options = new ChromeOptions();
        //required since Chrome 35, see https://code.google.com/p/chromedriver/issues/detail?id=799
        options.addArguments("test-type");
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return capabilities;
    }

    private DesiredCapabilities withProxy(DesiredCapabilities capabilities) {
        if (proxyEnabled) {
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
        return capabilities;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setProxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
    }
}
