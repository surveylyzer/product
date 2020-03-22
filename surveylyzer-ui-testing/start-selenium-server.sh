#!/usr/bin/env bash
SELENIUM_VERSION=3.141.59
SERVER_PATH=src/test/resources/selenium/server
DRIVERS_PATH=src/test/resources/webdrivers/macOS

find $DRIVERS_PATH/* ! -perm +ugo+x -exec echo "{} is not executable, please check the execution permissions for this driver with command ls -la. The permission can be set with the command chmod +x {}" \;

java -Dwebdriver.chrome.driver=$DRIVERS_PATH/chromedriver -Dwebdriver.gecko.driver=$DRIVERS_PATH/geckodriver -jar $SERVER_PATH/selenium-server-standalone-$SELENIUM_VERSION.jar
