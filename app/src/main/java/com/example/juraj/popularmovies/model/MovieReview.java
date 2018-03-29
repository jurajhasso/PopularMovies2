package com.example.juraj.popularmovies.model;

/**
 * Created by juraj on 3/19/18.
 */

public class MovieReview {

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String content;
    public String author;

    public MovieReview() {
    }

    ;

    public MovieReview(String content, String author) {
        this.content = content;
        this.author = author;

    }
}
