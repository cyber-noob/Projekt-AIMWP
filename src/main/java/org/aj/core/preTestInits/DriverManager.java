package org.aj.core.preTestInits;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.aj.core.Exceptions.MissingMandatoryPropException;
import org.aj.core.Exceptions.UnknownAutomationType;
import org.aj.core.propertiesHandler.PropertiesManager;
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

import static org.aj.core.preTestInits.CapabilitiesManager.packageName;
import static org.aj.core.propertiesHandler.PropertiesManager.getProperty;

public class DriverManager {

    private static final ThreadLocal<WebDriver> drivers = new ThreadLocal<>();

    private static final ThreadLocal<JadbDevice> deviceThreadLocal = new ThreadLocal<>();

    public static String automationType = null;

    CapabilitiesManager capabilitiesManager = new CapabilitiesManager();

    static JadbConnection jadbConnection = new JadbConnection();

    static ConcurrentHashMap<JadbDevice, Boolean> isDeviceAvailable = new ConcurrentHashMap<>();

    static List<JadbDevice> deviceList;

    private static ThreadLocal<URL> appiumServer = new ThreadLocal<>();

    static {
        try {
            //Get all devices
            deviceList = jadbConnection.getDevices();
            deviceList.forEach(device -> isDeviceAvailable.put(device, true));

            automationType = getProperty("automationType");

        } catch (IOException | JadbException e) {
            throw new RuntimeException(e);
        }
    }

    public void initDriver() throws Exception{

        System.out.println("Initialising driver ...");
        if(drivers.get() == null) {

            JadbDevice toBeUsedevice = null;

            //Init device to be used for driver creation
            for (JadbDevice device: deviceList) {
                if(isDeviceAvailable.get(device)) {
                    toBeUsedevice = device;
                    isDeviceAvailable.replace(toBeUsedevice, false);
                    break;
                }
                else
                    System.out.println("Reusing the existing driver");
            }

            //Init appium server
            if(appiumServer.get() == null)
                appiumServer.set(startAppiumServer());
            else
                System.out.println("Re-using the same server as it's already present");

            //Init driver
            assert toBeUsedevice != null;
            switch (automationType) {
                case "android":
                    drivers.set(getAndroidDriver(appiumServer.get(), toBeUsedevice));
                    deviceThreadLocal.set(toBeUsedevice);
                    break;
                case "ios":
                    drivers.set(getIosDriver(appiumServer.get()));
                    deviceThreadLocal.set(toBeUsedevice);
                    break;
                case "web":
                    drivers.set(getWebDriver());
                    deviceThreadLocal.set(toBeUsedevice);
                    break;
            }
        }else
            System.out.println("Driver for current Thread instance is available... Hence continuing test");
    }

    private URL startAppiumServer() {
        AppiumServiceBuilder builder = new AppiumServiceBuilder();

        builder.usingAnyFreePort();
        builder.usingDriverExecutable(new File("/usr/local/bin/node"));
        builder.withAppiumJS(new File("/usr/local/bin/appium"));
        builder.withArgument(GeneralServerFlag.LOG_LEVEL, "error");
        HashMap<String, String> environment = new HashMap<>();
        environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
        builder.withEnvironment(environment);

        AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
        service.start();

        return service.getUrl();
    }

    private WebDriver getAndroidDriver(URL appiumServer, JadbDevice device) throws Exception{
        System.out.println("Appium server URL -> " + appiumServer);
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

    public void killApp() throws UnknownAutomationType {
        System.out.println("Killing app ....");

        switch (automationType){
            case "android" -> ((AndroidDriver)drivers.get()).terminateApp(packageName);
            case "ios" -> ((IOSDriver) drivers.get()).terminateApp(packageName);
            case "web" -> drivers.get().close();
            default -> throw new UnknownAutomationType(automationType);
        }
    }

    public void launchApp() throws UnknownAutomationType{
        System.out.println("Laaunching app ....");

        switch (automationType){
            case "android" -> ((AndroidDriver)drivers.get()).activateApp(packageName);
            case "ios" -> ((IOSDriver) drivers.get()).activateApp(packageName);
            case "web" -> drivers.get().get(PropertiesManager.getProperty("url"));
            default -> throw new UnknownAutomationType(automationType);
        }
    }
}
