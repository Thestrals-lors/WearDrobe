package com.bl4nk.weardrobe.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bl4nk.weardrobe.R;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private List<Bitmap> images;
    private List<String> imageNames;

    public ImageAdapter(Context context, List<Bitmap> images, List<String> imageNames) {
        this.context = context;
        this.images = images;
        this.imageNames = imageNames;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_with_name, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Bitmap bitmap = images.get(position);
        String name = imageNames.get(position);
        holder.imageView.setImageBitmap(bitmap);
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    // Method to update bitmap images
    public void setImageBitmaps(List<Bitmap> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    // Method to update image names
    public void setImageNames(List<String> imageNames) {
        this.imageNames = imageNames;
        notifyDataSetChanged();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.imageName);
        }
    }
}
