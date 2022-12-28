package com.example.notesapp.viewmodels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notesapp.AppRepository;
import com.example.notesapp.models.Note;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditFragmentViewModel extends AndroidViewModel {


    AppRepository appRepository;
    public MutableLiveData<Note> noteLiveData=new MutableLiveData<>();

    public EditFragmentViewModel(@NonNull Application application) {
        super(application);

        //initializing app repository object

        appRepository=AppRepository.getInstance(application.getApplicationContext());
    }

    public void loadNoteById(String noteId) {

        //getting note from firebase using its id....

        FirebaseFirestore.getInstance().collection("Notes").document(noteId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if (documentSnapshot.exists()){
                    noteLiveData.setValue(documentSnapshot.toObject(Note.class));
                }else {
                    Toast.makeText(getApplication().getApplicationContext(), "Note not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void upSertNote(Note note) {

        appRepository.upSertNote(note);
    }
}
