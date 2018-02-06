package com.example.filipe.qualquercoisadeextraordinario;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayPoolActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper helper = new DatabaseHelper(this);
    String UserNameToGetPoolName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_pool);

        Bundle bundle = getIntent().getExtras();
        UserNameToGetPoolName = bundle.getString("username");

        String pool = helper.searchPool(UserNameToGetPoolName);
        TextView PoolNameTextView = (TextView)findViewById(R.id.Pool1_ID);
        PoolNameTextView.setText(pool);
        PoolNameTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {
        Intent i = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("username",UserNameToGetPoolName);
        i.putExtras(bundle);
        System.out.println( bundle.getString("username"));
        startActivity(i);
        finish();

    }
}
