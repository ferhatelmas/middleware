package middleware.common;


import java.util.Date;



/**
 *
 * @author Kenny Lienhard
 */
public class MSTweet {
    
    private String text;
    private String url;
    private Date createdAt;
    
    public MSTweet() {
        
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
