package stepDefinitions;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import ProjectLib.TranslationUtil;
import ProjectLib.packageUtils;
import context.testContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ArticlesPage;
import pages.HomePage;
import pages.OpinionPage;
import pojos.Article;

public class ElPaisSteps 
{

    private static Logger log = LogManager.getLogger(ElPaisSteps.class);

    private WebDriver driver;
    
    // Page Objects
    private HomePage homePage;
    private OpinionPage opinionPage;
    
    // State-holding variables to pass data between steps
    private List<String> articleLinks;
    private List<Article> scrapedArticles;
    private List<String> translatedTitles;

    public ElPaisSteps(testContext context) 
    {
        this.driver = context.getDriver();
        this.homePage = new HomePage(driver);
        this.scrapedArticles = new ArrayList<>();
        this.opinionPage = new OpinionPage(driver);
    }

    @Given("the user is on the ElPais homepage")
    public void the_user_is_on_the_application_homepage() 
    {
        String pageTitle = driver.getTitle();
        log.info("Verifying homepage title. Actual title is: '" + pageTitle + "'");
        Assert.assertTrue(pageTitle.contains("EL PAÍS"), "User is not on the El País homepage.");
    }

    @When("the user navigates to the Opinion section")
    public void the_user_navigates_to_the_opinion_page() 
    {
        homePage.acceptCookies();
        packageUtils.waitForPageToBeFullyLoaded(driver);
        homePage.navigateToOpinionsSection();
    }
    
    @When("the user fetches the first {int} articles")
    public void the_user_fetches_the_first_five_articles(int noOfArticles) 
    {
        log.info("Fetching links for the first " + noOfArticles + " articles.");
        //Assert.assertNotNull(opinionPage, "OpinionPage object is not initialized. Did navigation fail?");
        articleLinks = opinionPage.getFirstNArticleLinks(noOfArticles);
        Assert.assertFalse(articleLinks.isEmpty(), "No article links were found on the Opinion page.");
        
        System.out.println("--- Fetched Article Links ---");
        int counter = 1;
        for (String link : articleLinks) 
        {
            System.out.println("Link " + counter + ": " + link);
            counter++;
        }
    }

    @When("the user scrapes the data from each fetched article link")
    public void the_user_scrapes_the_data_from_each_fetched_article_link() 
    {
        log.info("\nScraping data for " + articleLinks.size() + " links.");

        Assert.assertNotNull(articleLinks, "Article links list is null. Cannot scrape.");
        if (packageUtils.waitForPageToBeFullyLoaded(driver))
        {
        	int i = 1;
        	for (String link : articleLinks) 
        	{
        		driver.get(link);
        		ArticlesPage articlePage = new ArticlesPage(driver);
            
        		String title = articlePage.getArticleTitle(i, link);
        		String subTitle = articlePage.getArticleSubTitle(i, link);
        		String content = articlePage.getArticleContent(i, link);
        		String imageUrl = articlePage.getCoverImageUrl(link);
            
        		i ++;
        		scrapedArticles.add(new Article(title, subTitle, content, imageUrl));
        		log.info("Scraped data for article: " + title);
        	}
        }
    }

    @Then("the article titles and content are displayed")
    public void the_article_titles_and_content_are_displayed() 
    {
        log.info("\n--- DISPLAYING SCRAPED ARTICLE DATA ---");
        
        Assert.assertFalse(scrapedArticles.isEmpty(), "No articles were scraped, so nothing to display.");
        for (int i = 0; i < scrapedArticles.size(); i++) 
        {
            Article article = scrapedArticles.get(i);
            System.out.println("\n--- ARTICLE " + (i + 1) + " ---");
            System.out.println("Title: " + article.getTitle());
            System.out.println("Subtitle: " + article.getSubTitle());
            System.out.println("Content: \n" + article.getContent());
        }
    }

    @Then("the cover image for each article is downloaded")
    public void the_cover_image_for_each_article_is_downloaded() 
    {
    	log.info("\n--- DOWNLOADING IMAGES ---");
        Assert.assertFalse(scrapedArticles.isEmpty(), "No articles available to download images for.");
        new File("images").mkdirs();

        for (int i = 0; i < scrapedArticles.size(); i++) 
        {
            Article article = scrapedArticles.get(i);
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) 
            {
                try 
                {
                    String fileName = "images/article_" + (i + 1) + ".jpg";
                    InputStream in = new URI(article.getImageUrl()).toURL().openStream();
                    Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
                    log.info("Successfully downloaded image to: " + fileName);
                } 
                catch (Exception e) 
                {
                    log.error("Failed during image download process for article '" + article.getTitle() + "'", e);
                }
            } 
            else 
            {
                log.warn("Skipping image download for article '" + article.getTitle() + "' (no image URL found).");
            }
        }
    }
    
    @When("the user translates the article titles to English")
    public void the_user_translates_the_article_titles_to_english() 
    {
    	log.info("\n--- TRANSLATING ARTICLE TITLES ---");
        try 
        {
            Assert.assertFalse(scrapedArticles.isEmpty(), "Cannot translate titles because no articles were scraped.");
            
            List<String> originalTitles = scrapedArticles.stream().map(Article::getTitle).collect(Collectors.toList());
            
            translatedTitles = TranslationUtil.translateTexts(originalTitles, "es", "en");
            
            Assert.assertEquals(originalTitles.size(), translatedTitles.size(), "Translation did not return the same number of titles.");
            log.info("Successfully translated " + translatedTitles.size() + " titles.");

        } 
        catch (Exception e) 
        {
            log.error("Failed during title translation process.", e);
            Assert.fail("Failed during title translation process.", e);
        }
    }
    
    @Then("the translated titles are displayed")
    public void the_translated_titles_are_displayed() 
    {
    	log.info("\n--- DISPLAYING TRANSLATED TITLES ---");
        Assert.assertNotNull(translatedTitles, "Translated titles list is null.");
        Assert.assertFalse(translatedTitles.isEmpty(), "No titles were translated to display.");

        System.out.println("\n--- TRANSLATED TITLES (ENGLISH) ---");
        for (int i = 0; i < translatedTitles.size(); i++) 
        {
            System.out.println("Original: " + scrapedArticles.get(i).getTitle());
            System.out.println("Translated: " + translatedTitles.get(i) + "\n");
        }
    }
    
    @Then("any word repeated more than twice in the translated titles is identified and counted")
    public void any_word_repeated_more_than_twice_is_identified_and_counted() 
    {
    	log.info("\n--- ANALYZING TRANSLATED TITLES FOR REPEATED WORDS ---");
        Assert.assertNotNull(translatedTitles, "Cannot analyze titles because the list is null.");
        Assert.assertFalse(translatedTitles.isEmpty(), "Cannot analyze titles because the list is empty.");

        Map<String, Long> repeatedWords = packageUtils.analyzeAndCountRepeatedWords(translatedTitles, 2);
        
        System.out.println("\n--- WORD FREQUENCY ANALYSIS ---");
        if (repeatedWords.isEmpty()) 
        {
            System.out.println("No words were repeated more than twice across all titles.");
            log.info("Analysis complete: No words found with a frequency > 2.");
        } 
        else 
        {
            System.out.println("Words repeated more than twice:");
            repeatedWords.forEach((word, count) -> {System.out.printf("- '%s': found %d times%n", word, count);
            log.info(String.format("Repeated word found: '%s' (Count: %d)", word, count));});
        }
    }
}
