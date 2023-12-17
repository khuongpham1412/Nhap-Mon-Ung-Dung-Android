package com.example.bt2.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt2.Activities.NoteActivity;
import com.example.bt2.DAO.NoteDAO;
import com.example.bt2.DTO.Note;
import com.example.bt2.R;
import com.example.bt2.Utilities.ConvertImage;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    List<Note> list;
    NoteDAO dao;

    public NoteAdapter(List<Note> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.itv_note, parent, false);
        dao = new NoteDAO(parent.getContext());
        return new NoteViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = list.get(position);

        holder.itvTvTitleNote.setText(note.getTitle());
        holder.itvTvDescriptionNote.setText(note.getDesc());
        holder.itvTvDateTimeNote.setText(note.getDate() + " - " + note.getTime());
        if (note.getImage() != null) {
            holder.itvImgNote.setImageBitmap(ConvertImage.getImage(note.getImage()));
        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), NoteActivity.class);
            intent.putExtra("ID_NOTE", note.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class NoteViewHolder extends RecyclerView.ViewHolder {
        private ImageView itvImgNote;
        private ConstraintLayout itvContentNote;
        private TextView itvTvTitleNote;
        private TextView itvTvDescriptionNote;
        private TextView itvTvDateTimeNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            itvImgNote = (ImageView) itemView.findViewById(R.id.itv_img_note);
            itvContentNote = (ConstraintLayout) itemView.findViewById(R.id.itv_content_note);
            itvTvTitleNote = (TextView) itemView.findViewById(R.id.itv_tv_title_note);
            itvTvDescriptionNote = (TextView) itemView.findViewById(R.id.itv_tv_description_note);
            itvTvDateTimeNote = (TextView) itemView.findViewById(R.id.itv_tv_date_time_note);
        }
    }
}
