package com.example.android.moviesapp;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesapp.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.iw);
        Picasso.with(this).load("https://www.albertina.at/site/assets/files/1456/9_pablo_picasso_-_frau_mit_gruenem_hut.1200x0.jpg").into(imageView);
        TextView textView = findViewById(R.id.tw);
        URL url = NetworkUtilities.buildUri(this, NetworkUtilities.sortByPopularity);
        String uriString = url.toString();
        textView.setText(uriString);
    }
}
