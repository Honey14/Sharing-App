package com.atmproofofconcept;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterImages extends RecyclerView.Adapter<AdapterImages.ImagesViewHolder> {
    ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public AdapterImages(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_show_images, parent, false);
        return new ImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        try {

            holder.image_taken.setImageBitmap(bitmaps.get(position));
            holder.image_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bitmaps.remove(position);
                    notifyDataSetChanged();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    class ImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView image_taken;
        ImageView image_delete;

        public ImagesViewHolder(@NonNull View itemView) {
            super(itemView);
            image_taken = itemView.findViewById(R.id.image_taken);
            image_delete = itemView.findViewById(R.id.image_delete);
        }
    }
}
