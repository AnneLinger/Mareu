package com.anne.linger.mareu.controller.adapters;

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
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.event.DeleteMeetingEvent;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Adapter and ViewHolder to display a recycler view for the meeting list
 */
public class ListMeetingAdapter extends RecyclerView.Adapter<ListMeetingAdapter.ViewHolder> {

    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static List<Meeting> mMeetings = mApiService.getMeetingList();

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
        holder.displayCollaborators(mMeetings.get(position));
        holder.deleteMeeting(mMeetings.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView circle;
        private final TextView name;
        private final TextView startTime;
        private final TextView topic;
        private final RecyclerView collaborators;
        private final ImageView delete;

        private Context mContext;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            circle = itemView.findViewById(R.id.im_circle);
            name = itemView.findViewById(R.id.tv_name);
            startTime = itemView.findViewById(R.id.tv_time);
            topic = itemView.findViewById(R.id.tv_topic);
            collaborators = itemView.findViewById(R.id.rv_collaborators);
            delete = itemView.findViewById(R.id.im_delete);
        }

        private void displayMeeting(Meeting meeting) {
            circle.setImageResource(meeting.getRoom().getDrawable());
            name.setText(meeting.getName());
            startTime.setText(meeting.getTime());
            topic.setText(meeting.getTopic());
        }

        private void displayCollaborators(Meeting meeting) {
            collaborators.setAdapter(new ListCollaboratorAdapter(meeting.getCollaborators()));
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            collaborators.setLayoutManager(layoutManager);
        }

        public void deleteMeeting(Meeting meeting) {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
                }
            });
        }
    }
}
