package com.example.betradict;


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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.betradict.Class.Msgs;
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
public class Support_admin extends Fragment {
    public RecyclerView mRecycler;
    public AdminAdapter mAdapter;
    DatabaseReference mFriendsReference;
    public LinearLayoutManager mManager;
    DatabaseReference mRef;
    Bundle b;
    String uID;
    EditText etMsgA;
    ImageView ivSendA;

    public Support_admin() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        b=getArguments();
        uID=b.getString("det");
        mRef = FirebaseDatabase.getInstance().getReference().child("msgs").child(uID);
        View view = inflater.inflate(R.layout.fragment_support_admin, container, false);
        mRecycler = view.findViewById(R.id.adminMlist);
        mManager = new LinearLayoutManager(getContext());
        mRecycler.setHasFixedSize(true);
        // mManager.setReverseLayout(true);
        //  mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        etMsgA=view.findViewById(R.id.etMsgA);
        ivSendA=view.findViewById(R.id.ivSendA);
        ivSendA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etMsgA.getText().toString().trim().isEmpty()) {

                    String text = etMsgA.getText().toString().trim();
                    String key=mRef.push().getKey();
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();
                    Msgs msgs=new Msgs(text,1,currentDate);
                    mRef.child(key).setValue(msgs);
                    etMsgA.setText("");
                    updatenewmsg(uID);
                }

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

                    return (Transaction.success(mutableData));
                }
                else {
                    m.num=0;
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

        mAdapter = new AdminAdapter(getContext(), mRef);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private static class AdminViewHolder extends RecyclerView.ViewHolder {

        TextView tvMsg;
        LinearLayout layout;

        public AdminViewHolder(View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            layout=itemView.findViewById(R.id.layoutSupp);

        }
    }

    private class AdminAdapter extends RecyclerView.Adapter<AdminViewHolder> {
        private Context mContext;
        private DatabaseReference mD;
        public List<Msgs> mMsgs = new ArrayList<>();

        public AdminAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mD = ref;
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
        public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view=null;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_chat, viewGroup, false);

            return new AdminViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdminViewHolder supportViewHolder, int i) {
            supportViewHolder.tvMsg.setText(mMsgs.get(i).text);

            if(mMsgs.get(i).id==0)
            {
                supportViewHolder.tvMsg.setBackgroundColor(0xFF42ACA8);
                supportViewHolder.layout.setBackgroundColor(0xFF42ACA8);
            }
        }

        @Override
        public int getItemCount() {
            return mMsgs.size();
        }
    }
}
