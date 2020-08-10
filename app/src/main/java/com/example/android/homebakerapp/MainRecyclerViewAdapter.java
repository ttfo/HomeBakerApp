package com.example.android.homebakerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.homebakerapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// REFERENCE: https://stackoverflow.com/questions/40587168/simple-android-grid-example-using-recyclerview-with-gridlayoutmanager-like-the
public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {

    // mData => ArrayList of Recipe objects
    private ArrayList<Recipe> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public MainRecyclerViewAdapter(Context context, ArrayList<Recipe> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the ImageView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.myTextView.setText(mData.get(position).getName()); // binds recipes names

        // REF. https://stackoverflow.com/questions/43510744/how-to-pass-context-to-picasso
        // && https://www.journaldev.com/13759/android-picasso-tutorial#android-picasso-8211-loading-image-from-file

        if (mData.get(position).getType().equals("savoury")) {

            Picasso.with(holder.itemView.getContext())
                    .load(mData.get(position).getImage()) // Calls method Recipe.getImage()
                    .placeholder(R.drawable.pizza)
                    .into(holder.myImageView);

        } else if (mData.get(position).getType().equals("sweet")) {

            Picasso.with(holder.itemView.getContext())
                    .load(mData.get(position).getImage()) // Calls method Recipe.getImage()
                    .placeholder(R.drawable.pastry_cherry)
                    .into(holder.myImageView);

        } else {

            Picasso.with(holder.itemView.getContext())
                    .load(mData.get(position).getImage()) // Calls method Recipe.getImage()
                    .placeholder(R.drawable.meat_pie) // TODO replace with question mark icon
                    .into(holder.myImageView);

        }

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myImageView = itemView.findViewById(R.id.recipe_image_iv);
            myTextView = itemView.findViewById(R.id.recipe_name_tv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Recipe getItem(int id) {
        return mData.get(id);
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
