package com.yu.kotlcall;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import de.inkvine.dota2stats.Dota2Stats;
import de.inkvine.dota2stats.domain.GameMode;
import de.inkvine.dota2stats.domain.MatchOverview;
import de.inkvine.dota2stats.domain.filter.MatchHistoryFilter;
import de.inkvine.dota2stats.domain.matchhistory.MatchHistory;
import de.inkvine.dota2stats.domain.playerstats.PlayerStats;
import de.inkvine.dota2stats.exceptions.Dota2StatsAccessException;
import de.inkvine.dota2stats.impl.Dota2StatsImpl;

public class Main3Activity extends AppCompatActivity {
    private Button btnLogout,btnFresh;
    private TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv11;
    List<MatchOverview> a;
    PlayerStats aaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        SendfeedbackJob job = new SendfeedbackJob();
        job.execute();
        btnLogout = (Button) findViewById(R.id.btn3_logout);
        btnFresh = (Button) findViewById(R.id.freshbtn);
        tv1        = (TextView)findViewById(R.id.id0);
        tv2        = (TextView)findViewById(R.id.id1);
        tv3        = (TextView)findViewById(R.id.id22);
        tv4        = (TextView)findViewById(R.id.tx1);
        tv5        = (TextView)findViewById(R.id.id33);
//        System.out.println(a.toString());
        tv6        = (TextView)findViewById(R.id.textView7);
        tv7        = (TextView)findViewById(R.id.textView8);
        tv8        = (TextView)findViewById(R.id.textView9);
        tv9        = (TextView)findViewById(R.id.textView6);
        tv11        =(TextView)findViewById(R.id.textView11);
        tv2.setText(getIntent().getStringExtra("steamidid"));
        btnFresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                tv6.setText(String.valueOf(a.get(0).getMatchId()));
                tv7.setText(String.valueOf(a.get(1).getMatchId()));
                tv8.setText(String.valueOf(a.get(2).getMatchId()));
                tv9.setText(String.valueOf(a.get(3).getMatchId()));
                tv4.setText(String.valueOf(aaa.getKillDeathAssistRatio()));
                tv11.setText(aaa.toString());

            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Main3Activity.this,
                        Main2Activity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                intent.putExtra("steamid", getIntent().getStringExtra("steamid"));
                //needs implementation pass steamid
                startActivity(intent);
            }
        });

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
                long abc = Long.valueOf(getIntent().getStringExtra("steamidid"));
                System.out.println("abc is "+abc);
                MatchHistory history = stats.getMatchHistory(new MatchHistoryFilter().forAccountId(abc).forGameMode(GameMode.All_Pick));
                List<MatchOverview> overviews = history.getMatchOverviews();

                //for (MatchOverview match : overviews)
                   // Log.v("match itemitem is",String.valueOf(match));
                a = history.getMatchOverviews();


                // just a number of recent matches
                aaa = stats.getStats(abc, 15);


                // print it!!
               // System.out.println("Player stats by recent"+aaa);
                // print all match overviews found




            } catch (Dota2StatsAccessException e1) {
                // Do something if an error occurs
            }
            return "some message";
        }

        @Override
        protected void onPostExecute(String message) {
            //process message
        }
    }
    public void SetText(List<MatchOverview> a){
        tv6.setText(String.valueOf(a.get(0).getMatchId()));
        tv7.setText(String.valueOf(a.get(1).getMatchId()));
        tv8.setText(String.valueOf(a.get(2).getMatchId()));
        tv9.setText(String.valueOf(a.get(3).getMatchId()));

    }
}
