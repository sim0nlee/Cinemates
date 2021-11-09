package com.example.cinemates.control;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.cinemates.GlideApp;
import com.example.cinemates.R;
import com.example.cinemates.helper.StatisticsUpdates;
import com.example.cinemates.activities.LogInActivity;
import com.example.cinemates.activities.MainActivity;
import com.example.cinemates.activities.MovieActivity;
import com.example.cinemates.activities.RegistrationActivity;
import com.example.cinemates.data.FirestoreUserData;
import com.example.cinemates.data.FirestoreUserFeedData;
import com.example.cinemates.data.FirestoreUserFollowingsData;
import com.example.cinemates.data.FirestoreUserMovieListsData;
import com.example.cinemates.fragments.FriendsListFragment;
import com.example.cinemates.fragments.HomeFragment;
import com.example.cinemates.fragments.MovieListAbstractFragment;
import com.example.cinemates.fragments.MovieSearchFragment;
import com.example.cinemates.model.AppUser;
import com.example.cinemates.model.Movie;
import com.example.cinemates.model.UserAction;
import com.example.cinemates.model.UserActionFollow;
import com.example.cinemates.model.UserActionMovieList;
import com.example.cinemates.helper.InputChecker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings("All")
public class Controller {

    private static final String TAG = Controller.class.getSimpleName();
    
    private static final FirestoreUserData userDataHandler = FirestoreUserData.getInstance();
    private static final FirestoreUserFollowingsData userFollowingsDataHandler = FirestoreUserFollowingsData.getInstance();
    private static final FirestoreUserFeedData userFeedDataHandler = FirestoreUserFeedData.getInstance();
    private static final FirestoreUserMovieListsData userMovieListsDataHandler = FirestoreUserMovieListsData.getInstance();



    //[START] Gestione dell'accesso ai dati
    public static FirestoreUserData getUserDataHandler () {
        return userDataHandler;
    }
    public static FirestoreUserFollowingsData getUserFollowingsDataHandler () {
        return userFollowingsDataHandler;
    }
    public static FirestoreUserFeedData getUserFeedDataHandler () {
        return userFeedDataHandler;
    }
    public static FirestoreUserMovieListsData getUserMovieListsDataHandler () {
        return userMovieListsDataHandler;
    }
    //[END] Gestione dell'accesso ai dati



    //[START] Gestione dell'autenticazione
    public static void onUserRegistrationSuccessful (RegistrationActivity registrationActivity,
                                                     String nome, String cognome, String email) {

        registrationActivity.getProgressDialog().dismiss();

        String uid = AuthHandler.auth.getCurrentUser().getUid();

        userDataHandler.addUser(nome, cognome, email, uid)
        .addOnSuccessListener(aVoid -> {

            StatisticsUpdates.updateUsers();

            userDataHandler.fetchUser(uid)
            .addOnSuccessListener(user -> {
                AuthHandler.setCurrentUserInstance(user.toObject(AppUser.class));
                updateUIRegistrationActivity(registrationActivity);
            });

        });

        Toast.makeText(registrationActivity, "Registrazione Avvenuta", Toast.LENGTH_SHORT).show();

    }
    public static void onUserRegistrationFailed (RegistrationActivity registrationActivity, Exception e) {
        String message = e.getMessage();
        if (message.contains("email address is already in use")) {
            message = "Questa Email è già in uso";
        }
        else if (message.contains("email address is badly formatted")) {
            message = "Siamo spiacenti: questa Email non è valida";
        }
        else if (message.contains("internal error has occurred")) {
            message = "Errore di connessione";
        }
        registrationActivity.getProgressDialog().dismiss();
        Toast.makeText(registrationActivity, message, Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }

    public static void onLogInSuccessful (LogInActivity logInActivity) {

        String uid = AuthHandler.auth.getUid();

        logInActivity.getProgressDialog().dismiss();

        userDataHandler.fetchUser(uid)
        .addOnSuccessListener(user -> {
            AuthHandler.setCurrentUserInstance(user.toObject(AppUser.class));
            Controller.updateUILogInActivity(logInActivity);
        });

    }
    public static void onFacebookLoginSuccessful (LogInActivity logInActivity, boolean isNewUser) {

        String[] fullname;
        String email, uid;

        if (isNewUser) {
            StatisticsUpdates.updateUsers();

            fullname = AuthHandler.auth.getCurrentUser().getDisplayName().split(" ", 2);
            email = AuthHandler.auth.getCurrentUser().getEmail();
            uid = AuthHandler.auth.getCurrentUser().getUid();
            userDataHandler.addUser(fullname[0], fullname[1], email, uid);
        }

        onLogInSuccessful(logInActivity);

    }
    public static void onLogInFailed (LogInActivity logInActivity, Exception e) {
        String message = e.getMessage();
        if (message.contains("password is invalid")) {
            message = "La password è errata o l'utente non ha una password";
        }
        else if (message.contains("no user record corresponding to this identifier")) {
            message = "Questa Email non è associata ad alcun account";
        }
        else if (message.contains("internal error has occurred")) {
            message = "Errore di connessione";
        }
        Toast.makeText(logInActivity, message, Toast.LENGTH_SHORT).show();
        e.printStackTrace();
        logInActivity.getProgressDialog().dismiss();
    }

    private static void updateUILogInActivity (LogInActivity logInActivity) {
        Toast.makeText(logInActivity, "Sei loggato come " +
                                       InputChecker.capitalizeNames(AuthHandler.getCurrentUserInstance().getName()) + " " +
                                       InputChecker.capitalizeNames(AuthHandler.getCurrentUserInstance().getSurname()), Toast.LENGTH_SHORT)
                                       .show();
        logInActivity.startActivity(new Intent(logInActivity, MainActivity.class));
        logInActivity.getProgressDialog().dismiss();
        logInActivity.finish();
    }
    private static void updateUIRegistrationActivity (RegistrationActivity registrationActivity) {
        Toast.makeText(registrationActivity, "Sei loggato come " +
                                              InputChecker.capitalizeNames(AuthHandler.getCurrentUserInstance().getName()) + " " +
                                              InputChecker.capitalizeNames(AuthHandler.getCurrentUserInstance().getSurname()), Toast.LENGTH_SHORT)
                                              .show();
        registrationActivity.startActivity(new Intent(registrationActivity, MainActivity.class));
        registrationActivity.getProgressDialog().dismiss();
        registrationActivity.finish();
    }

    public static void onLogout (MainActivity activity) {
        activity.getMovieSearchFragment().getMovies().clear();
        activity.getMovieSearchFragment().getMoviesAdapter().notifyDataSetChanged();
        activity.startActivity(new Intent(activity, LogInActivity.class));
        activity.finish();
        Toast.makeText(activity, "Ti sei sloggato", Toast.LENGTH_SHORT).show();
    }
    //[END] Gestione dell'autenticazione



    //[START] Gestione del feed
    public static void initUserFeed (HomeFragment fragment) {

        userFeedDataHandler.getUserFeed()
        .addOnSuccessListener(queryDocumentSnapshots -> {

            List<UserAction> actions = fragment.getActions();

            for (DocumentSnapshot actionSnapshot : queryDocumentSnapshots) {

                UserAction userAction = null;

                boolean isAddFriendAction = actionSnapshot.getString("actionType").equals(UserAction.ActionType.FOLLOW.toString());
                boolean isAddMovieAction = actionSnapshot.getString("actionType").equals(UserAction.ActionType.ADD_MOVIE.toString());

                if (isAddFriendAction)
                    userAction = actionSnapshot.toObject(UserActionFollow.class);
                else if (isAddMovieAction)
                    userAction = actionSnapshot.toObject(UserActionMovieList.class);

                actions.add(0, userAction);
                fragment.getUserActionAdapter().notifyItemInserted(0);

            }

            fragment.setMostRecentPostTimestamp(actions.size() > 0 ? actions.get(0).getDate() : new Date());

            if (actions.size() == 0)
                fragment.getNoPostsTextView().setVisibility(View.VISIBLE);

        });

    }
    public static void handleOnFeedRefresh (HomeFragment fragment) {

        userFeedDataHandler.getFeedUpdatesStartingFrom(fragment.getMostRecentPostTimestamp())
        .addOnSuccessListener(queryDocumentSnapshots -> {

                List<UserAction> actions = fragment.getActions();

                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot actionSnapshot : queryDocumentSnapshots) {
                        UserAction userAction = null;

                        boolean isAddFriendAction = actionSnapshot.getString("actionType").equals(UserAction.ActionType.FOLLOW.toString());
                        boolean isAddMovieAction = actionSnapshot.getString("actionType").equals(UserAction.ActionType.ADD_MOVIE.toString());

                        if (isAddFriendAction)
                            userAction = actionSnapshot.toObject(UserActionFollow.class);
                        else if (isAddMovieAction)
                            userAction = actionSnapshot.toObject(UserActionMovieList.class);

                        actions.add(0, userAction);
                        fragment.getUserActionAdapter().notifyItemInserted(0);
                    }
                    fragment.getLinearLayoutManager().scrollToPosition(0);
                    fragment.setMostRecentPostTimestamp(actions.size() > 0 ? actions.get(0).getDate() : new Date());
                }

                updateHomeFragmentAfterRefresh(fragment);

        });

    }
    private static void updateHomeFragmentAfterRefresh (HomeFragment fragment) {
        fragment.getSwipeRefreshLayout().setRefreshing(false);

        if (fragment.getActions().size() == 0)
            fragment.getNoPostsTextView().setVisibility(View.VISIBLE);
        else
            fragment.getNoPostsTextView().setVisibility(View.GONE);
    }

    public static void setAddFriendActionPropicViewsInFeed (Context context,
                                                            UserActionFollow action,
                                                            ImageView userPropicView, ImageView addedUserPropicView) {

        userDataHandler.fetchUser(action.getUser().getUid())
        .addOnSuccessListener(user -> {

            userDataHandler.fetchUser(action.getFollowedUser().getUid())
            .addOnSuccessListener(addedUser -> {

                        GlideApp
                                .with(context)
                                .load(user.getString("propic"))
                                .apply(new RequestOptions().error(R.drawable.user_placeholder).override(80, 80))
                                .into(userPropicView);
                        GlideApp
                                .with(context)
                                .load(addedUser.getString("propic"))
                                .apply(new RequestOptions().error(R.drawable.user_placeholder).override(80, 80))
                                .into(addedUserPropicView);

            });

        });

    }
    public static void setMovieActionPropicViewInFeed (Context context,
                                                       UserActionMovieList action,
                                                       ImageView userPropicView) {

        userDataHandler.fetchUser(action.getUser().getUid())
        .addOnSuccessListener(user -> {
            GlideApp
                .with(context)
                .load(user.getString("propic"))
                .apply(new RequestOptions().error(R.drawable.user_placeholder).override(80, 80))
                .into(userPropicView);
        });

    }
    //[END] Gestione del feed



    //[START] Gestione della ricerca degli utenti
    public static void handleUserSearch (FriendsListFragment fragment, String query) {

        query = query.toLowerCase();

        userDataHandler.fetchUsersByNameOrSurname(query)
        .addOnSuccessListener(objects -> {

            QuerySnapshot nameQuerySnapshot = (QuerySnapshot) objects.get(0);
            QuerySnapshot surnameQuerySnapshot = (QuerySnapshot) objects.get(1);
            QuerySnapshot fullnameQuerySnapshot = (QuerySnapshot) objects.get(2);

            List<AppUser> userList = fragment.getUsersList();
            userList.clear();

            for (QueryDocumentSnapshot snapshot : nameQuerySnapshot) {
                AppUser user = snapshot.toObject(AppUser.class);
                userList.add(user);
            }
            for (QueryDocumentSnapshot snapshot : surnameQuerySnapshot) {
                AppUser user = snapshot.toObject(AppUser.class);
                if (!userList.contains(user))
                    userList.add(user);
            }
            for (QueryDocumentSnapshot snapshot : fullnameQuerySnapshot) {
                AppUser user = snapshot.toObject(AppUser.class);
                if (!userList.contains(user))
                    userList.add(user);
            }
            updateFragmentAfterUserSearch(fragment);

        })
        .addOnFailureListener(Throwable::printStackTrace);

    }
    private static void updateFragmentAfterUserSearch (FriendsListFragment fragment) {
        if (fragment.getUsersList().size() == 0) {
            fragment.getNoSearchMessageTextView().setVisibility(View.GONE);
            fragment.getNoResultsMessageTextView().setVisibility(View.VISIBLE);
        }
        else {
            fragment.getNoSearchMessageTextView().setVisibility(View.GONE);
            fragment.getNoResultsMessageTextView().setVisibility(View.GONE);
        }
        fragment.getUsersAdapter().notifyDataSetChanged();
        fragment.getRecyclerView().setAdapter(fragment.getUsersAdapter());
    }

    public static void setUserButtonsInUserList (Context context,
                                                 ImageButton sendButton, ImageButton deleteButton,
                                                 String friendUid) {

        if (AuthHandler.getCurrentUserInstance().getUid().equals(friendUid)) {
            return;
        }

        setUserButtonsInUserListClickListeners(context, sendButton, deleteButton, friendUid);

        userFollowingsDataHandler.getFriend(friendUid)
        .addOnSuccessListener(friend -> {
            if (!friend.exists()) {
                userFollowingsDataHandler.getSentFriendRequest(friendUid)
                .addOnSuccessListener(request -> {
                    if (!request.exists()) {
                        sendButton.setVisibility(View.VISIBLE);
                    }
                    else {
                        deleteButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

    }
    private static void setUserButtonsInUserListClickListeners (Context context,
                                                                ImageButton sendButton, ImageButton deleteButton,
                                                                String targetUid) {
        sendButton.setOnClickListener(v -> {
            userFollowingsDataHandler.sendFriendRequest(targetUid)
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(context, "Richiesta inviata", Toast.LENGTH_SHORT).show();
                sendButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.VISIBLE);
            });
        });

        deleteButton.setOnClickListener(v -> {
            userFollowingsDataHandler.deleteFriendRequest(targetUid)
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(context, "Richiesta annullata", Toast.LENGTH_SHORT).show();
                sendButton.setVisibility(View.VISIBLE);
                deleteButton.setVisibility(View.GONE);
            });
        });

    }
    //[END] Gestione della ricerca degli utenti



    //[START] Gestione della presentazione dei risultati di TMDB
    public static void updateFragmentAfterMovieSearch (MovieSearchFragment fragment, List<Movie> results) {
        Collections.sort(results);
        fragment.getMovies().addAll(results);
        results.clear();
        fragment.getProgressBar().setVisibility(View.GONE);
        fragment.getMoviesAdapter().notifyDataSetChanged();
        if (fragment.getMoviesAdapter().getItemCount() == 0) {
            fragment.getSearchForAMovieTextView().setVisibility(View.GONE);
            fragment.getNoResultsTextView().setVisibility(View.VISIBLE);
        }
        else {
            fragment.getSearchForAMovieTextView().setVisibility(View.GONE);
            fragment.getNoResultsTextView().setVisibility(View.GONE);
        }
    }

    public static void setMovieActivity (MovieActivity activity,
                                          String backdrop, String title, String directors, String runtime,
                                          String genres, String year, String description) {

        ImageView movieImgView = activity.findViewById(R.id.film_imgview);
        TextView titleTextView = activity.findViewById(R.id.titolo_txtview);
        TextView directorsTextView = activity.findViewById(R.id.registi_txtview);
        TextView runtimeTextView = activity.findViewById(R.id.durata_txtview);
        TextView genresTextView = activity.findViewById(R.id.generi_txtview);
        TextView yearTextView = activity.findViewById(R.id.anno_txtview);
        TextView descriptionTextView = activity.findViewById(R.id.descrizione_txtview);

        GlideApp
                .with(activity)
                .load(backdrop)
                .apply(new RequestOptions().error(R.drawable.movie_backdrop_placeholder).override(movieImgView.getWidth(), movieImgView.getHeight()))
                .into(movieImgView);

        titleTextView.setText(title);
        directorsTextView.setText(directors);
        runtimeTextView.setText(runtime);
        genresTextView.setText(genres);
        yearTextView.setText(year);
        descriptionTextView.setText(description);

        activity.setMovieGenres(genres);
        ListenerDispenser.attachFavoriteMoviesButtonListener(activity);
        ListenerDispenser.attachSavedMoviesButtonListener(activity);

    }
    //[END] Gestione della presentazione dei risultati di TMDB



    //[START] Listener Helper
    public static void movieListListenersHelper (MovieListAbstractFragment fragment, QuerySnapshot movies) {

        for (DocumentChange documentChange : movies.getDocumentChanges()) {
            switch (documentChange.getType()) {
                case ADDED:
                    fragment.addMovie(documentChange.getDocument().toObject(Movie.class));
                    break;
                case REMOVED:
                    fragment.removeMovie(documentChange.getDocument().toObject(Movie.class));
                    break;
            }
            fragment.getMoviesAdapter().notifyDataSetChanged();
        }
        if (fragment.getMoviesAdapter().getItemCount() == 0)
            fragment.getNoMoviesTextView().setVisibility(View.VISIBLE);
        else
            fragment.getNoMoviesTextView().setVisibility(View.GONE);

    }

    public static void movieListButtonListenerHelper (MovieActivity activity,
                                                       QuerySnapshot movie,
                                                       UserActionMovieList.ListType listType) {

        final String LIST_TYPE = listType.toString().toLowerCase();

        UserActionMovieList.MovieActionType actionType = UserActionMovieList.MovieActionType.REMOVED;

        final int movieID = activity.getMovieID();
        final String moviePoster = activity.getMoviePoster();
        final String movieTitle = activity.getMovieTitle();
        final String movieDate = activity.getMovieDate();
        final String movieGenres = activity.getMovieGenres();

        final ImageButton button = listType == UserActionMovieList.ListType.PREFERITI ? activity.getFavoriteButton() : activity.getSaveButton();
        final int fullIcon = listType == UserActionMovieList.ListType.PREFERITI ? R.drawable.ic_heart_full : R.drawable.ic_bookmark_full;
        final int emptyIcon = listType == UserActionMovieList.ListType.PREFERITI ? R.drawable.ic_heart_empty : R.drawable.ic_bookmark_empty;

        for (DocumentSnapshot doc : movie) {
            if (doc.getId().equals(String.valueOf(movieID)))
                actionType = UserActionMovieList.MovieActionType.ADDED;
        }

        button.setImageResource(actionType == UserActionMovieList.MovieActionType.ADDED ? fullIcon : emptyIcon);

        final String ON_CLICK_TOAST_MESSAGE = (actionType == UserActionMovieList.MovieActionType.ADDED ? "Film rimosso dai " : "Film aggiunto ai ");

        UserActionMovieList.MovieActionType onClickActionType = actionType == UserActionMovieList.MovieActionType.ADDED ?
                                                                              UserActionMovieList.MovieActionType.REMOVED :
                                                                              UserActionMovieList.MovieActionType.ADDED;

        OnSuccessListener<Void> onSuccessListener = aVoid -> {
            Toast.makeText(activity, ON_CLICK_TOAST_MESSAGE + LIST_TYPE, Toast.LENGTH_SHORT).show();

            Date actionDate = new Date();
            AppUser actionUser = AuthHandler.getCurrentUserInstance();

            UserActionMovieList action = new UserActionMovieList(actionDate, actionUser, movieTitle, listType, onClickActionType);
            userFeedDataHandler.addUserActionToFollowersFeed(AuthHandler.getCurrentUserInstance().getUid(), action);
            StatisticsUpdates.updateMoviesInLists(movieGenres, onClickActionType);
        };

        button.setOnClickListener(v -> {
            if (onClickActionType == UserActionMovieList.MovieActionType.ADDED) {
                userMovieListsDataHandler.addMovieToCollection(LIST_TYPE, movieID, moviePoster, movieTitle, movieDate)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    runOnUIThread(() -> Toast.makeText(activity, "Errore di connessione", Toast.LENGTH_SHORT));
                });
            }
            else {
                userMovieListsDataHandler.deleteMovieFromCollection(LIST_TYPE, movieID)
                .addOnSuccessListener(onSuccessListener)
                .addOnFailureListener(e -> {
                    e.printStackTrace();
                    runOnUIThread(() -> Toast.makeText(activity, "Errore di connessione", Toast.LENGTH_SHORT));
                });
            }
        });

    }
    //[END] Listener Helper



    //[START] Gestione thread separati
    public static void runSeparateThread (Thread t) {
        t.start();
    }

    public static void runOnUIThread (Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
    //[END] Gestione thread separati

}
