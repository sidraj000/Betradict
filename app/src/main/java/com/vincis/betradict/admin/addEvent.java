package com.vincis.betradict.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vincis.betradict.Class.AllQuest;
import com.vincis.betradict.Class.Match;
import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.Quest_wall;
import com.vincis.betradict.Class.User;
import com.vincis.betradict.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class addEvent extends AppCompatActivity {
    Spinner spGame;
    EditText etId,etTeamA,etTeamB,etYrs,etMonth,etDate,etHrs,etMin;
    Button btnSubEvent;
    public DatabaseReference mRef;
    public List<String> mUserIds = new ArrayList<>();
    public List<User> mUser = new ArrayList<>( );
    public ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        spGame=findViewById(R.id.spGame);
        etYrs=findViewById(R.id.etYear);
        etMonth=findViewById(R.id.etMonth);
        etDate=findViewById(R.id.etDate);
        etHrs=findViewById(R.id.etHrs);
        etMin=findViewById(R.id.etMin);
        etId=findViewById(R.id.etId);
        etTeamA=findViewById(R.id.etTeamA);
        etTeamB=findViewById(R.id.etTeamB);
        btnSubEvent=findViewById(R.id.btnSubEvent);
        mRef= FirebaseDatabase.getInstance().getReference();
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.Games,R.layout.support_simple_spinner_dropdown_item);
        spGame.setAdapter(adapter);

        mRef = FirebaseDatabase.getInstance().getReference()
                .child("users");
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                User user = dataSnapshot.getValue(User.class);
                mUser.add(user);
                mUserIds.add(user.per.uid);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                User user = dataSnapshot.getValue(User.class);
                String userKey = dataSnapshot.getKey();

                // [START_EXCLUDE]
                int userIndex = mUserIds.indexOf(userKey);
                if (userIndex > -1) {

                    mUser.set(userIndex, user);

                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:" + userKey);
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                String userKey = dataSnapshot.getKey();

                int userIndex = mUserIds.indexOf(userKey);
                if (userIndex> -1) {

                    mUserIds.remove(userIndex);
                    mUser.remove(userIndex);

                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);
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
        mRef.addChildEventListener(childEventListener);
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener;
       btnSubEvent.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String game=spGame.getSelectedItem().toString();
               String id=etId.getText().toString();
               Date date=new Date(Integer.parseInt(etYrs.getText().toString())-1900,Integer.parseInt(etMonth.getText().toString())-1,Integer.parseInt(etDate.getText().toString()),Integer.parseInt(etHrs.getText().toString()),Integer.parseInt(etMin.getText().toString()));
               String teama=etTeamA.getText().toString();
               String teamb=etTeamB.getText().toString();
               Match event=new Match(date,id,teama,teamb,0);
               DatabaseReference mr=FirebaseDatabase.getInstance().getReference();
               mr.child("match").child(game).child(id).setValue(event);
               Toast.makeText(addEvent.this, "Successfully added", Toast.LENGTH_SHORT).show();
               AllQuest allQuest1=new AllQuest("Which team will win the match?",teama,teamb,"TIE","Q1", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest2=new AllQuest("Which team will have the leading run scorer?",teama,teamb,"TIE","Q2", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
              AllQuest allQuest3=new AllQuest("Which team will hit more sixes?",teama,teamb,"TIE","Q3", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest4=new AllQuest("Which team will hit more fours?",teama,teamb,"TIE","Q4", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest5=new AllQuest("Which team will have the highest opening partnership?",teama,teamb,"TIE","Q5", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest6=new AllQuest("Which team will have the highest partnership in the whole match?",teama,teamb,"TIE","Q6", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest7=new AllQuest("Which team will have the leading wicket taker?",teama,teamb,"TIE","Q7", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest8=new AllQuest("Which team will have the least economic bowler?",teama,teamb,"TIE","Q8", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest9=new AllQuest("Which team will give more extras?",teama,teamb,"TIE","Q9", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest10=new AllQuest("Which team will bowl more maiden overs?",teama,teamb,"TIE","Q10", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest11=new AllQuest("Which team will take more wickets?",teama,teamb,"TIE","Q11", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest12=new AllQuest("Which team will take more catches?",teama,teamb,"TIE","Q12", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest13=new AllQuest("Which team will do more stumps and run-outs?",teama,teamb,"TIE","Q13", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest14=new AllQuest("Which team will have the batsman facing most numbers of legal deliveries?",teama,teamb,"TIE","Q14", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest15=new AllQuest("Which team will have the most economic bowler?",teama,teamb,"TIE","Q15", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest16=new AllQuest("Which team will hit more half centuries?",teama,teamb,"TIE","Q16", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest17=new AllQuest("Which team will hit more centuries?",teama,teamb,"TIE","Q17", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest18=new AllQuest("Which team will use more number of bowlers in their attack?",teama,teamb,"TIE","Q18", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest19=new AllQuest("Which team will bowl more number of no balls?",teama,teamb,"TIE","Q19", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               AllQuest allQuest20=new AllQuest("Which team will bowl more wide balls?",teama,teamb,"TIE","Q20", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
               Quest quest1 = new Quest("Which team will win the match?", teama,teamb,"TIE", "Q1", 0, 0,0, "U", "U");
               Quest quest2= new Quest("Which team will have the leading run scorer?", teama,teamb,"TIE", "Q2", 0, 0,0, "U", "U");
               Quest quest3 = new Quest("Which team will hit more sixes?", teama,teamb,"TIE", "Q3", 0, 0,0, "U", "U");
               Quest quest4 = new Quest("Which team will hit more fours?", teama,teamb,"TIE", "Q4", 0, 0,0, "U", "U");
               Quest quest5 = new Quest("Which team will have the highest opening partnership?", teama,teamb,"TIE", "Q5", 0, 0,0, "U", "U");
               Quest quest6 = new Quest("Which team will have the highest partnership in the whole match?", teama,teamb,"TIE", "Q6", 0, 0,0, "U", "U");
               Quest quest7 = new Quest("Which team will have the leading wicket taker?", teama,teamb,"TIE", "Q7", 0, 0,0, "U", "U");
               Quest quest8 = new Quest("Which team will have the least economic bowler?", teama,teamb,"TIE", "Q8", 0, 0,0, "U", "U");
               Quest quest9 = new Quest("Which team will give more extras?", teama,teamb,"TIE", "Q9", 0, 0,0, "U", "U");
               Quest quest10 = new Quest("Which team will bowl more maiden overs?", teama,teamb,"TIE", "Q10", 0, 0,0, "U", "U");
               Quest quest11 = new Quest("Which team will take more wickets?", teama,teamb,"TIE", "Q11", 0, 0,0, "U", "U");
               Quest quest12 = new Quest("Which team will take more catches?", teama,teamb,"TIE", "Q12", 0, 0,0, "U", "U");
               Quest quest13 = new Quest("Which team will do more stumps and run-outs?", teama,teamb,"TIE", "Q13", 0, 0,0, "U", "U");
               Quest quest14 = new Quest("Which team will have the batsman facing most numbers of legal deliveries?", teama,teamb,"TIE", "Q14", 0, 0,0, "U", "U");
               Quest quest15 = new Quest("Which team will have the most economic bowler?", teama,teamb,"TIE", "Q15", 0, 0,0, "U", "U");
               Quest quest16 = new Quest("Which team will hit more half centuries?", teama,teamb,"TIE", "Q16", 0, 0,0, "U", "U");
               Quest quest17 = new Quest("Which team will hit more centuries?", teama,teamb,"TIE", "Q17", 0, 0,0, "U", "U");
               Quest quest18 = new Quest("Which team will use more number of bowlers in their attack?", teama,teamb,"TIE", "Q18", 0, 0,0, "U", "U");
               Quest quest19 = new Quest("Which team will bowl more number of no balls?", teama,teamb,"TIE", "Q19", 0, 0,0, "U", "U");
               Quest quest20 = new Quest("Which team will bowl more wide balls?", teama,teamb,"TIE", "Q20", 0, 0,0, "U", "U");
               DatabaseReference mD=FirebaseDatabase.getInstance().getReference();
               mD.child("quest").child("cricket").child(id).child("normal").child("Q1").setValue(allQuest1);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q2").setValue(allQuest2);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q3").setValue(allQuest3);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q4").setValue(allQuest4);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q5").setValue(allQuest5);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q6").setValue(allQuest6);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q7").setValue(allQuest7);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q8").setValue(allQuest8);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q9").setValue(allQuest9);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q10").setValue(allQuest10);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q11").setValue(allQuest11);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q12").setValue(allQuest12);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q13").setValue(allQuest13);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q14").setValue(allQuest14);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q15").setValue(allQuest15);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q16").setValue(allQuest16);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q17").setValue(allQuest17);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q18").setValue(allQuest18);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q19").setValue(allQuest19);
               mD.child("quest").child("cricket").child(id).child("normal").child("Q20").setValue(allQuest20);
               for (int i = 0; i < mUserIds.size(); i++) {
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q1").setValue(quest1);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q2").setValue(quest2);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q3").setValue(quest3);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q4").setValue(quest4);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q5").setValue(quest5);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q6").setValue(quest6);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q7").setValue(quest7);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q8").setValue(quest8);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q9").setValue(quest9);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q10").setValue(quest10);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q11").setValue(quest11);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q12").setValue(quest12);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q13").setValue(quest13);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q14").setValue(quest14);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q15").setValue(quest15);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q16").setValue(quest16);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q17").setValue(quest17);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q18").setValue(quest18);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q19").setValue(quest19);
                   mD.child("quest_usr").child(mUserIds.get(i)).child("cricket").child(id).child("normal").child("Q20").setValue(quest20);
               }
               Toast.makeText(addEvent.this, "Question Added Successfully", Toast.LENGTH_SHORT).show();
           }
       });

    }


}
