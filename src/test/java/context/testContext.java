package context;

import org.openqa.selenium.WebDriver;
import ProjectLib.BaseInitializer;

public class testContext 
{

    private WebDriver driver;


    public void setDriver(WebDriver driver) 
    {
        this.driver = driver;
    }
    
    public WebDriver getDriver() 
    {
        return driver;
    }
}
