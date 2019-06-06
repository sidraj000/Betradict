package com.vincis.betradict;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vincis.betradict.Class.Match;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class adminMList extends Fragment {
    private RecyclerView mRecycler;
    private cListAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;


    public adminMList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_admin_mlist, container, false);
        mRecycler = view.findViewById(R.id.adminmatchList);

        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("match").child("cricket");
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new cListAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    private static class cViewHolder extends RecyclerView.ViewHolder {

        ImageView ivTeamA,ivTeamB;
        TextView tvTeamA,tvTeamB;
        Button btn;

        public cViewHolder(View itemView) {
            super(itemView);
            ivTeamA=itemView.findViewById(R.id.ivTeam12);
            ivTeamB=itemView.findViewById(R.id.ivTeam22);
            tvTeamA=itemView.findViewById(R.id.tvTeam12);
            tvTeamB=itemView.findViewById(R.id.tvTeam22);
         btn=itemView.findViewById(R.id.btnEnd);
        }
    }



    private class cListAdapter extends RecyclerView.Adapter<cViewHolder> {
        private Context mContext;
        private DatabaseReference mRef;
        public List<Match> mCMatch=new ArrayList<>();
        public List<String> cId=new ArrayList<>();


        public cListAdapter(Context context, DatabaseReference ref) {
            mContext = context;
            mRef = ref;
            Query query=mRef.orderByChild("date/time");
            //mRef=FirebaseDatabase.getInstance().getReference().child("match").child("cricket");
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Match cMatch=dataSnapshot.getValue(Match.class);

                        mCMatch.add(cMatch);
                        String key = dataSnapshot.getKey();
                        cId.add(key);
                        notifyItemInserted(mCMatch.size() - 1);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Match cMatch=dataSnapshot.getValue(Match.class);
                    String key = dataSnapshot.getKey();
                    int index=cId.indexOf(key);



                        if(index>-1) {
                           mCMatch.set(index,cMatch);
                            notifyItemChanged(index);
                        }

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Match cricketMatch=dataSnapshot.getValue(Match.class);
                    String id=dataSnapshot.getKey();
                    int index=cId.indexOf(id);
                    if(index>-1) {
                        mCMatch.remove(index);
                        notifyItemRemoved(index);
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        @NonNull
        @Override
        public cViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.adminmcard, viewGroup, false);

            return new cViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull final cViewHolder cViewHolder, int i) {

            if (mCMatch.get(i).status == 0) {
                final int k = i;
                cViewHolder.tvTeamA.setText(mCMatch.get(i).teamA);
                cViewHolder.tvTeamB.setText(mCMatch.get(i).teamB);
                cViewHolder.btn.setText("Stop");
                cViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference md = FirebaseDatabase.getInstance().getReference();
                        md.child("match").child("cricket").child(mCMatch.get(k).id).child("status").setValue(1);
                        Toast.makeText(mContext, "Match Stopped", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                cViewHolder.btn.setText("Start");

                final int k = i;
                cViewHolder.tvTeamA.setText(mCMatch.get(i).teamA);
                cViewHolder.tvTeamB.setText(mCMatch.get(i).teamB);
                cViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference md = FirebaseDatabase.getInstance().getReference();
                        md.child("match").child("cricket").child(mCMatch.get(k).id).child("status").setValue(0);
                        Toast.makeText(mContext, "Match Started", Toast.LENGTH_SHORT).show();
                    }

                });


            }
        }


        @Override
        public int getItemCount() {
            return mCMatch.size();
        }
    }

    }
