package com.takwira.hamza.takwira.recycle_view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.takwira.hamza.takwira.R;

/**
 * Created by hamza on 14/08/17.
 */

public class CardViewContentsViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView text;
    Context context ;




    CardViewContentsViewHolder(View itemView,Context context) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.cardViewTitle);
        text = (TextView) itemView.findViewById(R.id.cardViewText);
        this.context = context ;

    }


}