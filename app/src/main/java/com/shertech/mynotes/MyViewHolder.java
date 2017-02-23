package com.shertech.mynotes;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lastwalker on 2/16/17.
 */
public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvTime;
    public TextView textView;
    public TextView etNode;
    public TextView etTitle;

    public MyViewHolder(View view) {
        super(view);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        etNode = (TextView) view.findViewById(R.id.etNote);
        etTitle = (TextView) view.findViewById(R.id.etTitle);
        textView = (TextView) view.findViewById(R.id.textView);
    }
}
