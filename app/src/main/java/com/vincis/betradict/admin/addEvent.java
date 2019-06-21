package com.vincis.betradict.admin;

import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.vincis.betradict.frag_addmatch;
import com.vincis.betradict.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class addEvent extends AppCompatActivity {



    TextView responseView;
    ProgressBar progressBar;
    ArrayList<HashMap<String, String>> matchList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        matchList=new ArrayList<>();

        responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new RetrieveFeedTask().execute();

    }

    public  class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            responseView.setText("");
        }

        protected String doInBackground(Void... urls) {

            // Do some validation here

            try {

                URL url = new URL("https://rest.cricketapi.com/rest/v2/recent_matches/?access_token=2s1136980546157678688s1142184943678534155");
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
                // Log.e("ERROR", e.getMessage(), e);
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
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array nodJ
                    JSONObject jsonObj2=jsonObj.getJSONObject("data");
                    JSONArray matches = jsonObj2.getJSONArray("cards");
                    Toast.makeText(addEvent.this,Integer.toString( matches.length()), Toast.LENGTH_SHORT).show();
                    final int t=matches.length();
                    JSONArray match = jsonObj2.getJSONArray("cards");
                    for (int k = 0; k <t; k++) {

                        JSONObject c = matches.getJSONObject(k);
                        String TeamA = c.getJSONObject("teams").getJSONObject("a").getString("key").toUpperCase();
                        if (TeamA.equals("IND") || TeamA.equals("BAN") || TeamA.equals("RSA") || TeamA.equals("NZ") || TeamA.equals("AUS") || TeamA.equals("WI") || TeamA.equals("PAK") || TeamA.equals("SL") || TeamA.equals("ENG") || TeamA.equals("AFG")) {
                            String TeamB = c.getJSONObject("teams").getJSONObject("b").getString("key").toUpperCase();
                            String uid = c.getString("key");


                            String date = c.getJSONObject("start_date").getString("iso");
                            ;
                            HashMap<String, String> contact = new HashMap<>();
                            contact.put("TeamA", TeamA);
                            contact.put("TeamB", TeamB);
                            contact.put("uid", uid);
                            contact.put("Date", date);
                            matchList.add(contact);

                        }
                    }


                    Bundle b=new Bundle();
                    b.putSerializable("det",matchList);
                    FragmentManager fm=getSupportFragmentManager();
                    frag_addmatch fragment=new frag_addmatch();
                    fragment.setArguments(b);
                    fm.beginTransaction().replace(R.id.addeve,fragment).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {}
        }
    }


}
