package com.example.cinemates.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cinemates.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SocialFragment extends Fragment {

    private final FriendsListFragment followingListFragment = new FriendsListFragment();
    private final FollowRequestsFragment requestsFragment = new FollowRequestsFragment();

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        tabLayout = view.findViewById(R.id.tab_layout_seguiti);
        viewPager = view.findViewById(R.id.pager_amici);

        FriendsFragmentAdapter adapter = new FriendsFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        attachTabLayout();
    }

    private void attachTabLayout () {
        if (tabLayout != null && viewPager != null) {
            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                if (position == 0)
                    tab.setText("Seguiti");
                else if (position == 1)
                    tab.setText("Richieste in arrivo");
            }).attach();
        }
    }

    public TabLayout getTabLayout () {
        return tabLayout;
    }



    private class FriendsFragmentAdapter extends FragmentStateAdapter {

        private static final int tabnum = 2;

        public FriendsFragmentAdapter (Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0:
                    return followingListFragment;
                case 1:
                    return requestsFragment;
            }
            return followingListFragment;
        }

        @Override
        public int getItemCount() {
            return tabnum;
        }

    }

}