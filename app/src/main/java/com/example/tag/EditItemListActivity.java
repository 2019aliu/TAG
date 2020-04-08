package com.example.tag;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import android.util.Log;
import android.net.Uri;



public class EditItemListActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {
    final String TAG = "ListItemsActivity";
    private RecyclerView mRecyclerView;
    //    private CardView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<MyItem> myDataset;  // this will be a list of items

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit an Item");
        setSupportActionBar(toolbar);
        System.out.println(toolbar.getTitle());


        myDataset = new ArrayList<>();
        // Populate the arraylist with a bunch of items
        myDataset.add(new MyItem("Phone", "Galaxy S9", "E8:99:C4:D1:CA:25", "1c:b0:94:86:4e:6c"));
        myDataset.add(new MyItem("Keys", "Dorm keys plus Explore lanyard", "00:00:00:00:00:00", "12:34:56:78:90:12"));
        myDataset.add(new MyItem("Wallet", "Buzzcard, debit, and cash", "11:22:33:44:55:66", "13:24:35:46:57:68"));
        myDataset.add(new MyItem("My sanity (rip)", "Help I've gone insane", "AA:BB:CC:DD:EE:FF", "10:29:38:47:56:65"));

        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);



        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this, myDataset);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        /*
        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");
        */

        /*
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
                //        mRecyclerView = (CardView) findViewById(R.id.rvItems);
                //        // use this setting to improve performance if you know that changes
                //        // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                mLayoutManager = new LinearLayoutManager(EditItemListActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new MyAdapter(EditItemListActivity.this, myDataset);
                mAdapter.setClickListener(EditItemListActivity.this);
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
        */


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent editIntent = new Intent(this, EditActivity.class);


        //pass any variables in here using .putExtra(), most likely user information
        editIntent.putExtra("Item_Name", mAdapter.getItem(position));
        startActivity(editIntent);
    }

}
