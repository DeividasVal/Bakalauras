package com.example.bakalauras.ui.zinutes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bakalauras.R;

import java.util.ArrayList;

import Model.Zinutes;

public class susirasytiCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int MESSAGE_SENT = 0;
    private final static int MESSAGE_RECEIVED = 1;

    private ArrayList<Zinutes> zinuciuList;
    private int dabartinisId;
    private Context context;

    public susirasytiCardAdapter(ArrayList<Zinutes> zinuciuList, int dabartinisId, Context context) {
        this.zinuciuList = zinuciuList;
        this.dabartinisId = dabartinisId;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        Zinutes zinute = zinuciuList.get(position);
        if (zinute.getSiuntejo_id() == dabartinisId) {
            return MESSAGE_SENT;
        } else {
            return MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_issiusta_zinute, parent, false);
            return new MessageSentHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_gauta_zinute, parent, false);
            return new MessageReceivedHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Zinutes zinute = zinuciuList.get(position);
        switch (holder.getItemViewType()) {
            case MESSAGE_SENT:
                ((MessageSentHolder) holder).setZinute(zinute);
                break;
            case MESSAGE_RECEIVED:
                ((MessageReceivedHolder) holder).setZinute(zinute);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return zinuciuList.size();
    }

    private static class MessageSentHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MessageSentHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text_view_siusta);
            timeText = itemView.findViewById(R.id.time_text_view);
        }

        void setZinute(Zinutes zinute) {
            messageText.setText(zinute.getZinutes_aprasymas());
            timeText.setText(zinute.getLaikas_zinutes());
        }
    }

    private static class MessageReceivedHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MessageReceivedHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.message_text_view_gauta);
            timeText = itemView.findViewById(R.id.time_text_view);
        }

        void setZinute(Zinutes zinute) {
            messageText.setText(zinute.getZinutes_aprasymas());
            timeText.setText(zinute.getLaikas_zinutes());
        }
    }
}

