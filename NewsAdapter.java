package com.example.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();


    /**
     * Constructs a new NewsAdapter
     *
     * @param context  of the app
     * @param NewsList is the list of Sonos articles, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> NewsList) {
        super(context, 0, NewsList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);

        }

        //Find the News article at the given position in the list of Sonos articles
        News currentNews = getItem(position);


        //Find the TextView in the news_list_item.xml with the ID article_title
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.article_title);
        titleTextView.setText(currentNews.getarticleTitle());

        //Find the TextView in the news_list_item.xml with the ID article_author
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.article_author);
        authorTextView.setText(currentNews.getauthorName());

        //Create a new date object from the time in milliseconds of the News articles
        Date dateObject = new Date(currentNews.getTimeInMilliseconds());

        //Find the TextView in the news_list_item.xml with the ID article_date
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.article_date);

        //Format the date string
        String formattedDate = formatDate(dateObject);

        dateTextView.setText(formattedDate);


        TextView timeTextView =(TextView) listItemView.findViewById(R.id.article_time);

        String formattedTime = formatTime(dateObject);

        timeTextView.setText(formattedTime);


        return listItemView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
