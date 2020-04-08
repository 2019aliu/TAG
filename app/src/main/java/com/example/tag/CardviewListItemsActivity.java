package com.example.tag;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class CardviewListItemsActivity extends AppCompatActivity implements MyAdapter.ItemClickListener{
    private final String TAG = "CardviewListActivity";
    private RecyclerView mRecyclerView;
//    private CardView mRecyclerView;
    private MyAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private ArrayList<MyItem> myDataset;  // this will be a list of items

    // Databasing
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_activity_list_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Find an Item");
        setSupportActionBar(toolbar);

        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");

        // Read from the database
        mUserItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, HashMap<String, String>> items =
                        (HashMap<String, HashMap<String, String>>) dataSnapshot.getValue();
                // Populate the arraylist with a bunch of items
                for (String itemName: items.keySet()) {
                    MyItem item = new MyItem(items.get(itemName));
                    myDataset.add(item);
                }

                mRecyclerView = findViewById(R.id.rvItems);
                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);
                int spanCount = 3; // 3 columns
                int spacing = 50; // 50px
                boolean includeEdge = true;
                mRecyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));

//                int mNoOfColumns = Utility.calculateNoOfColumns(getApplicationContext());
                mLayoutManager = new GridLayoutManager(CardviewListItemsActivity.this, 2);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new MyAdapter(CardviewListItemsActivity.this, myDataset);
                mAdapter.setClickListener(CardviewListItemsActivity.this);
                mRecyclerView.setAdapter(mAdapter);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                        mRecyclerView.getContext(),
                        mLayoutManager.getOrientation());
                mRecyclerView.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent mapIntent = new Intent(this, GPSFindActivity.class);


        //pass any variables in here using .putExtra(), most likely user information
        mapIntent.putExtra("Destination_Lat", 33.794723);
        mapIntent.putExtra("Destination_Long", -84.411220);
        startActivity(mapIntent);
    }
}
