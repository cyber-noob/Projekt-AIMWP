package org.aj.application.pages;

import org.aj.core.actionsHelper.AppiumWrapper;

public class LoginPage {

    AppiumWrapper appiumWrapper = new AppiumWrapper();

    public LoginPage() throws Exception {
    }

    /**
     * Feature class
     * This class denotes the Feature on this particular page
     */
    public class LoginFeature {

        public LoginFeature clickOnUsername() {
            return new LoginFeature();
        }

        public LoginFeature sendUsername(String username) {
           appiumWrapper.getElement("FormComponents.InputTextField.username")
                   .sendKeys(username);

           return new LoginFeature();
        }

        public LoginFeature clickOnPassWord() {
            return new LoginFeature();
        }

        public LoginFeature sendPassword(String password) {
            appiumWrapper.getElement("FormComponents.InputTextField.password")
                    .sendKeys(password);

            return new LoginFeature();
        }

        public LoginFeature clickLoginButton() {
            appiumWrapper.getElement("Buttons.login")
                    .click();

            return new LoginFeature();
        }
    }

    class SignupFeature {

        public LoginPage clickOnFirstName() throws Exception {
            return new LoginPage();
        }

        public LoginPage clickOnLastName() throws Exception {
            return new LoginPage();
        }

        public LoginPage clickOnSignupButton() throws Exception {
            return new LoginPage();
        }
    }
}
