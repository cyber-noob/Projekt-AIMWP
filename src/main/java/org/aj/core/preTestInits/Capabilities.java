package org.aj.core.preTestInits;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public interface Capabilities {

    static final Map<Enum<? extends Capabilities>, Object> cache = new HashMap<>();

    static Object getDtype(Enum<? extends Capabilities> capability) {
        if(cache.containsKey(capability))
            return cache.get(capability);

        throw new RuntimeException("Capabilities cache doesn't contain the expected key -> " + capability);
    }

    enum Global implements Capabilities {

        automationName(String.class),

        platformName(String.class),

        platformVersion(String.class),

        deviceName(String.class),

        app(String.class),

        browserName(String.class),

        newCommandTimeout(Number.class),

        udid(String.class),

        orientation(String.class),

        autoWebview(Boolean.class),

        noReset(Boolean.class),

        fullReset(Boolean.class),

        eventTimings(Boolean.class),

        enablePerformanceLogging(Boolean.class),

        printPageSourceOnFindFailure(Boolean.class);

        private final Object dtype;

        static {
            for (Global e: values()) {
                cache.put(e, e.dtype);
            }
        }

        Global(Object dtype) {
            this.dtype = dtype;
        }
    }

    enum Android implements Capabilities{

        appActivity(String.class),

        appPackage(String.class),

        appWaitActivity(String.class),

        appWaitPackage(String.class),

        appWaitDuration(Number.class),

        deviceReadyTimeout(Number.class),

        androidCoverageEndIntent(String.class),

        androidDeviceReadyTimeout(Number.class),

        androidInstallTimeout(Number.class),

        androidInstallPath(String.class),

        adbPort(Number.class),

        systemPort(Number.class),

        remoteAdbHost(Number.class),

        androidDeviceSocket(String.class),

        avd(String.class),

        avdLaunchTimeout(Number.class),

        avdReadyTimeout(Number.class),

        avdArgs(String.class),

        useKeystore(Boolean.class),

        keystorePath(String.class),

        keystorePassword(String.class),

        keyAlias(String.class),

        keyPassword(String.class),

        chromedriverExecutable(String.class),

        chromedriverExecutableDir(String.class),

        chromedriverChromeMappingFile(String.class),

        chromedriverUseSystemExecutable(Boolean.class),

        autoWebviewTimeout(Number.class),

        intentAction(String.class),

        intentCategory(String.class),

        intentFlags(String.class),

        optionalIntentArguments(String.class),

        dontStopAppOnReset(Boolean.class),

        unicodeKeyboard(Boolean.class),

        resetKeyboard(Boolean.class),

        noSign(Boolean.class),

        ignoreUnimportantViews(Boolean.class),

        disableAndroidWatchers(Boolean.class),

        chromeOptions(String.class),

        recreateChromeDriverSessions(Boolean.class),

        nativeWebScreenshot(Boolean.class),

        androidScreenshotPath(String.class),

        autoGrantPermissions(Boolean.class),

        networkSpeed(NetSpeedOptions.class),

        gpsEnabled(Boolean.class),

        isHeadless(Boolean.class),

        uiautomator2ServerLaunchTimeout(Number.class),

        uiautomator2ServerInstallTimeout(Number.class),

        otherApps(JSONObject.class),

        adbExecTimeout(Number.class),

        localeScript(String.class);

        private final Object dtype;

        static {
            for (Android e: values()) {
                cache.put(e, e.dtype);
            }
        }

        Android(Object dtype) {
            this.dtype = dtype;
        }
    }

    enum IOS implements Capabilities{

        calendarFormat(String.class),

        bundleId(String.class),

        udid(String.class),

        launchTimeout(Number.class),

        locationServicesEnabled(Boolean.class),

        locationServicesAuthorized(Boolean.class),

        autoAcceptAlerts(Boolean.class),

        autoDismissAlerts(Boolean.class),

        nativeInstrumentsLib(Boolean.class),

        nativeWebTap(Boolean.class),

        safariInitialUrl(String.class),

        safariAllowPopups(Boolean.class),

        safariIgnoreFraudWarning(Boolean.class),

        safariOpenLinksInBackground(Boolean.class),

        keepKeyChains(Boolean.class),

        localizableStringsDir(String.class),

        processArguments(String.class),

        interKeyDelay(Number.class),

        showIOSLog(Boolean.class),

        sendKeyStrategy(SendKeyStrategyOptionsIOS.class),

        screenshotWaitTimeout(Number.class),

        waitForAppScript(Boolean.class),

        webviewConnectRetries(Number.class),

        appName(String.class),

        customSSLCert(String.class),

        webkitResponseTimeout(Number.class),

        remoteDebugProxy(Number.class),

        xcodeOrgId(String.class),

        xcodeSigningId(String.class),

        updatedWDBundleId(String.class),

        useNewWDA(Boolean.class);

        private final Object dtype;

        static {
            for (IOS e: values()) {
                cache.put(e, e.dtype);
            }
        }

        IOS(Object dtype) {
            this.dtype = dtype;
        }
    }

    enum NetSpeedOptions{

        full,

        gsm,

        edge,

        hscsd,

        gprs,

        umts,

        hsdpa,

        lte,

        evdo
    }

    enum SendKeyStrategyOptionsIOS {

        oneByOne,

        grouped,

        setValue
    }
}
