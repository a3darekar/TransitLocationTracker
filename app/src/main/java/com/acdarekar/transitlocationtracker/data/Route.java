package com.acdarekar.transitlocationtracker.data;

import android.location.Location;

public class Route {
    private String route_no, route_name, uid;
    private int route_id;
    private Location location;
    public String getRoute_no() {

        return route_no;
    }

    public void setRoute_no(String route_no) {
        this.route_no = route_no;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Route(String uid, String route_no, String route_name, int route_id) {
        this.uid = uid;
        this.route_no = route_no;
        this.route_name = route_name;
        this.route_id = route_id;
        this.location = null;
    }

    public int getRoute_id() {
        return route_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }
}
