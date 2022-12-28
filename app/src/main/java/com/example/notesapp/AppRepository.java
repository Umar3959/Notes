package com.example.notesapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.models.Note;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.List;

public class AppRepository {

    public static  AppRepository INSTANCE;
    CollectionReference notesReferance;
    public MutableLiveData<List<Note>> notes=new MutableLiveData<List<Note>>();
    public MutableLiveData<Note> note=new MutableLiveData<Note>();
    Context context;


    private AppRepository(Context context){
        this.context=context;
      notesReferance=FirebaseFirestore.getInstance().collection("Notes");
    }

    public static AppRepository getInstance(Context context){
        //creating new instance of repository

        return INSTANCE=new AppRepository(context);
    }

    public MutableLiveData<List<Note>> getAllNotes(){


        //getting list of notes from firebase to show it in list fragment

        notesReferance.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<Note> list=new ArrayList<>();
                if (!value.isEmpty()){
                    for (DocumentSnapshot d:value.getDocuments()) {

                        Note note=d.toObject(Note.class);
                        if (note!=null){
                            list.add(note);
                        }

                    }
                }
                notes.setValue(list);
            }
        });

       return notes;
    }


    public void upSertNote(Note note) {

        //inserting and updating note from firebase using its id

        notesReferance.document(note.getId()).set(note);
    }

    public void deleNoteById(String id) {

        //deleting note from firebase using its id

        notesReferance.document(id).delete();
    }
}
