package com.example.android.finalproject_yaocmengqid.SideMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.android.finalproject_yaocmengqid.People;
import com.example.android.finalproject_yaocmengqid.R;
import com.example.android.finalproject_yaocmengqid.Utils.ChooseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChoosePeopleActivity extends AppCompatActivity {

    private RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference mPeopleRef;
    private String TAG = "ChoosePeopleActivity";

    private ChooseAdapter mAdapter;
    private ArrayList<People> mDataset;
    private ArrayList<Boolean> isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_people);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    mRecyclerview = (RecyclerView) findViewById(R.id.choosePeopleRecycler_view);
                    mRecyclerview.setHasFixedSize(true);
                    mLayoutManager = new LinearLayoutManager(ChoosePeopleActivity.this);
                    mRecyclerview.setLayoutManager(mLayoutManager);
                    mDataset = new ArrayList<People>();
                    isChecked = new ArrayList<Boolean>();
                    mAdapter = new ChooseAdapter(mDataset,isChecked);
                    mRecyclerview.setAdapter(mAdapter);

                    mPeopleRef = FirebaseDatabase.getInstance().getReference("people").child(user.getUid());
                    mPeopleRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            People people = dataSnapshot.getValue(People.class);
                            Log.v("DataSnapshot", people.toString());
                            mDataset.add(people);
                            isChecked.add(false);

                            mAdapter.notifyDataSetChanged();
//                display.setText(results);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
//                    mAdapter = new ChooseAdapter()
                }
            }
        });
    }

    public void selectMember(View view) {
        Intent data = new Intent();
        ArrayList<People> selected = new ArrayList<>();
        for (int i = 0; i < mDataset.size(); ++ i) {
            if (isChecked.get(i))
                selected.add(mDataset.get(i));
        }
        data.putExtra("selected", selected);
        setResult(0, data);
        finish();
    }
}



