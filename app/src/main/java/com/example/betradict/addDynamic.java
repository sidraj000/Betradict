package com.example.betradict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.betradict.Class.AllQuest;
import com.example.betradict.Class.Quest;
import com.example.betradict.Class.Quest_wall;
import com.example.betradict.Class.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class addDynamic extends AppCompatActivity {
   EditText tvDOver,tvDGid,q1opt1,q1opt2,q1opt3,q2opt1,q2opt2,q2opt3,q3opt1,q3opt2,q3opt3;
   TextView tvQ1,tvQ2,tvQ3;
   Button btnD;
    public DatabaseReference mRef;
    public List<String> mUserIds = new ArrayList<>();
    public List<User> mUser = new ArrayList<>( );
    public ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dynamic);
        tvDGid=findViewById(R.id.tvDGId);
        tvDOver=findViewById(R.id.tvDOver);
        btnD=findViewById(R.id.btnD);
        q1opt1=findViewById(R.id.q1opt1);
        q1opt2=findViewById(R.id.q1opt2);
        q1opt3=findViewById(R.id.q1opt3);
        q2opt1=findViewById(R.id.q2opt1);
        q2opt2=findViewById(R.id.q2opt2);
        q2opt3=findViewById(R.id.q2opt3);
        q3opt1=findViewById(R.id.q3opt1);
        q3opt2=findViewById(R.id.q3opt2);
        q3opt3=findViewById(R.id.q3opt3);
        tvQ1=findViewById(R.id.q1);
        tvQ2=findViewById(R.id.q2);
        tvQ3=findViewById(R.id.q3);
        tvQ1.setText("How much runs would be made in this over ");
        tvQ2.setText("How much sixes would be hit in this over ");
        tvQ3.setText("How much wickets would be taken in this over ");

        mRef = FirebaseDatabase.getInstance().getReference()
                .child("users");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                User user = dataSnapshot.getValue(User.class);

                // [START_EXCLUDE]
                // Update RecyclerView
                mUser.add(user);
                mUserIds.add(user.per.uid);
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
        mRef.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
        btnD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quest quest1 = new Quest("How much runs would be made in over "+tvDOver.getText().toString(), q1opt1.getText().toString(),  q1opt2.getText().toString(), q1opt3.getText().toString(),"q1"+tvDOver.getText().toString(), 0, 0,0, "U", "U");
                Quest quest2 = new Quest("How much sixes would be hit in over "+tvDOver.getText().toString(), q2opt1.getText().toString(), q2opt2.getText().toString(), q2opt3.getText().toString(),"q2"+tvDOver.getText().toString(), 0, 0,0, "U", "U");
                Quest quest3 = new Quest("How much wickets would be taken in over "+tvDOver.getText().toString(), q3opt1.getText().toString(), q2opt2.getText().toString(), q3opt3.getText().toString(),"q3"+tvDOver.getText().toString(), 0, 0,0, "U", "U");

                AllQuest allQuest1 = new AllQuest("How much runs would be made in over "+tvDOver.getText().toString(), q1opt1.getText().toString(),  q1opt2.getText().toString(), q1opt3.getText().toString(),"q1"+tvDOver.getText().toString(), new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                AllQuest allQuest2 = new AllQuest("How much sixes would be hit in over "+tvDOver.getText().toString(), q2opt1.getText().toString(), q2opt2.getText().toString(), q2opt3.getText().toString(),"q2"+tvDOver.getText().toString(), new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                AllQuest allQuest3 = new AllQuest("How much wickets would be taken in over "+tvDOver.getText().toString(), q3opt1.getText().toString(), q2opt2.getText().toString(), q3opt3.getText().toString(),"q3"+tvDOver.getText().toString(), new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));


                DatabaseReference mDRef=FirebaseDatabase.getInstance().getReference();

                for (int i = 0; i < mUserIds.size(); i++) {
                    mDRef.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(tvDGid.getText().toString()).child("live").child("q1"+tvDOver.getText().toString()).setValue(quest1);
                    mDRef.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(tvDGid.getText().toString()).child("live").child("q2"+tvDOver.getText().toString()).setValue(quest2);
                    mDRef.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(tvDGid.getText().toString()).child("live").child("q3"+tvDOver.getText().toString()).setValue(quest3);


                }
                mDRef.child("quest").child("cricket").child(tvDGid.getText().toString()).child("live").child("q1"+tvDOver.getText().toString()).setValue(allQuest1);
                mDRef.child("quest").child("cricket").child(tvDGid.getText().toString()).child("live").child("q2"+tvDOver.getText().toString()).setValue(allQuest2);
                mDRef.child("quest").child("cricket").child(tvDGid.getText().toString()).child("live").child("q3"+tvDOver.getText().toString()).setValue(allQuest3);


                Toast.makeText(addDynamic.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();

            }
        });
    }

}
