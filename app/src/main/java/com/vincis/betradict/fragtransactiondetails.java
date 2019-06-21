package com.vincis.betradict;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vincis.betradict.Class.AllQuest;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragtransactiondetails extends Fragment {
    TextView tv1,tv2,tv3,tv4;
    float tbid,tbid1,tbid2,tbid3;
    public Bundle b;
    public String arr[];
    DatabaseReference mref;
    List<String>id=new ArrayList<>();
    List<Float> amt1=new ArrayList<>();
    List<Float> amt2=new ArrayList<>();
    List<Float> amt3=new ArrayList<>();
    int count=0;
    public fragtransactiondetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_fragtransactiondetails, container, false);
        tv1=v.findViewById(R.id.tvTotalBid);
        tv2=v.findViewById(R.id.tvTotalBid1);
        tv3=v.findViewById(R.id.tvTotalBid2);
        tv4=v.findViewById(R.id.tvTotalBid3);
        b=getArguments();
        arr=b.getStringArray("details");
        mref=FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]);

        mref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AllQuest quest=dataSnapshot.getValue(AllQuest.class);
                Float bid1=quest.quest_wall.bids1;
                Float bid2=quest.quest_wall.bids2;
                Float bid3=quest.quest_wall.bids3;
                id.add(dataSnapshot.getKey());
                amt1.add(bid1);
                amt2.add(bid2);
                amt3.add(bid3);
                count++;
                tbid=0;
                tbid1=0;
                tbid2=0;
                tbid3=0;

                    for(int i=0;i<amt1.size();i++)
                    {
                        tbid1=tbid1+amt1.get(i);
                        tbid2=tbid2+amt2.get(i);
                        tbid3=tbid3+amt3.get(i);
                    }
                    tbid=tbid1+tbid2+tbid3;
                    tv1.setText(Float.toString(tbid));
                    tv2.setText(Float.toString(tbid1));
                    tv3.setText(Float.toString(tbid2));
                    tv4.setText(Float.toString(tbid3));


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                AllQuest quest=dataSnapshot.getValue(AllQuest.class);
                String idd=dataSnapshot.getKey();
                Integer index=id.indexOf(idd);
                Float bid1=quest.quest_wall.bids1;
                Float bid2=quest.quest_wall.bids2;
                Float bid3=quest.quest_wall.bids3;
                amt1.set(index,bid1);
                amt2.set(index,bid2);
                amt3.set(index,bid3);
                tbid=0;
                tbid1=0;
                tbid2=0;
                tbid3=0;

                    for(int i=0;i<amt1.size();i++)
                    {
                        tbid1=tbid1+amt1.get(i);
                        tbid2=tbid2+amt2.get(i);
                        tbid3=tbid3+amt3.get(i);
                    }
                    tbid=tbid1+tbid2+tbid3;
                    tv1.setText(Float.toString(tbid));
                    tv2.setText(Float.toString(tbid1));
                    tv3.setText(Float.toString(tbid2));
                    tv4.setText(Float.toString(tbid3));


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
        });

      return v;

    }

}
