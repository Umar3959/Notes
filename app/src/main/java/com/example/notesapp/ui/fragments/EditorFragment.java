package com.example.notesapp.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.models.Note;
import com.example.notesapp.ui.SelectLocationActivity;
import com.example.notesapp.viewmodels.EditFragmentViewModel;
import com.example.notesapp.viewmodels.ListFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;


public class EditorFragment extends Fragment {


    String noteId="null";
    EditFragmentViewModel editFragmentViewModel;
    EditText noteEt;
    FloatingActionButton addFab,locFab;
    ProgressBar pb;
    Note note=null;
    NavController navController;
    public static String latitude,longitude=null;

    public EditorFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_editor, container, false);

        //initializing xml view
        noteEt=view.findViewById(R.id.noteEt);
        addFab=view.findViewById(R.id.addFab);
        locFab=view.findViewById(R.id.locFab);
        pb=view.findViewById(R.id.pb);

        //getting note id from previous fragment to show the note content if user has clicked on any note in the list

        noteId=getArguments().getString("noteId");


        initViewModel();
        if (!noteId.equals("null")){
            editFragmentViewModel.loadNoteById(noteId);
            pb.setVisibility(View.VISIBLE);
            locFab.setVisibility(View.GONE);
        }

        //setting up navigation controller
        navController = NavHostFragment.findNavController(getActivity().getSupportFragmentManager().findFragmentById(R.id.notesNavHostFragment));


        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //checking if the note edittext is empty......

                if (TextUtils.isEmpty(noteEt.getText().toString())){
                    noteEt.setError("Please write something");
                    return;
                }

                if (note==null){
                    if (latitude==null&&longitude==null){
                        Toast.makeText(getContext(), "Please select location", Toast.LENGTH_SHORT).show();
                    }else {
                        //creating new note object to save in database.....

                        note=new Note();
                        note.setText(noteEt.getText().toString());
                        note.setDate(new SimpleDateFormat("dd-MM-yyy").format(new Date()));
                        note.setId(String.valueOf(System.currentTimeMillis()));
                        note.setLatitude(latitude);
                        note.setLongitude(longitude);

                        //inserting note through viewmodel

                        editFragmentViewModel.upSertNote(note);
                        //going back to list fragment after saving note

                        navController.popBackStack();
                        Toast.makeText(getContext(), "Note inserted successfully", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    //editing note object

                    note.setText(noteEt.getText().toString());
                    //editing note through viewmodel

                    editFragmentViewModel.upSertNote(note);

                    //going back to list fragment after editing note

                    navController.popBackStack();
                    Toast.makeText(getContext(), "Note updated successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });

        locFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //starting map activity to get current locatio

                startActivity(new Intent(getContext(), SelectLocationActivity.class));
            }
        });


        return view;
    }

    private void initViewModel() {

        //initializing viewmodel.....

        editFragmentViewModel= ViewModelProviders.of(this).get(EditFragmentViewModel.class);

        //observing note live data to get changes on real time

        editFragmentViewModel.noteLiveData.observe(getActivity(), new Observer<Note>() {
            @Override
            public void onChanged(Note notee) {
                note=notee;
                pb.setVisibility(View.GONE);
                noteEt.setText(note.getText());
                latitude=note.getLatitude();
                longitude=note.getLongitude();
            }
        });

    }


}