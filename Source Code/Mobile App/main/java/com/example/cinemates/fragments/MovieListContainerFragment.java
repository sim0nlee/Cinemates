package com.example.cinemates.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cinemates.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MovieListContainerFragment extends Fragment {

    private final SavedMoviesListFragment savedFragment = new SavedMoviesListFragment();
    private final FavoriteMoviesListFragment favoritesFragment = new FavoriteMoviesListFragment();

    private TabLayout tabLayout;

    private ViewPager2 viewPager;


    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lists, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tab_layout_liste);
        viewPager = view.findViewById(R.id.pager_liste);

        ListsFragmentAdapter adapter = new ListsFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        attachTabLayout();
    }

    public void attachTabLayout () {
        if (tabLayout != null && viewPager != null) {
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                if (position == 0) {
                    tab.setText("Preferiti");
                    tab.setIcon(R.drawable.ic_heart_full);
                }
                else if (position == 1) {
                    tab.setText("Salvati");
                    tab.setIcon(R.drawable.ic_bookmark_full);
                }
            }).attach();
        }
    }



    private class ListsFragmentAdapter extends FragmentStateAdapter {

        private static final int tabnum = 2;

        public ListsFragmentAdapter (Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0:
                    return favoritesFragment;
                case 1:
                    return savedFragment;
            }
            return favoritesFragment;
        }

        @Override
        public int getItemCount() {
            return tabnum;
        }

    }

}