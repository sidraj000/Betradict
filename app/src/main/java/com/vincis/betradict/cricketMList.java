package com.vincis.betradict;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.vincis.betradict.Class.Match;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class cricketMList extends Fragment {


    private RecyclerView mRecycler;
    private cListAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    Animation animation;
    ImageView ivAnime;


    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public cricketMList() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_cricketmlist, container, false);
        mRecycler = view.findViewById(R.id.matchList);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        mFriendsReference = FirebaseDatabase.getInstance().getReference().child("match").child("cricket");
        mManager = new LinearLayoutManager(getContext());
        //mManager.setReverseLayout(true);
        //mManager.setStackFromEnd(true);
        mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(mManager);
       // ivAnime=view.findViewById(R.id.ivanime2);
        animation= AnimationUtils.loadAnimation(getContext(),R.anim.rotate);

        return view;

    }


    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new cListAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private static class cViewHolder extends RecyclerView.ViewHolder {

      ImageView ivTeamA,ivTeamB;
      TextView tvTeamA,tvTeamB,tvDate;
     TextView tvCounter;

        public cViewHolder(View itemView) {
            super(itemView);
            ivTeamA=itemView.findViewById(R.id.ivTeam1);
            ivTeamB=itemView.findViewById(R.id.ivTeam2);
            tvTeamA=itemView.findViewById(R.id.tvTeam1);
            tvTeamB=itemView.findViewById(R.id.tvTeam2);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvCounter=itemView.findViewById(R.id.tvCounter);
        }
    }



    private class cListAdapter extends RecyclerView.Adapter<cViewHolder> {
        private Context mContext;
        private DatabaseReference mRef;

        public List<Match> mCMatch=new ArrayList<>();
        public List<String> cId=new ArrayList<>();


        public cListAdapter(Context context, DatabaseReference ref) {
            mContext = context;
            mRef = ref;
            Query query=mRef.orderByChild("date/time");
            //mRef=FirebaseDatabase.getInstance().getReference().child("match").child("cricket");
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Match cMatch=dataSnapshot.getValue(Match.class);
                    if(cMatch.status==0) {
                        mCMatch.add(cMatch);
                        String key = dataSnapshot.getKey();
                        cId.add(key);
                        notifyItemInserted(mCMatch.size() - 1);
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Match cMatch=dataSnapshot.getValue(Match.class);
                    if(cMatch.status==1) {
                        String key = dataSnapshot.getKey();
                        Integer index = cId.indexOf(key);
                        if(index>-1){

                          mCMatch.remove(index);
                          notifyItemRemoved(index);
                        }


                    }
                    if(cMatch.status==0) {
                        String key = dataSnapshot.getKey();
                        Integer index = cId.indexOf(key);
                        if(index>-1){
                            mCMatch.set(index,cMatch);
                        }
                        else {
                            mCMatch.add(cMatch);
                            cId.add(key);
                        }


                    }

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Match cricketMatch=dataSnapshot.getValue(Match.class);
                    String id=dataSnapshot.getKey();
                    int index=cId.indexOf(id);
                    if(index>-1) {
                        mCMatch.remove(index);
                        notifyItemRemoved(index);
                    }
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
        public cViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.match_card, viewGroup, false);

            return new cViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final cViewHolder cViewHolder, int i) {

            if(mCMatch.get(i).status==0) {
                final int k = i;
                cViewHolder.tvTeamA.setText(mCMatch.get(i).teamA);
                cViewHolder.tvTeamB.setText(mCMatch.get(i).teamB);
                DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                final Date date = mCMatch.get(i).date;
                String formattedDateString = formatter.format(date);
                cViewHolder.tvDate.setText(formattedDateString);
                StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                final File file = new File(mContext.getFilesDir(),mCMatch.get(i).teamA +".png");
                if(file.exists())
                {
                    cViewHolder.ivTeamA.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
                 //
                    //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                }
                else {

                        storageRef.child("cricketTeamLogo/" + mCMatch.get(i).teamA + ".png").getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                cViewHolder.ivTeamA.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                }
                final File file2= new File(mContext.getFilesDir(),mCMatch.get(i).teamB +".png");
                if(file2.exists())
                {
                    cViewHolder.ivTeamB.setImageBitmap(BitmapFactory.decodeFile(file2.getPath()));
                   // Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                }
                else {

                    storageRef.child("cricketTeamLogo/" + mCMatch.get(i).teamB + ".png").getFile(file2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            cViewHolder.ivTeamB.setImageBitmap(BitmapFactory.decodeFile(file2.getPath()));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });


                }

             /*   storageRef.child("cricketTeamLogo/" + mCMatch.get(i).teamA + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                      new DownLoadImageTask(cViewHolder.ivTeamA).execute(uri.toString());

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
                storageRef.child("cricketTeamLogo/" + mCMatch.get(i).teamB + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        new DownLoadImageTask(cViewHolder.ivTeamB).execute(uri.toString());


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });*/


                Calendar cal = Calendar.getInstance();
                final Date currentDate = cal.getTime();
                long old, neww, diff;
                old = date.getTime();
                neww = currentDate.getTime();
                diff = old - neww;
                new CountDownTimer(diff, 500) {
                    public void onTick(long millisUntilFinished) {
                        long seconds = millisUntilFinished / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        long days = hours / 24;
                        String time;
                        if(days>1)
                        {
                          time=days + " " + "days" + " :" + hours % 24+" hrs";
                        }
                       else if(days==1)
                        {

                            time=days + " " + "day" + " :" + hours % 24+" hrs";
                        }
                       else  {
                             time = hours % 24 + ":" + minutes % 60 + ":" + seconds % 60;
                        }
                        cViewHolder.tvCounter.setText(time);
                    }

                    public void onFinish() {
                        cViewHolder.tvCounter.setText("Match Has Started!");
                    }
                }.start();

                cViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            Intent intent = new Intent(mContext, MainActivity.class);
                            String arr[] = {"cricket", mCMatch.get(k).id,"normal"};
                            intent.putExtra("details", arr);
                            startActivity(intent);
                            //mCMatch.clear();
                         //   cId.clear();


                    }
                });
            }


        }


        @Override
        public int getItemCount() {
            return mCMatch.size();
        }
    }


    private  class DownLoadImageTask extends AsyncTask<String,Void, Bitmap> {
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
