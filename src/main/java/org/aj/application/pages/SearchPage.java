package org.aj.application.pages;

import org.aj.core.actionsHelper.AppiumWrapper;
import org.aj.core.actionsHelper.locatorStrategy.LocatorFinderHelper;

/**
 * This class will be further flourished if the app under automation is an e-commerce site
 * A typical e-commerce app might have the following features:
 * Search
 * Auto-suggestion
 * Recent search
 * Quick nav banners or links
 */
public class SearchPage {

    String pageName = this.getClass().getSimpleName();

    AppiumWrapper appiumWrapper = new AppiumWrapper();

    public SearchPage() throws Exception {
    }

    /**
     * Contains all inclusive features of core search module
     * 1. Search an item
     * 2. Quick results
     * 3. Recent search if any
     */
    public class SearchBarfeature{

        public SearchPage.SearchBarfeature typeOnSearchBar(String text) throws Exception {
            System.out.println("Typing " + text + " on searchbar");

            //Fetching element from re-usable page
            appiumWrapper.getElement(pageName, "Bar.TopBar.InputTextfield.Search")
                    .sendKeys(text);

            return new SearchPage().new SearchBarfeature();
        }

        public SearchPage hitEnter() throws Exception {
            System.out.println("Clicking enter on search page");

            appiumWrapper.clickEnter();
            return new SearchPage();
        }
    }

    /**
     * Recent Mail search section
     */
    public class RecentSearch{

    }
}
