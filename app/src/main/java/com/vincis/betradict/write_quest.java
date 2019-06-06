package com.vincis.betradict;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vincis.betradict.Class.AllQuest;
import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.User;
import com.vincis.betradict.Class.Quest_wall;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class write_quest extends AppCompatActivity {

    Spinner ptGame;
    EditText ptQues,ptOpt1,ptOpt2,ptOpt3,tvId,tvGId;
    TextView tvQuest;
    Button btnSub,btnEdit;
    int clickStatus=0;
    public DatabaseReference mRef;
    public List<String> mUserIds = new ArrayList<>();
    public List<User> mUser = new ArrayList<>( );
    public ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_quest);

        ptQues=findViewById(R.id.ptQues);
        ptOpt1=findViewById(R.id.ptOpt1);
        ptOpt2=findViewById(R.id.ptOpt2);
        ptOpt3=findViewById(R.id.ptOpt3);
        btnSub=findViewById(R.id.btnSub);
        btnEdit=findViewById(R.id.btnEdit);
        tvId=findViewById(R.id.tvId);
        tvGId=findViewById(R.id.tvGId);
        tvQuest=findViewById(R.id.tvQuest);
        ptGame=findViewById(R.id.ptGame);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.Games,R.layout.support_simple_spinner_dropdown_item);
        ptGame.setAdapter(adapter);

        tvQuest.setVisibility(View.GONE);
        btnEdit.setVisibility(View.GONE);

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
        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ptQues.getText().toString().isEmpty() || ptOpt1.getText().toString().isEmpty() || ptOpt2.getText().toString().isEmpty() || ptOpt3.getText().toString().isEmpty() || tvId.getText().toString().isEmpty()||tvGId.getText().toString().isEmpty()) {

                    Toast.makeText(write_quest.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
                else if (clickStatus == 0) {
                    tvQuest.setText("Game "+ptGame.getSelectedItem().toString()+"\n"+"ID=  " + tvId.getText().toString() + "\n" + ptQues.getText().toString() + "\n" + "1-  " + ptOpt1.getText().toString() + "\n" + "2-  " + ptOpt2.getText().toString() + "\n" + "3-  " + ptOpt3.getText().toString());
                    tvQuest.setVisibility(View.VISIBLE);
                    ptQues.setVisibility(View.GONE);
                    ptOpt1.setVisibility(View.GONE);
                    ptOpt2.setVisibility(View.GONE);
                    ptOpt3.setVisibility(View.GONE);
                    tvId.setVisibility(View.GONE);
                    ptGame.setVisibility(View.GONE);
                    clickStatus = 1;
                    btnSub.setText("Submit");
                    btnEdit.setVisibility(View.VISIBLE);
                } else {
                    DatabaseReference mDRef = FirebaseDatabase.getInstance().getReference();
                    if (ptQues.getText().toString().isEmpty() || ptOpt1.getText().toString().isEmpty() || ptOpt2.getText().toString().isEmpty() || ptOpt3.getText().toString().isEmpty() || tvId.getText().toString().isEmpty()||tvGId.getText().toString().isEmpty()) {

                        Toast.makeText(write_quest.this, "Enter all details", Toast.LENGTH_SHORT).show();
                    } else {
                        Quest quest = new Quest(ptQues.getText().toString(), ptOpt1.getText().toString(), ptOpt2.getText().toString(), ptOpt3.getText().toString(), tvId.getText().toString(), 0, 0,0, "U", "U");

                        for (int i = 0; i < mUserIds.size(); i++) {
                            mDRef.child("quest_usr").child(mUserIds.get(i)).child(ptGame.getSelectedItem().toString()).child(tvGId.getText().toString()).child("normal").child(tvId.getText().toString()).setValue(quest);
                        }

                        AllQuest allQuest = new AllQuest(ptQues.getText().toString(), ptOpt1.getText().toString(), ptOpt2.getText().toString(), ptOpt3.getText().toString(), tvId.getText().toString(), new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                        mDRef.child("quest").child(ptGame.getSelectedItem().toString()).child(tvGId.getText().toString()).child("normal").child(tvId.getText().toString()).setValue(allQuest);
                        Toast.makeText(write_quest.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();

                        btnSub.setText("PREVIEW");
                        tvQuest.setVisibility(View.GONE);
                        ptQues.setVisibility(View.VISIBLE);
                        ptOpt1.setVisibility(View.VISIBLE);
                        ptOpt2.setVisibility(View.VISIBLE);
                        ptOpt3.setVisibility(View.VISIBLE);
                        tvId.setVisibility(View.VISIBLE);
                        btnEdit.setVisibility(View.GONE);
                        ptQues.setText("");
                        tvId.setText("");
                        ptOpt1.setText("");
                        ptOpt2.setText("");
                        ptOpt3.setText("");

                        clickStatus = 0;
                    }
                }
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSub.setText("PREVIEW");
                tvQuest.setVisibility(View.GONE);
                ptQues.setVisibility(View.VISIBLE);
                ptOpt1.setVisibility(View.VISIBLE);
                ptOpt2.setVisibility(View.VISIBLE);
                ptOpt3.setVisibility(View.VISIBLE);
                tvId.setVisibility(View.GONE);
                ptGame.setVisibility(View.VISIBLE);

                clickStatus=0;

            }
        });


    }
    @Override
    public void onStop() {
        super.onStop();
        if (mChildEventListener != null) {
            mRef.removeEventListener(mChildEventListener);
        }
    }

}
