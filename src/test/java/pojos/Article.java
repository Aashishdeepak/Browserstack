package pojos;

public class Article 
{
    private String title;
    private String subTitle;
    private String content;
    private String imageUrl;

    public Article(String title, String subTitle, String content, String imageUrl) 
    {
        this.title = title;
        this.content = content;
        this.subTitle = subTitle;
        this.imageUrl = imageUrl;
    }

    public String getTitle() 
    {
        return title;
    }

    public void setTitle(String title) 
    {
        this.title = title;
    }
    
    public String getSubTitle() 
    {
        return subTitle;
    }

    public void setSubTitle(String title) 
    {
        this.title = subTitle;
    }

    public String getContent() 
    {
        return content;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getImageUrl() 
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) 
    {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() 
    {
        return "Article{" + "title='" + title + '\'' + '}';
    }
}
