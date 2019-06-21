package com.vincis.betradict;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class scorecard extends Fragment {
    String[] response;
    Button teama,teamb;
    TextView fow;
    private RecyclerView mRecycler;
    private RecyclerView mRecycler2;
    private Team1Adapter mAdapter;
    private Team1Adapter mAdapter2;
    private LinearLayoutManager mManager;
    public JSONObject jsonObj,jsonObj2,jsonObject3;
    public JSONArray jsonBattingOrder;
    int count=2;
    public List<String> teambattingorder=new ArrayList<>();
    public scorecard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        response=getArguments().getStringArray("data");
       View view=inflater.inflate(R.layout.fragment_scorecard, container, false);
       teama=view.findViewById(R.id.btnteamaa);
       teamb=view.findViewById(R.id.btnteambb);
       fow=view.findViewById(R.id.fallofwickets);
       teamb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(jsonBattingOrder.length()==2) {
                   teamb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                   teama.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                   Team1Adapter newAdapter = new Team1Adapter(getContext(), 1);
                   mRecycler2.setAdapter(newAdapter);
                   mRecycler.setVisibility(View.GONE);
                   mRecycler2.setVisibility(View.VISIBLE);
               }
               else
               {
                   Toast.makeText(getContext(), "Yet to Bat", Toast.LENGTH_SHORT).show();
               }
           }
       });
       teama.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               teama.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               teamb.setBackgroundColor(getResources().getColor(R.color.colorAccent));
               Team1Adapter newAdapter=new Team1Adapter(getContext(),0);
               mRecycler.setAdapter(newAdapter);
               mRecycler2.setVisibility(View.GONE);
               mRecycler.setVisibility(View.VISIBLE);
           }
       });
        try {
            jsonObj=new JSONObject(response[0]);

            jsonObj2=jsonObj.getJSONObject("data");
            jsonObject3=jsonObj2.getJSONObject("card");
            jsonBattingOrder=jsonObject3.getJSONArray("batting_order");
            for(int l=0;l<jsonBattingOrder.length();l++)
            {
                teambattingorder.add(jsonObject3.getJSONObject("teams").getJSONObject(jsonBattingOrder.getJSONArray(l).getString(0)).getString("short_name"));
            }
            if(jsonBattingOrder.length()==1)
            {
                if ( jsonBattingOrder.getJSONArray(0).getString(0).equals("a"))
                {
                  teambattingorder.add( jsonObject3.getJSONObject("teams").getJSONObject("b").getString("short_name"));
                }
                else
                {
                    teambattingorder.add( jsonObject3.getJSONObject("teams").getJSONObject("a").getString("short_name"));
                }
            }
            teama.setText(teambattingorder.get(0));
            teamb.setText(teambattingorder.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRecycler = view.findViewById(R.id.scorecardRecycler);
        mRecycler2=view.findViewById(R.id.scorecardRecycler2);
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        mRecycler2.setHasFixedSize(false);
        mRecycler.setLayoutManager(mManager);
        mRecycler2.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new Team1Adapter(getContext(),count);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class Team1ViewHolder extends RecyclerView.ViewHolder {

        TextView batsman,run,balls,s4,s6,sr;

        public Team1ViewHolder(View itemView) {
            super(itemView);
            batsman=itemView.findViewById(R.id.batsman);
            run=itemView.findViewById(R.id.R);
            balls=itemView.findViewById(R.id.B);
            s4=itemView.findViewById(R.id.s4);
            s6=itemView.findViewById(R.id.s6);
            sr=itemView.findViewById(R.id.sr);
        }
    }


    private class Team1Adapter extends RecyclerView.Adapter<Team1ViewHolder> {


        private Context mContext;
        public JSONArray batting_order;
        protected JSONArray fo;
        public Integer mark;
        public Team1Adapter(final Context context,Integer count) {
            mContext = context;
            mark=count%2;


            try {
               batting_order= jsonObject3.getJSONObject("innings").getJSONObject(jsonBattingOrder.getJSONArray(mark).getString(0)+"_1").getJSONArray("batting_order");
               fo=jsonObject3.getJSONObject("innings").getJSONObject(jsonBattingOrder.getJSONArray(mark).getString(0)+"_1").getJSONArray("fall_of_wickets");
              String str="";
               for(int k=0;k<fo.length();k++)
               {
                   str=str+fo.getString(k)+"\n"+"\n";
               }
               fow.setText(str);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @NonNull
        @Override
        public Team1ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.itemscorecard, viewGroup, false);

            return new Team1ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Team1ViewHolder friendViewHolder, int i) {
            try {
                JSONObject player= jsonObject3.getJSONObject("players").getJSONObject(batting_order.getString(i));
                friendViewHolder.batsman.setText(player.getString("name"));
                friendViewHolder.run.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("runs"));
                friendViewHolder.balls.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("balls"));
                friendViewHolder.s4.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("sixes"));
                friendViewHolder.s6.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("fours"));
                friendViewHolder.sr.setText(player.getJSONObject("match").getJSONObject("innings").getJSONObject("1").getJSONObject("batting").getString("strike_rate"));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }



        @Override
        public int getItemCount()
        {

            return batting_order.length();
        }


    }

}
