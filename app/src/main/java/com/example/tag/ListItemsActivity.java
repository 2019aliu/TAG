package com.example.tag;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ListItemsActivity extends AppCompatActivity implements MyAdapter.ItemClickListener{
    private RecyclerView mRecyclerView;
//    private CardView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<MyItem> myDataset;  // this will be a list of items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Find an Item");
        setSupportActionBar(toolbar);
        System.out.println(toolbar.getTitle());

        myDataset = new ArrayList<>();
        // Populate the arraylist with a bunch of items
        myDataset.add(new MyItem("Phone", "Galaxy S9", "E8:99:C4:D1:CA:25", "1c:b0:94:86:4e:6c"));
        myDataset.add(new MyItem("Keys", "Dorm keys plus Explore lanyard", "00:00:00:00:00:00", "12:34:56:78:90:12"));
        myDataset.add(new MyItem("Wallet", "Buzzcard, debit, and cash", "11:22:33:44:55:66", "13:24:35:46:57:68"));
        myDataset.add(new MyItem("My sanity (rip)", "Help I've gone insane", "AA:BB:CC:DD:EE:FF", "10:29:38:47:56:65"));


        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
//        mRecyclerView = (CardView) findViewById(R.id.rvItems);
//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter= new MyAdapter(this, myDataset);
        mAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);


<<<<<<< Updated upstream
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
=======
>>>>>>> Stashed changes
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
