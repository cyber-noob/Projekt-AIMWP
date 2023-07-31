package org.aj.tests;

import org.aj.application.records.Mail;
import org.aj.core.platformHandlers.Android;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class HomePage {

    @Android
    @Test(groups = {"regression"})
    public void areMailsDisplayed() throws Exception {
        org.aj.application.businessLogics.HomePage homePage = new org.aj.application.businessLogics.HomePage();

        List<Mail> mails = homePage.new Mails().getMails();
        Assert.assertTrue(mails.size() > 0, "No mails are displayed");
    }

    @Android
    @Test(groups = {"regression"})
    public void searchForMail() throws Exception {
        org.aj.application.businessLogics.HomePage homePage = new org.aj.application.businessLogics.HomePage();

        List<Mail> mails = homePage.new Mails().getMails();

        String searchItem = mails.get(0).snippet().toLowerCase();
        List<Mail> searchResults = homePage.new Search()
                .searchForMail(searchItem)
                .new Mails()
                .getMails();


        Assert.assertTrue(searchResults.stream().anyMatch(mail -> (
                        mail.sender().toLowerCase().contains(searchItem) ||
                                mail.subject().toLowerCase().contains(searchItem) ||
                                mail.snippet().toLowerCase().contains(searchItem)
                        )), "Search resuts doesn't contain relevant mail.\nExpected: " + searchItem + "\nFound: " + searchResults);
    }
}
