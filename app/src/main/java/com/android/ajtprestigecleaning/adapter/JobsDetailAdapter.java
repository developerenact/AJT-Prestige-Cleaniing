package com.android.ajtprestigecleaning.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ajtprestigecleaning.R;
import com.android.ajtprestigecleaning.activities.JobDetailActivity;
import com.android.ajtprestigecleaning.activities.LogsActivity;
import com.android.ajtprestigecleaning.model.JobsPojo.Datum;

import org.w3c.dom.Text;

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
        final boolean[] isopen = {false};
        Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/Montserrat-Medium.ttf");
        holder.task.setText(data.getCheckList().get(position).getName());
        holder.task.setTypeface(custom_font);

        holder.expand_view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isopen[0]) {
                    holder.subitems.setVisibility(View.VISIBLE);
                    holder.plus.setImageResource(R.mipmap.small_minus);
                    isopen[0] = false;
                } else {
                    holder.subitems.setVisibility(View.GONE);
                    holder.plus.setImageResource(R.mipmap.small_plus);
                    isopen[0] = true;
                }

            }
        });

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            holder.subitems.removeAllViews();
        } catch (Exception e) {

        }

        for (int i = 0; i < data.getCheckList().get(position).getTasks().size(); i++) {
            View view = inflater.inflate(R.layout.sub_timeline_itemview, null, false);
            TextView name = view.findViewById(R.id.sub_task);
            ImageView checkimg = view.findViewById(R.id.uncheck_circle);
            TextView tv_note_count = view.findViewById(R.id.tv_text_count);
            TextView tv_camera_count = view.findViewById(R.id.tv_img_count);
            LinearLayoutCompat camera_count = view.findViewById(R.id.unread_layout);
            LinearLayoutCompat note_count = view.findViewById(R.id.unread_layout2);
            name.setText(data.getCheckList().get(position).getTasks().get(i).getName());
            if (data.getCheckList().get(position).getTasks().get(i).getStatus().equals("1")) {
                checkimg.setImageResource(R.mipmap.checked_circle);
            } else {
                checkimg.setImageResource(R.mipmap.uncheck_circle);

            }


            if (!data.getCheckList().get(position).getTasks().get(i).getImageCount().isEmpty()) {
                tv_camera_count.setText(data.getCheckList().get(position).getTasks().get(i).getImageCount());

            } else {
                camera_count.setVisibility(View.GONE);
            }

            if (!data.getCheckList().get(position).getTasks().get(i).getTextCount().isEmpty()) {
                tv_note_count.setText(data.getCheckList().get(position).getTasks().get(i).getTextCount());
            } else {
                note_count.setVisibility(View.GONE);
            }

            holder.subitems.addView(view);


            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, LogsActivity.class);
                    intent.putExtra("Logs", (Serializable) data.getCheckList().get(position).getTasks().get(finalI));
                    intent.putExtra("Alldata", (Serializable) data);
                    intent.putExtra("position", finalI);
                    intent.putExtra("checklistId", data.getCheckList().get(position).getId());
                    intent.putExtra("taskId", data.getCheckList().get(position).getTasks().get(finalI).getId());
                    // context.startActivity(intent);
                    ((JobDetailActivity) context).startActivityForResult(intent, 11);
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return data.getCheckList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView task;
        ImageView circle, plus;
        LinearLayout subitems;
        View view;
        LinearLayoutCompat expand_view_btn;

        public ViewHolder(View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.task);
            subitems = itemView.findViewById(R.id.subitems);
            plus = itemView.findViewById(R.id.plus);
            expand_view_btn = itemView.findViewById(R.id.expanable_view_btn);

        }


    }


}
