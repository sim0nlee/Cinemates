package com.example.cinemates.data;

import android.content.Context;

import com.google.android.gms.tasks.Task;

@SuppressWarnings("All")
public interface UserFollowingsDataAsyncAccess {

    Task sendFriendRequest (String target_uid);

    Task getSentFriendRequest (String targetUid);

    Task getFriend (String uid);

    Task acceptFriendRequest (Context context, String senderUid);

    Task refuseFriendRequest (Context context, String senderUid);

}
