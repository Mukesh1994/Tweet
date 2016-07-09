package com.example.mukesh.tweet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class searchTweets extends AppCompatActivity {

    private Button buttonSearch;
    TextView textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tweets);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeComponent();

    }

    private void initializeComponent() {
        buttonSearch = (Button)findViewById(R.id.buttonSearch);
        textView = (TextView) findViewById(R.id.texttosearch);
        buttonSearch.setOnClickListener(buttonSearchlistener);

    }
    private View.OnClickListener buttonSearchlistener = new View.OnClickListener(){
        public void onClick(View v )
        {
            String query = textView.getText().toString();
            Intent intent = new Intent(searchTweets.this,showTimeline.class);
            intent.putExtra("query",query);
            startActivity(intent);
        }
    };
}
