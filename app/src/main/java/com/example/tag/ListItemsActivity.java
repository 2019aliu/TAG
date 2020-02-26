package com.example.tag;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class ListItemsActivity extends AppCompatActivity implements MyAdapter.ItemClickListener{
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<MyItem> myDataset;  // this will be a list of items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myDataset = new ArrayList<>();
        // Populate the arraylist with a bunch of items
        myDataset.add(new MyItem("Phone"));
        myDataset.add(new MyItem("Keys"));
        myDataset.add(new MyItem("Wallet"));
        myDataset.add(new MyItem("My sanity (rip)"));


        mRecyclerView = (RecyclerView) findViewById(R.id.rvItems);
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
        Intent mapIntent = new Intent(this, GPSFindActivity.class);
        //pass any variables in here using .putExtra(), most likely user information
        mapIntent.putExtra("Destination_Lat", 33.794723);
        mapIntent.putExtra("Destination_Long", -84.411220);
        startActivity(mapIntent);
    }
}
