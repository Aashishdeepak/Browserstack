package ProjectLib;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseInitializer 
{
    
    private static final String USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub";

    /**
     * Initializes the driver by fetching parameters from the DriverManager for the
     * current execution thread.
     * @return A fully configured WebDriver instance.
     */
    public WebDriver initializeDriver() 
    {
        Map<String, String> params = DriverManager.getParameters();
        String browser = params.get("browser");
        String os = params.get("os");
        String deviceName = params.get("deviceName");

        WebDriver driver;
        
        if (os == null || os.isEmpty()) 
        {
            System.out.println("Running test locally on: " + browser);
            driver = createLocalDriver(browser);
        } 
        else 
        {
            System.out.println("Running test on BrowserStack: " + browser + " on " + os);
            driver = createRemoteDriver(params);
        }
        
        configureDriver(driver, deviceName);
        return driver;
    }

    private WebDriver createLocalDriver(String browserName) 
    {
        if (browserName.equalsIgnoreCase("firefox")) 
        {
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver();
        } 
        else 
        {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }
    }

    private WebDriver createRemoteDriver(Map<String, String> params) 
    {
        try 
        {
            MutableCapabilities capabilities = new MutableCapabilities();
            HashMap<String, Object> bstackOptions = new HashMap<>();

            bstackOptions.put("os", params.get("os"));
            bstackOptions.put("osVersion", params.get("osVersion"));
            bstackOptions.put("browserName", params.get("browser"));
            bstackOptions.put("buildName", "ElPais Scraping Assignment - Final Run");
            bstackOptions.put("sessionName", "Test on " + params.get("browser") + " on " + params.get("os"));
            
            if (params.get("deviceName") != null && !params.get("deviceName").isEmpty()) 
            {
                bstackOptions.put("deviceName", params.get("deviceName"));
            }

            capabilities.setCapability("bstack:options", bstackOptions);
            return new RemoteWebDriver(new URL(URL), capabilities);
            
        } 
        catch (MalformedURLException e) 
        {
            throw new RuntimeException("Failed to create RemoteWebDriver for BrowserStack due to malformed URL", e);
        }
    }
    
    /**
     * This helper method now accepts the driver as a parameter to ensure
     * configurations are applied to the correct instance.
     */
    private void configureDriver(WebDriver driver, String deviceName) 
    {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(configReader.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(configReader.getPageLoadTimeout()));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(configReader.getScriptTimeout()));
        
        if (deviceName == null || deviceName.isEmpty()) 
        {
            driver.manage().window().maximize();
        }
    }
}
