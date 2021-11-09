package com.example.cinemates.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.example.cinemates.control.ListenerDispenser;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.control.Controller;
import com.example.cinemates.GlideApp;
import com.example.cinemates.R;
import com.example.cinemates.helper.InputChecker;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragment extends Fragment {

    private static final String TAG = FriendsListFragment.class.getSimpleName();

    private final List<AppUser> followingsList = new ArrayList<>();
    private final List<AppUser> usersList = new ArrayList<>();

    private SearchView searchView;
    private RecyclerView recyclerView;

    private FriendsAdapter friendsAdapter;
    private UsersAdapter usersAdapter;

    private TextView noSearchMessageTextView;
    private TextView noResultsMessageTextView;
    private TextView noFollowsMessageTextView;



    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        friendsAdapter = new FriendsAdapter(getContext(), followingsList);
        usersAdapter = new UsersAdapter(getContext(), usersList);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        initTextViews(view);
        initRecyclerView(view);
        ListenerDispenser.attachUserFollowingListener(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu (@NonNull Menu menu, @NonNull MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");
        inflater.inflate(R.menu.menu_ricerca_utenti, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu (@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        Log.d(TAG, "onPrepareOptionsMenu");
        initSearchViewFromMenu(menu);
    }

    @Override
    public void onDestroy () {
        Log.d(TAG, "onDestroy");
        ListenerDispenser.detachUserFollowingListener();
        super.onDestroy();
    }



    private void initTextViews (View inflatedView) {
        noSearchMessageTextView = inflatedView.findViewById(R.id.cerca_un_utente_txtview);
        noResultsMessageTextView = inflatedView.findViewById(R.id.nessun_utente_txtview);
        noFollowsMessageTextView = inflatedView.findViewById(R.id.nessun_seguito_txtview);
    }

    private void initRecyclerView (View inflatedView) {
        recyclerView = inflatedView.findViewById(R.id.lista_seguiti_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(friendsAdapter);
    }


    private void initSearchViewFromMenu (Menu menu) {

        searchView = (SearchView) menu.findItem(R.id.ricerca_utenti).getActionView();
        searchView.setQueryHint("Cerca tra tutti gli utenti");
        changeSearchViewTextColor(searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit (String query) {
                handleOnQueryTextSubmit(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange (String newText) {
                //
                return true;
            }

        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                handleSearchViewFocusGained();
            }
            else {
                handleSearchViewFocusLost();
            }
        });

        searchView.setOnCloseListener(() -> {
            super.onOptionsMenuClosed(menu);
            handleSearchViewClosed();
            return false;
        });

    }
    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(getResources().getColor(R.color.dark_grey));
                ((TextView) view).setHintTextColor(getResources().getColor(R.color.dark_grey));
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    private void handleOnQueryTextSubmit (String text) {
        final String query = text.trim().toLowerCase();
        if (!query.isEmpty()) {
            hideKeyboard(getActivity());
            Controller.handleUserSearch(this, query);
        }
        else
            recyclerView.setAdapter(friendsAdapter);
    }

    private void handleSearchViewFocusGained () {
        setTabTitle("Utenti");

        noFollowsMessageTextView.setVisibility(View.GONE);
        noSearchMessageTextView.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(usersAdapter);
        searchView.setQuery("", false);
    }
    private void handleSearchViewFocusLost () {
        setTabTitle("Seguiti");

        noSearchMessageTextView.setVisibility(View.GONE);
        noResultsMessageTextView.setVisibility(View.GONE);

        if (followingsList.size() == 0)
            noFollowsMessageTextView.setVisibility(View.VISIBLE);

        recyclerView.setAdapter(friendsAdapter);

        usersList.clear();
        usersAdapter.notifyDataSetChanged();
    }

    private void handleSearchViewClosed () {
        setTabTitle("Seguiti");
        recyclerView.setAdapter(friendsAdapter);
    }


    private void hideKeyboard (Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    private void setTabTitle (final String text) {
        ((SocialFragment)getParentFragment()).getTabLayout().getTabAt(0).setText(text);
    }



    public RecyclerView getRecyclerView () {
        return recyclerView;
    }

    public List<AppUser> getFollowingsList () {
        return followingsList;
    }
    public List<AppUser> getUsersList () {
        return usersList;
    }

    public FriendsAdapter getFriendsAdapter () { return friendsAdapter; }
    public UsersAdapter getUsersAdapter () { return usersAdapter; }

    public TextView getNoFollowsMessageTextView () {
        return noFollowsMessageTextView;
    }
    public TextView getNoSearchMessageTextView () {
        return noSearchMessageTextView;
    }
    public TextView getNoResultsMessageTextView () {
        return noResultsMessageTextView;
    }


//////////////////////////////////////////////////FriendsAdapter//////////////////////////////////////////////////

    public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendHolder> {

        final Context context;
        final List<AppUser> follows;

        public FriendsAdapter (Context context, List<AppUser> follows) {
            this.context = context;
            this.follows = follows;
        }

        @NonNull
        @Override
        public FriendHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_friend, parent, false);
            return new FriendHolder(view);
        }

        @Override
        public void onBindViewHolder (@NonNull FriendHolder holder, int position) {

            String name = follows.get(position).getName();
            String surname = follows.get(position).getSurname();
            String propic = follows.get(position).getPropic();

            holder.friendNameTextView.setText(InputChecker.capitalizeNames(name));
            holder.friendSurnameTextView.setText(InputChecker.capitalizeNames(surname));
            GlideApp
                .with(getContext())
                .load(propic)
                .apply(new RequestOptions().placeholder(R.drawable.user_placeholder))
                .into(holder.friendPropicView);

        }

        @Override
        public int getItemCount () {
            return follows.size();
        }



        private class FriendHolder extends RecyclerView.ViewHolder {

            private final ImageView friendPropicView;
            private final TextView friendNameTextView;
            private final TextView friendSurnameTextView;

            public FriendHolder (@NonNull View itemView) {
                super(itemView);
                friendPropicView = itemView.findViewById(R.id.propic_amico_imgview);
                friendNameTextView = itemView.findViewById(R.id.nome_amico_txtview);
                friendSurnameTextView = itemView.findViewById(R.id.cognome_amico_txtview);
            }

        }

    }

//////////////////////////////////////////////////UsersAdapter//////////////////////////////////////////////////

    public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {

        final Context context;
        final List<AppUser> users;

        public UsersAdapter (Context context, List<AppUser> users) {
            this.context = context;
            this.users = users;
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder (@NonNull UserHolder holder, int position) {

            String name = users.get(position).getName();
            String surname = users.get(position).getSurname();
            String propic = users.get(position).getPropic();
            String uid = users.get(position).getUid();

            holder.userNameTextView.setText(InputChecker.capitalizeNames(name));
            holder.userSurnameTextView.setText(InputChecker.capitalizeNames(surname));
            GlideApp.with(getContext()).load(propic).placeholder(R.drawable.user_placeholder).into(holder.userPropicView);
            Controller.setUserButtonsInUserList(getContext(), holder.followButton, holder.deleteFollowButton, uid);

        }

        @Override
        public int getItemCount () {
            return users.size();
        }



        private class UserHolder extends RecyclerView.ViewHolder {
            private final ImageView userPropicView;
            private final TextView userNameTextView;
            private final TextView userSurnameTextView;
            private final ImageButton followButton;
            private final ImageButton deleteFollowButton;

            public UserHolder (@NonNull View itemView) {
                super(itemView);

                userPropicView = itemView.findViewById(R.id.propic_utente_imgview);
                userNameTextView = itemView.findViewById(R.id.nome_utente_txtview);
                userSurnameTextView = itemView.findViewById(R.id.cognome_utente_txtview);
                followButton = itemView.findViewById(R.id.followButton);
                deleteFollowButton = itemView.findViewById(R.id.deleteRequestButton);
            }

        }

    }

}