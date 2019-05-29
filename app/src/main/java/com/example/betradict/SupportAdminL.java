package com.example.betradict;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.betradict.transition_act.trans_adminmsgs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public class SupportAdminL extends Fragment {

    public RecyclerView mRecycler;
    public SupportLAdapter mAdapter;
    DatabaseReference mFriendsReference;
    public LinearLayoutManager mManager;
    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    final String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    DatabaseReference mRef;


    public SupportAdminL() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_support_admin_l, container, false);
        mRecycler = view.findViewById(R.id.msgAlist);
        mManager = new LinearLayoutManager(getContext());
        // mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new SupportLAdapter(getContext(), mRef);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private static class SupportLViewHolder extends RecyclerView.ViewHolder {

        TextView tvMsgName,tvMsgCount;


        public SupportLViewHolder(View itemView) {
            super(itemView);
           tvMsgName=itemView.findViewById(R.id.tvMsgName);
           tvMsgCount=itemView.findViewById(R.id.tvMsgCount);

        }
    }

    private class SupportLAdapter extends RecyclerView.Adapter<SupportLViewHolder> {
        private Context mContext;
        private DatabaseReference mD;
        public List<newMsg> mNew = new ArrayList<>();
        public List<String> key=new ArrayList<>();

        public SupportLAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mD = FirebaseDatabase.getInstance().getReference().child("support");
            Query query=mD.orderByChild("num");
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    newMsg n=dataSnapshot.getValue(newMsg.class);
                    mNew.add(n);
                    String k=dataSnapshot.getKey();
                    key.add(k);
                    notifyItemInserted(mNew.size()-1);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    newMsg n=dataSnapshot.getValue(newMsg.class);
                    String k=dataSnapshot.getKey();
                   int index=key.indexOf(k);
                   mNew.set(index,n);
                   notifyItemChanged(index);
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
        public SupportLViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.item_msglist, viewGroup, false);

            return new SupportLViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SupportLViewHolder supportLViewHolder, int i) {

            supportLViewHolder.tvMsgName.setText(mNew.get(i).name);
            if(mNew.get(i).num==0)
            {
                supportLViewHolder.tvMsgCount.setVisibility(View.GONE);
            }
            else {
                supportLViewHolder.tvMsgCount.setText(Integer.toString(mNew.get(i).num));
            }
            final int k=i;
            supportLViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, trans_adminmsgs.class);
                    intent.putExtra("det",mNew.get(k).uid);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mNew.size();
        }
    }
}
