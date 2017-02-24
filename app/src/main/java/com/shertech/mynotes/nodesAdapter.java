package com.shertech.mynotes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lastwalker on 2/16/17.
 */

public class nodesAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "NodesAdapter";
    private List<share> noteList;
    private MainActivity mainAct;

    public nodesAdapter(List<share> nList, MainActivity ma){
        this.noteList=nList;
        this.mainAct=ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flaglayout, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        share note = noteList.get(position);
        holder.tvTime.setText(note.getName());
        if (note.getDescription().length()<80){
            holder.etNode.setText(note.getDescription());
        }else{
        holder.etNode.setText(note.getDescription().substring(0,80)+"...");}
        holder.etTitle.setText(note.getTitle());
        holder.textView.setText(R.string.tvLU);
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
