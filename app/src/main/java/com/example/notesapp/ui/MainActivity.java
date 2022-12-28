package com.example.notesapp.ui;

import static androidx.navigation.ui.NavigationUI.setupActionBarWithNavController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.notesapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      // Get the navigation host fragment from this Activity
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.notesNavHostFragment);
      // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.getNavController();
       // Make sure actions in the ActionBar get propagated to the NavController
        //setupActionBarWithNavController(this, navController);

        NavigationUI.setupActionBarWithNavController(this,navController);

        Toast.makeText(this, "This is second commit", Toast.LENGTH_SHORT).show();

        //This is third commit

        //This is fourth commit

    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
}