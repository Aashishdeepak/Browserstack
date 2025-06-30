package pages;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
//import ProjectLib.packageUtils;

public class OpinionPage 
{
	private static final Logger log = LogManager.getLogger(OpinionPage.class);
    private WebDriver driver;
	
    public OpinionPage(final WebDriver driver) 
    {
        this.driver = driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
    }
	
	@FindBy(how = How.XPATH, using = "//section[contains(@class, 'b-d')]//article")
    private List<WebElement> articleList;
	
	
	public List<String> getFirstNArticleLinks(int numberOfArticles) 
	{
        log.info("Found " + articleList.size() + " potential article blocks on the page.");
        
        List<String> fetchedLinks = new ArrayList<>();
        
        for (WebElement articleElement : articleList) 
        {
            if (fetchedLinks.size() >= numberOfArticles) 
            {
                break;
            }
            
            String link = findLinkInArticle(articleElement);
            
            if (link != null && !link.isEmpty() && link.matches(".*\\d{4}.*")) 
            {
                fetchedLinks.add(link);
            }
        }
        
        log.info("Successfully filtered and fetched " + fetchedLinks.size() + " valid article links.");
        return fetchedLinks;
    }
	
	private String findLinkInArticle(WebElement articleElement) 
	{
        try
        {
            return articleElement.findElement(By.xpath(".//h2/a")).getAttribute("href");
        } 
        catch (Exception e) 
        {
        	
        }

        try 
        {
            return articleElement.findElement(By.xpath(".//figure/a")).getAttribute("href");
        } 
        catch (Exception e) 
        {
            
        }
        
        log.warn("Could not find a valid h2 or figure link for one of the article blocks.");
        return null;
	}

}











