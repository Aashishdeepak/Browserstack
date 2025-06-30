package pages;

import java.time.Duration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import ProjectLib.packageUtils;

import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class HomePage 
{
	private static Logger log = LogManager.getLogger(HomePage.class);
	private WebDriver driver;
	protected WebDriverWait wait;
	
	public HomePage(final WebDriver driver) 
	{
		this.driver = driver;
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(80));
	}
	
	@FindBy(how = How.ID, using = "didomi-notice-agree-button")
    private WebElement cookieAgreeButton;
	
	@FindBy(how = How.XPATH, using = "//div[@class = 'sm _df']/a[normalize-space() = 'Opinión']")
	private WebElement Opinion;
	
	
	public void acceptCookies() 
	{
		packageUtils.waitForPageToBeFullyLoaded(driver);
        try 
        {
            wait.until(ExpectedConditions.elementToBeClickable(cookieAgreeButton)).click();
            System.out.println("Clicked on the cookie consent button.");
            packageUtils.waitForSpecificTime(5); 
        } 
        catch (Exception e) 
        {
            System.out.println("Cookie consent button not found or not clickable. Continuing...");
        }
    }
	
	
	public void navigateToOpinionsSection()
	{	
		wait.until(ExpectedConditions.elementToBeClickable(Opinion));
		
		JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", Opinion);
        if(wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete")))
		{
			log.info("Navigated to the Opinion section.");
		}
	}
}
