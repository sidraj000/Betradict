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

import com.google.firebase.database.ValueEventListener;
import com.vincis.betradict.Class.Match;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.User;

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
    public float lastmatch;


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
        public List<User> mUser=new ArrayList<>();
        public List<Float> amtGiven=new ArrayList<>();
        public List<List<Float>> mAmt= new ArrayList<List<Float>>();
        public List<Float> mFloat=new ArrayList<>();
        public List<Quest> mQuest=new ArrayList<>();


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
            final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("users");
            databaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    final int index;
                    User user=dataSnapshot.getValue(User.class);
                    DatabaseReference md=FirebaseDatabase.getInstance().getReference();
                   md.child("users").child(user.per.uid).child("wallet").child("lastmatch").setValue(0);
                    mUser.add(user);
                    mAmt.add(mFloat);


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
        public void onBindViewHolder(@NonNull final cViewHolder cViewHolder,final int i) {
            List<Float> data=new ArrayList<>();
            data.add((float) 0.1);
            mAmt.add(data);
            if (mCMatch.get(i).status == 0) {
                for(int index=0;index<mUser.size();index++)
                {
                    mAmt.get(i).add(index, (float) 0.1);
                    final int ind=index;
                    DatabaseReference md = FirebaseDatabase.getInstance().getReference();
                    md.child("quest_usr").child(mUser.get(index).per.uid).child("cricket").child(mCMatch.get(i).id).child("normal").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Quest quest=dataSnapshot.getValue(Quest.class);
                            if(!quest.amtearned.equals(null)) {
                                mAmt.get(i).set(ind, (float) mAmt.get(i).get(ind) + quest.amtearned - quest.mybid);
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    md.child("quest_usr").child(mUser.get(index).per.uid).child("cricket").child(mCMatch.get(i).id).child("live").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            Quest quest=dataSnapshot.getValue(Quest.class);
                            if(!quest.amtearned.equals(null)) {
                                mAmt.get(i).set(ind, mAmt.get(i).get(ind) + quest.amtearned - quest.mybid);
                            }

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                final int k = i;
                cViewHolder.tvTeamA.setText(mCMatch.get(i).teamA);
                cViewHolder.tvTeamB.setText(mCMatch.get(i).teamB);
                cViewHolder.btn.setText("Stop");
                cViewHolder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lastmatch=0;
                        DatabaseReference md2=FirebaseDatabase.getInstance().getReference();
                        md2.child("match").child("cricket").child(mCMatch.get(k).id).child("status").setValue(1);
                        Toast.makeText(mContext, "Match Stopped", Toast.LENGTH_SHORT).show();
                      //  md2.child("users").child(mUser.get(i).per.uid).child("wallet").child("lastmatch").setValue(mAmt.get(i));
                        for(int p=0;p<mUser.size();p++)
                        {
                            md2.child("users").child(mUser.get(p).per.uid).child("wallet").child("lastmatch").setValue(mAmt.get(i).get(p));
                        }

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
