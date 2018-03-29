package com.example.juraj.popularmovies.model;

/**
 * Created by juraj on 3/19/18.
 */

public class MovieVideo {

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String key;

    public String name;

    public MovieVideo() {
    }

    ;

    public MovieVideo(String key, String name) {
        this.key = key;
        this.name = name;

    }

}
