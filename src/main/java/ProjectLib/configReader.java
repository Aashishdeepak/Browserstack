package ProjectLib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class configReader 
{

    private static Properties prop;

    static 
    {
        prop = new Properties();
        try 
        {
            // Path to the configuration file.
            FileInputStream ip = new FileInputStream("src/main/resources/config/config.properties");
            prop.load(ip);
        } 
        catch (FileNotFoundException e) 
        {
            System.err.println("Configuration file not found at src/main/resources/config/config.properties");
            e.printStackTrace();
        } 
        catch (IOException e) 
        {
            System.err.println("Error loading the configuration file.");
            e.printStackTrace();
        }
    }

    /**
     * Gets the browser type from the config file.
     * @return The browser name as a String.
     */
    public static String getBrowser() 
    {
        return prop.getProperty("browser");
    }

    /**
     * Gets the application URL from the config file.
     * @return The application URL as a String.
     */
    public static String getUrl() 
    {
        return prop.getProperty("url");
    }

    /**
     * Gets the implicit wait timeout value.
     * @return The timeout value as an Integer.
     */
    public static int getImplicitWait() 
    {
        return Integer.parseInt(prop.getProperty("implicit.wait"));
    }
    
    /**
     * Gets the page load timeout value.
     * @return The timeout value as an Integer.
     */
    public static int getPageLoadTimeout() 
    {
        return Integer.parseInt(prop.getProperty("page.load.timeout"));
    }
    
    /**
     * Gets the script timeout value.
     * @return The timeout value as an Integer.
     */
    public static int getScriptTimeout() 
    {
        return Integer.parseInt(prop.getProperty("script.timeout"));
    }
    
    /**
     * Gets the rapid API Key.
     * @return The apiKey.
     */
    public static String getRapidApiKey() 
    {
        String apiKey = prop.getProperty("rapid.api.key");
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_API_KEY_HERE")) 
        {
            throw new RuntimeException("RapidAPI key is not configured in config.properties.");
        }
        return apiKey;
    }
    
}
