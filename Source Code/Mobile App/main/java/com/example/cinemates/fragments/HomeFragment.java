package com.example.cinemates.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.cinemates.control.AuthHandler;
import com.example.cinemates.control.Controller;
import com.example.cinemates.R;
import com.example.cinemates.model.UserAction;
import com.example.cinemates.model.UserActionFollow;
import com.example.cinemates.model.UserActionMovieList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private Date mostRecentPostTimestamp;

    private final List<UserAction> actions = new ArrayList<>();

    private SwipeRefreshLayout swipeRefreshLayout;

    private UserActionAdapter userActionAdapter;
    private LinearLayoutManager linearLayoutManager;

    private TextView noPostsTextView;



    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        noPostsTextView = view.findViewById(R.id.nessun_post_txtview);

        if (AuthHandler.getCurrentUserInstance().isValid()) {
            initRecyclerView(view);
            initSwipeRefreshLayout(view);
        }
        return view;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void initRecyclerView (View inflatedView) {

        userActionAdapter = new UserActionAdapter(getContext(), actions);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        RecyclerView recyclerView = inflatedView.findViewById(R.id.home_recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(userActionAdapter);

        Controller.initUserFeed(this);

    }
    private void initSwipeRefreshLayout (View inflatedView) {
        swipeRefreshLayout = inflatedView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> Controller.handleOnFeedRefresh(this));
    }



    public Date getMostRecentPostTimestamp () {
        return mostRecentPostTimestamp;
    }
    public void setMostRecentPostTimestamp (Date d) {
        this.mostRecentPostTimestamp = d;
    }

    public List<UserAction> getActions () {
        return actions;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout () {return swipeRefreshLayout; }

    public UserActionAdapter getUserActionAdapter () {
        return userActionAdapter;
    }

    public LinearLayoutManager getLinearLayoutManager () { return linearLayoutManager; }

    public TextView getNoPostsTextView () {
        return noPostsTextView;
    }

//////////////////////////////////////////////UserActionAdapter//////////////////////////////////////////////

    public class UserActionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final Context context;
        private final List<UserAction> actions;

        public UserActionAdapter (Context context, List<UserAction> actions) {
            setHasStableIds(true);
            this.context = context;
            this.actions = actions;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (actions.get(position) instanceof UserActionFollow)
                return 0;
            else if (actions.get(position) instanceof UserActionMovieList)
                return 1;
            else
                return -1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
            View view;
            switch (viewType) {
                case 0:
                    view = LayoutInflater.from(context).inflate(R.layout.row_user_action_add_friend, parent, false);
                    return new UserActionAddFriendHolder(view);
                case 1:
                    view = LayoutInflater.from(context).inflate(R.layout.row_user_action_movie, parent, false);
                    return new UserActionMovieHolder(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, int position) {

            switch (holder.getItemViewType()) {

                case 0:
                    UserActionAddFriendHolder addFriendHolder = (UserActionAddFriendHolder) holder;
                    UserActionFollow addFriendAction = (UserActionFollow) actions.get(position);

                    Controller.setAddFriendActionPropicViewsInFeed(getContext(), addFriendAction,
                                                                   addFriendHolder.userPropicView, addFriendHolder.addedUserPropicView);

                    addFriendHolder.dateTextView.setText(addFriendAction.getDate().toString().substring(0, 19));
                    addFriendHolder.descriptionTextView.setText(addFriendAction.getDescription());
                    break;

                case 1:
                    UserActionMovieHolder movieActionHolder = (UserActionMovieHolder) holder;
                    UserActionMovieList movieAction = (UserActionMovieList) actions.get(position);

                    Controller.setMovieActionPropicViewInFeed(getContext(), movieAction, movieActionHolder.userPropicView);

                    movieActionHolder.dateTextView.setText(movieAction.getDate().toString().substring(0, 19));
                    movieActionHolder.descriptionTextView.setText(movieAction.getDescription());

            }

        }

        @Override
        public int getItemCount () {
            return actions.size();
        }



        private class UserActionAddFriendHolder extends RecyclerView.ViewHolder {
            private final ImageView userPropicView;
            private final ImageView addedUserPropicView;
            private final TextView dateTextView;
            private final TextView descriptionTextView;

            public UserActionAddFriendHolder (@NonNull View itemView) {
                super(itemView);
                userPropicView = itemView.findViewById(R.id.user_imgview);
                addedUserPropicView = itemView.findViewById(R.id.addeduser_imgview);
                dateTextView = itemView.findViewById(R.id.date_txtview);
                descriptionTextView = itemView.findViewById(R.id.description_txtview);
            }

        }

        private class UserActionMovieHolder extends RecyclerView.ViewHolder {
            private final ImageView userPropicView;
            private final TextView dateTextView;
            private final TextView descriptionTextView;

            public UserActionMovieHolder (@NonNull View itemView) {
                super(itemView);
                userPropicView = itemView.findViewById(R.id.user_imgview);
                dateTextView = itemView.findViewById(R.id.date_txtview);
                descriptionTextView = itemView.findViewById(R.id.description_txtview);
            }
        }

    }

}