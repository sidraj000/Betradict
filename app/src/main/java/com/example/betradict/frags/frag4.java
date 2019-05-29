package com.example.betradict.frags;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.betradict.Class.AllQuest;
import com.example.betradict.Class.Quest;
import com.example.betradict.Class.User;
import com.example.betradict.Class.Usrwal;
import com.example.betradict.Class.Wallet;
import com.example.betradict.Class.Quest_wall;
import com.example.betradict.R;
import com.example.betradict.transactions;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class frag4 extends Fragment {

    private RecyclerView mRecycler;
    private FriendAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Wallet wallet=new Wallet();
    public Usrwal u;
    public Quest_wall quest_wall;
    public Bundle b;
    public String arr[];
    public TextView  tvEmpt;

    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public frag4() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag4, container, false);
        mRecycler = view.findViewById(R.id.questList);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        b=getArguments();
        arr=b.getStringArray("details");
        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]);
        mManager = new LinearLayoutManager(getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        tvEmpt=view.findViewById(R.id.qE);


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new FriendAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanupListener();
    }



    private static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView tvquest, tvRate1, tvRate2, tvRate3;
        EditText etAmt;
        Button btn1;
        Button btn2;
        Button btn3;
        ImageView ivStatus,btnWorth;

        public FriendViewHolder(View itemView) {
            super(itemView);
            tvquest = itemView.findViewById(R.id.tvQuest);
            tvRate1 = itemView.findViewById(R.id.tvRate1);
            tvRate2 = itemView.findViewById(R.id.tvRate2);
            tvRate3 = itemView.findViewById(R.id.tvRate3);
            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
            btn3 = itemView.findViewById(R.id.btn3);
            btnWorth = itemView.findViewById(R.id.btnWorth);
            etAmt = itemView.findViewById(R.id.etAmt);

        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {


        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;
        private ChildEventListener mChildEventListener2;
        private ChildEventListener mChildEventListener3;

        private List<String> mQuestIds = new ArrayList<>();
        private List<Quest> mQuest = new ArrayList<>();

        public FriendAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;


            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());


                        Quest quest = dataSnapshot.getValue(Quest.class);
                        if ( quest.cans.equals("U")) {

                            // [START_EXCLUDE]
                            // Update RecyclerView
                            mQuestIds.add(dataSnapshot.getKey());
                            mQuest.add(quest);
                            notifyItemInserted(mQuest.size() - 1);
                        }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    String userKey = dataSnapshot.getKey();
                    if (quest.cans.equals("U")) {

                        // [START_EXCLUDE]
                        int userIndex = mQuestIds.indexOf(userKey);
                        if (userIndex > -1) {
                            // Replace with the new data
                            mQuest.set(userIndex, quest);

                            // Update the RecyclerView
                            notifyItemChanged(userIndex);
                        } else {
                            Log.w(TAG, "onChildChanged:unknown_child:" + userKey);
                        }
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String userKey = dataSnapshot.getKey();
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    if (quest.cans.equals("U")) {

                        // [START_EXCLUDE]
                        int userIndex = mQuestIds.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQuestIds.remove(userIndex);
                            mQuest.remove(userIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(userIndex);
                        } else {
                            Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);
                        }
                        // [END_EXCLUDE]
                    }
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                    // A comment has changed position, use the key to determine if we are
                    // displaying this comment and if so move it.

                    // ...
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            ref.addChildEventListener(childEventListener);
            mChildEventListener = childEventListener;

      DatabaseReference md1=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            md1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usr=dataSnapshot.getValue(User.class);
                        wallet=usr.wallet;
                    //Toast.makeText(context, Integer.toString(usr.wallet.balance), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.question, viewGroup, false);

            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendViewHolder friendViewHolder, int i) {
            final Quest quest = mQuest.get(i);

           tvEmpt.setVisibility(View.GONE);
            friendViewHolder.tvquest.setText(quest.ques);
            friendViewHolder.btn1.setText(quest.opt1);
            friendViewHolder.btn2.setText(quest.opt2);
            friendViewHolder.btn3.setText(quest.opt3);
            friendViewHolder.tvRate1.setVisibility(View.GONE);
            friendViewHolder.tvRate2.setVisibility(View.GONE);
            friendViewHolder.tvRate3.setVisibility(View.GONE);
            final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child("quest_wall");
            mdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    quest_wall = dataSnapshot.getValue(Quest_wall.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            friendViewHolder.btnWorth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mdb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Quest_wall q=dataSnapshot.getValue(Quest_wall.class);
                            float rate1 = 0, rate2 = 0, rate3 = 0;

                            rate1=q.ctbids1/q.cbids1;
                            rate2=q.ctbids2/q.cbids2;
                            rate3=q.ctbids3/q.cbids3;



                            friendViewHolder.tvRate1.setText("X" + Float.toString(rate1));
                            friendViewHolder.tvRate2.setText("X" + Float.toString(rate2));
                            friendViewHolder.tvRate3.setText("X" + Float.toString(rate3));
                            friendViewHolder.tvRate1.setVisibility(View.VISIBLE);
                            friendViewHolder.tvRate2.setVisibility(View.VISIBLE);
                            friendViewHolder.tvRate3.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });

            if (quest.status == 1) {
             //   friendViewHolder.ivStatus.setVisibility(View.VISIBLE);
                friendViewHolder.etAmt.setVisibility(View.GONE);
                if(quest.myans.equals("A"))
                {
                    friendViewHolder.btn1.setBackgroundColor(Color.BLUE);
                    friendViewHolder.btn2.setBackgroundColor(0xFF42ACA8);
                    friendViewHolder.btn3.setBackgroundColor(0xFF42ACA8);
                }
                else if(quest.myans.equals("B"))
                {
                    friendViewHolder.btn1.setBackgroundColor(0xFF42ACA8);
                    friendViewHolder.btn2.setBackgroundColor(Color.BLUE);
                    friendViewHolder.btn3.setBackgroundColor(0xFF42ACA8);
                }
                else if(quest.myans.equals("C"))
                {
                    friendViewHolder.btn1.setBackgroundColor(0xFF42ACA8);
                    friendViewHolder.btn2.setBackgroundColor(0xFF42ACA8);
                    friendViewHolder.btn3.setBackgroundColor(Color.BLUE);
                }

            } else{
             //   friendViewHolder.ivStatus.setVisibility(View.GONE);
                friendViewHolder.etAmt.setVisibility(View.VISIBLE);
                friendViewHolder.btn1.setBackgroundColor(0xFF42ACA8);
                friendViewHolder.btn2.setBackgroundColor(0xFF42ACA8);
                friendViewHolder.btn3.setBackgroundColor(0xFF42ACA8);
            }
            friendViewHolder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet "+wallet.balance, Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                        else
                     {
                        float myrate=quest_wall.ctbids1/quest_wall.cbids1;
                        Quest qust = new Quest(quest.ques, quest.opt1, quest.opt2, quest.opt3, quest.qid, 1, parseInt(friendViewHolder.etAmt.getText().toString()),myrate, "A", quest.cans);
                        mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).setValue(qust);
                        updatequest(qust.mybid, "A", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));
                        updatewallet(qust.mybid,quest.qid);
                        Usrwal usr = new Usrwal(uId, parseInt(friendViewHolder.etAmt.getText().toString()),myrate,0);
                        mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(qust.qid).child("A").child(uId).setValue(usr);


                    }
                }
            });

            friendViewHolder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        float myrate=quest_wall.ctbids2/quest_wall.cbids2;
                        Quest qust = new Quest(quest.ques, quest.opt1, quest.opt2, quest.opt3, quest.qid, 1, parseInt(friendViewHolder.etAmt.getText().toString()),myrate, "B", quest.cans);
                        mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).setValue(qust);
                        updatequest(qust.mybid, "B", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));
                        // User us = new User(finalUsr.username, finalUsr.email, finalUsr.picId, finalUsr.uid, finalUsr.wallet - parseInt(friendViewHolder.etAmt.getText().toString()));
                        updatewallet(qust.mybid,quest.qid);
                        Usrwal usr = new Usrwal(uId, parseInt(friendViewHolder.etAmt.getText().toString()),myrate,0);

                        mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(qust.qid).child("B").child(uId).setValue(usr);



                    }
                }
            });

            friendViewHolder.btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        float myrate=quest_wall.ctbids1/quest_wall.cbids1;
                        Quest qust = new Quest(quest.ques, quest.opt1, quest.opt2, quest.opt3, quest.qid, 1, parseInt(friendViewHolder.etAmt.getText().toString()), myrate,"C", quest.cans);
                        mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).setValue(qust);
                        updatequest(qust.mybid, "C", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));
                        // User us = new User(finalUsr.username, finalUsr.email, finalUsr.picId, finalUsr.uid, finalUsr.wallet - parseInt(friendViewHolder.etAmt.getText().toString()));
                        updatewallet(qust.mybid,quest.qid);
                        Usrwal usr = new Usrwal(uId, parseInt(friendViewHolder.etAmt.getText().toString()),myrate,0);

                        mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(qust.qid).child("C").child(uId).setValue(usr);



                    }
                }
            });


        }



        @Override
        public int getItemCount() {
            return mQuest.size();
        }
        public void updatequest(final float bid,final String opt,final String qid,final DatabaseReference mD)
        {
            mD.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    AllQuest allQuest=mutableData.getValue(AllQuest.class);
                    if(allQuest==null)
                    {
                        return (Transaction.success(mutableData));
                    }
                    else {
                        if(opt.equals("A"))
                        {
                            allQuest.quest_wall.bids1=allQuest.quest_wall.bids1+bid;
                            allQuest.quest_wall.cbids1=bid;
                            allQuest.quest_wall.ctbids1=bid;
                            allQuest.quest_wall.ctbids2=allQuest.quest_wall.ctbids2+bid;
                            allQuest.quest_wall.ctbids3=allQuest.quest_wall.ctbids3+bid;
                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else if(opt.equals("B"))
                        {
                            allQuest.quest_wall.bids2=allQuest.quest_wall.bids2+bid;
                            allQuest.quest_wall.ctbids2=bid;
                            allQuest.quest_wall.cbids2=bid;
                            allQuest.quest_wall.ctbids3=allQuest.quest_wall.ctbids3+bid;
                            allQuest.quest_wall.ctbids1=allQuest.quest_wall.ctbids1+bid;
                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else
                        {
                            allQuest.quest_wall.bids3=allQuest.quest_wall.bids3+bid;
                            allQuest.quest_wall.ctbids3=bid;
                            allQuest.quest_wall.cbids3=bid;
                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }

                    }
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });

        }





        public void updatewallet(final float mybid,final String qid)
        {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    User user=mutableData.getValue(User.class);

                if(user==null)
                {

                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    user.wallet.balance=user.wallet.balance-mybid;
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();

                    user.wallet.lastTransactions.add(new transactions(mybid*-1,qid,arr[1],currentDate,arr[2]));
                    mutableData.setValue(user);
                    return (Transaction.success(mutableData));
                }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });
        }



        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
            if (mChildEventListener2!= null) {
                mDatabaseReference.removeEventListener(mChildEventListener2);
            }
            if (mChildEventListener3!= null) {
                mDatabaseReference.removeEventListener(mChildEventListener3);
            }
        }

    }

}
