package com.example.notesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.AppRepository;
import com.example.notesapp.models.Note;

import java.util.List;

public class ListFragmentViewModel extends AndroidViewModel {


    public MutableLiveData<List<Note>> notes;
    AppRepository appRepository;

    public ListFragmentViewModel(@NonNull Application application) {
        super(application);
        appRepository=AppRepository.getInstance(application.getApplicationContext());
        notes=appRepository.getAllNotes();

    }

    public void deleteNoteById(String id) {
        //calling repository method to delete note
        appRepository.deleNoteById(id);
    }
}
