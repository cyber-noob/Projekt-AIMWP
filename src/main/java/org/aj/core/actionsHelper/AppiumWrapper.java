package org.aj.core.actionsHelper;

import io.appium.java_client.AppiumBy;
import org.aj.core.actionsHelper.locatorStrategy.LocatorFinderHelper;
import org.aj.core.preTestInits.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

public class AppiumWrapper {

    WebDriver driver;

    public AppiumWrapper() throws Exception {
        driver = new DriverManager().getDriver();
        System.out.println("Driver assigned ....");

        System.out.println("wrapper -> " + driver);
    }

    public WebElement getElement(String pageName, String locator) throws Exception {
        return new LocatorFinderHelper().getElement(pageName, locator);
    }

    public List<WebElement> getElements(String pageName, String locator) throws Exception {
        return new LocatorFinderHelper().getElements(pageName, locator);
    }

    public void clickEnter() throws Exception{
        new Actions(driver)
                .keyDown(Keys.ENTER)
                .perform();
    }
}
