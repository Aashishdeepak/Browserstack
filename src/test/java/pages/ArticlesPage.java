package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class ArticlesPage 
{

	private static Logger log = LogManager.getLogger(ArticlesPage.class);
	private WebDriver driver;
	protected WebDriverWait wait;
	
	public ArticlesPage(final WebDriver driver) 
	{
		this.driver = driver;
		PageFactory.initElements(new AjaxElementLocatorFactory(driver, 20), this);
		
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

    @FindBy(how = How.XPATH, using = "//h1")
    private WebElement header1Element;
    
    @FindBy(how = How.XPATH, using = "//h2[@class = 'a_st']")
    private WebElement header2Element;

    @FindBy(how = How.XPATH, using = "//div[@data-dtm-region = 'articulo_cuerpo']")
    private WebElement contentContainer;

    @FindBy(how = How.XPATH, using = "//img[@class = '_re  a_m-h']")
    private WebElement coverImage;
    
    @FindBy(how = How.CSS, using = ".a_m_p")
    private List<WebElement> galleryFigcaptions;


    /**
     * Waits for the h1 element to be visible and then returns its text.
     * @return The title of the article as a String.
     */
    public String getArticleTitle(int articleNo, String articleLink) 
    {
        wait.until(ExpectedConditions.visibilityOf(header1Element));
        String header1 = header1Element.getText();
        log.info("Fetched main header for article " + articleNo + ": " + articleLink);
        
        return header1;
    }
    
    /**
     * Waits for the h2 element to be visible and then returns its text.
     * @return The title of the article as a String.
     */
    public String getArticleSubTitle(int articleNo, String articleLink)
    {
    	wait.until(ExpectedConditions.visibilityOf(header2Element));
    	String header2 = header2Element.getText();
    	log.info("Fetched sub header for this article.");
    	
        return header2;
    }

    /**
     * Waits for the content container to be visible, finds all paragraph (<p>) tags
     * within it if available, if not finds all <figurecaptions> tags and joins their text together into a single string.
     * @return The full text content of the article.
     */
    public String getArticleContent(int articleNo, String articleLink) 
    {

        try 
        {
            wait.until(ExpectedConditions.visibilityOf(contentContainer));
            List<WebElement> paragraphs = contentContainer.findElements(By.tagName("p"));
            if (!paragraphs.isEmpty()) 
            {
                String fullContent = paragraphs.stream().map(WebElement::getText).collect(Collectors.joining("\n\n"));
                log.info("Successfully scraped content from standard article layout for this Article.");
                return fullContent;
            }
        } 
        catch (Exception e) 
        {
            log.warn("Standard article content container not found. Checking for photo gallery layout...");
        }

        try 
        {
            if (!galleryFigcaptions.isEmpty()) 
            {
                String galleryContent = galleryFigcaptions.stream().map(WebElement::getText).collect(Collectors.joining("\n\n"));
                log.info("Successfully scraped content from photo gallery layout for this Article.");
                return galleryContent;
            }
        } 
        catch (Exception e) 
        {
            log.error("Could not find content using gallery figcaption strategy either.", e);
        }

        log.error("Unable to find any recognizable content container on this page.");
        return "Content not found.";
    }

    /**
     * Tries to find the cover image and return its source URL.
     * @return The URL of the cover image, or an empty string if not found.
     */
    public String getCoverImageUrl(String articleLink) 
    {
    	try 
    	{
            wait.until(ExpectedConditions.visibilityOf(coverImage));
            String srcSetValue = coverImage.getAttribute("srcset");

            if (srcSetValue != null && !srcSetValue.isEmpty()) 
            {
                String firstEntry = srcSetValue.split(",")[0].trim();
                return firstEntry.split(" ")[0];
            }
            return coverImage.getAttribute("src");
        } 
    	catch (Exception e) 
    	{
            System.out.println("No cover image found for this article.");
            return "";
        }
    }
}
