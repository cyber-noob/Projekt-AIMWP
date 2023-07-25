package org.aj.application.businessLogics;

import org.aj.application.pages.LoginPage;

public class Login {

    /**
     * Feature class
     * This class denotes the Feature on this particular page
     */
    public class LoginFeature {

        /**
         * BusinessActions class
         * Write all business actions here
         */
        public class actions {

            public void performLogin(String username, String password) throws Exception {
                LoginPage.LoginFeature loginPage = new LoginPage().new LoginFeature();

                loginPage.clickOnUsername()
                        .sendUsername(username)
                        .clickOnPassWord()
                        .sendPassword(password);
            }

            public void performLogut() {

            }
        }

        /**
         * Element integrity class
         * Checks if an element is accessible/ displayed/ clickable/ state changes
         */
        public class checkPresence {

            public boolean isLoginFormPresent() {
                return true;
            }

            public boolean isLoginButtonPresent() {
                return true;
            }
        }
    }

    /**
     * Feature class
     * Signup Feature for Login/Signup page
     */
    class SignupFeature{

        class actions {

            public void performSignup(String username, String firstname, String lastname, String email, String password){

            }
        }

        class checkPresence {

            public boolean isSignUpFormDisplayed() {
                return true;
            }

            public boolean isSignUpButtonDisplayed() {
                return true;
            }
        }
    }
}
