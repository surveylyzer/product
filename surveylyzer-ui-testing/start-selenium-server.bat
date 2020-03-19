set SELENIUM_VERSION=3.141.59
set SERVER_PATH=src\test\resources\selenium\server
set DRIVERS_PATH=src\test\resources\webdrivers\windows

java -Dwebdriver.chrome.driver=%DRIVERS_PATH%\chromedriver.exe -Dwebdriver.gecko.driver=%DRIVERS_PATH%\geckodriver.exe -Dwebdriver.ie.driver=%DRIVERS_PATH%\IEDriverServer.exe -jar %SERVER_PATH%\selenium-server-standalone-%SELENIUM_VERSION%.jar