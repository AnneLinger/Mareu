package com.anne.linger.mareu.controller.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anne.linger.mareu.R;
import com.anne.linger.mareu.di.DIMeeting;
import com.anne.linger.mareu.model.Meeting;
import com.anne.linger.mareu.services.meeting.MeetingApiService;

import java.util.ArrayList;
import java.util.List;

/**
 *Created by Anne Linger on 24/12/2021.
 */
public class ListCollaboratorAdapter extends RecyclerView.Adapter<ListCollaboratorAdapter.ViewHolder>{

    private static final MeetingApiService mApiService = DIMeeting.getMeetingApiService();
    private static Meeting meeting = mApiService.getMeetingList().get(0);
    private static List<String> mCollaborators = meeting.getCollaborators();

    public ListCollaboratorAdapter(List<String> collaborators) {
        mCollaborators = collaborators;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_collaborators, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.displayCollaborator(mCollaborators.get(position));
    }

    @Override
    public int getItemCount() {
        return mCollaborators.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final TextView mail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mail = itemView.findViewById(R.id.tv_mail);
        }

        public void displayCollaborator(String collaborator) {
            mail.setText(collaborator);
        }
    }
}

