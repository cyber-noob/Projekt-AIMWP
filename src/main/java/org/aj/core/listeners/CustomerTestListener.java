package org.aj.core.listeners;

import org.aj.core.preTestInits.DriverManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerTestListener implements ITestListener {

    DriverManager driverManager = new DriverManager();

    AtomicInteger passCount = new AtomicInteger(0);

    AtomicInteger failCount = new AtomicInteger(0);

    AtomicInteger skipCount = new AtomicInteger(0);

    @Override
    public void onTestStart(ITestResult result) {
        try {
            new DriverManager().initDriver();
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

        passCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " passed ....!");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ITestListener.super.onTestFailure(result);

        failCount.addAndGet(1);
        System.out.println(result.getMethod().getMethodName() + " failed ....!");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ITestListener.super.onTestSkipped(result);

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
