package com.example.bai3.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bai3.R;
import com.example.bai3.models.FolderInDevice;
import com.example.bai3.models.ListPath;
import com.example.bai3.models.Music;
import com.example.bai3.utils.Helper;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private Context context;

    private ArrayList<File> listItem;

    private ArrayList<ListPath> paths;

    private String path;

    public CustomAdapter(Context context, ArrayList<File> listItem){
        this.context = context;
        this.listItem = listItem;
        this.paths = new ArrayList<>();
    }

    public void setListItem(ArrayList<File> listItem){
        this.listItem = listItem;
        this.paths = new ArrayList<>();
    }

    public ArrayList<File> getListItem(){
        return listItem;
    }

    public ArrayList<ListPath> getListPaths(){
        return paths;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_recyclerview, parent, false);

        return new ViewHolder(view);
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File file = listItem.get(position);
        holder.getTxtTextName().setText(file.getName());
        if(file.isDirectory()){
            holder.getImageFile().setImageResource(R.drawable.folder_icon);
        }else if(file.getName().endsWith(".mp3") || file.getName().endsWith(".awb")){
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(file.getAbsolutePath());
            byte [] data = mmr.getEmbeddedPicture();
            if(data != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                holder.getImageFile().setImageBitmap(bitmap);
                paths.add(new ListPath(file.getName(), file.getAbsolutePath(), bitmap));
            }else{
                holder.getImageFile().setImageResource(R.drawable.mp3_file_icon);
            }
            String album = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
            if(album != null) {
//                albumView.setText(album);
//                albumView.setVisibility(View.VISIBLE);
            }
            paths.add(new ListPath(file.getName(), file.getAbsolutePath()));

        }else{
            holder.getImageFile().setImageResource(R.drawable.file);
        }
        holder.getCardView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (listItem != null) ? listItem.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTextName;
        private ImageView imageFile;
        private CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTextName = itemView.findViewById(R.id.txtTextName);
            imageFile = itemView.findViewById(R.id.imageFile);
            cardView = itemView.findViewById(R.id.card_view);
        }
        public TextView getTxtTextName() {
            return txtTextName;
        }
        public CardView getCardView() {
            return cardView;
        }
        public ImageView getImageFile() {
            return imageFile;
        }
    }
}
