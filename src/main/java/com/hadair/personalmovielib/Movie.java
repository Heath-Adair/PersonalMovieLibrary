package com.hadair.personalmovielib;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String genre;
    private int yearReleased;
    private String rating; //An enum would be better
    private String duration;

    protected Movie() {}

    public Movie(Long id, String title, String genre, int yearReleased, String rating, String duration) {
        this(title, genre, yearReleased, rating, duration);
        this.id = id;
    }

    public Movie(String title, String genre, int yearReleased, String rating, String duration) {
        this.title = title;
        this.genre = genre;
        this.yearReleased = yearReleased;
        this.rating = rating;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYearReleased() {
        return yearReleased;
    }

    public void setYearReleased(int yearReleased) {
        this.yearReleased = yearReleased;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return String.format("Movie [id=%s, title=%s, genre=%s, yearReleased=%s, rating=%s, duration=%s]",
                id, title, genre, yearReleased, rating, duration);
    }
}