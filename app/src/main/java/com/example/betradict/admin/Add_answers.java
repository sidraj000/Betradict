package com.example.betradict.admin;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Add_answers extends Fragment {
    private RecyclerView mRecycler;
    private AnsAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mRef;
    FirebaseUser muser;
    public Bundle b;
    public String arr[];


    public Add_answers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_answers, container, false);
        mRecycler = view.findViewById(R.id.ansList);
        b=getArguments();
        arr=b.getStringArray("details");
        muser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference();
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

        mAdapter = new AnsAdapter(getContext(), mRef);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanupListener();
    }

    private static class AnsViewHolder extends RecyclerView.ViewHolder {

        TextView ansQues,ansConfirm;
        Button  ans1,ans2,ans3,ans4,btnConfirm,btnHold,btnRe;

        public AnsViewHolder(View itemView) {
            super(itemView);
           ansQues=itemView.findViewById(R.id.ansQuest);
           ans1=itemView.findViewById(R.id.ans1);
            ans2=itemView.findViewById(R.id.ans2);
            ans3=itemView.findViewById(R.id.ans3);
            ans4=itemView.findViewById(R.id.ans4);
            btnHold=itemView.findViewById(R.id.btnHold);
            ansConfirm=itemView.findViewById(R.id.ansConf);
            btnConfirm=itemView.findViewById(R.id.btnConfirm);
            ansConfirm.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            btnRe=itemView.findViewById(R.id.btnRe);
            btnRe.setVisibility(View.GONE);
        }
    }

    private class AnsAdapter extends RecyclerView.Adapter<AnsViewHolder> {

        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;
        private ChildEventListener mChildEventListener2;
        private ChildEventListener mChildEventListener3;
        public List<AllQuest> mAllQuest=new ArrayList<>();
        public List<String> mQid=new ArrayList<>();
        private List<String> mQuestIds = new ArrayList<>();
        private List<Quest> mQuest = new ArrayList<>();

        public List<String> mUserIds = new ArrayList<>();
        public List<User> mUser = new ArrayList<>( );
        public String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();


        public AnsAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;

            DatabaseReference mDatabase;
            mDatabase=FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]);

            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String key=dataSnapshot.getKey();



                        // [START_EXCLUDE]
                        // Update RecyclerView
                        mAllQuest.add(allQuest);
                        mQid.add(key);
                        // notifyDataSetChanged();
                        notifyItemInserted(mAllQuest.size() - 1);
                        // [END_EXCLUDE]

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);
                    String userKey=dataSnapshot.getKey();


                        // [START_EXCLUDE]
                        int userIndex = mQid.indexOf(userKey);
                        if (userIndex > -1) {
                            // Replace with the new data
                            mAllQuest.set(userIndex, allQuest);
                            // notifyDataSetChanged();
                            notifyItemChanged(userIndex);

                            // Update the RecyclerView
                        } else {
                            Log.w(TAG, "onChildChanged:unknown_child:" + userKey);
                        }
                        // [END_EXCLUDE]

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String userKey = dataSnapshot.getKey();
                    AllQuest allQuest = dataSnapshot.getValue(AllQuest.class);



                        // [START_EXCLUDE]
                        int userIndex = mQid.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQid.remove(userIndex);
                            mAllQuest.remove(userIndex);
                            // notifyDataSetChanged();
                            notifyItemRemoved(userIndex);

                            // Update the RecyclerView
                        } else {
                            Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);

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
            mDatabase.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener= childEventListener;


            DatabaseReference mRef2;


            mRef2=FirebaseDatabase.getInstance().getReference().child("users");


            ChildEventListener childEventListener3 = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    User user = dataSnapshot.getValue(User.class);
                    String key=dataSnapshot.getKey();


                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mUser.add(user);
                    mUserIds.add(key);
                    //notifyItemInserted(mUser.size() - 1);
                    notifyDataSetChanged();
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    User user = dataSnapshot.getValue(User.class);
                    String userKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int userIndex = mUserIds.indexOf(userKey);
                    if (userIndex > -1) {
                        // Replace with the new data
                        mUser.set(userIndex, user);
                        notifyDataSetChanged();
                        // notifyItemChanged(userIndex);
                        // Update the RecyclerView
                    } else {
                        Log.w(TAG, "onChildChanged:unknown_child:" + userKey);
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String userKey = dataSnapshot.getKey();

                    // [START_EXCLUDE]
                    int userIndex = mUserIds.indexOf(userKey);
                    if (userIndex> -1) {
                        // Remove data from the list
                        mUserIds.remove(userIndex);
                        mUser.remove(userIndex);
                        //notifyItemRemoved(userIndex);
                        notifyDataSetChanged();

                        // Update the RecyclerView
                    } else {
                        Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);
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
            mRef2.addChildEventListener(childEventListener3);
            mChildEventListener3=childEventListener3;



        }


        @NonNull
        @Override
        public AnsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.input_answer, viewGroup, false);
            return new AnsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AnsViewHolder ansViewHolder, int i) {

            final AllQuest allQuest = mAllQuest.get(i);
            ansViewHolder.ansQues.setText(allQuest.ques);
            ansViewHolder.ans1.setText(allQuest.opt1);
            ansViewHolder.ans2.setText(allQuest.opt2);
            ansViewHolder.ans3.setText(allQuest.opt3);

                if(allQuest.quest_wall.ans.equals("H"))
                {
                    ansViewHolder.ansConfirm.setText("Ans: On Hold");
                    ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.btnHold.setVisibility(View.GONE);
                    ansViewHolder.ans1.setVisibility(View.VISIBLE);
                    ansViewHolder.ans2.setVisibility(View.VISIBLE);
                    ansViewHolder.ans3.setVisibility(View.VISIBLE);
                    ansViewHolder.ans4.setVisibility(View.VISIBLE);
                    ansViewHolder.btnConfirm.setVisibility(View.GONE);
                    ansViewHolder.btnRe.setVisibility(View.GONE);

                }
                else if(allQuest.quest_wall.ans.equals("A"))
                {
                    ansViewHolder.ans4.setVisibility(View.GONE);
                    ansViewHolder.ans3.setVisibility(View.GONE);
                    ansViewHolder.ans2.setVisibility(View.GONE);
                    ansViewHolder.ans1.setVisibility(View.GONE);
                    ansViewHolder.btnConfirm.setVisibility(View.GONE);
                    ansViewHolder.ansConfirm.setText("Ans: A");
                    ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                    ansViewHolder.btnHold.setVisibility(View.GONE);
                    ansViewHolder.btnRe.setVisibility(View.VISIBLE);


                }
                else if(allQuest.quest_wall.ans.equals("B"))
                {
                    ansViewHolder.ans4.setVisibility(View.GONE);
                    ansViewHolder.ans3.setVisibility(View.GONE);
                    ansViewHolder.ans2.setVisibility(View.GONE);
                    ansViewHolder.ans1.setVisibility(View.GONE);
                    ansViewHolder.btnConfirm.setVisibility(View.GONE);
                    ansViewHolder.ansConfirm.setText("Ans: B");
                    ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                    ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.btnHold.setVisibility(View.GONE);
                    ansViewHolder.btnRe.setVisibility(View.VISIBLE);


                }
                else if(allQuest.quest_wall.ans.equals("C"))
                {
                    ansViewHolder.ans4.setVisibility(View.GONE);
                    ansViewHolder.ans3.setVisibility(View.GONE);
                    ansViewHolder.ans2.setVisibility(View.GONE);
                    ansViewHolder.ans1.setVisibility(View.GONE);
                    ansViewHolder.btnConfirm.setVisibility(View.GONE);
                    ansViewHolder.ansConfirm.setText("Ans: C");
                    ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                    ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.btnHold.setVisibility(View.GONE);
                    ansViewHolder.btnRe.setVisibility(View.VISIBLE);


                }
                else if(allQuest.quest_wall.ans.equals("U"))
                {
                    ansViewHolder.ans1.setVisibility(View.VISIBLE);
                    ansViewHolder.ans2.setVisibility(View.VISIBLE);
                    ansViewHolder.ans3.setVisibility(View.VISIBLE);
                    ansViewHolder.ans4.setVisibility(View.GONE);
                    ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                    ansViewHolder.ansConfirm.setVisibility(View.GONE);
                    ansViewHolder.btnConfirm.setVisibility(View.VISIBLE);
                    ansViewHolder.btnRe.setVisibility(View.GONE);
                    ansViewHolder.btnHold.setVisibility(View.VISIBLE);
                }

                ansViewHolder.ans1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ansViewHolder.ansConfirm.setText("A");
                        ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               // AllQuest qu = new AllQuest(allQuest.ques, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, allQuest.bids1, allQuest.bids2, allQuest.bids3, "A",allQuest.wStatus);
                                mDatabaseReference.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("quest_wall").child("ans").setValue("A");
                                for(int p=0;p<mUser.size();p++)
                                {
                                    mDatabaseReference.child("quest_usr").child(mUser.get(p).per.uid).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("cans").setValue("A");
                                }

                                ansViewHolder.ans3.setVisibility(View.GONE);
                                ansViewHolder.ans2.setVisibility(View.GONE);
                                ansViewHolder.ans1.setVisibility(View.GONE);
                                ansViewHolder.btnConfirm.setVisibility(View.GONE);
                                ansViewHolder.ansConfirm.setText("Ans: A");
                                ansViewHolder.btnHold.setVisibility(View.GONE);
                                //DatabaseReference mD=FirebaseDatabase.getInstance().getReference().child("quest_opt").child(allQuest.qid).child("A");

                            }
                        });
                    }
                });


                ansViewHolder.ans2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ansViewHolder.ansConfirm.setText("B");
                        ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDatabaseReference.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("quest_wall").child("ans").setValue("B");
                                for(int p=0;p<mUser.size();p++)
                                {
                                    mDatabaseReference.child("quest_usr").child(mUser.get(p).per.uid).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("cans").setValue("B");
                                }


                                ansViewHolder.ans3.setVisibility(View.GONE);
                                ansViewHolder.ans2.setVisibility(View.GONE);
                                ansViewHolder.ans1.setVisibility(View.GONE);
                                ansViewHolder.btnConfirm.setVisibility(View.GONE);
                                ansViewHolder.ansConfirm.setText("Ans: B");
                                ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                                ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                                ansViewHolder.btnHold.setVisibility(View.GONE);


                            }
                        });
                    }
                });
                ansViewHolder.ans3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ansViewHolder.ansConfirm.setText("C");
                        ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDatabaseReference.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("quest_wall").child("ans").setValue("C");
                                for(int p=0;p<mUser.size();p++)
                                {
                                    mDatabaseReference.child("quest_usr").child(mUser.get(p).per.uid).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("cans").setValue("C");
                                }

                                ansViewHolder.ans3.setVisibility(View.GONE);
                                ansViewHolder.ans2.setVisibility(View.GONE);
                                ansViewHolder.ans1.setVisibility(View.GONE);
                                ansViewHolder.btnConfirm.setVisibility(View.GONE);
                                ansViewHolder.ansConfirm.setText("Ans: C");
                                ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                                ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                                ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                                ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                                ansViewHolder.btnHold.setVisibility(View.GONE);


                            }
                        });
                    }
                });
                ansViewHolder.ans4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ansViewHolder.ansConfirm.setText("U");
                        ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDatabaseReference.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("quest_wall").child("ans").setValue("U");
                                for(int p=0;p<mUser.size();p++)
                                {
                                    mDatabaseReference.child("quest_usr").child(mUser.get(p).per.uid).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("cans").setValue("U");
                                }

                                ansViewHolder.ans3.setVisibility(View.GONE);
                                ansViewHolder.ans2.setVisibility(View.GONE);
                                ansViewHolder.ans1.setVisibility(View.GONE);
                                ansViewHolder.btnConfirm.setVisibility(View.GONE);
                                ansViewHolder.ansConfirm.setText("Ans: C");
                                ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                                ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                                ansViewHolder.ansQues.setVisibility(View.VISIBLE);
                                ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                                ansViewHolder.btnHold.setVisibility(View.GONE);


                            }
                        });
                    }
                });
                ansViewHolder.btnHold.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabaseReference.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("quest_wall").child("ans").setValue("H");
                        for(int p=0;p<mUser.size();p++)
                        {
                            mDatabaseReference.child("quest_usr").child(mUser.get(p).per.uid).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("cans").setValue("H");
                        }
                        ansViewHolder.ansConfirm.setText("Ans: On Hold");
                        ansViewHolder.ansConfirm.setVisibility(View.VISIBLE);
                        ansViewHolder.btnHold.setVisibility(View.GONE);
                        ansViewHolder.ans1.setVisibility(View.VISIBLE);
                        ansViewHolder.ans2.setVisibility(View.VISIBLE);
                        ansViewHolder.ans3.setVisibility(View.VISIBLE);
                        ansViewHolder.ans4.setVisibility(View.VISIBLE);
                        ansViewHolder.btnConfirm.setVisibility(View.GONE);
                        ansViewHolder.btnRe.setVisibility(View.GONE);
                    }
                });
                ansViewHolder.btnRe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ansViewHolder.ans1.setVisibility(View.VISIBLE);
                        ansViewHolder.ans2.setVisibility(View.VISIBLE);
                        ansViewHolder.ans3.setVisibility(View.VISIBLE);
                        ansViewHolder.ans4.setVisibility(View.VISIBLE);
                        ansViewHolder.btnHold.setVisibility(View.VISIBLE);
                        ansViewHolder.btnRe.setVisibility(View.GONE);

                    }
                });


        }

        @Override
        public int getItemCount() {

            return mAllQuest.size();
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
        }
    }
}