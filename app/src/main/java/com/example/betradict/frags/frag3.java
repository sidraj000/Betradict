package com.example.betradict.frags;


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
import com.example.betradict.Class.Quest_wall;
import com.example.betradict.Class.User;
import com.example.betradict.Class.Usrwal;
import com.example.betradict.Class.Wallet;
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
public class frag3 extends Fragment {

    private RecyclerView mRecycler;
    private AnsweredAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Bundle b;
    public String arr[];
    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public Quest_wall quest_wall=null;
    public Usrwal u;
    public int count=0;
    TextView ansEmpt;



    public frag3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_frag3, container, false);
        mRecycler = view.findViewById(R.id.questAnsList);
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
        ansEmpt=view.findViewById(R.id.questAnsEmpt);


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AnsweredAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanupListener();
    }

    private static class AnsweredViewHolder extends RecyclerView.ViewHolder {

        TextView tvquest,tvAns,tvRate,tvAmt,tvCans,tvPop;
        ImageView ivStatus;

        public AnsweredViewHolder(View itemView) {
            super(itemView);
            tvquest = itemView.findViewById(R.id.aQuest);
            tvAns=itemView.findViewById(R.id.aAns);
            tvRate=itemView.findViewById(R.id.aRate);
            tvAmt=itemView.findViewById(R.id.aAmt);
            tvCans=itemView.findViewById(R.id.aCans);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvPop=itemView.findViewById(R.id.aPop);


        }
    }


    private class AnsweredAdapter extends RecyclerView.Adapter<AnsweredViewHolder> {


        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;
        private ChildEventListener mChildEventListener2;
        private ChildEventListener mChildEventListener3;

        private List<String> mQuestIds = new ArrayList<>();
        private List<Quest> mQuest = new ArrayList<>();
        public List<AllQuest> mAllQuest = new ArrayList<>();
        public List<String> mQid = new ArrayList<>();
        public List<String> mUserIds = new ArrayList<>();
        public List<User> mUser = new ArrayList<>();

        public AnsweredAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    Quest qu = dataSnapshot.getValue(Quest.class);
                    if (!qu.myans.equals("U")) {

                        // [START_EXCLUDE]
                        // Update RecyclerView
                        mQuestIds.add(dataSnapshot.getKey());
                        mQuest.add(qu);
                        notifyItemInserted(mQuest.size() - 1);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    String userKey = dataSnapshot.getKey();
                    if (!quest.myans.equals("U")) {

                        // [START_EXCLUDE]
                        int userIndex = mQuestIds.indexOf(userKey);
                        if (userIndex > -1) {
                            // Replace with the new data
                            mQuest.set(userIndex, quest);

                            // Update the RecyclerView
                            notifyItemChanged(userIndex);
                        } else {
                            mQuest.add(quest);
                            mQuestIds.add(userKey);
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
                    if (!quest.myans.equals("U")) {

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


        }

        @NonNull
        @Override
        public AnsweredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.answered_questions, viewGroup, false);

            return new AnsweredViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AnsweredViewHolder friendViewHolder, int i) {
           ansEmpt.setVisibility(View.GONE);
            friendViewHolder.tvPop.setVisibility(View.GONE);
            final Quest quest = mQuest.get(i);

            if(quest.cans.equals("U"))
            {
                friendViewHolder.tvCans.setVisibility(View.GONE);
            }
            else
            {
                friendViewHolder.tvCans.setVisibility(View.VISIBLE);
                friendViewHolder.tvCans.setText(quest.cans);
                friendViewHolder.ivStatus.setVisibility(View.GONE);
            }
            friendViewHolder.tvquest.setText(quest.ques);
            friendViewHolder.tvAns.setText("My Ans: "+quest.myans);
            friendViewHolder.tvRate.setText("My Rate: "+Float.toString(quest.myrate));
            friendViewHolder.tvAmt.setText("My Amount "+Float.toString(quest.mybid));


            DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child("quest_wall");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    quest_wall=dataSnapshot.getValue(Quest_wall.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            friendViewHolder.ivStatus.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    float rate1 = 0, rate2 = 0, rate3 = 0,rate;

                    rate1=quest_wall.ctbids1/quest_wall.cbids1;
                    rate2=quest_wall.ctbids2/quest_wall.cbids2;
                    rate3=quest_wall.ctbids2/quest_wall.cbids2;
                    if(rate1<rate2&&rate1<rate3)
                    {
                        rate=rate1;
                    }
                    else if(rate2<rate1&&rate2<rate3)
                    {
                        rate=rate2;
                    }
                    else {
                        rate = rate3;
                    }
                    final float r=rate/2;
                    DatabaseReference database=FirebaseDatabase.getInstance().getReference().child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child(quest.myans).child(uId);
                    database.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            u=dataSnapshot.getValue(Usrwal.class);
                            if(u.cashoutstatus==1)
                            {
                                friendViewHolder.tvPop.setText("Already cashed out for this Question");
                                friendViewHolder.tvPop.setVisibility(View.VISIBLE);
                            }
                            else {

                                if(count>0) {
                                    updateqwall(u.bid * r, quest.qid);
                                    friendViewHolder.tvPop.setText("Successfully done");
                                    friendViewHolder.tvPop.setVisibility(View.VISIBLE);
                                    DatabaseReference m = FirebaseDatabase.getInstance().getReference();
                                    m.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child(quest.myans).child(uId).child("cashoutstatus").setValue(1);
                                    cashout(u.bid, quest.qid, r);
                                    count=0;
                                }
                                else{
                                    friendViewHolder.tvPop.setText("Amount to be credited: "+Integer.toString((int) (u.bid * r)));
                                    friendViewHolder.tvPop.setVisibility(View.VISIBLE);
                                    count++;
                                }

                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }
            });




        }



        @Override
        public int getItemCount()
        {

            return mQuest.size();
        }

        public void updateqwall(final float bid,final String qid)
        {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(qid);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    AllQuest qest=mutableData.getValue(AllQuest.class);

                    if(qest==null)
                    {

                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                        return (Transaction.success(mutableData));
                    }
                    else {

                            qest.quest_wall.profit=qest.quest_wall.profit-bid;

                        mutableData.setValue(qest);
                        return (Transaction.success(mutableData));
                    }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });
        }
        public void updateusrwal(final String qid,final String  ans)
        {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(qid).child(ans).child(uId);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Usrwal us=mutableData.getValue(Usrwal.class);

                    if(us==null)
                    {

                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                        return (Transaction.success(mutableData));
                    }
                    else {
                        us.cashoutstatus=1;
                        mutableData.setValue(us);
                        return (Transaction.success(mutableData));
                    }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });
        }

        public void cashout(final float mybid,final String qid,final float r)
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
                        float money=mybid*r;
                        user.wallet.balance=user.wallet.balance+money;
                        Calendar cal = Calendar.getInstance();
                        Date currentDate = cal.getTime();

                        user.wallet.lastTransactions.add(new transactions(money,qid,arr[1],currentDate,arr[2]));
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
