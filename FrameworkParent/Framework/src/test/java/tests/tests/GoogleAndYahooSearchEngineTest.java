package tests.tests;

import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import pageFactory.google.GoogleSearchPage;
import pageFactory.yahoo.YahooSearchPage;
import tests.Base;

import java.util.*;

public class
GoogleAndYahooSearchEngineTest extends Base {
    GoogleSearchPage googleSearchPage;
    YahooSearchPage yahooSearchPage;

    List<Map<String, String>> googleResults = new ArrayList<>();
    List<Map<String, String>> yahooResults = new ArrayList<>();

    @Test(priority = 1)
    public void verifyGoogleSearchPage(){
        String googleUrl = "https://www.google.com/";
        driver.get(googleUrl);
        commonOperations.waitUntilPageLoaded(driver);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, googleUrl);
    }

    @Test(priority = 2)
    public void searchUsingKeywordAndVerifyGoogle(){
        googleSearchPage= PageFactory.initElements(driver,GoogleSearchPage.class);
        googleResults = googleSearchPage
                .searchUsingGoogle("Information Technology");// Assertion validate in main page
    }

    @Test(priority = 3)
    public void verifyYahooSearchPage(){
        driver.manage().deleteAllCookies();
        String yahooUrl = "https://www.yahoo.com/";
        driver.get(yahooUrl);
        commonOperations.waitUntilPageLoaded(driver);
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, yahooUrl);
    }

    @Test(priority = 4)
    public void searchUsingKeywordAndVerifyYahoo(){
        yahooSearchPage= PageFactory.initElements(driver, YahooSearchPage.class);
        yahooResults = yahooSearchPage.searchUsingYahoo("Information Technology");
    }

    @Test(priority = 5)
    public void compareResults(){
        Set<String> similarLinks = new HashSet<>();
        googleResults.forEach(gr->{
            String googleResultLink = gr.get("link");

            yahooResults.forEach(yr->{
                String yahooResultLink = yr.get("link");
                if(googleResultLink.equals(yahooResultLink)){
                    similarLinks.add(yahooResultLink);

                }
            });
        });

        similarLinks.forEach(link->{
            System.out.println("Common Search results : " + link);
        });
    }
}
