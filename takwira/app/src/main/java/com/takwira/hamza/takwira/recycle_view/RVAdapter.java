package com.takwira.hamza.takwira.recycle_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takwira.hamza.takwira.R;

import java.util.List;

/**
 * Created by hamza on 09/08/17.
 */

public class RVAdapter extends RecyclerView.Adapter<CardViewContentsViewHolder> {
    private static Context context;
    private static List<CardViewContents> cardViewContents;





    public RVAdapter(Context context, List<CardViewContents> cardViewContents) {
        this.context = context;
        this.cardViewContents = cardViewContents;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CardViewContentsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_activity, viewGroup, false);
        CardViewContentsViewHolder pvh = new CardViewContentsViewHolder(v,context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CardViewContentsViewHolder holder, int position) {
        if (holder instanceof CardViewContentsViewHolder) {
            CardViewContentsViewHolder rowHolder = (CardViewContentsViewHolder) holder;
            holder.title.setText(cardViewContents.get(position).title);
            holder.text.setText(cardViewContents.get(position).text);
        }


    }

    @Override
    public int getItemCount() {
        return cardViewContents.size();
    }
}
