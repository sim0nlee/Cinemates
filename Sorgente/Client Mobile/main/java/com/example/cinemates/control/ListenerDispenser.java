package com.example.cinemates.control;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.cinemates.GlideApp;
import com.example.cinemates.R;
import com.example.cinemates.activities.MovieActivity;
import com.example.cinemates.data.FirestoreReferences;
import com.example.cinemates.fragments.FavoriteMoviesListFragment;
import com.example.cinemates.fragments.FollowRequestsFragment;
import com.example.cinemates.fragments.FriendsListFragment;
import com.example.cinemates.fragments.ProfileFragment;
import com.example.cinemates.fragments.SavedMoviesListFragment;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.model.UserActionMovieList;
import com.example.cinemates.helper.InputChecker;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;

public class ListenerDispenser {

    private static ListenerRegistration userProfileListener;
    private static ListenerRegistration userFollowingsListener;
    private static ListenerRegistration userRequestsListener;
    private static ListenerRegistration favoriteMoviesListener;
    private static ListenerRegistration savedMoviesListener;
    private static ListenerRegistration favoriteMoviesButtonListener;
    private static ListenerRegistration savedMoviesButtonListener;


    public static void attachUserProfileListener (ProfileFragment profileFragment) {

        DocumentReference users = FirestoreReferences.getUserReference(AuthHandler.getCurrentUserInstance().getUid());

        userProfileListener = users.addSnapshotListener((value, error) -> {

            if (error != null) {
                error.printStackTrace();
                return;
            }

            ImageView imgView = profileFragment.getActivity().findViewById(R.id.propic_imgView);
            TextView nomeView = profileFragment.getActivity().findViewById(R.id.nome_textView);
            TextView cognomeView = profileFragment.getActivity().findViewById(R.id.cognome_textView);
            TextView emailView = profileFragment.getActivity().findViewById(R.id.email_textView);

            GlideApp
                    .with(profileFragment.getActivity())
                    .load(value.get("propic"))
                    .apply(new RequestOptions().error(R.drawable.user_placeholder))
                    .into(imgView);

            nomeView.setText(InputChecker.capitalizeNames(value.getString("name")));
            cognomeView.setText(InputChecker.capitalizeNames(value.getString("surname")));
            emailView.setText(value.getString("email"));

        });

    }
    public static void detachUserProfileListener () {
        if (userProfileListener != null) {
            userProfileListener.remove();
            userProfileListener = null;
        }
    }

    public static void attachUserFollowingListener (FriendsListFragment fragment) {

        CollectionReference followings = FirestoreReferences.getUserFollowingsReference();

        userFollowingsListener = followings.addSnapshotListener((value, error) -> {

            if (error != null) {
                error.printStackTrace();
                return;
            }

            TextView noFollowsMessageTextView = fragment.getNoFollowsMessageTextView();

            if (fragment.getFollowingsList().size() == 0)
                noFollowsMessageTextView.setVisibility(View.VISIBLE);
            else
                noFollowsMessageTextView.setVisibility(View.GONE);

            for (DocumentChange dc : value.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        noFollowsMessageTextView.setVisibility(View.GONE);

                        String newFriendId = dc.getDocument().getString("amico");
                        Controller.getUserDataHandler().fetchUser(newFriendId)
                        .addOnSuccessListener(friend -> {

                            String name, surname, propic, uid;

                            name = friend.getString("name");
                            surname = friend.getString("surname");
                            propic = friend.getString("propic");
                            uid = friend.getString("uid");

                            fragment.getFollowingsList().add(new AppUser(name, surname, null, propic, uid));
                            fragment.getFriendsAdapter().notifyDataSetChanged();

                        });
                        break;
                    case REMOVED:
                        //
                }
            }

        });

    }
    public static void detachUserFollowingListener () {
        if (userFollowingsListener != null) {
            userFollowingsListener.remove();
            userFollowingsListener = null;
        }
    }

    public static void attachRequestsListener (FollowRequestsFragment fragment) {

        CollectionReference requests = FirestoreReferences.getUserRequestsReference();

        userRequestsListener = requests.addSnapshotListener((value, error) -> {

            if (error != null) {
                error.printStackTrace();
                return;
            }

            if (fragment.getRequests().size() == 0)
                fragment.getNoRequestsMessageTextView().setVisibility(View.VISIBLE);
            else
                fragment.getNoRequestsMessageTextView().setVisibility(View.GONE);

            for (DocumentChange dc : value.getDocumentChanges()) {
                String senderUid = dc.getDocument().getId();
                switch (dc.getType()) {

                    case ADDED:
                        fragment.getNoRequestsMessageTextView().setVisibility(View.GONE);
                        Controller.getUserDataHandler().fetchUser(senderUid)
                        .addOnSuccessListener(sender -> {
                            String nome = sender.getString("name");
                            String cognome = sender.getString("surname");
                            String propic = sender.getString("propic");
                            String uid = sender.getString("uid");
                            fragment.getRequests().add(new AppUser(nome, cognome, null, propic, uid));
                            fragment.getRequestsAdapter().notifyItemInserted(fragment.getRequests().size()-1);
                        });
                        break;

                    case REMOVED:
                        fragment.removeRequest(senderUid);
                        fragment.getRequestsAdapter().notifyDataSetChanged();
                        if (fragment.getRequests().size() == 0)
                            fragment.getNoRequestsMessageTextView().setVisibility(View.VISIBLE);
                        else
                            fragment.getNoRequestsMessageTextView().setVisibility(View.GONE);
                        break;

                }
            }

        });

    }
    public static void detachRequestsListener () {
        if (userRequestsListener != null) {
            userRequestsListener.remove();
            userRequestsListener = null;
        }
    }

    public static void attachFavoriteMoviesListener (FavoriteMoviesListFragment fragment) {

        CollectionReference favorites = FirestoreReferences.getUserFavoriteMoviesReference();

        favoriteMoviesListener = favorites.addSnapshotListener((value, error) -> {

            if (error != null) {
                error.printStackTrace();
                return;
            }

            Controller.movieListListenersHelper(fragment, value);

        });

    }
    public static void detachFavoriteMoviesListener () {
        if (favoriteMoviesListener != null) {
            favoriteMoviesListener.remove();
            favoriteMoviesListener = null;
        }
    }

    public static void attachSavedMoviesListener (SavedMoviesListFragment fragment) {

        CollectionReference saved = FirestoreReferences.getUserSavedMoviesReference();

        favoriteMoviesListener = saved.addSnapshotListener((value, error) -> {

            if (error != null) {
                error.printStackTrace();
                return;
            }

            Controller.movieListListenersHelper(fragment, value);

        });

    }
    public static void detachSavedMoviesListener () {
        if (savedMoviesListener != null) {
            savedMoviesListener.remove();
            savedMoviesListener = null;
        }
    }

    public static void attachFavoriteMoviesButtonListener (MovieActivity activity) {

        CollectionReference favorites = FirestoreReferences.getUserFavoriteMoviesReference();

        favoriteMoviesButtonListener = favorites.addSnapshotListener((value, error) -> {

            if (error != null) {
                Controller.runOnUIThread(() -> Toast.makeText(activity, "Errore di connessione", Toast.LENGTH_SHORT));
                error.printStackTrace();
                return;
            }

            Controller.movieListButtonListenerHelper(activity, value, UserActionMovieList.ListType.PREFERITI);

        });

    }
    public static void detachFavoriteMoviesButtonListener () {
        if (favoriteMoviesButtonListener != null) {
            favoriteMoviesButtonListener.remove();
            favoriteMoviesButtonListener = null;
        }
    }

    public static void attachSavedMoviesButtonListener (MovieActivity activity) {

        CollectionReference saved = FirestoreReferences.getUserSavedMoviesReference();

        favoriteMoviesButtonListener = saved.addSnapshotListener((value, error) -> {

            if (error != null) {
                Controller.runOnUIThread(() -> Toast.makeText(activity, "Errore di connessione", Toast.LENGTH_SHORT));
                error.printStackTrace();
                return;
            }

            Controller.movieListButtonListenerHelper(activity, value, UserActionMovieList.ListType.SALVATI);

        });

    }
    public static void detachSavedMoviesButtonListener () {
        if (savedMoviesButtonListener != null) {
            savedMoviesButtonListener.remove();
            savedMoviesButtonListener = null;
        }
    }

}
