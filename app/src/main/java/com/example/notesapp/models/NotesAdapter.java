package com.example.notesapp.models;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notesapp.R;
import com.example.notesapp.interfaces.NoteClickCallBack;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesVH> {

    //Notes adapter for showing the list of notes on list fragment

    Context context;
    List<Note> notes;
    NoteClickCallBack noteClickCallBack;

    public NotesAdapter(Context context, List<Note> notes,NoteClickCallBack noteClickCallBack) {
        this.context = context;
        this.notes = notes;
        this.noteClickCallBack=noteClickCallBack;
    }

    @NonNull
    @Override
    public NotesVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_list_layout,null);
        return new NotesVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesVH holder, @SuppressLint("RecyclerView") int position) {

        holder.dateTv.setText(notes.get(position).getDate());
        holder.textTv.setText(notes.get(position).getText());

        //Setting click listener on every note item in the list

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notifying the callback method in list fragment whenever user click on any note so that we can pass its id to editor fragment to edit the current note
                noteClickCallBack.noteId(notes.get(position).getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        //setting the size of recycler view according to size of list
        return notes.size();
    }

    class NotesVH extends RecyclerView.ViewHolder{

        TextView textTv,dateTv;
        public NotesVH(@NonNull View itemView) {
            super(itemView);
            textTv=itemView.findViewById(R.id.textTv);
            dateTv=itemView.findViewById(R.id.dateTv);
        }
    }
}
