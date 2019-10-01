package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.LogsActivity;
import com.android.ajtprestigecleaning.model.AllJobsPojo.Datum;
import java.io.Serializable;



public class JobsDetailAdapter extends RecyclerView.Adapter<JobsDetailAdapter.ViewHolder> {
    Datum data;
    private final Context context;


    public JobsDetailAdapter(Datum data, Context context) {
        this.data = data;
        this.context = context;

    }


    @Override
    public JobsDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new JobsDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_itemview, parent, false));

    }

    @Override
    public void onBindViewHolder(final JobsDetailAdapter.ViewHolder holder, final int position) {
        holder.task.setText(data.getCheckList().get(position).getName());
        holder.circle.setImageResource(R.mipmap.uncheck_circle);

        int text_num = 0;
        int img_num = 0;
        for (int i = 0; i < data.getCheckList().get(position).getLogs().size(); i++) {
            if (!data.getCheckList().get(position).getLogs().get(i).getText().isEmpty()) {
                text_num += 1;
            }
        }

        for (int j = 0; j < data.getCheckList().get(position).getLogs().size(); j++) {
            if (!data.getCheckList().get(position).getLogs().get(j).getImage().equals( "")) {
                img_num += 1;
            }
        }
        if (text_num > 0) {
            holder.tv_note_count.setText(String.valueOf(text_num));
        }
        else if (text_num > 99) {
            holder.tv_note_count.setText("99+");

        }
        else {
            holder.note_count.setVisibility(View.GONE);
        }

        if (img_num > 0) {
            holder.tv_camera_count.setText(String.valueOf(img_num));
        }
        else if (img_num > 99) {
            holder.tv_camera_count.setText("99+");

        }
        else {
            holder.camera_count.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LogsActivity.class);
                intent.putExtra("Logs", (Serializable) data.getCheckList().get(position));
                intent.putExtra("Alldata", (Serializable) data);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.getCheckList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task, tv_camera_count, tv_note_count;
        ImageView circle;
        LinearLayoutCompat camera_count, note_count;

        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            circle = itemView.findViewById(R.id.uncheck_circle);
            tv_camera_count = itemView.findViewById(R.id.tv_img_count);
            tv_note_count = itemView.findViewById(R.id.tv_text_count);
            camera_count = itemView.findViewById(R.id.camera_count);
            note_count = itemView.findViewById(R.id.note_count);

        }


    }


}
