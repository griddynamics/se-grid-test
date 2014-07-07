package com.griddynamics.cd.selenium.monitoring;

import com.google.common.collect.Lists;
import org.openqa.grid.internal.Registry;
import org.openqa.grid.internal.RemoteProxy;
import org.openqa.grid.internal.TestSession;
import org.openqa.grid.internal.TestSlot;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * In order to expose beans for different capabilities (Win+FF, Mac+Chrome, etc.) you'd need to create a lot of
 * instances of this class and expose them via JMX (or other means). Use {@link #forDifferentCapabilities(Registry)} for
 * these purposes.
 */
public class SeleniumSessions implements SeleniumSessionsMBean {
    private final Registry registry;
    private final DesiredCapabilities capabilities;

    public SeleniumSessions(String browser, Platform platform, Registry registry) {
        this.capabilities = new DesiredCapabilities();
        this.capabilities.setBrowserName(browser);
        this.capabilities.setPlatform(platform);
        this.registry = registry;
    }

    /**
     * Creates a number of selenium sessions to gather metrics for, they include different combination of browsers and
     * OSes as well as ANY-ANY combination to get metric about the whole Se Grid.
     *
     * @param registry selenium registry to get information about nodes
     * @return list of selenium sessions infos to be retrieved over JMX
     */
    public static List<SeleniumSessions> forDifferentCapabilities(Registry registry) {
        List<SeleniumSessions> seleniumSessionsList = new ArrayList<SeleniumSessions>();
        for (Platform platform : Platform.values()) {
            for (String browser : Lists.newArrayList("firefox", "chrome", "iexplorer", "ANY")) {
                seleniumSessionsList.add(new SeleniumSessions(browser, platform, registry));
            }
        }
        return seleniumSessionsList;
    }

    public int getNumberOfUsedSessions() {
        int numberOfUsedSessions = 0;
        for (TestSession session : registry.getActiveSessions()) {
            Map<String, Object> nodeCapabilities = session.getSlot().getCapabilities();
            //noinspection unchecked
            if (registry.getCapabilityMatcher().matches(nodeCapabilities, (Map<String, Object>) this.capabilities.asMap())) {
                numberOfUsedSessions++;
            }
        }
        return numberOfUsedSessions;
    }

    /**
     * Gets max number of sessions that can be initiated in the whole grid for {@link #capabilities} passed in the
     * constructor. Note, that node can have a global constraint on the number of concurrent sessions, so there are 2
     * figures: 1. Number of browsers matching capabilities 2. Global constraint on number of sessions. The lower number
     * will be returned.
     *
     * @return max number of sessions that's possible to start on grid for the {@link #capabilities}
     */
    public int getNumberOfMaxSessions() {
        int numberOfSlotsMatchingCapabilities = 0;
        for (RemoteProxy node : registry.getAllProxies()) {
            int numberOfSlotsMatchingCapabilitiesOnNode = 0;
            for (TestSlot slot : node.getTestSlots()) {
                Map<String, Object> nodeCapabilities = slot.getCapabilities();
                //noinspection unchecked
                if (registry.getCapabilityMatcher().matches(nodeCapabilities, (Map<String, Object>) this.capabilities.asMap())) {
                    numberOfSlotsMatchingCapabilitiesOnNode++;
                }
            }
            numberOfSlotsMatchingCapabilities += Math.min(node.getMaxNumberOfConcurrentTestSessions(), numberOfSlotsMatchingCapabilitiesOnNode);
        }
        return numberOfSlotsMatchingCapabilities;
    }

    public String getJmxObjectName() {
        return getClass().getPackage().getName()
                + ":type=" + getClass().getSimpleName()
                + ",platform=" + capabilities.getPlatform().toString() + ",browser=" + capabilities.getBrowserName();
    }
}
