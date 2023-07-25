package org.aj.core.preTestInits;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class DriverManager {

    private static ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    private static ThreadLocal<JadbDevice> deviceThreadLocal = new ThreadLocal<>();

    String automationType = "android";

    CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

    static JadbConnection jadbConnection = new JadbConnection();

    static ConcurrentHashMap<JadbDevice, Boolean> isDriverInitialized = new ConcurrentHashMap<>();

    static List<JadbDevice> deviceList;

    ThreadLocal<URL> appiumServer = new ThreadLocal<>();

    static {
        try {
            deviceList = jadbConnection.getDevices();

            deviceList.forEach(device -> isDriverInitialized.put(device, false));
        } catch (IOException | JadbException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeTest(alwaysRun = true)
    public void initDriver() throws Exception{

        System.out.println("Initialising driver ...");
        if(drivers.get() == null) {

            JadbDevice toBeUsedevice = null;

            for (JadbDevice device: deviceList) {
                if(!isDriverInitialized.get(device)) {
                    toBeUsedevice = device;
                    isDriverInitialized.replace(toBeUsedevice, true);
                    break;
                }
            }
            appiumServer.set(startAppiumServer());

            assert toBeUsedevice != null;
            switch (automationType) {
                case "android" -> drivers.set(getAndroidDriver(appiumServer.get(), toBeUsedevice));
                case "ios" -> drivers.set(getIosDriver(appiumServer.get()));
                case "web" -> drivers.set(getWebDriver());
            }
        }else
            System.out.println("Driver for current Thread instance is available... Hence continuing test");
    }

    private URL startAppiumServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();

        builder.usingAnyFreePort();
        builder.usingDriverExecutable(new File("/usr/local/bin/node"));
        builder.withAppiumJS(new File("/usr/local/bin/appium"));
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
        builder.withEnvironment(environment);

        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
        service.start();

        return service.getUrl();
    }

    private WebDriver getAndroidDriver(URL appiumServer, JadbDevice device) throws Exception{
        return new AndroidDriver(appiumServer, capabilitiesManager.AndroidCapabilities(device));
    }

    private WebDriver getIosDriver(URL appiumServer) throws Exception{
        return new IOSDriver(appiumServer, capabilitiesManager.IOSCapabilities());
    }


    private WebDriver getWebDriver() throws Exception{
        return new ChromeDriver();
    }

    public WebDriver getDriver() throws Exception {
        return drivers.get();
    }

    @AfterTest(alwaysRun = true)
    public void killDriver() {
        System.out.println("Killing driver ....");

        if(drivers.get() != null) {
            drivers.get().quit();
            isDriverInitialized.replace(deviceThreadLocal.get(), false);
        }
    }
}
