package ProjectLib;

import java.io.File;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class packageUtils 
{
	private static Logger log = LogManager.getLogger(packageUtils.class);
		
	public static boolean waitForPageToBeFullyLoaded(WebDriver driver) 
	{
        try 
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(80));
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            return true;
        } 
        catch (Exception e) 
        {
            System.err.println("Timeout waiting for page to be fully loaded. Error: " + e.getMessage());
            return false;
        }
    }

	public static void waitForSpecificTime(double numOfSeconds) 
	{
		try 
		{
			Thread.sleep((long) (numOfSeconds * 1000));
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
			Thread.currentThread().interrupt();
			log.info("Exception caught while waiting for {} seconds", numOfSeconds);
		}
		
	}
	
	public static void cleanDirectory(String directoryPath) 
	{
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) 
        {
            System.out.println("Cleaning directory: " + directoryPath);
            File[] files = directory.listFiles();
            if (files != null) 
            {
                for (File file : files) 
                {
                    if (file.delete()) 
                    {
                        System.out.println("Deleted file: " + file.getName());
                    } 
                    else 
                    {
                        System.err.println("Failed to delete file: " + file.getName());
                    }
                }
            }
        } 
        else 
        {
            System.out.println("Directory not found, skipping cleanup: " + directoryPath);
        }
	}
	
	public static Map<String, Long> analyzeAndCountRepeatedWords(List<String> texts, int repeatThreshold) 
	{
        Map<String, Long> wordCounts = texts.stream().flatMap(title -> Arrays.stream(title.toLowerCase().split("\\W+"))) 
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        
        return wordCounts.entrySet().stream().filter(entry -> entry.getValue() > repeatThreshold).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
