package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class NewsActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>> {


    public static final String LOG_TAG = NewsActivity.class.getName();

    //URL for Sonos News data from the request URL
    private static final String SONOS_REQUEST_URL = "https://content.guardianapis.com/search?" +
            "from-date=2018-02-01&to-date=2018-08-06&q=Sonos&api-key= de58bf78-aa9a-4a31-8282-d37c378ed090" +
            "&show-tags=contributor&page-size=10";

    //Constant value for the news loader id.
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of Sonos News
     */
    private NewsAdapter mAdapter;
    /**
     * When the list is empty this is displayed
     */
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        //Find a reference to the ListView in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mAdapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                News currentNews = mAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getarticleUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);


            }


        });

        //Query the active network and determine if it has internet connectivity
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View progressBar = findViewById(R.id.loading_indicator);
            progressBar.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }


    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, SONOS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> theNews) {
        mAdapter.clear();
        View progressBar = findViewById(R.id.loading_indicator);
        progressBar.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_articles);

        if (theNews != null && !theNews.isEmpty()) {
            mAdapter.addAll(theNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }

}
