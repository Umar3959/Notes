package com.example.notesapp.ui.fragments;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.interfaces.NoteClickCallBack;
import com.example.notesapp.models.Note;
import com.example.notesapp.models.NotesAdapter;
import com.example.notesapp.viewmodels.ListFragmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment implements NoteClickCallBack {

  ListFragmentViewModel listFragmentViewModel;
  RecyclerView notesRv;
  NotesAdapter notesAdapter;
  FloatingActionButton addNewFab;
  NavController navController;
  List<Note> noteslist=new ArrayList<>();



    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_list, container, false);

        //initializing xml view
        addNewFab=view.findViewById(R.id.addNewFab);

        //setting up navigation controller

        navController = NavHostFragment.findNavController(getActivity().getSupportFragmentManager().findFragmentById(R.id.notesNavHostFragment));

        //initializing recyclerview and viewmodel

        initRecyclerView(view);
        initViewModel();


        addNewFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //navigating to editor fragment when user click on add new note button with navigation component

                navController.navigate(ListFragmentDirections.actionListFragmentToEditorFragment("null"));
            }
        });


        return view;
    }

    private void initRecyclerView(View view) {
        notesRv=view.findViewById(R.id.notesRv);
        notesRv.setLayoutManager(new LinearLayoutManager(getContext()));
        notesRv.setHasFixedSize(true);

        //setting up swipe action on recycler view to delete the note

        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                //deleting the not when user swipe any item in recyclerview

                listFragmentViewModel.deleteNoteById(noteslist.get(viewHolder.getAdapterPosition()).getId());
                Toast.makeText(getContext(), "Note deleted successfully", Toast.LENGTH_SHORT).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(notesRv);
    }

    private void initViewModel() {

        //observing view model to detect any changes in notes list

        Observer<List<Note>> observer=new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteslist.clear();
                noteslist.addAll(notes);

                notesAdapter=new NotesAdapter(getContext(),noteslist,ListFragment.this);
                notesRv.setAdapter(notesAdapter);

            }
        };
        listFragmentViewModel= ViewModelProviders.of(this).get(ListFragmentViewModel.class);

        listFragmentViewModel.notes.observe(getActivity(),observer);

    }

    @Override
    public void noteId(String noteId) {
        navController.navigate(ListFragmentDirections.actionListFragmentToEditorFragment(noteId));
    }
}