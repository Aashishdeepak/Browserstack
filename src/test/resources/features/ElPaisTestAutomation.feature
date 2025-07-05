#Author: inddranil.kargupta@outlook.com

@ElPaisWebAutomation
Feature: El Pais Website - Article Scraping and Processing

  Background:
    Given the user is on the ElPais homepage

  @TC_001 @WebScrapping @TxtProcessing
  Scenario: Scrape and verify articles from the Opinion section
    When the user navigates to the Opinion section
    And the user fetches the first 5 articles
    Then the user scrapes the data from each fetched article link
    Then the article titles and content are displayed
    Then the cover image for each article is downloaded
    When the user translates the article titles to English
    Then the translated titles are displayed
    And any word repeated more than twice in the translated titles is identified and counted