package Hooks;

import ProjectLib.BaseInitializer;
import ProjectLib.configReader;
import ProjectLib.packageUtils;
import context.testContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppHooks 
{

    private testContext tc;
    private WebDriver driver;
    private static Logger logger = LogManager.getLogger(AppHooks.class);
    private Scenario sc;
    
    public AppHooks(testContext context) 
    {
        this.tc = context;
    }

    /**
     * This method runs before each Cucumber scenario.
     * It gets the driver from the TestContext and navigates to the URL.
     */
    @Before
    public void setup(Scenario scenario) 
    {
        packageUtils.cleanDirectory("images");
        
        this.sc = scenario;
        BaseInitializer base = new BaseInitializer();
        driver = base.initializeDriver(); 
        
        tc.setDriver(driver); 
        
        String url = configReader.getUrl();
        driver.get(url);
        
        System.out.println("Browser launched for scenario: " + scenario.getName());
    }

    /**
     * This method runs after each Cucumber scenario.
     * It ensures that the WebDriver is properly closed.
     */
    @After
    public void quitBrowser() 
    {
        if (driver != null) 
        {
            driver.quit();
            System.out.println("Browser closed successfully.");
        }
    }
    
    /**
	 * Function Name: takeScreenshot 
	 * @Description: This function is used to take a Screenshot and attach to report
	 */
    public void takeScreenshot() 
    {
		final String screenshotName = sc.getName().replaceAll(" ", "");
		final byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		sc.attach(sourcePath, "image/png", screenshotName);
		logger.info("Screen captured and added to report...");
	}

	/**
	 * Function Name: writeToReport 
	 * @Description: This function is used to write custom message on extent html report
	 */
	public void writeToReport(final String msg) 
	{
		ExtentCucumberAdapter.addTestStepLog(msg);
	}
	
	/**
	 * Function Name: ReportFail 
	 * @Description: This function is used to report failure on a test
	 */
	public void ReportFail(final String msg) 
	{
		ExtentCucumberAdapter.addTestStepLog(msg);
		logger.info(msg);
		Assert.fail(msg);
		takeScreenshot();
	}
	
	/**
	 * Function Name: ReportPass 
	 * @Description: This function is used to report pass on a test
	 */
	public void ReportPass(final String msg) 
	{
		ExtentCucumberAdapter.addTestStepLog(msg);
		logger.info(msg);
		takeScreenshot();
	}
}
