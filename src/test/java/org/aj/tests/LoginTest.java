package org.aj.tests;

import org.aj.application.businessLogics.Login;
import org.testng.annotations.Test;

public class LoginTest {

    @Test(groups = {"regression"})
    public void doLogin() throws Exception {
        new Login().new LoginFeature().new actions().performLogin("", "");
    }

    @Test(groups = {"regression"})
    public void doLogout() throws Exception {
        new Login().new LoginFeature().new actions().performLogin("", "");
    }
}
