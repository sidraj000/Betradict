package com.example.betradict;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.example.betradict.Class.Quest;
import com.example.betradict.Class.User;
import com.example.betradict.Class.Wallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class walletdetail extends Fragment {
    String uid;
    private RecyclerView mRecycler;
    private WalletAdapter mAdapter;
    private LinearLayoutManager mManager;
    DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
    FirebaseUser muser;
    public Wallet wallet=new Wallet();
    public Quest q;
    TextView we;


    private List<Integer> data;



    public walletdetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_walletdetail, container, false);
        mRecycler = view.findViewById(R.id.translist);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        mManager = new LinearLayoutManager(getContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        we=view.findViewById(R.id.we);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new WalletAdapter(getContext(), mRef);
        mRecycler.setAdapter(mAdapter);



    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private static class WalletViewHolder extends RecyclerView.ViewHolder {

        TextView tvDet, tvAmt, tvDate;
        TextView tv;

        public WalletViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDet = itemView.findViewById(R.id.tvDet);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmt = itemView.findViewById(R.id.tvAmt);

        }
    }

    private class WalletAdapter extends RecyclerView.Adapter<WalletViewHolder> {
        private Context mContext;
        private DatabaseReference mD;
        List<Integer> u=new ArrayList<>();
        final List<transactions> trans=new ArrayList<>();


        public WalletAdapter(Context context, DatabaseReference mRef) {
            mContext = context;
            mD=mRef;

            DatabaseReference md1=FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("wallet").child("lastTransactions");
            md1.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    transactions t=dataSnapshot.getValue(transactions.class);
                    trans.add(t);
                    notifyItemInserted(trans.size()-1);
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
        public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = null;

                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.item_tranaction, viewGroup, false);
                return new WalletViewHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull WalletViewHolder walletViewHolder, int i) {
            String det=null;
            String formattedDateString=null;

          if(i==0)
            {
                walletViewHolder.tvAmt.setVisibility(View.GONE);
                walletViewHolder.tvDate.setVisibility(View.GONE);
                walletViewHolder.tvDet.setVisibility(View.GONE);
            }

             else if(i>0){
                 we.setVisibility(View.GONE);
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date currentDate = trans.get(i).date;
           formattedDateString = formatter.format(currentDate);
            walletViewHolder.tvAmt.setText(Float.toString(trans.get(i).amt));
            walletViewHolder.tvDate.setText(formattedDateString);

            if (trans.get(i).amt > 0) {
                walletViewHolder.tvAmt.setTextColor(Color.BLUE);
                det="Deposited " + trans.get(i).amt + " trollars for " + trans.get(i).qid;
                walletViewHolder.tvDet.setText(det);
            } else {
                walletViewHolder.tvAmt.setTextColor(Color.RED);
                det="Deducted " + trans.get(i).amt * -1 + " trollars for " + trans.get(i).qid;
                walletViewHolder.tvDet.setText(det);
            }


        }
             final String p=det;
             final int k=i;
             final String da=formattedDateString;


             walletViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {

                     DatabaseReference md=FirebaseDatabase.getInstance().getReference().child("quest_usr").child(uid).child("cricket").child(trans.get(k).Mid).child(trans.get(k).type).child(trans.get(k).qid);
                     md.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                             q=dataSnapshot.getValue(Quest.class);
                             Bundle b=new Bundle();
                              String arr[]={p,trans.get(k).Mid,da,q.ques,q.opt1,q.opt2,q.opt3,Float.toString(q.mybid),Float.toString(q.myrate),q.myans,q.cans};
                             Intent intent=new Intent(getActivity(),wallet_click.class);
                             intent.putExtra("det",arr);
                             startActivity(intent);
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });

                 }
             });



        }

        @Override
        public int getItemCount() {
            return trans.size();
        }
    }
}
