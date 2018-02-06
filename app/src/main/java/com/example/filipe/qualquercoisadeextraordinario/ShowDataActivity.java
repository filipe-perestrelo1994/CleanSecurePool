package com.example.filipe.qualquercoisadeextraordinario;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ShowDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        Intent teste = getIntent();
        ArrayList<String> bundleResult = getIntent().getStringArrayListExtra("result");

       //ActionBar actionBar = getActionBar();
        ActionBar actionBar = getSupportActionBar();
        //System.out.println(actionBar);
       actionBar.setHomeButtonEnabled(true);
       actionBar.setDisplayHomeAsUpEnabled(true);

        TextView dataTextView = (TextView)findViewById(R.id.Data_ID);

        System.out.println(bundleResult);

        String bundleResultList = "";

        StringBuilder sb = new StringBuilder();

        for(String s : bundleResult)
        {
            sb.append(s);
            sb.append("\n");
            sb.append("\n");
        }

        bundleResultList = sb.toString();
        dataTextView.setText(bundleResultList);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        //return super.onOptionsItemSelected(item);
        return true;
    }
}
