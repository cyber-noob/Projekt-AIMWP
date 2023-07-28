package org.aj.application.businessLogics;

import org.aj.application.records.Mail;

import java.util.List;

/**
 * Please add business logic actions here
 */
public class HomePage {

    public class Search{

        org.aj.application.pages.HomePage.Search entryPoint = new org.aj.application.pages.HomePage().new Search();

        public Search() throws Exception {
        }

        public HomePage searchForMail(String data) throws Exception {
            entryPoint.clickOnSearchBar()
                    .new SearchBarfeature()
                    .typeOnSearchBar(data)
                    .hitEnter();

            return new HomePage();
        }
    }

    public class Mails{

        public List<Mail> getMails() throws Exception{
            return new org.aj.application.pages.HomePage().new Mails().getAllMails();
        }

        public HomePage starAMail(String data) throws Exception {
            new org.aj.application.pages.HomePage().new Mails().starMail(data);

            return new HomePage();
        }
    }
}
