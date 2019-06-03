package com.example.betradict.frags;


import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.betradict.Class.AllQuest;
import com.example.betradict.Class.Quest;
import com.example.betradict.Class.User;
import com.example.betradict.Class.Usrwal;
import com.example.betradict.Class.Wallet;
import com.example.betradict.Class.Quest_wall;
import com.example.betradict.R;
import com.example.betradict.SnapHelperByOne;
import com.example.betradict.transactions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;
import static java.lang.Integer.parseInt;


/**
 * A simple {@link Fragment} subclass.
 */
public class frag4 extends Fragment {

    private RecyclerView mRecycler;
    private FriendAdapter mAdapter;
    private LinearLayoutManager mManager;
    private DatabaseReference mFriendsReference;
    FirebaseUser muser;
    public Wallet wallet=new Wallet();
    public Usrwal u;
    public Quest_wall quest_wall;
    public Bundle b;
    public String arr[];
    public TextView  tvEmpt;
    public float rr;
    Animation animation;
    ImageView ivAnime;
    ImageView lf,ri;
    public int size=19;

    final String uId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public frag4() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frag4, container, false);
        mRecycler = view.findViewById(R.id.questList);
        muser = FirebaseAuth.getInstance().getCurrentUser();
        b=getArguments();
        arr=b.getStringArray("details");
        lf=view.findViewById(R.id.arrl);
        ri=view.findViewById(R.id.arrr);
        mFriendsReference = FirebaseDatabase.getInstance().getReference()
                .child("quest_usr").child(muser.getUid()).child(arr[0]).child(arr[1]).child(arr[2]);
        lf.setVisibility(View.GONE);
        mManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        LinearSnapHelper linearSnapHelper = new SnapHelperByOne(ri,lf,size);
        linearSnapHelper.attachToRecyclerView(mRecycler);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        tvEmpt=view.findViewById(R.id.qE);
        ivAnime=view.findViewById(R.id.ivanime);
         animation=AnimationUtils.loadAnimation(getContext(),R.anim.rotate);


        ri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lf.setVisibility(View.VISIBLE);
              //Toast.makeText(getContext(), Integer.toString(mManager.findFirstVisibleItemPosition()), Toast.LENGTH_SHORT).show();
               if (mManager.findFirstVisibleItemPosition() >1) {
                    mRecycler.smoothScrollToPosition(mManager.findFirstVisibleItemPosition() - 1);
                    ri.setVisibility(View.VISIBLE);
                } else {
                    mRecycler.smoothScrollToPosition(mManager.findFirstVisibleItemPosition() - 1);
                   ri.setVisibility(View.GONE);
                }
                lf.setVisibility(View.VISIBLE);

            }
        });

        lf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mManager.findLastVisibleItemPosition() <size-2) {
                    mRecycler.smoothScrollToPosition(mManager.findLastVisibleItemPosition() + 1);
                }
                else {
                    lf.setVisibility(View.GONE);
                    mRecycler.smoothScrollToPosition(mManager.findLastVisibleItemPosition() + 1);
                }
                ri.setVisibility(View.VISIBLE);
            }
        });


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new FriendAdapter(getContext(), mFriendsReference);
        mRecycler.setAdapter(mAdapter);
        ivAnime.setVisibility(View.VISIBLE);
        ivAnime.startAnimation(animation);

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.cleanupListener();
    }



    private static class FriendViewHolder extends RecyclerView.ViewHolder {

        TextView tvquest, tvRate1, tvRate2, tvRate3;
        EditText etAmt;
        ImageView btn1;
        ImageView btn2;
        ImageView btn3;
        Button b1,b2,b3;

        ImageView ivStatus,btnWorth;

        public FriendViewHolder(View itemView) {
            super(itemView);
            tvquest = itemView.findViewById(R.id.tvQuest);
            tvRate1 = itemView.findViewById(R.id.tvRate1);
            tvRate2 = itemView.findViewById(R.id.tvRate2);
            tvRate3 = itemView.findViewById(R.id.tvRate3);
            btn1 = itemView.findViewById(R.id.btn1);
            btn2 = itemView.findViewById(R.id.btn2);
            btn3 = itemView.findViewById(R.id.btn3);
            btnWorth = itemView.findViewById(R.id.btnWorth);
            etAmt = itemView.findViewById(R.id.etAmt);
           b1=itemView.findViewById(R.id.b1);
            b2=itemView.findViewById(R.id.b2);
           b3=itemView.findViewById(R.id.b3);

        }
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendViewHolder> {


        private Context mContext;
        private DatabaseReference mDatabaseReference;
        private ChildEventListener mChildEventListener;
        private ChildEventListener mChildEventListener2;
        private ChildEventListener mChildEventListener3;

        private List<String> mQuestIds = new ArrayList<>();
        private List<Quest> mQuest = new ArrayList<>();

        public FriendAdapter(final Context context, DatabaseReference ref) {
            mContext = context;
            mDatabaseReference = ref;
            Query query=ref.orderByChild("myans");
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());


                        Quest quest = dataSnapshot.getValue(Quest.class);
                        if ( quest.cans.equals("U")) {

                            // [START_EXCLUDE]
                            // Update RecyclerView
                            mQuestIds.add(dataSnapshot.getKey());
                            mQuest.add(quest);
                            notifyItemInserted(mQuest.size() - 1);
                        }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                    Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so displayed the changed comment.
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    String userKey = dataSnapshot.getKey();
                    int userIndex = mQuestIds.indexOf(userKey);
                    if (quest.cans.equals("U")) {

                        // [START_EXCLUDE]

                        if (userIndex > -1) {
                            // Replace with the new data
                            mQuest.set(userIndex, quest);

                            // Update the RecyclerView
                            notifyItemChanged(userIndex);
                        } else {

                        }
                    }
                    else
                    {
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQuestIds.remove(userIndex);
                            mQuest.remove(userIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(userIndex);
                        }
                    }
                    // [END_EXCLUDE]
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                    // A comment has changed, use the key to determine if we are displaying this
                    // comment and if so remove it.
                    String userKey = dataSnapshot.getKey();
                    Quest quest = dataSnapshot.getValue(Quest.class);
                    if (quest.cans.equals("U")) {

                        // [START_EXCLUDE]
                        int userIndex = mQuestIds.indexOf(userKey);
                        if (userIndex > -1) {
                            // Remove data from the list
                            mQuestIds.remove(userIndex);
                            mQuest.remove(userIndex);

                            // Update the RecyclerView
                            notifyItemRemoved(userIndex);
                        } else {
                            Log.w(TAG, "onChildRemoved:unknown_child:" + userKey);
                        }
                        // [END_EXCLUDE]
                    }
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
            query.addChildEventListener(childEventListener);
            mChildEventListener = childEventListener;

      DatabaseReference md1=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            md1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User usr=dataSnapshot.getValue(User.class);
                        wallet=usr.wallet;
                    //Toast.makeText(context, Integer.toString(usr.wallet.balance), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;

            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.question, viewGroup, false);

            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final FriendViewHolder friendViewHolder, int i) {
            final Quest quest = mQuest.get(i);



            if(arr[2].equals("normal"))
            {
                friendViewHolder.btn1.setVisibility(View.VISIBLE);
             friendViewHolder.b1.setVisibility(View.GONE);
                friendViewHolder.btn2.setVisibility(View.VISIBLE);
             friendViewHolder.b2.setVisibility(View.GONE);
                friendViewHolder.btn3.setVisibility(View.VISIBLE);
               friendViewHolder.b3.setVisibility(View.GONE);
            }
            else
            {
                friendViewHolder.btn1.setVisibility(View.GONE);
               friendViewHolder.b1.setVisibility(View.VISIBLE);
                friendViewHolder.btn2.setVisibility(View.GONE);
              friendViewHolder.b2.setVisibility(View.VISIBLE);
                friendViewHolder.btn3.setVisibility(View.GONE);
             friendViewHolder.b3.setVisibility(View.VISIBLE);
            }
            if(arr[2].equals("normal")) {
                if (quest.myans.equals("A")) {
                    StorageReference storageRef2 = FirebaseStorage.getInstance().getReference();
                    final File filex = new File(mContext.getFilesDir(), quest.opt1 + "C" + ".png");
                    if (filex.exists()) {
                        friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt1 + "C" + ".png").getFile(filex).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex2 = new File(mContext.getFilesDir(), quest.opt2 + ".png");
                    if (filex2.exists()) {
                        friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt2 + ".png").getFile(filex2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex3 = new File(mContext.getFilesDir(), quest.opt3 + ".png");
                    if (filex3.exists()) {
                        friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt3 + ".png").getFile(filex3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                } else if (quest.myans.equals("B")) {

                    StorageReference storageRef2 = FirebaseStorage.getInstance().getReference();
                    final File filex = new File(mContext.getFilesDir(), quest.opt1 + ".png");
                    if (filex.exists()) {
                        friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt1 + ".png").getFile(filex).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex2 = new File(mContext.getFilesDir(), quest.opt2 + "C" + ".png");
                    if (filex2.exists()) {
                        friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt2 + "C" + ".png").getFile(filex2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex3 = new File(mContext.getFilesDir(), quest.opt3 + ".png");
                    if (filex3.exists()) {
                        friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt3 + ".png").getFile(filex3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }

                } else if (quest.myans.equals("C")) {
                    StorageReference storageRef2 = FirebaseStorage.getInstance().getReference();
                    final File filex = new File(mContext.getFilesDir(), quest.opt1 + ".png");
                    if (filex.exists()) {
                        friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt1 + ".png").getFile(filex).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex2 = new File(mContext.getFilesDir(), quest.opt2 + ".png");
                    if (filex2.exists()) {
                        friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt2 + ".png").getFile(filex2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex3 = new File(mContext.getFilesDir(), quest.opt3 + "C" + ".png");
                    if (filex3.exists()) {
                        friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt3 + "C" + ".png").getFile(filex3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                }
                else if(quest.myans.equals("U"))
                {
                    StorageReference storageRef2 = FirebaseStorage.getInstance().getReference();
                    final File filex = new File(mContext.getFilesDir(), quest.opt1 + ".png");
                    if (filex.exists()) {
                        friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt1 + ".png").getFile(filex).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn1.setImageBitmap(BitmapFactory.decodeFile(filex.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex2 = new File(mContext.getFilesDir(), quest.opt2 + ".png");
                    if (filex2.exists()) {
                        friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt2 + ".png").getFile(filex2).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn2.setImageBitmap(BitmapFactory.decodeFile(filex2.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                    final File filex3 = new File(mContext.getFilesDir(), quest.opt3 + ".png");
                    if (filex3.exists()) {
                        friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));
                        //
                        //   Toast.makeText(mContext, "exists", Toast.LENGTH_SHORT).show();

                    } else {

                        storageRef2.child("cricketTeamLogo/" + quest.opt3+ ".png").getFile(filex3).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                friendViewHolder.btn3.setImageBitmap(BitmapFactory.decodeFile(filex3.getPath()));

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });


                    }
                }
            }
            else
            {

                if (quest.myans.equals("A")) {
                    friendViewHolder.b1.setBackground(getResources().getDrawable(R.drawable.buttonshape2));
                    friendViewHolder.b2.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friendViewHolder.b3.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                } else if (quest.myans.equals("B")) {

                    friendViewHolder.b1.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friendViewHolder.b2.setBackground(getResources().getDrawable(R.drawable.buttonshape2));
                    friendViewHolder.b3.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                } else if (quest.myans.equals("C")) {

                    friendViewHolder.b1.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friendViewHolder.b2.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friendViewHolder.b3.setBackground(getResources().getDrawable(R.drawable.buttonshape2));
                } else {

                    friendViewHolder.b1.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friendViewHolder.b2.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                    friendViewHolder.b3.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                }

            }



            if(i==(mQuest.size()-1))
            {
                ivAnime.setVisibility(View.GONE);
                ivAnime.clearAnimation();
            }



           tvEmpt.setVisibility(View.GONE);
            friendViewHolder.tvquest.setText(quest.ques);
        friendViewHolder.b1.setText(quest.opt1);
         friendViewHolder.b2.setText(quest.opt2);
           friendViewHolder.b3.setText(quest.opt3);

            friendViewHolder.tvRate1.setVisibility(View.GONE);
            friendViewHolder.tvRate2.setVisibility(View.GONE);
            friendViewHolder.tvRate3.setVisibility(View.GONE);
            final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid).child("quest_wall");
               mdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    quest_wall = dataSnapshot.getValue(Quest_wall.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            friendViewHolder.btnWorth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mdb.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Quest_wall q=dataSnapshot.getValue(Quest_wall.class);
                            float rate1=0,rate2=0,rate3=0;


                            if(q.ctbids1/q.cbids1==1)
                            {
                                Random r = new Random();
                                int ret = r.nextInt(20 + 1);
                                rate1=(float)((ret/100.0)+1.3);
                            }
                            else
                            {
                                rate1=q.ctbids1/q.cbids1;
                            }
                            if(q.ctbids2/q.cbids2==1)
                            {
                                Random r = new Random();
                                int ret = r.nextInt(20 + 1);
                                rate2=(float)((ret/100.0)+1.3);
                            }
                            else
                            {
                                rate2=q.ctbids2/q.cbids2;
                            }
                            if(q.ctbids3/q.cbids3==1)
                            {
                                Random r = new Random();
                                int ret = r.nextInt(20 + 1);
                                rate3=(float)((ret/100.0)+1.3);
                            }
                            else
                            {
                                rate3=q.ctbids3/q.cbids3;
                            }



                            friendViewHolder.tvRate1.setText("X" + Float.toString(rate1));
                            friendViewHolder.tvRate2.setText("X" + Float.toString(rate2));
                            friendViewHolder.tvRate3.setText("X" + Float.toString(rate3));
                            friendViewHolder.tvRate1.setVisibility(View.VISIBLE);
                            friendViewHolder.tvRate2.setVisibility(View.VISIBLE);
                            friendViewHolder.tvRate3.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                    });

                }
            });


             //   friendViewHolder.ivStatus.setVisibility(View.VISIBLE);
                friendViewHolder.etAmt.setVisibility(View.GONE);


          if(quest.status==0)
          {
             //   friendViewHolder.ivStatus.setVisibility(View.GONE);
                friendViewHolder.etAmt.setVisibility(View.VISIBLE);
            }
            friendViewHolder.btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Integer a =Integer.parseInt(friendViewHolder.etAmt.getText().toString());


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        ivAnime.startAnimation(animation);
                        ivAnime.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, arr[0]+arr[1]+arr[2]+quest.qid, Toast.LENGTH_SHORT).show();
                        updatequest(a, "A", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));



                    }
                }
            });
          friendViewHolder.b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        ivAnime.startAnimation(animation);
                        ivAnime.setVisibility(View.VISIBLE);
                        updatequest(parseInt(friendViewHolder.etAmt.getText().toString()), "A", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));



                    }
                }
            });

            friendViewHolder.btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        ivAnime.startAnimation(animation);
                        ivAnime.setVisibility(View.VISIBLE);
                        updatequest(parseInt(friendViewHolder.etAmt.getText().toString()), "B", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));



                    }
                }
            });
           friendViewHolder.b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;
                    Toast.makeText(mContext, "here lies the error" , Toast.LENGTH_SHORT).show();

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        ivAnime.startAnimation(animation);
                        ivAnime.setVisibility(View.VISIBLE);
                        updatequest(parseInt(friendViewHolder.etAmt.getText().toString()), "B", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));



                    }
                }
            });

            friendViewHolder.btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        ivAnime.startAnimation(animation);
                        ivAnime.setVisibility(View.VISIBLE);
                        updatequest(parseInt(friendViewHolder.etAmt.getText().toString()), "C", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));


                    }
                }
            });

            friendViewHolder.b3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    DatabaseReference mDatabase;

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    if(quest.status==1)
                    {
                        Toast.makeText(mContext, "Already answered", Toast.LENGTH_SHORT).show();
                    }
                    else if (friendViewHolder.etAmt.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "Enter Amount", Toast.LENGTH_SHORT).show();
                    } else if (wallet.balance < parseInt(friendViewHolder.etAmt.getText().toString())) {
                        Toast.makeText(mContext, "Insufficient Amount in Wallet", Toast.LENGTH_SHORT).show();
                    }
                    else if (parseInt(friendViewHolder.etAmt.getText().toString())<2)
                    {
                        Toast.makeText(mContext, "Minimum bid is 2 trollars"+wallet.balance, Toast.LENGTH_SHORT).show();

                    }
                    else {
                        ivAnime.startAnimation(animation);
                        ivAnime.setVisibility(View.VISIBLE);
                        updatequest(parseInt(friendViewHolder.etAmt.getText().toString()), "C", quest.qid,mDatabase.child("quest").child(arr[0]).child(arr[1]).child(arr[2]).child(quest.qid));


                    }
                }
            });



        }



        @Override
        public int getItemCount() { size=mQuest.size();

            return mQuest.size();
        }

        public void updatequest(final float bid,final String opt,final String qid,final DatabaseReference mD)
        {
            mD.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    AllQuest allQuest=mutableData.getValue(AllQuest.class);
                    if(allQuest==null)
                    {
                        return (Transaction.success(mutableData));
                    }
                    else {
                        if(opt.equals("A"))
                        {
                            rr=allQuest.quest_wall.ctbids2/allQuest.quest_wall.cbids2;

                            if(rr==1)
                            {
                                Random r = new Random();
                                int ret = r.nextInt(20 + 1);
                                rr=(float)((ret/100.0)+1.3);
                            }
                            allQuest.quest_wall.bids1=allQuest.quest_wall.bids1+bid;
                            allQuest.quest_wall.ctbids1=bid;
                            allQuest.quest_wall.cbids1=bid;
                            allQuest.quest_wall.ctbids3=allQuest.quest_wall.ctbids3+bid;
                            allQuest.quest_wall.ctbids2=allQuest.quest_wall.ctbids2+bid;
                            Quest qust = new Quest(allQuest.ques, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 1, bid,rr, "A", allQuest.quest_wall.ans);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("A").child(uId).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else if(opt.equals("B"))
                        {

                            rr=allQuest.quest_wall.ctbids2/allQuest.quest_wall.cbids2;

                            if(rr==1)
                            {
                                Random r = new Random();
                                int ret = r.nextInt(20 + 1);
                                rr=(float)((ret/100.0)+1.3);
                            }
                            allQuest.quest_wall.bids2=allQuest.quest_wall.bids2+bid;
                            allQuest.quest_wall.ctbids2=bid;
                            allQuest.quest_wall.cbids2=bid;
                            allQuest.quest_wall.ctbids3=allQuest.quest_wall.ctbids3+bid;
                            allQuest.quest_wall.ctbids1=allQuest.quest_wall.ctbids1+bid;
                            Quest qust = new Quest(allQuest.ques, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 1, bid,rr, "B", allQuest.quest_wall.ans);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("B").child(uId).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }
                       else
                        {

                            rr=allQuest.quest_wall.ctbids3/allQuest.quest_wall.cbids3;
                            if(rr==1)
                            {
                                Random r = new Random();
                                int ret = r.nextInt(20 + 1);
                                rr=(float)((ret/100.0)+1.3);
                            }

                            allQuest.quest_wall.bids3=allQuest.quest_wall.bids3+bid;
                            allQuest.quest_wall.ctbids3=bid;
                            allQuest.quest_wall.cbids3=bid;
                            Quest qust = new Quest(allQuest.ques, allQuest.opt1, allQuest.opt2, allQuest.opt3, allQuest.qid, 1, bid,rr, "C", allQuest.quest_wall.ans);
                            DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
                            mDatabase.child("quest_usr").child(uId).child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).setValue(qust);

                            updatewallet(bid,allQuest.qid);
                            Usrwal usr = new Usrwal(uId,bid,rr,0);
                            mDatabase.child("quest_opt").child(arr[0]).child(arr[1]).child(arr[2]).child(allQuest.qid).child("C").child(uId).setValue(usr);

                            mutableData.setValue(allQuest);
                            return (Transaction.success(mutableData));
                        }

                    }
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                }
            });

        }





        public void updatewallet(final float mybid,final String qid)
        {
            DatabaseReference mRef=FirebaseDatabase.getInstance().getReference().child("users").child(uId);
            mRef.runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    User user=mutableData.getValue(User.class);

                if(user==null)
                {

                    Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                    return (Transaction.success(mutableData));
                }
                else {
                    user.wallet.balance=user.wallet.balance-mybid;
                    Calendar cal = Calendar.getInstance();
                    Date currentDate = cal.getTime();

                    user.wallet.lastTransactions.add(new transactions(mybid*-1,qid,arr[1],currentDate,arr[2]));
                    mutableData.setValue(user);
                    return (Transaction.success(mutableData));
                }

                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    ivAnime.setVisibility(View.GONE);
                    ivAnime.clearAnimation();
                }
            });
        }



        public void cleanupListener() {
            if (mChildEventListener != null) {
                mDatabaseReference.removeEventListener(mChildEventListener);
            }
            if (mChildEventListener2!= null) {
                mDatabaseReference.removeEventListener(mChildEventListener2);
            }
            if (mChildEventListener3!= null) {
                mDatabaseReference.removeEventListener(mChildEventListener3);
            }
        }

    }

}
