package tests.tests.dummy;

import org.testng.Assert;
import org.testng.annotations.Test;
import tests.Base;

public class DummyTest extends Base {
    @Test(priority = 1)
    public void verifyGoogleSearchPage(){
        String googleUrl = "https://www.google.com/"; // please add page URL
        driver.get(googleUrl);
        commonOperations.waitUntilPageLoaded(driver);
        Assert.assertEquals("Expected URL", googleUrl);
    }
}
