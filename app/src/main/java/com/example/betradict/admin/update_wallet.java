package com.example.betradict.admin;


import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.betradict.Class.AllQuest;
import com.example.betradict.Class.Quest;
import com.example.betradict.R;
import com.example.betradict.Class.User;
import com.example.betradict.Class.Usrwal;
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

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Integer.max;
import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class update_wallet extends Fragment {

    private RecyclerView mRecycler;
    private FriendAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    Button btn;
    public Bundle b;
    public String arr[];
    float amt=0;


    public update_wallet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_update_wallet, container, false);
        mRecycler = view.findViewById(R.id.wall_list);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        b=getArguments();
        arr=b.getStringArray("details");
        mFriendsReference = FirebaseDatabase.getInstance().getReference();
        mManager = new LinearLayoutManager(getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);

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

        TextView tvquest, tvBids1, tvBids2, tvBids3, tvProfit;
        Button btnWallet;
        Button btn1;


        public FriendViewHolder(View itemView) {
            super(itemView);
            tvquest = itemView.findViewById(R.id.uQuest);
            tvBids1 = itemView.findViewById(R.id.uBid1);
            tvBids2 = itemView.findViewById(R.id.uBid2);
            tvBids3 = itemView.findViewById(R.id.uBid3);
            tvProfit=itemView.findViewById(R.id.tvProfit);
            btnWallet=itemView.findViewById(R.id.btnUpdate);

        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {


        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;
        private ChildEventListener mChildEventListener2;
        private ChildEventListener mChildEventListener3;
        private ChildEventListener mChildEventListenerz;

        private List<String> mQuestIds = new ArrayList<>();
        private List<Quest> mQuest = new ArrayList<>();
        public List<AllQuest> mAllQuest = new ArrayList<>();
        public List<String> mQid = new ArrayList<>();
        public List<String> mUserIds = new ArrayList<>();
        public List<User> mUser = new ArrayList<>();
        public List<List<User>> mNusr = new ArrayList<>();

        public FriendAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            // Create child event listener
            // [START child_event_listener_recycler]

            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]);


            ChildEventListener childEventListener2 = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String key = dataSnapshot.getKey();
                    if (!allQuest.quest_wall.ans.equals("U")) {

                        mAllQuest.add(allQuest);
                        mQid.add(key);

                        notifyItemInserted(mAllQuest.size() - 1);
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String key = dataSnapshot.getKey();
                    if (!allQuest.quest_wall.ans.equals("U")) {
                      int index=mQid.indexOf(key);
                      if(index>-1) {
                          mAllQuest.set(index, allQuest);
                          notifyItemChanged(index);
                      }
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String userKey = dataSnapshot.getKey();
                    if (!allQuest.quest_wall.ans.equals("U")) {
                        // [START_EXCLUDE]
                        int userIndex = mQid.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQid.remove(userIndex);
                            mAllQuest.remove(userIndex);
                            notifyItemRemoved(userIndex);
                            // notifyDataSetChanged();

                            // Update the RecyclerView
                        } else {
                            Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);
                        }
                    }
                    // [END_EXCLUDE]
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
            mDatabase.addChildEventListener(childEventListener2);



        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.wall_update, viewGroup, false);

            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendViewHolder friendViewHolder, final int i) {

            final List<Usrwal> mNusr = new ArrayList<>();
            final List<String> mNkey = new ArrayList<>();
            if(mAllQuest.get(i).quest_wall.wStatus==1)
            {
                friendViewHolder.btnWallet.setVisibility(View.GONE);
                friendViewHolder.tvProfit.setText(Float.toString(mAllQuest.get(i).quest_wall.profit));
            }
            else {
                friendViewHolder.btnWallet.setVisibility(View.VISIBLE);
                friendViewHolder.tvquest.setText(mAllQuest.get(i).ques);
                friendViewHolder.tvBids1.setText(Float.toString(mAllQuest.get(i).quest_wall.bids1));
                friendViewHolder.tvBids2.setText(Float.toString(mAllQuest.get(i).quest_wall.bids2));
                friendViewHolder.tvBids3.setText(Float.toString(mAllQuest.get(i).quest_wall.bids3));
                friendViewHolder.btnWallet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference mD = FirebaseDatabase.getInstance().getReference().child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(mAllQuest.get(i).qid).child(mAllQuest.get(i).quest_wall.ans);
                        ChildEventListener childEventListenerz = new ChildEventListener() {

                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());


                                final Usrwal u = dataSnapshot.getValue(Usrwal.class);
                                if (u.cashoutstatus == 0) {


                                    float add;
                                    final int k = i;
                                    if (mAllQuest.get(k).quest_wall.ans.equals("A")) {

                                        add = u.bid * u.rate;
                                        amt = amt + add;

                                        DatabaseReference sd = FirebaseDatabase.getInstance().getReference();
                                        sd.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(mAllQuest.get(k).qid).child("quest_wall").child("wStatus").setValue(1);
                                        updatewallet(add, u.uid, mAllQuest.get(k).qid);


                                    } else if (mAllQuest.get(k).quest_wall.ans.equals("B")) {

                                        add = u.bid * u.rate;
                                        amt = amt + add;
                                        updatewallet(add, u.uid, mAllQuest.get(k).qid);
                                        DatabaseReference sd = FirebaseDatabase.getInstance().getReference();
                                        sd.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(mAllQuest.get(k).qid).child("quest_wall").child("wStatus").setValue(1);


                                    } else if (mAllQuest.get(k).quest_wall.ans.equals("C")) {

                                        add = u.bid * u.rate;
                                        amt = amt + add;
                                        updatewallet(add, u.uid, mAllQuest.get(k).qid);
                                        DatabaseReference sd = FirebaseDatabase.getInstance().getReference();
                                        sd.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(mAllQuest.get(k).qid).child("quest_wall").child("wStatus").setValue(1);


                                    }
                                }


                            }


                            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());


                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };
                        mD.addChildEventListener(childEventListenerz);
                        mChildEventListenerz = childEventListenerz;
                        DatabaseReference sd = FirebaseDatabase.getInstance().getReference();
                        sd.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(mAllQuest.get(i).qid).child("quest_wall").child("wStatus").setValue(1);

                        //   friendViewHolder.tvProfit.setText(Float.toString(amt));
                        updateprofit(amt, mAllQuest.get(i).qid);

                    }
                });

            }

        }


        @Override
        public int getItemCount() {
            return mAllQuest.size();
        }
        public void updateprofit(final float amt,final String qid)
        {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[2]).child(qid);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    AllQuest q=mutableData.getValue(AllQuest.class);

                    if(q==null)
                    {

                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                        return (Transaction.success(mutableData));
                    }
                    else {
                       q.quest_wall.profit=q.quest_wall.profit+amt;
                        mutableData.setValue(q);
                        return (Transaction.success(mutableData));
                    }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });
        }


        public void updatewallet(final float amt, String id, final String qid) {

            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(id);
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
                        user.wallet.balance=user.wallet.balance+amt;
                        Calendar cal = Calendar.getInstance();
                        Date currentDate = cal.getTime();
                        user.wallet.lastTransactions.add(new transactions(amt,qid,arr[1],currentDate,arr[2]));
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
            if (mChildEventListener2 != null) {
                mDatabaseReference.removeEventListener(mChildEventListener2);
            }
            if (mChildEventListener3 != null) {
                mDatabaseReference.removeEventListener(mChildEventListener3);
            }
            if (mChildEventListenerz != null) {
                mDatabaseReference.removeEventListener(mChildEventListenerz);
            }

        }


    }
}


