package org.aj.core.listeners;

import org.aj.core.Exceptions.MissingLocatorFileException;
import org.aj.core.actionsHelper.locatorStrategy.LocatorRepoManager;
import org.aj.core.preTestInits.DriverManager;
import org.testng.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerTestListener implements ITestListener, ISuiteListener {

    DriverManager driverManager = new DriverManager();

    AtomicInteger passCount = new AtomicInteger(0);

    AtomicInteger failCount = new AtomicInteger(0);

    AtomicInteger skipCount = new AtomicInteger(0);

    private static Set<String> invokedClasses = new HashSet<>();


    public static Set<String> getInvokedClasses() {
        return invokedClasses;
    }

    @Override
    public void onStart(ISuite suite) {
        ISuiteListener.super.onStart(suite);

        suite.getAllInvokedMethods()
                .forEach(iInvokedMethod -> invokedClasses.add(iInvokedMethod
                        .getTestMethod()
                        .getTestClass()
                        .getName()
                ));

        //Load Locators on suite start
        try {
            new LocatorRepoManager().loadLocators();
        } catch (IOException | MissingLocatorFileException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onFinish(ISuite suite) {
        ISuiteListener.super.onFinish(suite);
    }

    @Override
    public void onTestStart(ITestResult result) {
        try {
            driverManager.initDriver();
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

        driverManager.killDriver();

        passCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " passed ....!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);

        driverManager.killDriver();

        failCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " failed ....!");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);

        driverManager.killDriver();

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
}
