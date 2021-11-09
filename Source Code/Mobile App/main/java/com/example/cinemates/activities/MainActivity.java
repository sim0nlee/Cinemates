package com.example.cinemates.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.cinemates.fragments.SocialFragment;
import com.example.cinemates.fragments.HomeFragment;
import com.example.cinemates.fragments.MovieListContainerFragment;
import com.example.cinemates.fragments.MovieSearchFragment;
import com.example.cinemates.fragments.ProfileFragment;
import com.example.cinemates.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView navigationView;

    private final HomeFragment homeFragment = new HomeFragment();
    private final SocialFragment socialFragment = new SocialFragment();
    private final MovieSearchFragment movieSearchFragment = new MovieSearchFragment();
    private final MovieListContainerFragment movieListContainerFragment = new MovieListContainerFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();

    private final FragmentManager fm = getSupportFragmentManager();

    private Fragment activeFragment = homeFragment;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragmentTransitions();
        setNavigationView();
        setToolbar();
    }

    private void setFragmentTransitions () {
        fm.beginTransaction().add(R.id.main_activity_content, profileFragment, "Profilo").hide(profileFragment).commit();
        fm.beginTransaction().add(R.id.main_activity_content, movieListContainerFragment, "Liste").hide(movieListContainerFragment).commit();
        fm.beginTransaction().add(R.id.main_activity_content, movieSearchFragment, "Ricerca").hide(movieSearchFragment).commit();
        fm.beginTransaction().add(R.id.main_activity_content, socialFragment, "Social").hide(socialFragment).commit();
        fm.beginTransaction().add(R.id.main_activity_content, homeFragment, "Home").commit();
    }
    private void setNavigationView () {
        navigationView = findViewById(R.id.bottomNavigationView);

        navigationView.setOnNavigationItemSelectedListener(item -> {

            int item_id = item.getItemId();

            if (item_id == R.id.nav_home)
                switchToHomeFragment();
            else if (item_id == R.id.nav_friends)
                switchToSocialFragment();
            else if (item_id == R.id.nav_search)
                switchToMovieSearchFragment();
            else if (item_id == R.id.nav_lists)
                switchToListsFragment();
            else if (item_id == R.id.nav_user)
                switchToProfileFragment();

            return true;

        });
    }
    private void setToolbar () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.yellow));
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

        findViewById(R.id.clickable_logo_view).setOnClickListener(v -> {
            hideKeyboard(this);
            navigationView.setSelectedItemId(R.id.nav_home);
        });
    }

    private void switchToHomeFragment () {
        getSupportActionBar().setTitle("Home");
        fm.beginTransaction().hide(activeFragment).show(homeFragment).commit();
        activeFragment = homeFragment;
    }
    private void switchToSocialFragment () {
        getSupportActionBar().setTitle("Social");
        fm.beginTransaction().hide(activeFragment).show(socialFragment).commit();
        activeFragment = socialFragment;
    }
    private void switchToMovieSearchFragment () {
        getSupportActionBar().setTitle("Ricerca Film");
        fm.beginTransaction().hide(activeFragment).show(movieSearchFragment).commit();
        activeFragment = movieSearchFragment;
    }
    private void switchToListsFragment () {
        getSupportActionBar().setTitle("Le tue liste");
        fm.beginTransaction().hide(activeFragment).show(movieListContainerFragment).commit();
        activeFragment = movieListContainerFragment;
    }
    private void switchToProfileFragment () {
        getSupportActionBar().setTitle("Profilo");
        fm.beginTransaction().hide(activeFragment).show(profileFragment).commit();
        activeFragment = profileFragment;
    }


    private void hideKeyboard (Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public MovieSearchFragment getMovieSearchFragment () {
        return movieSearchFragment;
    }

}