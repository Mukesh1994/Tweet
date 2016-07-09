package com.example.mukesh.tweet;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.auth.RequestToken;

public class MainActivity extends AppCompatActivity {

//    static String TWITTER_CONSUMER_KEY = "U2jtJqizsPBLduzdDhR0Ex3jr";
//    static String TWITTER_CONSUMER_SECRET = "pjA3bjMki2emdQgh2iW45ylI3loXBUak2wuLsoN4qrDzqE9rDV";

    private boolean isUseStoredTokenKey = false;
    private boolean isUseWebViewForAuthentication = false;



    // Login button
    Button btnLoginTwitter;
    // Progress dialog
    ProgressDialog pDialog;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    // Internet Connection detector
    private ConnectionDetector cd;
    // Alert Dialog Manager

    alertDialogManager alert = new alertDialogManager();


    private void initializeComponent() {
        btnLoginTwitter = (Button) findViewById(R.id.btnLoginTwitter);

        btnLoginTwitter.setOnClickListener(buttonLoginOnClickListener);
    }

    private View.OnClickListener buttonLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            loginToTwitter();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        cd = new ConnectionDetector(getApplicationContext());
        // Check if Internet present
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getApplicationContext());

        //AlertDialog.Builder builder2 = new AlertDialog.Builder(context);


        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
//            alert.showAlertDialog(MainActivity.this, "Internet Connection Error",
//                    "Please connect to working Internet connection", false);
            builder1.setTitle("INTERNET CONNECTION ERROR");
            builder1.setMessage("Please connect to working Internet connection.");
            builder1.setCancelable(true);
            Log.d("log:", "internet error");
            // if(status != null)
            // Setting alert dialog icon
            //     alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            // Setting OK Button

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // stop executing code by return

            AlertDialog alert11 = builder1.create();
            alert11.show();
            return;
        }

        // Check if twitter keys are set
        if (constantValues.TWITTER_CONSUMER_KEY.trim().length() == 0 || constantValues.TWITTER_CONSUMER_SECRET.trim().length() == 0) {
            // Internet Connection is not present
            builder1.setTitle("Twitter oAuth tokens");
            builder1.setMessage("Please set your twitter oauth tokens first!");
            builder1.setCancelable(true);
            // if(status != null)
            // Setting alert dialog icon
            //     alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

            // Setting OK Button

            builder1.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // stop executing code by return

            AlertDialog alert11 = builder1.create();
            alert11.show();
            // stop executing code by return
            return;
        }

        initializeComponent();
        if (isUseStoredTokenKey)
            loginToTwitter();

       // mSharedPreferences = getApplicationContext().getSharedPreferences(
       //         "MyPref", 0);

    }


    /**
     * Function to login twitter
     */
    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            new TwitterAuthenticateTask().execute();
        } else {
            // user already logged into twitter
            Intent intent = new Intent(this, TwitterActivity.class);
            startActivity(intent);

            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return (sharedPreferences.getBoolean(constantValues.PREFERENCE_TWITTER_IS_LOGGED_IN,false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //asynchronus call for authentication

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken != null) {
                if (!isUseWebViewForAuthentication) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                    startActivity(intent);
                } else {
//                    Intent intent = new Intent(getApplicationContext(),OAuthActivity.class);
//                    intent.putExtra(constantValues.STRING_EXTRA_AUTHENCATION_URL, requestToken.getAuthenticationURL());
//                    startActivity(intent);
                }
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return twitterUtil.getInstance().getRequestToken();
        }
    }
}