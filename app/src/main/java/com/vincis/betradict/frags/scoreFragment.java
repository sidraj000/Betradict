package com.vincis.betradict.frags;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vincis.betradict.Converter;
import com.vincis.betradict.Converter2;
import com.vincis.betradict.Main4Activity;
import com.vincis.betradict.MainActivity;
import com.vincis.betradict.R;
import com.vincis.betradict.admin.addEvent;
import com.vincis.betradict.frag_addmatch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class scoreFragment extends Fragment {
    ProgressBar progressBar;
    List<String> pla=new ArrayList<>();
    List<String> plb=new ArrayList<>();
    TextView tvmsg,tvtoss,tvscore1,tvscore2,tvResult,tvStatus;
    ImageView ball1,ball2,ball3,ball4,ball5,ball6;
    Button refresh,det;
    String data;
    public Bundle b;
    public String arr[];
    public scoreFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_score, container, false);
        progressBar=view.findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        tvmsg=view.findViewById(R.id.tvMatchMsg);
        ball1=view.findViewById(R.id.ball1);
        ball2=view.findViewById(R.id.ball2);
        ball3=view.findViewById(R.id.ball3);
        ball4=view.findViewById(R.id.ball4);
        ball5=view.findViewById(R.id.ball5);
        ball6=view.findViewById(R.id.ball6);
        tvtoss=view.findViewById(R.id.tossdata);
        tvscore1=view.findViewById(R.id.score1);
        refresh=view.findViewById(R.id.btnRefresh);
        tvscore2=view.findViewById(R.id.score2);
        tvResult=view.findViewById(R.id.result);
        tvStatus=view.findViewById(R.id.tvStatus);
        det=view.findViewById(R.id.btnDetails);
        b=getArguments();
        arr=b.getStringArray("details");
        det.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Main4Activity.class);
                Bundle b=new Bundle();
                String arr[]={data,"0"};
                b.putStringArray("data",arr);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        tvResult.setVisibility(View.GONE);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RetrieveFeedTask().execute();
            }
        });
        new RetrieveFeedTask().execute();

        return view;
    }

    public class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {

            // Do some validation here

            try {

                URL url = new URL("https://rest.cricketapi.com/rest/v2/match/"+arr[1]+"/?access_token=2s1136980546157678688s1139463129789512541");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            progressBar.setVisibility(View.GONE);
            // Log.i("INFO", response);
            //  responseView.setText(response);
            if (response != null) {
                data=response;
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array nodJ
                    JSONObject jsonObj2=jsonObj.getJSONObject("data");
                    JSONObject jsonObject3=jsonObj2.getJSONObject("card");
                    String short_name=jsonObject3.getString("short_name");
                    tvmsg.setText(short_name);
                    JSONObject tossobject=jsonObject3.getJSONObject("toss");
                    String tossDetails=tossobject.getString("str");
                    tvtoss.setText(tossDetails);
                    JSONArray playing11a=jsonObject3.getJSONObject("teams").getJSONObject("a").getJSONObject("match").getJSONArray("playing_xi");
                    JSONArray playing11b=jsonObject3.getJSONObject("teams").getJSONObject("b").getJSONObject("match").getJSONArray("playing_xi");
                    JSONArray batorder=jsonObject3.getJSONArray("batting_order");
                    String batsecond;


                    String batfirst=batorder.getJSONArray(0).getString(0);
                    String score=jsonObject3.getJSONObject("innings").getJSONObject(batfirst+"_1").getString("run_str");
                    String team1=jsonObject3.getJSONObject("teams").getJSONObject(batfirst).getString("short_name");
                    tvscore1.setText(team1+": "+score);
                    if(batorder.length()==2)
                    {
                        batsecond=batorder.getJSONArray(1).getString(0);
                        String team2=jsonObject3.getJSONObject("teams").getJSONObject(batsecond).getString("short_name");
                        String score2=jsonObject3.getJSONObject("innings").getJSONObject(batsecond+"_1").getString("run_str");
                        tvscore2.setText(team2+": "+score2);
                        tvscore2.setVisibility(View.VISIBLE);
                    }
                    else {
                        tvscore2.setVisibility(View.GONE);
                    }
                    String result;
                    result=jsonObject3.getJSONObject("msgs").getString("result");
                    if(!result.equals("null"))
                    {
                        tvResult.setText(result);
                        tvResult.setVisibility(View.VISIBLE);

                    }

                    else {
                        tvResult.setVisibility(View.GONE);
                    }
                    String status=jsonObject3.getString("status");
                    tvStatus.setText(status);
                    JSONArray lastOver=jsonObject3.getJSONObject("now").getJSONArray("recent_overs_str").getJSONArray(0).getJSONArray(1);;
                    final int t=playing11a.length();
                    int p=lastOver.length();

                    //  Toast.makeText(getContext(), Integer.toString(p), Toast.LENGTH_SHORT).show();
                    if(p==1)
                    {   String a= lastOver.getString(0);
                        char a1=a.charAt(0);
                        if(a1=='w')
                        {

                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),a.charAt(1),R.drawable.wallet));

                        }


                    }
                    if(p==2)
                    {

                        String a= lastOver.getString(0);
                        char a1=a.charAt(0);
                        if(a1=='w')
                        {

                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),a.charAt(1),R.drawable.wallet));

                        }
                        String b= lastOver.getString(1);
                        char b1=b.charAt(0);
                        if(b1=='w')
                        {

                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),b.charAt(1),R.drawable.wallet));

                        }

                    }
                    if(p==3)
                    {

                        String a= lastOver.getString(0);
                        char a1=a.charAt(0);
                        if(a1=='w')
                        {

                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),a.charAt(1),R.drawable.wallet));

                        }
                        String b= lastOver.getString(1);
                        char b1=b.charAt(0);
                        if(b1=='w')
                        {

                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),b.charAt(1),R.drawable.wallet));

                        }
                        String c= lastOver.getString(2);
                        char c1=c.charAt(0);
                        if(c1=='w')
                        {

                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),c.charAt(1),R.drawable.wallet));

                        }
                    }
                    if(p==4)
                    {

                        String a= lastOver.getString(0);
                        char a1=a.charAt(0);
                        if(a1=='w')
                        {

                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),a.charAt(1),R.drawable.wallet));

                        }
                        String b= lastOver.getString(1);
                        char b1=b.charAt(0);
                        if(b1=='w')
                        {

                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),b.charAt(1),R.drawable.wallet));

                        }
                        String c= lastOver.getString(2);
                        char c1=c.charAt(0);
                        if(c1=='w')
                        {

                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),c.charAt(1),R.drawable.wallet));

                        }
                        String d= lastOver.getString(3);
                        char d1=d.charAt(0);
                        if(d1=='w')
                        {

                            ball4.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball4.setImageDrawable(Converter2.convertLayoutToImage(getContext(),d.charAt(1),R.drawable.wallet));

                        }

                    }
                    if(p==5)
                    {String a= lastOver.getString(0);
                        char a1=a.charAt(0);
                        if(a1=='w')
                        {

                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),a.charAt(1),R.drawable.wallet));

                        }
                        String b= lastOver.getString(1);
                        char b1=b.charAt(0);
                        if(b1=='w')
                        {

                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),b.charAt(1),R.drawable.wallet));

                        }
                        String c= lastOver.getString(2);
                        char c1=c.charAt(0);
                        if(c1=='w')
                        {

                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),c.charAt(1),R.drawable.wallet));

                        }
                        String d= lastOver.getString(3);
                        char d1=d.charAt(0);
                        if(d1=='w')
                        {

                            ball4.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball4.setImageDrawable(Converter2.convertLayoutToImage(getContext(),d.charAt(1),R.drawable.wallet));

                        }
                        String e= lastOver.getString(4);
                        char e1=e.charAt(0);
                        if(e1=='w')
                        {

                            ball5.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball5.setImageDrawable(Converter2.convertLayoutToImage(getContext(),e.charAt(1),R.drawable.wallet));

                        }


                    }
                    if(p==6)
                    {
                        String a= lastOver.getString(0);
                        char a1=a.charAt(0);
                        if(a1=='w')
                        {

                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball1.setImageDrawable(Converter2.convertLayoutToImage(getContext(),a.charAt(1),R.drawable.wallet));

                        }
                        String b= lastOver.getString(1);
                        char b1=b.charAt(0);
                        if(b1=='w')
                        {

                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball2.setImageDrawable(Converter2.convertLayoutToImage(getContext(),b.charAt(1),R.drawable.wallet));

                        }
                        String c= lastOver.getString(2);
                        char c1=c.charAt(0);
                        if(c1=='w')
                        {

                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball3.setImageDrawable(Converter2.convertLayoutToImage(getContext(),c.charAt(1),R.drawable.wallet));

                        }
                        String d= lastOver.getString(3);
                        char d1=d.charAt(0);
                        if(d1=='w')
                        {

                            ball4.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball4.setImageDrawable(Converter2.convertLayoutToImage(getContext(),d.charAt(1),R.drawable.wallet));

                        }
                        String e= lastOver.getString(4);
                        char e1=e.charAt(0);
                        if(e1=='w')
                        {

                            ball5.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball5.setImageDrawable(Converter2.convertLayoutToImage(getContext(),e.charAt(1),R.drawable.wallet));

                        }

                        String f= lastOver.getString(5);
                        char f1=f.charAt(0);
                        if(f1=='w')
                        {

                            ball6.setImageDrawable(Converter2.convertLayoutToImage(getContext(),'w',R.drawable.wallet));

                        }
                        else
                        {
                            ball6.setImageDrawable(Converter2.convertLayoutToImage(getContext(),f.charAt(1),R.drawable.wallet));

                        }


                    }



                    for (int k = 0; k <t; k++) {
                        pla.add(playing11a.getString(k));
                        plb.add(playing11b.getString(k));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {}
        }
    }

}
