package com.vincis.betradict;


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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vincis.betradict.Class.Msgs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Support extends Fragment {
    public RecyclerView mRecycler;
    public SupportAdapter mAdapter;
    DatabaseReference mFriendsReference;
    public LinearLayoutManager mManager;
    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final String name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("msgs").child(uId);
    EditText etMsg;
    ImageView ivSend;
    Button btnSell,btnReq;
    DatabaseReference mDatabase;

    public Support() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support, container, false);
        mRecycler=view.findViewById(R.id.supportlist);
        mManager = new LinearLayoutManager(getContext());
       // mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        etMsg=view.findViewById(R.id.etMsg);
       btnReq=view.findViewById(R.id.btnReq);
        //btnSell=view.findViewById(R.id.btnSell);
        ivSend=view.findViewById(R.id.ivSend);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etMsg.getText().toString().trim().isEmpty()) {

                    String text = etMsg.getText().toString().trim();
                    String key=mRef.push().getKey();
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();
                    Msgs msgs=new Msgs(text,0,currentDate);
                    mRef.child(key).setValue(msgs);
                    etMsg.setText("");
                    updatenewmsg(uId);
                }

            }
        });
        btnReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Date currentDate = cal.getTime();
                Msgs msgs=new Msgs("Oops, out of Trollers?\n" +
                        "Don't worry, you don't need to stop playing for that. Just enter the amount you need",1,currentDate);
                String key=mRef.push().getKey();
                mRef.child(key).setValue(msgs);
            }
        });

        return view;
    }

    private void updatenewmsg(final String uId) {
        final DatabaseReference mref=FirebaseDatabase.getInstance().getReference().child("support").child(uId);
        mref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                newMsg m=mutableData.getValue(newMsg.class);

                if(m==null)
                {
                    newMsg n=new newMsg(1,uId,name);
                  mref.setValue(n);
                    return (Transaction.success(mutableData));
                }
                else {
                    m.num=m.num+1;
                    mutableData.setValue(m);
                    return (Transaction.success(mutableData));
                }

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new SupportAdapter(getContext(), mRef);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

    }


    private static class SupportViewHolder extends RecyclerView.ViewHolder {

             TextView tvMsg;
             LinearLayout layout;
        public SupportViewHolder(View itemView) {
            super(itemView);
            tvMsg=itemView.findViewById(R.id.tvMsg);
            layout=itemView.findViewById(R.id.layoutSupp);

        }
    }

    private class SupportAdapter extends RecyclerView.Adapter<SupportViewHolder> {
        private Context mContext;
        private DatabaseReference mD;
        public List<Msgs> mMsgs=new ArrayList<>();
        public SupportAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mD=FirebaseDatabase.getInstance().getReference().child("msgs").child(uId);
            mD.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Msgs m=dataSnapshot.getValue(Msgs.class);
                    mMsgs.add(m);
                    notifyItemInserted(mMsgs.size()-1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        }

        @NonNull
        @Override
        public SupportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_chat, viewGroup, false);

            return new SupportViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SupportViewHolder supportViewHolder, int i) {

            supportViewHolder.tvMsg.setText(mMsgs.get(i).text);

            if(mMsgs.get(i).id==0)
            {
                supportViewHolder.tvMsg.setBackgroundColor(0xFF42ACA8);
               supportViewHolder.layout.setBackgroundColor(0xFF42ACA8);

            }

        }

        @Override
        public int getItemCount() {
//            Toast.makeText(mContext, mMsgs.size(), Toast.LENGTH_SHORT).show();
            return mMsgs.size();
        }
    }

}
