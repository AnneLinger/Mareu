package com.anne.linger.mareu.controller.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.di.DI;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;

import java.util.ArrayList;
import java.util.List;

/**
*Created by Anne Linger on 24/12/2021.
*/
public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.ViewHolder>{

    private final List<Meeting> mMeetings;
    private static final MeetingApiService mApiService = DI.getMeetingApiService();
    private static List<String> mCollaboratorList;
    private Context mContext;

    public ListMeetingAdapter(List<Meeting> meetings) {
        mMeetings = meetings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_meeting, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayMeeting(mMeetings.get(position));
        mCollaboratorList = mApiService.getCollaboratorList();
        holder.collaborators.setAdapter(new ListCollaboratorAdapter(mCollaboratorList));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.collaborators.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView name;
        private final TextView startTime;
        private final TextView topic;
        private final RecyclerView collaborators;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            startTime = itemView.findViewById(R.id.tv_start_time);
            topic = itemView.findViewById(R.id.tv_topic);
            collaborators = itemView.findViewById(R.id.rv_collaborators);
        }

        public void displayMeeting(Meeting meeting) {
            name.setText(meeting.getName());
            startTime.setText(meeting.getStartTime());
            topic.setText(meeting.getTopic());
        }
    }
}
