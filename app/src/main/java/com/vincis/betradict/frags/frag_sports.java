package com.vincis.betradict.frags;


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.User;
import com.vincis.betradict.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class frag_sports extends Fragment {
    private RecyclerView mRecycler;
    private QuestAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mRef;
  //  private ChildEventListener mChildEventListener;
    //private ChildEventListener mChildEventListener2;
    private ChildEventListener mChildEventListener3;
    //public List<AllQuest> mAllQuest=new ArrayList<>();
//    public List<String> mQid=new ArrayList<>();

    FirebaseUser muser;


    public frag_sports() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag_sports, container, false);
        mRecycler = view.findViewById(R.id.questList);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        mRef = FirebaseDatabase.getInstance().getReference().child("quest_usr").child(muser.getUid());
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

        mAdapter = new QuestAdapter(getContext(), mRef);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanupListener();
    }

    private static class QuestViewHolder extends RecyclerView.ViewHolder {

        public TextView tvquest, tvRate1, tvRate2, tvRate3;
        public EditText etAmt;
        public Button btn1;
        public Button btn2;
        public Button btn3;
        public Button btnWorth;
        public ImageView ivStatus;

        public QuestViewHolder(View itemView) {
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
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }

    private class QuestAdapter extends RecyclerView.Adapter<QuestViewHolder> {


        private Context mContext;
        private DatabaseReference mDatabaseReference;

        private List<String> mUserIds = new ArrayList<>();
        private List<User> mUser = new ArrayList<>();
        public List<Quest> mQuest=new ArrayList<>();
        public List<String>mKey=new ArrayList<>();

        public QuestAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;


            ChildEventListener childEventListener3=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Quest qu=dataSnapshot.getValue(Quest.class);
                    String key=dataSnapshot.getKey();
                    mQuest.add(qu);
                    mKey.add(key);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Quest qu=dataSnapshot.getValue(Quest.class);
                    String key=dataSnapshot.getKey();
                    Integer ind=mKey.indexOf(key);
                    mQuest.set(ind,qu);

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
            };
            mChildEventListener3=childEventListener3;
            mDatabaseReference.addChildEventListener(childEventListener3);
            //Toast.makeText(context,"hii", Toast.LENGTH_SHORT).show();





        }

        @NonNull
        @Override
        public QuestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view = inflater.inflate(R.layout.question, viewGroup, false);
            return new QuestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final QuestViewHolder questViewHolder, int i) {
            final Quest quest=mQuest.get(i);
            questViewHolder.tvquest.setText(quest.ques);
            questViewHolder.btn1.setText(quest.opt1);
            questViewHolder.btn2.setText(quest.opt2);
            questViewHolder.btn3.setText(quest.opt3);
        //    Toast.makeText(mContext, "done", Toast.LENGTH_SHORT).show();



        }

        @Override
        public int getItemCount() {
            return mQuest.size();
        }

        public void cleanupListener() {
            if (mChildEventListener3 != null) {
                mDatabaseReference.removeEventListener(mChildEventListener3);
            }
        }


    }
}


// Create child event listener
// [START child_event_listener_recycler]
           /* ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                    // A new comment has been added, add it to the displayed list
                    User user = dataSnapshot.getValue(User.class);

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mUserIds.add(dataSnapshot.getKey());
                    mUser.add(user);
                    notifyItemInserted(mUser.size() - 1);
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

                        // Update the RecyclerView
                        notifyItemChanged(userIndex);
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
                    if (userIndex > -1) {
                        // Remove data from the list
                        mUserIds.remove(userIndex);
                        mUser.remove(userIndex);

                        // Update the RecyclerView
                        notifyItemRemoved(userIndex);
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
            ref.addChildEventListener(childEventListener);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener = childEventListener;


            DatabaseReference mDatabase;
            mDatabase=FirebaseDatabase.getInstance().getReference().child("quest");

            ChildEventListener childEventListener2 = new ChildEventListener() {
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
                    int userIndex = mQid.indexOf(userKey);
                    if (userIndex> -1) {
                        // Remove data from the list
                        mQid.remove(userIndex);
                        mAllQuest.remove(userIndex);

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
            mDatabase.addChildEventListener(childEventListener2);
            // [END child_event_listener_recycler]

            // Store reference to listener so it can be removed on app stop
            mChildEventListener2 = childEventListener2;*/

//DatabaseReference mD=FirebaseDatabase.getInstance().getReference().child("quest_usr").child(muser.getUid());