package com.yu.kotlcall;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.text.Text;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.yu.kotlcall.app.AppConfig;
import com.yu.kotlcall.app.AppController;
import com.yu.kotlcall.helper.SQLiteHandler;
import com.yu.kotlcall.helper.SessionManager;

import static com.yu.kotlcall.R.id.btnLogout;
import static java.lang.String.valueOf;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.inkvine.dota2stats.Dota2Stats;
import de.inkvine.dota2stats.domain.GameMode;
import de.inkvine.dota2stats.domain.MatchOverview;
import de.inkvine.dota2stats.domain.filter.MatchHistoryFilter;
import de.inkvine.dota2stats.domain.matchdetail.MatchDetail;
import de.inkvine.dota2stats.domain.matchdetail.MatchDetailPlayer;
import de.inkvine.dota2stats.domain.matchhistory.MatchHistory;
import de.inkvine.dota2stats.domain.playersearch.PlayerSearchResult;
import de.inkvine.dota2stats.domain.playerstats.PlayerStats;
import de.inkvine.dota2stats.exceptions.Dota2StatsAccessException;
import de.inkvine.dota2stats.impl.Dota2StatsImpl;

public class Main2Activity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final String TAG = Main2Activity.class.getSimpleName();
    private static final int  MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 111;
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    TextView txtOutputLat, txtOutputLon;
    Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    String lat, lon;
    private Button btnLogout,btnUpdate,btnCheck;
    private TextView tv1,tv2,tv3,tv4,tv5;
    private SQLiteHandler db;
    private SessionManager session;
    private ProgressDialog pDialog;
    private static String url = "http://138.68.50.197/android_login_api/location.php";
    private long bigsteamid;
    ArrayList<HashMap<String, String>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);
        bigsteamid = 0;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.INTERNET}, MY_PERMISSION_ACCESS_COARSE_LOCATION);
        }
       // txtOutputLat = (TextView) findViewById(R.id.textView);
       // txtOutputLon = (TextView) findViewById(R.id.textView2);
        btnLogout = (Button) findViewById(R.id.btnLogout2);
        btnUpdate = (Button) findViewById(R.id.btnNearby);
        btnCheck = (Button) findViewById(R.id.btnCheck);
        tv1        = (TextView)findViewById(R.id.textView13);
        tv2        = (TextView)findViewById(R.id.textView14);
        tv3        = (TextView)findViewById(R.id.textView16);
        tv4        = (TextView)findViewById(R.id.textView15);
        tv5        = (TextView)findViewById(R.id.textView12);
        String e = getIntent().getStringExtra("email");
        String s = getIntent().getStringExtra("steamid");
        tv5.setText("  your email is:  "+e);
        tv2.setText("  your steamid is:  "+s);
        db = new SQLiteHandler(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        // session manager
        session = new SessionManager(getApplicationContext());
        //SendfeedbackJob job = new SendfeedbackJob();
        //job.execute();
        if (!session.isLoggedIn()) {
            logoutUser();
        }



// just a number of recent matches


// print it!!
        btnCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(tv2.getText()!=null){
                Intent intent = new Intent(
                        Main2Activity.this,
                        Main3Activity.class);
                //needs implementation pass steamid
                intent.putExtra("steamidid", tv4.getText());
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    intent.putExtra("steamid", getIntent().getStringExtra("steamid"));
                startActivity(intent);
            }
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!lat.isEmpty()&& !lon.isEmpty()) {
                    UpdateLocation(getIntent().getStringExtra("email"), getIntent().getStringExtra("steamid"));
                }else{
                    Toast.makeText(getApplicationContext(),
                            "location not detected!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        buildGoogleApiClient();
    }
    private class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            // do above Server call here
             Dota2Stats stats = new Dota2StatsImpl("DEF7E6B553D105EBD6A34018C1167F73");
            try {
                /*
                PlayerStats playerStatsWithFilter = stats.getStats(Integer.parseInt(getIntent().getStringExtra("steamid")),
                        new MatchHistoryFilter().forDateMaximum(1349827200)
                                .forDateMinimum(1349395200));

                PlayerStats playerStatsByRecentNumberOfMatches = stats.getStats(Integer.parseInt(getIntent().getStringExtra("steamid")), 10);
                System.out.println(playerStatsByRecentNumberOfMatches);
                System.out.println(playerStatsWithFilter)*/

                //MatchDetail detail = stats.getMatchDetails(2818652698L);

                // extract all player stats from this game
                //List<MatchDetailPlayer> playersStatsOfTheMatch = detail.getPlayers();
                long abc = Long.valueOf(getIntent().getStringExtra("steamid"));
                System.out.println("abc is "+abc);
                MatchHistory history = stats.getMatchHistory(new MatchHistoryFilter().forAccountId(abc).forGameMode(GameMode.All_Pick));
                List<MatchOverview> overviews = history.getMatchOverviews();

                for (MatchOverview match : overviews)
                    Log.v("match item is",String.valueOf(match));

                // Show 'em

                //List<PlayerSearchResult> results = stats.searchByPlayerName("Grenade");

                // Look at the results!
               // for(PlayerSearchResult item : results)
                  //  Log.v("item is",String.valueOf(item));
                // Look at the results!
                //for(MatchOverview item : overviews)
                  //  Log.v("item is",String.valueOf(item));
                // print all match overviews found
                //for (MatchOverview match : overviews)
                  //  Log.v("item is",String.valueOf(match.getMatchId()));


            } catch (Dota2StatsAccessException e1) {
                // Do something if an error occurs
                Log.v("error is",e1.toString());
            }
            return "some message";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }
    private void UpdateLocation(final String email, final String steamid){
        //needs implementation
        String tag_string_req = "req_Nearby";

        pDialog.setMessage("Linking steamid ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                //Log.i("JSON Parser", response);
                hideDialog();
                try {
                    Log.d("123","\nobject is ****************"+response.toString()+"*************************\n");
                    JSONObject jObj = new JSONObject(response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1));
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sz`qlite

                        //JSONObject user = jObj.getJSONObject("user");
                        //String steamid = user.getString("steamid");
                        //String email = user.getString("email");
                        JSONObject nearby = jObj.getJSONObject("nearby");

                        String a = "nearby user steamid:";
                        tv3.setText(a);
                        if(nearby.getString("steamid")!=null) {
                            tv4.setText(nearby.getString("steamid"));
                        }
                        else{
                            tv4.setText("no recently user nearby");
                        }
                        /*
                        JSONObject o1 = nearby.getJSONObject(0);
                        JSONObject o2 = nearby.getJSONObject(1);
                        JSONObject o3 = nearby.getJSONObject(2);
                        JSONObject o4 = nearby.getJSONObject(3);*/
                        //String[][] locationlist = user.get;
                        //String name = user.getString("name");
                        //String created_at = user
                             //   .getString("created_at");
                        /*
                        JSONObject[] o = new JSONObject[4];
                        String[] text=new String[4];

                        for (int i = 0; i<4;i++) {
                            String j = String.valueOf(i);
                            //o[i] = nearby.getString(j);
                        }
                        tv1.setText(o1.getString("email")+o1.getString("steamid"));
                        tv2.setText(o2.getString("email")+o2.getString("steamid"));
                        tv3.setText(o3.getString("email")+o3.getString("steamid"));
                        tv4.setText(o4.getString("email")+o4.getString("steamid"));
                        tv5.setText("Players that are near you:");
                        8?
                        // Inserting row in users table
                        //db.addUser(name, email, uid, created_at);

                        Toast.makeText(getApplicationContext(), "location successfully registered. ", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        /*Intent intent = new Intent(
                                Main2Activity.this,
                                Main3Activity.class);
                        //needs implementation pass steamid
                        intent.putExtra("email", email);
                        intent.putExtra("steamid", steamid);
                        startActivity(intent);*/
                        //finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.get("error_msg").toString();
                        Toast.makeText(getApplicationContext(),
                               errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Steamid Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url

                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("steamid", steamid);
                params.put("lat", lat);
                params.put("lon", lon);
                return params;
            }

        };
        Log.d("123","\nobject is ****************"+strReq.toString()+"*************************\n");
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second
        int permissionCheck = ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.WRITE_CALENDAR);
        if (ContextCompat.checkSelfPermission(Main2Activity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Main2Activity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Main2Activity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = valueOf(mLastLocation.getLatitude());
            lon = valueOf(mLastLocation.getLongitude());

        }
        Log.d("lat is ", "\n"+lat+"\n");
        Log.d("lon is ", "\n"+lon+"\n");
        updateUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = valueOf(location.getLatitude());
        lon = valueOf(location.getLongitude());
        updateUI();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    void updateUI() {
        //txtOutputLat.setText(lat);
        //txtOutputLon.setText(lon);
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(Main2Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}