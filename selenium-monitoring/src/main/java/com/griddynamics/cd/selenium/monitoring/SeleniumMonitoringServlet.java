package com.griddynamics.cd.selenium.monitoring;

import org.eclipse.jetty.jmx.ConnectorServer;
import org.openqa.grid.web.servlet.RegistryBasedServlet;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXServiceURL;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.management.ManagementFactory;

/**
 * Start JMX server that can provide info about Selenium Grid via JMX over RMI. Selenium doesn't have lots of means to
 * add this functionality apart from patching it or providing servlets. Because this functionality is implemented as
 * a servlet which is instantiated during first access, JMX will start only when you open the page with this servlet.
 * See README to figure out how this servlet should be configured.
 */
public class SeleniumMonitoringServlet extends RegistryBasedServlet {
    private static final int DEFAULT_RMI_PORT = 8778;
    /** This system property can be passed during startup to override default value {@link #DEFAULT_RMI_PORT}. */
    private static final String JMX_RMI_PORT_PROPERTY = "selenium.jmxrmiport";
    /** For internal usage to indicate whether we already have started JMX or not. */
    private static volatile boolean jmxInitialized = false;

    /** Used by Selenium. Registry is obtained in the super class from servlet app context if it's null. */
    public SeleniumMonitoringServlet() {
        super(null);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        initJmx();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(200);
        writeStringAndClose(response, "JMX was initialized, see logs for URL to connect");
        response.getOutputStream().close();
    }

    /**
     * Initializes JMX and starts it only once for all the instances of this servlet. Uses global static field for these
     * purposes.
     */
    private void initJmx() {
        synchronized (getClass()) {
            if (!jmxInitialized) {
                try {
                    MBeanServer jmxServer = ManagementFactory.getPlatformMBeanServer();
                    for (SeleniumSessions sessions : SeleniumSessions.forDifferentCapabilities(getRegistry())) {
                        jmxServer.registerMBean(sessions, new ObjectName(sessions.getJmxObjectName()));
                    }
                    jmxServer.registerMBean(new SeleniumGridGeneralMetrics(getRegistry()),
                            new ObjectName(SeleniumGridGeneralMetrics.getJmxObjectName()));

                    ConnectorServer jmx = new ConnectorServer(new JMXServiceURL(
                            String.format("service:jmx:rmi:///jndi/rmi://localhost:%d/jmxrmi", getJmxPort())),
                            "org.eclipse.jetty.jmx:name=rmiconnectorserver");
                    jmx.start();
                } catch (Exception e) {
                    log("Error while starting JMX Server", e);
                    throw new IllegalStateException(e);
                }
                jmxInitialized = true;
            }
        }
    }

    private int getJmxPort() {
        String jmxPort = System.getProperty(JMX_RMI_PORT_PROPERTY);
        try {
            return Integer.parseInt(jmxPort);
        } catch (NumberFormatException e) {
            return DEFAULT_RMI_PORT;
        }
    }

    private void writeStringAndClose(HttpServletResponse response, String textualResponse) throws IOException {
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        response.getOutputStream().write(textualResponse.getBytes());
    }
}
