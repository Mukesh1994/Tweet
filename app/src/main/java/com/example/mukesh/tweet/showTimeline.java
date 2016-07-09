package com.example.mukesh.tweet;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;

public class showTimeline extends AppCompatActivity {

    private RecyclerView recyclerView;
    //private List<Status> mDataSet ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        String query = getIntent().getStringExtra("query");

        if(query.equals("nosearch"))
            new twitterShowTimeline().execute();
         else
            new twitterSearchTimeline().execute(query);






    }


     // asynchronous calls . .

     // for simply showing the timeline .

    class twitterShowTimeline extends AsyncTask<String, String, List<twitter4j.Status>> {


        protected void onPostExecute(List<twitter4j.Status> Tweets) {

            if (Tweets!=null)
                Toast.makeText(getApplicationContext(), "Tweet fetched successfully", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "Tweet fetching failed", Toast.LENGTH_SHORT).show();

            Log.d("Log:","tweets: "+Tweets.toString());

            MyAdapter myAdapter = new MyAdapter(Tweets);

            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            recyclerView.setAdapter(myAdapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            myAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(String... params) {
            //List<String> list = new List<String>()
            List<twitter4j.Status> tweets = null;
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(constantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(constantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                //!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)
                if (accessTokenString.trim().length()>0 && accessTokenSecret.trim().length()>0) {


                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                    tweets = twitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).getHomeTimeline();
                    //twitter4j.Status status = twitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
                    //return true;
                }

            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            Log.d("Loog:","tweets: "+tweets.toString());
            return tweets;  //To change body of implemented methods use File | Settings | File Templates.

        }
    }


    // for searching query
    class twitterSearchTimeline extends AsyncTask<String, String, List<twitter4j.Status>> {


        protected void onPostExecute(List<twitter4j.Status> Tweets) {

            if (!Tweets.isEmpty())
                Toast.makeText(getApplicationContext(), "Tweets founded!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(), "No tweets found", Toast.LENGTH_SHORT).show();
            Log.d("Loog:","tweets: "+Tweets.toString());
            MyAdapter myAdapter = new MyAdapter(Tweets);

            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            recyclerView.setAdapter(myAdapter);

            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

            myAdapter.notifyDataSetChanged();
        }

        @Override
        protected List<twitter4j.Status> doInBackground(String... params) {
            //List<String> list = new List<String>()
            List<twitter4j.Status> tweets = null;
            //ResponseList<twitter4j.Status> tweets = null;
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(constantValues.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(constantValues.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                //!StringUtil.isNullOrWhitespace(accessTokenString) && !StringUtil.isNullOrWhitespace(accessTokenSecret)
                if (accessTokenString.trim().length()>0 && accessTokenSecret.trim().length()>0) {


                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                    Query query = new Query(params[0]);
                    //query.setCount();

                    Log.d("LOG", "searchquery" + query);
                    QueryResult result ;
                    result = twitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).search(query);
                    tweets = result.getTweets();

                    //twitter4j.Status status = twitterUtil.getInstance().getTwitterFactory().getInstance(accessToken).updateStatus(params[0]);
                    //return true;
                }

            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            return tweets;  //To change body of implemented methods use File | Settings | File Templates.

        }
    }
}
