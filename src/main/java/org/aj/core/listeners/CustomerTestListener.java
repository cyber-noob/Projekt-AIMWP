package org.aj.core.listeners;

import io.appium.java_client.android.AndroidDriver;
import org.aj.core.Exceptions.MissingMandatoryPropException;
import org.aj.core.Exceptions.UnknownAutomationType;
import org.aj.core.actionsHelper.locatorStrategy.LocatorRepoManager;
import org.aj.core.preTestInits.DriverManager;
import org.aj.core.propertiesHandler.PropertiesManager;
import org.testng.*;
import org.testng.annotations.Test;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.aj.core.actionsHelper.locatorStrategy.LocatorRepoManager.locatorsCache;
import static org.aj.core.preTestInits.CapabilitiesManager.packageName;
import static org.aj.core.preTestInits.DriverManager.automationType;
import static org.aj.core.propertiesHandler.PropertiesManager.getProperty;

public class CustomerTestListener implements ITestListener, IMethodInterceptor {

    DriverManager driverManager = new DriverManager();

    AtomicInteger passCount = new AtomicInteger(0);

    AtomicInteger failCount = new AtomicInteger(0);

    AtomicInteger skipCount = new AtomicInteger(0);

    private static final Set<String> invokedClasses = new HashSet<>();

    private static final String groups;

    static {
        //Fetch all required props
        try {
            new PropertiesManager().getAllProperties();
        } catch (MissingMandatoryPropException e) {
            throw new RuntimeException(e);
        }

        groups = getProperty("groups");
    }


    public static Set<String> getInvokedClasses() {
        return invokedClasses;
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            driverManager.initDriver();
            driverManager.launchApp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(String.join("", Collections.nCopies(100, "=")));
        System.out.println("Current running testcase: " + result.getMethod().getMethodName());
        System.out.println("PASS: " + passCount);
        System.out.println("FAIL: " + failCount);
        System.out.println("SKIP: " + skipCount);
        System.out.println(String.join("", Collections.nCopies(100, "=")));

        ITestListener.super.onTestStart(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ITestListener.super.onTestSuccess(result);

        try {
            ((AndroidDriver) new DriverManager().getDriver()).terminateApp(packageName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        passCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " passed ....!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);

        try {
            driverManager.killApp();
        } catch (UnknownAutomationType e) {
            throw new RuntimeException(e);
        }

        failCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " failed ....!");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);

        try {
            driverManager.killApp();
        } catch (UnknownAutomationType e) {
            throw new RuntimeException(e);
        }

        skipCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " skipped ....!");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
    }

    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        ITestListener.super.onTestFailedWithTimeout(result);
    }

    @Override
    public void onStart(ITestContext context) {
        ITestListener.super.onStart(context);

        Set<Thread> threads = Thread.getAllStackTraces().keySet();
        System.out.println("Current active threads in Listener -> " + threads.size());
    }

    @Override
    public void onFinish(ITestContext context) {
        ITestListener.super.onFinish(context);
    }

    /**
     * Filter out platform specific tests from suite using annotation processing
     * @param methods
     * @param context
     * @return
     */
    @Override
    public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
        List<IMethodInstance> filteredMethods = filterPlatforms(methods);
        filteredMethods = filterGroups(filteredMethods);

        for (IMethodInstance method : filteredMethods) {
            String className = method.getMethod()
                    .getConstructorOrMethod()
                    .getDeclaringClass()
                    .getName();

            invokedClasses.add(className);
        }

        //Load Locators on suite start
        try {
            new LocatorRepoManager().loadLocators();
            System.out.println("Locators cache: " + locatorsCache);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return filteredMethods;
    }

    private List<IMethodInstance> filterPlatforms(List<IMethodInstance> methods) {
        System.out.println("Filtering platforms");
        String currentAutomation = switch (automationType){
            case "android": {
                yield "Android";
            }
            case "ios":{
                yield "IOS";
            }
            case "web":{
                yield "Web";
            }
            case "msite":{
                yield "Msite";
            }
            default:
                throw new IllegalStateException("Unexpected value: " + automationType);
        };

        List<IMethodInstance> filteredMethods = new ArrayList<>();
        for (IMethodInstance method : methods) {
            Annotation[] annotations = method.getMethod()
                    .getConstructorOrMethod()
                    .getMethod()
                    .getAnnotations();

            for (Annotation annotation : annotations) {
                if(annotation.annotationType().getName().contains(currentAutomation))
                    filteredMethods.add(method);
            }
        }

        return filteredMethods;
    }

    private List<IMethodInstance> filterGroups(List<IMethodInstance> methods) {
        System.out.println("Filtering groups");

        List<IMethodInstance> filteredMethods = new ArrayList<>();
        List<String> propGroups = Arrays.stream(groups.split(":")).toList();

        for (IMethodInstance method : methods) {
            String[] groups = method.getMethod()
                    .getConstructorOrMethod()
                    .getMethod()
                    .getAnnotation(Test.class)
                    .groups();

            for (String group : groups) {
                if(propGroups.contains(group))
                    filteredMethods.add(method);
            }
        }

        return filteredMethods;
    }
}
