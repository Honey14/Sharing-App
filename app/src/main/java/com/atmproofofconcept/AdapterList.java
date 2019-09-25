package com.atmproofofconcept;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ListViewHolder> {

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
