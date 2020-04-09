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
        toolbar.setTitle("Find an Item");
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
                mLayoutManager = new GridLayoutManager(ListItemsActivity.this, 2);
                mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new MyAdapter(ListItemsActivity.this, myDataset);
                mAdapter.setClickListener(ListItemsActivity.this);
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;
                    case R.id.navigation_dashboard:
                        Intent addIntent = new Intent(ListItemsActivity.this, RegisterActivity.class);
                        startActivity(addIntent);
                        break;
                    case R.id.navigation_notifications:
                        String queryDestination = "1600 Pennsylvania Ave NW, Washington, DC 20500";
                        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s", queryDestination));

                        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        // Make the Intent explicit by setting the Google Maps package
                        mapIntent.setPackage("com.google.android.apps.maps");
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

        // Create a Uri from an intent string. Use the result to create an Intent.
        double destLatitude = itemSelected.getLatitude();
        double destLongitude = itemSelected.getLongitude();
        System.out.println(String.format("Latitude: %s, Longitude: %s", destLatitude, destLongitude));
        String queryDestination = String.format("%s, %s", destLatitude, destLongitude);
//        String queryDestination = String.format("9908 Mill Run Drive, Great Falls");
        Uri gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s", queryDestination));

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        // Attempt to start an activity that can handle the Intent
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            // Open up the next activity first
            Intent BTWifiIntent = new Intent(this, BTWifiActivity.class);
            startActivity(BTWifiIntent);
            // And then open up Google Maps
            startActivity(mapIntent);
        }

    }
}

//class Utility {
//    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
//        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
//        return noOfColumns;
//    }
//}