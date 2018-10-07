package com.acdarekar.transitlocationtracker.data;

import com.google.firebase.auth.FirebaseUser;

public class Constants {
    private static FirebaseUser user;
    private static Route route;

    public static Route getRoute() {
        return route;
    }

    public static void setRoute(Route route) {
        Constants.route = route;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    public static void setUser(FirebaseUser user) {
        Constants.user = user;
    }
}
