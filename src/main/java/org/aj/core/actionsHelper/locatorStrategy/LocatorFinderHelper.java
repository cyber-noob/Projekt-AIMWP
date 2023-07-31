package org.aj.core.actionsHelper.locatorStrategy;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.aj.core.Exceptions.UnknownAutomationType;
import org.aj.core.actionsHelper.AppiumWrapper;
import org.aj.core.preTestInits.DriverManager;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

import static org.aj.core.actionsHelper.locatorStrategy.LocatorRepoManager.locatorsCache;
import static org.aj.core.preTestInits.DriverManager.automationType;

/**
 * Actual locator finding logics
 */
public class LocatorFinderHelper {

    WebDriver driver = new DriverManager().getDriver();

    public LocatorFinderHelper() throws Exception {
    }

    public WebElement getElement(String pageName, String jsonPath) throws UnknownAutomationType {
        Map value = locatorsCache.get(pageName)
                .read(jsonPath + "." + automationType, Map.class);

        JSONObject locatorsJson = new JSONObject(value);
        String locatorStrat = locatorsJson.keys().next();

        return parseLocator(locatorStrat, locatorsJson.getString(locatorStrat));
    }

    public List<WebElement> getElements(String pageName, String jsonPath) throws UnknownAutomationType {
        Map value = locatorsCache.get(pageName)
                .read(jsonPath + "." + automationType, Map.class);

        JSONObject locatorsJson = new JSONObject(value);
        String locatorStrat = locatorsJson.keys().next();

        return parseLocators(locatorStrat, locatorsJson.getString(locatorStrat));
    }

    private WebElement webDespatcher(String locatorStrat, String value) {
        switch (locatorStrat) {
            case "id" -> {
                return driver.findElement(By.id(value));
            }
            case "name" -> {
                return driver.findElement(By.name(value));
            }
            case "classname" -> {
                return driver.findElement(By.className(value));
            }
            case "cssselector" -> {
                return driver.findElement(By.cssSelector(value));
            }
            case "linktext" -> {
                return driver.findElement(By.linkText(value));
            }
            case "partiallinktext" -> {
                return driver.findElement(By.partialLinkText(value));
            }
            case "tagname" -> {
                return driver.findElement(By.tagName(value));
            }
            case "xpath" -> {
                return driver.findElement(By.xpath(value));
            }
        }

        throw new RuntimeException("The provided locator strategy doesn't exist." +
                "Please use one of the following [id, name, classname, cssselector, linktext, partiallinktext, tagname, xpath]");
    }

    private WebElement mobileDespatcher(String locatorStrat, String value) {
        switch (locatorStrat) {
            case "id" -> {
                return driver.findElement(AppiumBy.id(value));
            }
            case "name" -> {
                return driver.findElement(AppiumBy.name(value));
            }
            case "classname" -> {
                return driver.findElement(AppiumBy.className(value));
            }
            case "accessibilityid" -> {
                return driver.findElement(AppiumBy.accessibilityId(value));
            }
            case "xpath" -> {
                return driver.findElement(AppiumBy.xpath(value));
            }
            case "image" -> {
                return driver.findElement(AppiumBy.image(value));
            }
            case "androiduiautomator" -> {
                return driver.findElement(AppiumBy.androidUIAutomator(value));
            }

            //IOS
            case "iosclasschain" -> {
                return driver.findElement(AppiumBy.iOSClassChain(value));
            }
            case "iosnspredicatestring" -> {
                return driver.findElement(AppiumBy.iOSNsPredicateString(value));
            }
        }

        throw new RuntimeException("The provided locator strategy doesn't exist." +
                "Please use one of the following [id, name, classname, accessibilityid, xpath, image, androiduiautomator, iosclasschain, iosnspredicatestring]");
    }

    private WebElement parseLocator(String locatorStrat, String value) throws UnknownAutomationType {
        switch (automationType){
            case "web", "msite" -> {
                return webDespatcher(locatorStrat, value);
            }
            case "android", "ios" -> {
                return mobileDespatcher(locatorStrat, value);
            }
        }

        throw new UnknownAutomationType("Please provide a valid automation type." +
                "Recognised automation types [web, msite, android, ios]");
    }

    private List<WebElement> webDespatchers(String locatorStrat, String value) {
        switch (locatorStrat) {
            case "id" -> {
                return driver.findElements(By.id(value));
            }
            case "name" -> {
                return driver.findElements(By.name(value));
            }
            case "classname" -> {
                return driver.findElements(By.className(value));
            }
            case "cssselector" -> {
                return driver.findElements(By.cssSelector(value));
            }
            case "linktext" -> {
                return driver.findElements(By.linkText(value));
            }
            case "partiallinktext" -> {
                return driver.findElements(By.partialLinkText(value));
            }
            case "tagname" -> {
                return driver.findElements(By.tagName(value));
            }
            case "xpath" -> {
                return driver.findElements(By.xpath(value));
            }
        }

        throw new RuntimeException("The provided locator strategy doesn't exist." +
                "Please use one of the following [id, name, classname, cssselector, linktext, partiallinktext, tagname, xpath]");
    }

    private List<WebElement> mobileDespatchers(String locatorStrat, String value) {
        switch (locatorStrat) {
            case "id" -> {
                return driver.findElements(AppiumBy.id(value));
            }
            case "name" -> {
                return driver.findElements(AppiumBy.name(value));
            }
            case "classname" -> {
                return driver.findElements(AppiumBy.className(value));
            }
            case "accessibilityid" -> {
                return driver.findElements(AppiumBy.accessibilityId(value));
            }
            case "xpath" -> {
                return driver.findElements(AppiumBy.xpath(value));
            }
            case "image" -> {
                return driver.findElements(AppiumBy.image(value));
            }
            case "androiduiautomator" -> {
                return driver.findElements(AppiumBy.androidUIAutomator(value));
            }

            //IOS
            case "iosclasschain" -> {
                return driver.findElements(AppiumBy.iOSClassChain(value));
            }
            case "iosnspredicatestring" -> {
                return driver.findElements(AppiumBy.iOSNsPredicateString(value));
            }
        }

        throw new RuntimeException("The provided locator strategy doesn't exist." +
                "Please use one of the following [id, name, classname, accessibilityid, xpath, image, androiduiautomator, iosclasschain, iosnspredicatestring]");
    }

    private List<WebElement> parseLocators(String locatorStrat, String value) throws UnknownAutomationType {
        switch (automationType){
            case "web", "msite" -> {
                return webDespatchers(locatorStrat, value);
            }
            case "android", "ios" -> {
                return mobileDespatchers(locatorStrat, value);
            }
        }

        throw new UnknownAutomationType("Please provide a valid automation type." +
                "Recognised automation types [web, msite, android, ios]");
    }
}
