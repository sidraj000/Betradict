package com.vincis.betradict;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.vincis.betradict.Class.AllQuest;
import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.Quest_wall;
import com.vincis.betradict.Class.User;
import com.vincis.betradict.Class.Usrwal;
import com.vincis.betradict.frags.frag3;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class playinng11details extends Fragment {
String[] response;
Button teama,teamb;
    private RecyclerView mRecycler;
    private AnsweredAdapter mAdapter;
    private LinearLayoutManager mManager;
    private RecyclerView mRecycler2;
    private AnsweredAdapter mAdapter2;
    private LinearLayoutManager mManager2;
    int count=0;

    public playinng11details() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        response=getArguments().getStringArray("data");

       View view= inflater.inflate(R.layout.fragment_playinng11details, container, false);
       teama=view.findViewById(R.id.btnteama);
       teamb=view.findViewById(R.id.btnteamb);
       teama.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               teama.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               teamb.setBackgroundColor(getResources().getColor(R.color.colorAccent));
             mAdapter=new AnsweredAdapter(getContext(),0);
             mRecycler.setAdapter(mAdapter);
             mRecycler.setVisibility(View.VISIBLE);
             mRecycler2.setVisibility(View.GONE);
           }
       });
       teamb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               teamb.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
               teama.setBackgroundColor(getResources().getColor(R.color.colorAccent));
               mAdapter2=new AnsweredAdapter(getContext(),1);
               mRecycler2.setAdapter(mAdapter2);
              mRecycler2.setVisibility(View.VISIBLE);
             mRecycler.setVisibility(View.GONE);
           }
       });
        mRecycler = view.findViewById(R.id.playerlist);
        mRecycler2 = view.findViewById(R.id.playerlist2);
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        mRecycler2.setHasFixedSize(true);
        mRecycler2.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setLayoutManager(mManager);
         return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AnsweredAdapter(getContext(),count);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class AnsweredViewHolder extends RecyclerView.ViewHolder {

        TextView tvPlayer,tvPlayerRole;

        public AnsweredViewHolder(View itemView) {
            super(itemView);
            tvPlayer=itemView.findViewById(R.id.tvPlayer);
            tvPlayerRole=itemView.findViewById(R.id.tvPlayerRole);
        }
    }


    private class AnsweredAdapter extends RecyclerView.Adapter<AnsweredViewHolder> {
        public JSONObject jsonObj,jsonObj2,jsonObject3;

        private Context mContext;
        public  JSONArray playing11a,playing11b;
        public int mark;
        public AnsweredAdapter(final Context context,int count) {
            mContext = context;
            mark=count;



            try {
               jsonObj=new JSONObject(response[0]);
                jsonObj2=jsonObj.getJSONObject("data");
                 jsonObject3=jsonObj2.getJSONObject("card");
                playing11a=jsonObject3.getJSONObject("teams").getJSONObject("a").getJSONObject("match").getJSONArray("playing_xi");
                playing11b=jsonObject3.getJSONObject("teams").getJSONObject("b").getJSONObject("match").getJSONArray("playing_xi");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        @NonNull
        @Override
        public AnsweredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.itemplayerss, viewGroup, false);

            return new AnsweredViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AnsweredViewHolder friendViewHolder, int i) {
            if(mark==0)
            {
                try {
                    friendViewHolder.tvPlayer.setText(jsonObject3.getJSONObject("players").getJSONObject(playing11a.getString(i)).getString("name"));
                    friendViewHolder.tvPlayerRole.setText(jsonObject3.getJSONObject("players").getJSONObject(playing11a.getString(i)).getString("seasonal_role"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else {
                try { friendViewHolder.tvPlayer.setText(jsonObject3.getJSONObject("players").getJSONObject(playing11b.getString(i)).getString("name"));
                    friendViewHolder.tvPlayerRole.setText(jsonObject3.getJSONObject("players").getJSONObject(playing11b.getString(i)).getString("seasonal_role"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }



        }



        @Override
        public int getItemCount()
        {

            return playing11a.length();
        }


    }

}
