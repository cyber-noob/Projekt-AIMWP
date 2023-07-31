package org.aj.application.pages;

import org.aj.application.records.Mail;
import org.aj.core.actionsHelper.AppiumWrapper;
import org.aj.core.actionsHelper.locatorStrategy.LocatorFinderHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class HomePage {

    AppiumWrapper appiumWrapper = new AppiumWrapper();

    String pageName = this.getClass().getSimpleName();

    public HomePage() throws Exception {
    }

    /**
     * Search Feature
     */
    public class Search{

        public SearchPage clickOnSearchBar() throws Exception {
            System.out.println("Clicking on search button");
            appiumWrapper.getElement("Components", "Bar.TopBar.InputTextfield.Search")
                    .click();

            return new SearchPage();
        }
    }

    /**
     * MailBucket Quick switch Feature on cards view
     */

    public class MailBuckets{

        public HomePage clickOnMailBucket(String type) throws Exception {
            System.out.println("clicking on mail bucket -> " + type);
            List<WebElement> parentCards = appiumWrapper.getElements(pageName, "Cards.MailBoxes.ParentComponent");

            for (WebElement parentCard : parentCards) {
               WebElement boxType = parentCard.findElement(By.id("com.google.android.gm:id/teaser_row_1"));

               if(boxType.getText().equalsIgnoreCase(type)) {
                   boxType.click();
                   return new HomePage();
               }
            }

            throw new Exception("Quick Mail box switcher with provided spec was not found");
        }
    }

    /**
     * Mails Feature
     */

    public class Mails{

        public List<Mail> getAllMails() throws Exception {
            System.out.println("Fetching all mails");
            List<Mail> result = new ArrayList<>();

            List<WebElement> parentElements = appiumWrapper.getElements(pageName, "Cards.Mails.ParentComponent");

            for (WebElement parentElement : parentElements) {
                try {
                    String sender = parentElement.findElement(By.id("com.google.android.gm:id/senders")).getText();
                    String subject = parentElement.findElement(By.id("com.google.android.gm:id/subject")).getText();
                    String snippet = parentElement.findElement(By.id("com.google.android.gm:id/snippet")).getText();
                    String date = parentElement.findElement(By.id("com.google.android.gm:id/date")).getText();

                    result.add(new Mail(sender, subject, snippet, date));
                }catch (Exception e){
                    System.out.println("Retrying with another element...");
                }
            }

            return result;
        }

        /**
         * Tries to star a mail containing the following data on either sender, subject or sender
         * @param data
         * @return
         * @throws Exception
         */
        public HomePage starMail(String data) throws Exception {
            System.out.println("searching for mail containing data: " + data);

            List<WebElement> parentElements = appiumWrapper.getElements(pageName, "Cards.Mails.ParentComponent");
            for (WebElement parentElement : parentElements) {
                String sender = parentElement.findElement(By.id("com.google.android.gm:id/senders")).getText();
                String subject = parentElement.findElement(By.id("com.google.android.gm:id/subject")).getText();
                String snippet = parentElement.findElement(By.id("com.google.android.gm:id/snippet")).getText();
                String date = parentElement.findElement(By.id("com.google.android.gm:id/date")).getText();

                if(sender.contains(data) || subject.contains(data) || snippet.contains(data)){
                    parentElement.findElement(By.id("com.google.android.gm:id/star"));
                    return new HomePage();
                }
            }

            throw new Exception("Mail not found for provided data");
        }
    }
}
