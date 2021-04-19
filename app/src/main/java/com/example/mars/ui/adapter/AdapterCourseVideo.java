package com.example.mars.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mars.R;
import com.example.mars.data.CourseContentData;
import com.example.mars.ui.VideoViewActivity;

import java.util.ArrayList;

public class AdapterCourseVideo extends RecyclerView.Adapter<AdapterCourseVideo.MyVideoHolder> {
    ArrayList<CourseContentData> _list;
    Context _context;
    public AdapterCourseVideo(ArrayList<CourseContentData> list, Context context) {
        _list=list;
        _context=context;
    }

    @NonNull
    @Override
    public AdapterCourseVideo.MyVideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.adapter_course_list, parent, false);

        return new MyVideoHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCourseVideo.MyVideoHolder holder, int position) {
        holder.tv_type.setVisibility(View.GONE);
        holder.tv_name.setText(_list.get(position).getName());
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(_context, VideoViewActivity.class);
                intent.putExtra("url",_list.get(position).getUrl());
                _context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public class MyVideoHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_type;
        ConstraintLayout parentView;
        public MyVideoHolder(@NonNull View itemView) {
            super(itemView);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_type=itemView.findViewById(R.id.tv_type);
            parentView=itemView.findViewById(R.id.parentView);
        }
    }
}
