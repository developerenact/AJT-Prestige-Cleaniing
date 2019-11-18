package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.LogsActivity;
import com.android.ajtprestigecleaning.model.AllLogsPojo.Datum;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class TaskLogsAdapter extends RecyclerView.Adapter<TaskLogsAdapter.ViewHolder> {

    List<Datum> results;
    private final Context context;


    public TaskLogsAdapter(List<Datum> results, Context context) {
        this.results = results;
        this.context = context;

    }


    @Override
    public TaskLogsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TaskLogsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.logs_item, parent, false));

    }

    @Override
    public void onBindViewHolder(final TaskLogsAdapter.ViewHolder holder,  int position) {
        holder.note_text.setText(results.get(position).getText());

        if (!results.get(holder.getAdapterPosition()).getImage().isEmpty()) {
           Glide.with(context)
                    .load(results.get(holder.getAdapterPosition()).getImage())
                    .placeholder(R.color.grey)
                    .error(R.color.grey)
                    .into(holder.note_img);

            Log.d("images", "onBindViewHolder: "+results.get(holder.getAdapterPosition()).getImage());

/*
            Picasso.with(context).load(results.get(holder.getAdapterPosition()).getImage()).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.color.grey).error(R.color.grey).into(holder.note_img, new Callback() {
                @Override
                public void onSuccess() {


                }

                @Override
                public void onError() {
                    //Try again online if cache failed
                    Picasso.with(context)
                            .load(results.get(holder.getAdapterPosition()).getImage())
                            .error(R.color.grey)
                            .into(holder.note_img, new Callback() {
                                @Override
                                public void onSuccess() {


                                }

                                @Override
                                public void onError() {
                                    Log.v("Picasso", "Could not fetch image");

                                }
                            });
                }
            });
*/



        } else {
            holder.card.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {

        if (results == null)
            return 0;
        else
            return results.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView note_text;
        ImageView note_img;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            note_text = itemView.findViewById(R.id.note_text);
           note_img = itemView.findViewById(R.id.log_img);
            card = itemView.findViewById(R.id.card);

        }

    }

    public void addItems(Datum newItems) {
        results.add(newItems);
        notifyDataSetChanged();
    }

}
