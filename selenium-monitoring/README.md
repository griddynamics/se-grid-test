Selenium Monitoring
----

Provides an ability to turn on JMX into Selenium Hub and gather metrics such as number of browsers in Grid and number
of currently running sessions.

###Usage
- Run `mvn package` in order to produce binaries to be hooked up to Selenium Hub
- You'll be able to find a jar file: `selenium-monitoring/target/selenium-monitoring-${VERSION}.jar`
- Copy the jar file to the folder with Selenium Hub and given selenium server is called `selenium.jar` start it like that:
`java -Dselenium.jmxrmiport=8778 -cp "selenium-monitoring-${VERSION}.jar:selenium.jar" org.openqa.grid.selenium.GridLauncher
-role hub -servlets com.griddynamics.cd.selenium.monitoring.SeleniumMonitoringServlet`
Note, that you can add `selenium.jmxrmiport` option overriding the default port (8778) if you need that. You should see
in console:
> INFO: binding com.griddynamics.cd.selenium.monitoring.SeleniumMonitoringServlet to /grid/admin/SeleniumMonitoringServlet/*

- Open Selenium page: http://host.com:4444/grid/admin/SeleniumMonitoringServlet/ this will start up the servlet and will
 initialize JMX. This should be done after every Selenium Hub startup.

If you configure Selenium Hub via
[JSON file](https://code.google.com/p/selenium/source/browse/java/server/src/org/openqa/grid/common/defaults/DefaultHub.json),
you can pass `com.griddynamics.cd.selenium.monitoring.SeleniumMonitoringServlet` in there in `servlets` field.

###How it works
Selenium provides an ability to hook up additional servlets as an extension point. This ability is used to add our monitoring
related functionality - it's initialized when the servlet gets created (on the first access).

### Copyright and License

Copyright 2014, Grid Dynamics International, Inc.

Licensed under the [Apache License, Version 2.0](LICENSE.txt).

Classification level: Public
