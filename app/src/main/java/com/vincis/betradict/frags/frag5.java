package com.vincis.betradict.frags;


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
import android.widget.ImageView;
import android.widget.TextView;

import com.vincis.betradict.Class.AllQuest;
import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.Quest_wall;
import com.vincis.betradict.Class.User;
import com.vincis.betradict.Class.Usrwal;
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

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class frag5 extends Fragment {
    String det[];
    private RecyclerView mRecycler;
    private AnsweredAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Bundle b;
    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public Quest_wall quest_wall = null;
    public Usrwal u;
    public int count = 0;


    public frag5() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle b = getArguments();
        det = b.getStringArray("det");
        View view = inflater.inflate(R.layout.fragment_frag5, container, false);
        mRecycler = view.findViewById(R.id.questAnsPList);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("quest_usr").child(muser.getUid()).child("cricket").child(det[0]).child(det[1]);
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

        mAdapter = new AnsweredAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private static class AnsweredViewHolder extends RecyclerView.ViewHolder {

        TextView tvquest, tvAns, tvRate, tvAmt, tvCans, tvPop;
        ImageView ivStatus;

        public AnsweredViewHolder(View itemView) {
            super(itemView);
            tvquest = itemView.findViewById(R.id.aQuest);
            tvAns = itemView.findViewById(R.id.aAns);
            tvRate = itemView.findViewById(R.id.aRate);
            tvAmt = itemView.findViewById(R.id.aAmt);
            tvCans = itemView.findViewById(R.id.aCans);
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvPop = itemView.findViewById(R.id.aPop);


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

                    // [START_EXCLUDE]
                    // Update RecyclerView
                    mQuestIds.add(dataSnapshot.getKey());
                    mQuest.add(qu);
                    notifyItemInserted(mQuest.size() - 1);

                    // [END_EXCLUDE]
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                    Quest quest = dataSnapshot.getValue(Quest.class);
                    String userKey = dataSnapshot.getKey();


                    // [START_EXCLUDE]
                    int userIndex = mQuestIds.indexOf(userKey);
                    if (userIndex > -1) {
                        mQuest.set(userIndex, quest);

                        notifyItemChanged(userIndex);
                    }
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    String userKey = dataSnapshot.getKey();


                    int userIndex = mQuestIds.indexOf(userKey);
                    if (userIndex > -1) {
                        mQuestIds.remove(userIndex);
                        mQuest.remove(userIndex);
                        notifyItemRemoved(userIndex);
                    }

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


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

            friendViewHolder.tvPop.setVisibility(View.GONE);
            friendViewHolder.ivStatus.setVisibility(View.GONE);
            final Quest quest = mQuest.get(i);
            friendViewHolder.tvquest.setText(quest.ques);

            friendViewHolder.tvCans.setVisibility(View.GONE);
            if (!quest.cans.equals("U")) {
                friendViewHolder.tvCans.setText(quest.cans);
                friendViewHolder.tvCans.setVisibility(View.VISIBLE);
            }
            if (quest.myans.equals("U")) {
                friendViewHolder.tvAmt.setVisibility(View.GONE);
                friendViewHolder.tvAns.setText("UnAnswered");
                friendViewHolder.tvRate.setVisibility(View.GONE);
            } else {
                friendViewHolder.tvAmt.setText(Float.toString(quest.mybid));
                friendViewHolder.tvAmt.setVisibility(View.VISIBLE);
                friendViewHolder.tvRate.setVisibility(View.VISIBLE);
                friendViewHolder.tvRate.setText(Float.toString(quest.myrate));
                friendViewHolder.tvAns.setText(quest.myans);
            }


        }

        @Override
        public int getItemCount() {

            return mQuest.size();
        }
    }
}
