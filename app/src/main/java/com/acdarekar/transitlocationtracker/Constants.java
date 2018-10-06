package com.acdarekar.transitlocationtracker;

import com.google.firebase.auth.FirebaseUser;

public class Constants {
    static FirebaseUser user;

    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        Constants.user = user;
    }
}
