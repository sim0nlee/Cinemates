package com.example.cinemates.data;

import com.example.cinemates.model.UserAction;
import com.google.android.gms.tasks.Task;

import java.util.Date;

@SuppressWarnings("All")
public interface UserFeedDataAsyncAccess {

    Task addUserActionToFollowersFeed (String userId, UserAction userAction);

    Task getUserFeed ();

    Task getFeedUpdatesStartingFrom (Date startingFrom);

}
