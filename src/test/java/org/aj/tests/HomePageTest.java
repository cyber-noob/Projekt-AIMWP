package org.aj.tests;

import org.aj.application.businessLogics.HomePage;
import org.aj.application.records.Mail;
import org.aj.core.platformHandlers.Android;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class HomePageTest {

    @Android
    @Test(groups = {"regression"})
    public void areMailsDisplayed() throws Exception {
        HomePage homePage = new HomePage();

        List<Mail> mails = homePage.new Mails().getMails();
        Assert.assertTrue(mails.size() > 0, "No mails are displayed");
    }

    @Android
    @Test(groups = {"regression"})
    public void searchForMail() throws Exception {
        HomePage homePage = new HomePage();

        List<Mail> mails = homePage.new Mails().getMails();

        String searchItem = mails.get(0).snippet().toLowerCase();
        List<Mail> searchResults = homePage.new Search()
                .searchForMail(searchItem)
                .new Mails()
                .getMails();


        Assert.assertTrue(searchResults.stream().anyMatch(mail -> (
                        mail.sender().contains(searchItem) ||
                                mail.subject().contains(searchItem) ||
                                mail.snippet().contains(searchItem)
                        )), "Search resuts doesn't contain relevant mail.\nExpected: " + searchItem + "\nFound: " + searchResults);
    }
}
