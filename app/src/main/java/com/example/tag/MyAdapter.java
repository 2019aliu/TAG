package com.example.tag;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<MyItem> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
//    private final PublishSubject<String> onClickSubject = PublishSubject.create();
    private static final String TAG = "debugging";

    // stores and recycles views as they are scrolled off screen
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        TextView myTextView;

        MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            myTextView = itemView.findViewById(R.id.tvItem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // data is passed into the constructor
    public MyAdapter(Context context, List<MyItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MyItem item = mData.get(position);
        holder.myTextView.setText(item.getText());
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.setSelected(!item.isSelected());
                holder.view.setBackgroundColor(item.isSelected() ? Color.CYAN : Color.WHITE);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id).getText();
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}