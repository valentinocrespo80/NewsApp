package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;


import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String mArticleUrl;

    public NewsLoader(Context context, String articleUrl) {
        super(context);

        mArticleUrl = articleUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mArticleUrl == null) {
            return null;
        }
        //Perform the network request, and parse the response, and extract a list of Sonos articles.
        List<News> theNews = QueryUtils.fetchNewsData(mArticleUrl);
        return theNews;
    }
}

