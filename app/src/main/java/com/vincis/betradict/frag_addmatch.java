package com.vincis.betradict;


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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vincis.betradict.Class.AllQuest;
import com.vincis.betradict.Class.Match;
import com.vincis.betradict.Class.Quest;
import com.vincis.betradict.Class.Quest_wall;
import com.vincis.betradict.Class.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class frag_addmatch extends Fragment {
    private RecyclerView mRecycler;
    private AnsweredAdapter mAdapter;
    private LinearLayoutManager mManager;
    public Bundle b;
    public Date date=null;

    ArrayList<HashMap<String, String>> matchList;



    public frag_addmatch() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);
        mRecycler = view.findViewById(R.id.list2);
        b=getArguments();
        matchList= (ArrayList<HashMap<String, String>>) b.getSerializable("det");
       mManager = new LinearLayoutManager(getContext());
     //   mManager.setReverseLayout(true);
     //   mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AnsweredAdapter(getContext());
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private static class AnsweredViewHolder extends RecyclerView.ViewHolder {

        TextView tvteam1,tvteam2,tvDate;
        Button btnAdd;

        public AnsweredViewHolder(View itemView) {
            super(itemView);
       tvteam1=itemView.findViewById(R.id.tvteam1);
         tvteam2=itemView.findViewById(R.id.tvteam2);
         tvDate=itemView.findViewById(R.id.tvdatematch);
         btnAdd=itemView.findViewById(R.id.addMatchButton);

        }
    }


    private class AnsweredAdapter extends RecyclerView.Adapter<AnsweredViewHolder> {
        DatabaseReference mRef;
        public List<String> mUserIds = new ArrayList<>();
        public List<User> mUser = new ArrayList<>( );


        private Context mContext;

        public AnsweredAdapter(final Context context) {
            mContext = context;


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
        }

        @NonNull
        @Override
        public AnsweredViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.list_items, viewGroup, false);

            return new AnsweredViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final AnsweredViewHolder friendViewHolder, final int i) {

            final String teama=matchList.get(i).get("TeamA");
            final String teamb=matchList.get(i).get("TeamB");
            friendViewHolder.tvteam1.setText(matchList.get(i).get("TeamA"));

            friendViewHolder.tvteam2.setText(matchList.get(i).get("TeamB"));
          /*  if (matchList.get(i).get("Date").endsWith("Z")) {
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            } else {
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            }*/

           final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            try {
                date = inputFormat.parse(matchList.get(i).get("Date"));
            } catch (ParseException e) {
                Toast.makeText(mContext, "date error", Toast.LENGTH_SHORT).show();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR_OF_DAY, 5);
            calendar.add(Calendar.MINUTE,30);
            date=calendar.getTime();
            String formattedDate = outputFormat.format(date);
           friendViewHolder.tvDate.setText(formattedDate);
           friendViewHolder.btnAdd.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   DatabaseReference mre=FirebaseDatabase.getInstance().getReference();
                   final String id=matchList.get(i).get("uid");

                   try {
                       date = inputFormat.parse(matchList.get(i).get("Date"));
                   } catch (ParseException e) {
                       Toast.makeText(mContext, "date error", Toast.LENGTH_SHORT).show();
                   }
                   Calendar calendar = Calendar.getInstance();
                   calendar.setTime(date);
                   calendar.add(Calendar.HOUR_OF_DAY, 5);
                   calendar.add(Calendar.MINUTE,30);
                   date=calendar.getTime();

                   final Match event=new Match(date,id,teama,teamb,0);
                   mre.child("match").child("cricket").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           if(dataSnapshot.exists())
                           {
                               Toast.makeText(mContext, "Already Exists", Toast.LENGTH_SHORT).show();
                           }
                           else

                           {
                               DatabaseReference mr=FirebaseDatabase.getInstance().getReference();
                               mr.child("match").child("cricket").child(id).setValue(event);
                               Toast.makeText(mContext, "Successfully added", Toast.LENGTH_SHORT).show();

                               AllQuest allQuest9=new AllQuest("Which team will win the match?","Winner",teama,teamb,"TIE","Q9", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest7=new AllQuest("Which team will have the leading run scorer?","Leading scorer",teama,teamb,"TIE","Q7", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest4=new AllQuest("Which team will hit more sixes?","Sixes",teama,teamb,"TIE","Q4", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest5=new AllQuest("Which team will hit more fours?","Fours",teama,teamb,"TIE","Q5", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest15=new AllQuest("Which team will have the highest opening partnership?","Opening Partnership",teama,teamb,"TIE","Q15", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest14=new AllQuest("Which team will have the highest partnership in the whole match?","Highest Partnership",teama,teamb,"TIE","Q14", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest6=new AllQuest("Which team will have the leading wicket taker?","WicketTaker",teama,teamb,"TIE","Q6", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest17=new AllQuest("Which team will have the least economic bowler?","Least Economic",teama,teamb,"TIE","Q17", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest13=new AllQuest("Which team will give more extras?","Extras",teama,teamb,"TIE","Q13", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest12=new AllQuest("Which team will bowl more maiden overs?","Maiden Overs",teama,teamb,"TIE","Q12", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest8=new AllQuest("Which team will take more wickets?","Wicket",teama,teamb,"TIE","Q8", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest3=new AllQuest("Which team will take more catches?","Catches",teama,teamb,"TIE","Q3", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest20=new AllQuest("Which team will do more stumps and run-outs?","Stumps&Runouts",teama,teamb,"TIE","Q20", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest16=new AllQuest("Which team will have the batsman facing most numbers of legal deliveries?","Most Deliveries",teama,teamb,"TIE","Q16", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest18=new AllQuest("Which team will have the most economic bowler?","Most Economic",teama,teamb,"TIE","Q18", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest2=new AllQuest("Which team will hit more half centuries?","HalfCenturies",teama,teamb,"TIE","Q2", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest19=new AllQuest("Which team will hit more centuries?","Centuries",teama,teamb,"TIE","Q19", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest11=new AllQuest("Which team will use more number of bowlers in their attack?","Bowlers",teama,teamb,"TIE","Q11", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest1=new AllQuest("Which team will bowl more number of no balls?","No Balls",teama,teamb,"TIE","Q1", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               AllQuest allQuest10=new AllQuest("Which team will bowl more wide balls?","Wides",teama,teamb,"TIE","Q10", new Quest_wall(0,0,0,150,150,150,50,50, 50, "U", 0,0));
                               Quest quest9 = new Quest("Which team will win the match?","Winner", teama,teamb,"TIE", "Q9", 0, 0,0, "U", "U", (float) 0);
                               Quest quest7= new Quest("Which team will have the leading run scorer?","Leading Scorer", teama,teamb,"TIE", "Q7", 0, 0,0, "U", "U", (float) 0);
                               Quest quest4 = new Quest("Which team will hit more sixes?","Sixes", teama,teamb,"TIE", "Q4", 0, 0,0, "U", "U", (float) 0);
                               Quest quest5 = new Quest("Which team will hit more fours?","Fours", teama,teamb,"TIE", "Q5", 0, 0,0, "U", "U", (float) 0);
                               Quest quest15 = new Quest("Which team will have the highest opening partnership?","Opening Partnership", teama,teamb,"TIE", "Q15", 0, 0,0, "U", "U", (float) 0);
                               Quest quest14= new Quest("Which team will have the highest partnership in the whole match?","Highest Partnership", teama,teamb,"TIE", "Q14", 0, 0,0, "U", "U", (float) 0);
                               Quest quest6= new Quest("Which team will have the leading wicket taker?","WicketTaker", teama,teamb,"TIE", "Q6", 0, 0,0, "U", "U", (float) 0);
                               Quest quest17 = new Quest("Which team will have the least economic bowler?","Least Economic", teama,teamb,"TIE", "Q17", 0, 0,0, "U", "U", (float) 0);
                               Quest quest13= new Quest("Which team will give more extras?","Extras", teama,teamb,"TIE", "Q13", 0, 0,0, "U", "U", (float) 0);
                               Quest quest12 = new Quest("Which team will bowl more maiden overs?","Miaden Overs", teama,teamb,"TIE", "Q12", 0, 0,0, "U", "U", (float) 0);
                               Quest quest8 = new Quest("Which team will take more wickets?","Wicket", teama,teamb,"TIE", "Q8", 0, 0,0, "U", "U", (float) 0);
                               Quest quest3 = new Quest("Which team will take more catches?","Catches", teama,teamb,"TIE", "Q3", 0, 0,0, "U", "U", (float) 0);
                               Quest quest20 = new Quest("Which team will do more stumps and run-outs?","Stumps&Runouts", teama,teamb,"TIE", "Q20", 0, 0,0, "U", "U", (float) 0);
                               Quest quest16 = new Quest("Which team will have the batsman facing most numbers of legal deliveries?","Most Deliveries", teama,teamb,"TIE", "Q16", 0, 0,0, "U", "U", (float) 0);
                               Quest quest18= new Quest("Which team will have the most economic bowler?","Most Economic", teama,teamb,"TIE", "Q18", 0, 0,0, "U", "U", (float) 0);
                               Quest quest2 = new Quest("Which team will hit more half centuries?","HalfCenturies", teama,teamb,"TIE", "Q2", 0, 0,0, "U", "U", (float) 0);
                               Quest quest19 = new Quest("Which team will hit more centuries?","Centuries", teama,teamb,"TIE", "Q19", 0, 0,0, "U", "U", (float) 0);
                               Quest quest11 = new Quest("Which team will use more number of bowlers in their attack?","Bowlers", teama,teamb,"TIE", "Q11", 0, 0,0, "U", "U", (float) 0);
                               Quest quest1 = new Quest("Which team will bowl more number of no balls?","No Balls", teama,teamb,"TIE", "Q1", 0, 0,0, "U", "U", (float) 0);
                               Quest quest10 = new Quest("Which team will bowl more wide balls?","Wides",teama,teamb,"TIE", "Q10", 0, 0,0, "U", "U", (float) 0);
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
                               Toast.makeText(mContext, "Question Added Successfully", Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onCancelled(@NonNull DatabaseError databaseError) {

                       }
                   });
                   {

                   }


               }
           });
               }



        @Override
        public int getItemCount() {
            //Toast.makeText(mContext, Integer.toString(matchList.size()), Toast.LENGTH_SHORT).show();
            return matchList.size();
        }





    }

}
