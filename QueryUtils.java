package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<News> fetchNewsData(String requestedUrl) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Log.e(TAG, "fetchNewsData: Interrupted", ie);
        }

        //Create URL object
        URL newsUrl = createUrl(requestedUrl);

        //Perform httpsRequest to URL
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpsRequest(newsUrl);
        } catch (IOException e) {
            Log.e(TAG, "fetchNewsData: Problem making Https Request.", e);
        }

        //Extract fields from the Json response and create a list of Sonos articles
        List<News> theNews = extractNewsFromJson(jsonResponse);

        //Return the list of News
        return theNews;
    }

    /**
     * @param stringUrl creates a string.
     * @return a new URL object from the string URL.
     */

    private static URL createUrl(String stringUrl) {
        URL newsUrl = null;
        try {
            newsUrl = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Problem building the URL ", e);
        }
        return newsUrl;
    }

    private static String makeHttpsRequest(URL newsUrl) throws IOException {
        String jsonResponse = "";

        if (newsUrl == null) {
            return jsonResponse;

        }

        HttpsURLConnection newsUrlConnection = null;
        InputStream inputStream = null;
        try {
            newsUrlConnection = (HttpsURLConnection) newsUrl.openConnection();
            newsUrlConnection.setReadTimeout(1000 /*milliseconds */);
            newsUrlConnection.setConnectTimeout(15000 /*milliseconds */);
            newsUrlConnection.setRequestMethod("GET");
            newsUrlConnection.connect();


            if (newsUrlConnection.getResponseCode() == 200) {
                inputStream = newsUrlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + newsUrlConnection.getResponseCode());
            }


        } catch (IOException e) {
            Log.e(TAG, "Problem receiving the Sonos News JSON results.", e);
        } finally {
            if (newsUrlConnection != null) {
                newsUrlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<News> extractNewsFromJson(String newsJSON) {

        //If the JSON string is empty or null, return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        List<News> theNews = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            //Extract the JSONObject associated with the key "response"
            JSONObject baseJsonResponseResult = baseJsonResponse.getJSONObject("response");

            //Extract the JSONArray associated with key "results"
            JSONArray articleArray = baseJsonResponseResult.getJSONArray("results");

            for (int m = 0; m < articleArray.length(); m++) {

                //Get a single news article at position m within a list of news articles
                JSONObject currentNews = articleArray.getJSONObject(m);

                String articleTitle = currentNews.getString("webTitle");
                long time = currentNews.getLong("webPublicationDate");
                String articleUrl = currentNews.getString("webUrl");


                JSONArray currentAuthors = baseJsonResponseResult.getJSONArray("tags");


                for (int b = 0; b < currentAuthors.length(); b++) {
                    //Get a single author at b position within a list of news articles
                    JSONObject listAuthors = currentAuthors.getJSONObject(b);

                    String authorName = listAuthors.getString("webTitle");

                    News sonosNews = new News(articleTitle, authorName, articleUrl, time);

                    theNews.add(sonosNews);


                }


            }


        } catch (JSONException ie) {
            Log.e(TAG, "extractNewsFromJson: " + "Problem parsing the News Json results", ie);
        }
        //Return the List of News
        return theNews;

    }


}
