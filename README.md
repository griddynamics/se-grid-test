### Tests for the Selenium Grid to be run after Se Infrastructure started

First it checks whether Hub Console opens, then starts N of tests where N is the number of nodes. First all the sessions
are created and afterwards a test  page opens from these tests. The test page is available via HTTP, for these purposes an
embedded HTTP Servers starts in the tests.

- Start Se Infrastructure (hub + nodes)
- Get these sources, step into the folder and run tests:
```
mvn test \
 -DSELENIUM_URL=http://localhost:4444 \
 -DEXPECTED_NUMBER_OF_BROWSERS=20 \
 -DEXPECTED_NUMBER_OF_NODES=5
 -DTEST_PAGE_HOST=http://localhost
```
Replace Hub URL with your own and numbers with your own. Full list of parameters can be found 
[here](selenium-infrastructure-tests/src/test/resources/com/griddynamics/cd/selenium/app-context.xml).

Note, that test page is running on the same machine where tests are running, so you should specify its address along with
the protocol (http in most cases or https if you have some kind of proxy or whatever).

### How does it work

Tests for Hub - download the Hub page and compare the actual number of
browsers and nodes to the expected number.

Tests for Nodes - starts N browsers (N - expected number of nodes) and runs
trivial tests there that opens the test page and checks some basic interactions.


### TBD

Right now the project is pretty young, it's not very generic and might have lots of things to improve. Feel free to contribute.


### Copyright and License

Copyright 2014, Grid Dynamics International, Inc.

Licensed under the [Apache License, Version 2.0](LICENSE.txt).

