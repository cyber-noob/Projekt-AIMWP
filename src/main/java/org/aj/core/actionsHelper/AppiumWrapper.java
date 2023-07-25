package org.aj.core.actionsHelper;

import org.aj.core.preTestInits.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AppiumWrapper {

    WebDriver driver;

    public AppiumWrapper() throws Exception {
        driver = new DriverManager().getDriver();
        System.out.println("Driver assigned ....");

        System.out.println("wrapper -> " + driver);
    }

    public WebElement getElement(String locator) {
        return driver.findElement(By.id(locator));
    }
}
