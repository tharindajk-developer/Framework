package pageFactory.yahoo;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.CommonOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YahooSearchPage {
    public WebDriver driver;
    private CommonOperations commonOperations;

    public YahooSearchPage(WebDriver driver){
        this.driver = driver;
        PageFactory.initElements(driver,this);
        commonOperations = new CommonOperations(null);
    }

    @FindBy(name = YahooXpathContents.YAHOO_SEARCH_INPUT)
    private WebElement yahooSearchInput;

    @FindBy(xpath = YahooXpathContents.YAHOO_SEARCH_RESULTS_PAGINATION)
    private WebElement yahooSearchResultsPagination;

    public List<Map<String, String>> searchUsingYahoo(String searchKeys){
        yahooSearchInput.clear();
        yahooSearchInput.sendKeys(searchKeys);
        yahooSearchInput.sendKeys(Keys.ENTER);
        commonOperations.waitUntilPageLoaded(driver);
        commonOperations.waitForAnElementDisplayed(driver,yahooSearchResultsPagination,10,10000);
        return getAllDetails(searchKeys.toLowerCase());
    }

    public List<Map<String, String>> getAllDetails(String searchKeys){
        List<WebElement> resultEntries = driver.findElements(By.xpath("//h3[@class='title']/a"));
        List<Map<String, String>> finalResult = new ArrayList<>();
        for(WebElement element : resultEntries){
            Map<String, String> resultMap = new HashMap<>();
            String href = element.getAttribute("href");
            String title = element.getText();

            if(title.toLowerCase().contains(searchKeys)){
                resultMap.put("title",title);
                resultMap.put("link",href);
                System.out.println("Results Contains Search term :");
                System.out.println("title : "+ title);
                System.out.println("href : "+ href);
            }else {
                System.out.println("Results doesn't contains Search term :");
                resultMap.put("titleX", title);
                resultMap.put("linkX", href);
                System.out.println("title : "+ title);
                System.out.println("href : "+ href);
            }
            System.out.println("-------------------------------------------------------------------------------");
            finalResult.add(resultMap);
        }
        return finalResult;
    }

}
