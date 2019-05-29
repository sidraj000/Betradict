package com.example.betradict;


import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.example.betradict.Class.Quest;
import com.example.betradict.Class.User;
import com.example.betradict.Class.Usrwal;
import com.example.betradict.Class.Wallet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LeaderBoard extends Fragment {

    private RecyclerView mRecycler;
    private LeaderAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Wallet wallet = new Wallet();
    public Bundle b;
    public String arr[];

    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public LeaderBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);
        mRecycler = view.findViewById(R.id.leaderlist);
        muser = FirebaseAuth.getInstance().getCurrentUser();

        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("users");
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

        mAdapter = new LeaderAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private static class LeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvBal, tvName;
        ImageView ivPic;

        public LeaderViewHolder(View itemView) {
            super(itemView);
            tvBal = itemView.findViewById(R.id.tvBal);
            tvName = itemView.findViewById(R.id.tvName);
            ivPic = itemView.findViewById(R.id.ivPic);

        }
    }

    private class LeaderAdapter extends RecyclerView.Adapter<LeaderViewHolder> {

    public Context mContext;
    public  DatabaseReference mDatabaseReference;
    public List<User> mUser=new ArrayList<>();
    public List<String> mKey=new ArrayList<>();

        public LeaderAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;
            Query query=mDatabaseReference.orderByChild("wallet/balance").limitToFirst(100);

           query.addChildEventListener(new ChildEventListener() {
               @Override
               public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                   User user=dataSnapshot.getValue(User.class);
                   mUser.add(user);
                   String key=dataSnapshot.getKey();
                   mKey.add(key);
                   notifyItemInserted(mUser.size()-1);

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
        public LeaderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view = null;

                LayoutInflater inflater = LayoutInflater.from(mContext);
                view = inflater.inflate(R.layout.item_leaderboard, viewGroup, false);

                return new LeaderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull LeaderViewHolder leaderViewHolder, int i) {

            leaderViewHolder.tvName.setText(mUser.get(i).per.username);
            leaderViewHolder.tvBal.setText(Integer.toString((int)mUser.get(i).wallet.balance));
            new DownLoadImageTask(leaderViewHolder.ivPic).execute(mUser.get(i).per.picId);

        }

        @Override
        public int getItemCount() {
            return mUser.size();
        }
    }

    private static class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlOfImage = strings[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }
            catch(IOException e)
            {
            }
            return logo;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
