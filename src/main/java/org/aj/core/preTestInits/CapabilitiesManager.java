package org.aj.core.preTestInits;

import org.aj.core.Exceptions.DtypeException;
import org.openqa.selenium.remote.DesiredCapabilities;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.aj.core.propertiesHandler.PropertiesManager.getProperty;

public class CapabilitiesManager {

    public static String packageName;

    protected DesiredCapabilities AndroidCapabilities(JadbDevice device) throws DtypeException, IOException, JadbException {
        System.out.println("Fetching android capabilties");

        System.out.println("device -> " + device.getSerial());

        ByteArrayOutputStream androidVersion = new ByteArrayOutputStream();
        device.executeShell(androidVersion, "getprop ro.build.version.release");
        System.out.write(androidVersion.toByteArray());

        ByteArrayOutputStream packageNames = new ByteArrayOutputStream();
        device.executeShell(packageNames, "pm list packages");
//        System.out.println(packageName.toString(StandardCharsets.UTF_8));
        List<String> packages = packageNames.toString(StandardCharsets.UTF_8).lines().toList();
        packageName = packages.stream()
                .filter(pack -> {
                    String[] arr = pack.split("\\.");
                    return arr[arr.length - 1].contains(getProperty("appName"));
                })
                .findFirst()
                .orElseThrow()
                .replace("package:", "");
        System.out.println("packageName found -> " + packageName);



        DesiredCapabilities capabilities = new DesiredCapabilities();
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.platformName, "android");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.platformVersion, androidVersion.toString(StandardCharsets.UTF_8));
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.deviceName, device.getSerial());
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.automationName, "UiAutomator2");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.fullReset, false);
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.noReset, true);
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Android.appPackage, packageName);
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Android.autoGrantPermissions, true);
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Android.appActivity, "com.google.android.gm.GmailActivity");

        return capabilities;
    }

    protected DesiredCapabilities IOSCapabilities() throws DtypeException{
        System.out.println("Fetching ios capabilties");

        DesiredCapabilities capabilities = new DesiredCapabilities();
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.platformName, "iOS");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.platformVersion, "");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.deviceName, "iPhone 13");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.IOS.udid, "");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.IOS.bundleId, "");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.IOS.xcodeOrgId, "");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.IOS.xcodeSigningId, "");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.IOS.updatedWDBundleId, "");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.IOS.useNewWDA, true);

        return capabilities;
    }
}
