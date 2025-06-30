package Runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ProjectLib.DriverManager;

@Test
@CucumberOptions(features = "src/test/resources/features", glue = {"stepDefinitions", "Hooks"}, 
				 plugin = {"pretty",
				 		   "html:target/cucumber-reports/cucumber-pretty.html",
				 		   "json:target/cucumber-reports/Cucumber.json",
						   "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
				 monochrome = true,
				 tags = "")


public class BaseRunner extends AbstractTestNGCucumberTests
{
	@BeforeTest(alwaysRun = true)
    @Parameters({"browser", "os", "osVersion", "deviceName"})
    public void setupTestParameters
    (
            @Optional("chrome") String browser, 
            @Optional("") String os, 
            @Optional("") String osVersion, 
            @Optional("") String deviceName) 
	{
        
        Map<String, String> params = new HashMap<>();
        params.put("browser", browser);
        params.put("os", os);
        params.put("osVersion", osVersion);
        params.put("deviceName", deviceName);
        DriverManager.setParameters(params);
    }
	
	@DataProvider(parallel = true)
	public Object[][] scenario()
	{
		return super.scenarios();
		
	}
}
