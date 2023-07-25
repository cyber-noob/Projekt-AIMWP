package org.aj.core.preTestInits;

import org.aj.core.Exceptions.DtypeException;
import org.openqa.selenium.remote.DesiredCapabilities;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CapabilitiesManager {

    protected DesiredCapabilities AndroidCapabilities(JadbDevice device) throws DtypeException, IOException, JadbException {
        System.out.println("Fetching android capabilties");

        System.out.println("device -> " + device.getSerial());

        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        device.executeShell(bout, "getprop ro.build.version.release");
        System.out.write(bout.toByteArray());

        DesiredCapabilities capabilities = new DesiredCapabilities();
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.platformName, "android");
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.platformVersion, new String(bout.toByteArray(), StandardCharsets.UTF_8));
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.deviceName, device.getSerial());
        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Global.automationName, "UiAutomator2");
//        MyDesiredCapabilities.setCapability(capabilities, Capabilities.Android.avd, "Pixel_34_25Jul23");

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
