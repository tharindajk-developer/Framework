package pageFactory.google;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

public class GoogleSearchPage {
    public WebDriver driver;
    private CommonOperations commonOperations;

    public GoogleSearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        commonOperations = new CommonOperations(null);
    }

    @FindBy(name = GoogleXpathContents.CHROME_SEARCH_INPUT)
    private WebElement chromeSearchInput;

    @FindBy(xpath = GoogleXpathContents.CHROME_SEARCH_RESULTS)
    private WebElement googleSearchResults;

    public List<Map<String, String>> searchUsingGoogle(String searchKeys) {
        chromeSearchInput.clear();
        chromeSearchInput.sendKeys(searchKeys);
        chromeSearchInput.sendKeys(Keys.ENTER);
        commonOperations.waitUntilPageLoaded(driver);
        commonOperations.waitForAnElementDisplayed(driver,googleSearchResults,10,10000);
        return getAllDetails(searchKeys.toLowerCase());
    }

    public List<Map<String, String>> getAllDetails(String searchKeys){
        List<WebElement> resultEntries = driver.findElements(By.xpath("//div[@class='jtfYYd']//a"));
        List<Map<String, String>> finalResult = new ArrayList<>();
        for(WebElement element : resultEntries){
            Map<String, String> resultMap = new HashMap<>();
            String href = element.getAttribute("href");
            String title = element.getText();
            if(title.toLowerCase().contains(searchKeys)){
                resultMap.put("title",title);
                resultMap.put("link",href);
                System.out.println("Results Contains Search term :");
                System.out.println("title : "+title);
                System.out.println("link : "+href);
            }else {
                System.out.println("Results doesn't contains Search term :");
                resultMap.put("titleX", title);
                resultMap.put("linkX", href);
                System.out.println("title : "+ title);
                System.out.println("link : "+ href);
            }
            System.out.println("-------------------------------------------------------------------------------");
            finalResult.add(resultMap);
        }
        return finalResult;
    }

    public static List<Map<String, String>> parseLinks(Document doc){
        List<Map<String, String>> result = new ArrayList<>();
        Elements results = doc.select("a > h3");
        for (Element link : results) {
            Map<String, String> resultMap = new HashMap<>();
            String title = link.text().split("\\|")[0];
            Elements parent = link.parent().getAllElements();
            String relHref = parent.attr("href");
            if (relHref.startsWith("/url?esrc=")) {
                relHref = relHref.replace("/url?esrc=s&q=&rct=j&sa=U&url=", "");
            }
            String[] splittedString = relHref.split("&ved=");
            if (splittedString.length > 1) {
                relHref = splittedString[0];
            }

            if(title.toLowerCase().contains("technology")){
                resultMap.put("title", title);
                resultMap.put("link", relHref);
                System.out.println("title : " + resultMap.get("title"));
                System.out.println("link : " + resultMap.get("link"));
            }else{
                resultMap.put("titleX", title);
                resultMap.put("linkX", relHref);
                System.out.println("titleX : " + resultMap.get("titleX"));
                System.out.println("linkX : " + resultMap.get("linkX"));
            }
            result.add(resultMap);
        }
        return result;
    }

}