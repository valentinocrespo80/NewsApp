package com.example.android.newsapp;

/**
 * A News object contains information about Sonos articles topic.
 */

public class News {

    /**
     * The title of the article about Sonos.
     */
    private String mArticleTitle;

    /**
     * The authors name.
     */
    private String mAuthorName;


    /**
     * The website URL of the Sonos articles.
     */
    private String mArticleUrl;


    private long mTimeInMilliseconds;


    /**
     * Constructs a News object.
     *
     * @param articleTitle is the title of the Sonos article
     * @param authorName   is the name of the Author
     * @param articleUrl   is the website URL to find more info on Sonos
     */


    public News(String articleTitle, String authorName, String articleUrl, long timeInMilliseconds) {
        mArticleTitle = articleTitle;
        mAuthorName = authorName;
        mArticleUrl = articleUrl;
        mTimeInMilliseconds = timeInMilliseconds;

    }


    /**
     * @return the article title
     */
    public String getarticleTitle() {
        return mArticleTitle;
    }


    /**
     * @return the author name
     */
    public String getauthorName() {
        return mAuthorName;
    }


    /**
     * @return the URL to find more information on Sonos
     */
    public String getarticleUrl() {
        return mArticleUrl;
    }


    public long getTimeInMilliseconds() {
        return mTimeInMilliseconds;
    }
}
