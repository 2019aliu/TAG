package com.example.tag;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListItemsActivity extends AppCompatActivity implements MyAdapter.ItemClickListener {
    final String TAG = "ListItemsActivity";
    private RecyclerView mRecyclerView;
    //    private CardView mRecyclerView;
    private MyAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
    private ArrayList<MyItem> myDataset = new ArrayList<>();  // this will be a list of items

    // Databasing
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Toolbar toolbar = findViewById(R.id.list_item_toolbar);
        toolbar.setTitle("T∆G");
        setSupportActionBar(toolbar);
        System.out.println(toolbar.getTitle());

        // Initializing the database
        mDatabase = FirebaseDatabase.getInstance().getReference("test");
        final DatabaseReference mUserItems = mDatabase.child("testUser");

        // Read from the database
        mUserItems.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap<String, HashMap<String, Object>> items =
                        (HashMap<String, HashMap<String, Object>>) dataSnapshot.getValue();
                // Populate the arraylist with a bunch of items
                for (String itemName: items.keySet()) {
                    if (! ((Boolean) items.get(itemName).get("pending")) ) {
                        Log.d(TAG, itemName);
                        MyItem item = new MyItem(items.get(itemName));
                        item.setId(itemName);
                        myDataset.add(item);
                    }
                }

                mRecyclerView = findViewById(R.id.rvItems);
//                // use this setting to improve performance if you know that changes
//                // in content do not change the layout size of the RecyclerView
//                mRecyclerView.setHasFixedSize(true);
                int spanCount = 2; // 2 columns
                int spacing = 80; // 80px
                boolean includeEdge = true;
                mRecyclerView.addItemDecoration(new SpacesItemDecoration(spanCount, spacing, includeEdge));

                mLayoutManager = new GridLayoutManager(ListItemsActivity.this, 2);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new MyAdapter(ListItemsActivity.this, myDataset);
                mAdapter.setClickListener(ListItemsActivity.this);
                mRecyclerView.setAdapter(mAdapter);

//                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
//                        mRecyclerView.getContext(),
//                        mLayoutManager.getOrientation());
//                mRecyclerView.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_add:
                        Intent addIntent = new Intent(ListItemsActivity.this, RegisterActivity.class);
                        startActivity(addIntent);
                        break;
                    case R.id.navigation_map:
                        Intent mapIntent = new Intent(ListItemsActivity.this, MapsActivity.class);
                        startActivity(mapIntent);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) throws ExecutionException, InterruptedException {
        MyItem itemSelected = mAdapter.getItem(position);
        Toast.makeText(this, "You clicked " + itemSelected.getName() + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent myItemIntent = new Intent(ListItemsActivity.this, MyItemActivity.class);

        // put in all of the necessary properties of the item
        myItemIntent.putExtra("id", itemSelected.getId());
        myItemIntent.putExtra("name", itemSelected.getName());
        myItemIntent.putExtra("description", itemSelected.getDescription());
        myItemIntent.putExtra("btAddress", itemSelected.getBtAddress());
        myItemIntent.putExtra("wifiMAC", itemSelected.getName());
        myItemIntent.putExtra("device", itemSelected.getDevice());
        myItemIntent.putExtra("latitude", itemSelected.getLatitude());
        myItemIntent.putExtra("longitude", itemSelected.getLongitude());

        // start the intent
        startActivity(myItemIntent);
    }
}
