package ProjectLib;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TranslationUtil 
{
	private static final String API_URL_TEMPLATE = "https://api.mymemory.translated.net/get?q=%s&langpair=%s|%s&de=%s";
    private static final String FAKE_EMAIL = "test@example.com";

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
    
    
    /**
     * @Description: Translates a list of texts using the RapidAPI service.
     * @Note: This specific API translates one piece of text per request, so this method will loop through the list and make multiple API calls.
     * @param texts The list of strings to be translated.
     * @param sourceLangCode The source language code (e.g., "es").
     * @param targetLangCode The target language code (e.g., "en").
     * @return A list of translated strings.
     **/
    public static List<String> translateTexts(List<String> texts, String sourceLangCode, String targetLangCode) 
    {
        System.out.println("--- LIVE TRANSLATION (from " + sourceLangCode + " to " + targetLangCode + " via MyMemory API) ---");
        
        List<String> translatedTexts = new ArrayList<>();
        
        for (String text : texts) 
        {
            String translated = translateSingleText(text, sourceLangCode, targetLangCode);
            translatedTexts.add(translated);
        }
        return translatedTexts;
    }

    /**
     * @Description: This is a helper method to translate a single piece of text.
     * 
     * @param textToTranslate - the text to translate
     * @param from - the original text language
     * @param to - the language of translation
     * @return a single translated String
     */
    private static String translateSingleText(String textToTranslate, String from, String to) 
    {
        try 
        {
            String encodedText = URLEncoder.encode(textToTranslate, StandardCharsets.UTF_8.toString());
            String url = String.format(API_URL_TEMPLATE, encodedText, from, to, FAKE_EMAIL);
            Request request = new Request.Builder().url(url).get().build();

            try (Response response = client.newCall(request).execute())
            {
                if (!response.isSuccessful()) 
                {
                    System.err.println("API request failed for text '" + textToTranslate + "' with code: " + response);
                    return textToTranslate + " (Translation Failed)";
                }

                String responseBody = response.body().string();
                JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
                JsonObject responseData = jsonObject.getAsJsonObject("responseData");
                return responseData.get("translatedText").getAsString();
            }
        }
        catch (Exception e) 
        {
            System.err.println("Failed to translate text: '" + textToTranslate + "'. Error: " + e.getMessage());
            return textToTranslate + " (Translation Failed)"; 
        }
    }
    
}
