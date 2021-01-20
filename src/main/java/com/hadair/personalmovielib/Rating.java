package com.hadair.personalmovielib;

public enum Rating {
    G("General Audiences"),
    PG("Parental Guidance"),
    PG13("Parental Guidance 13"),
    R("Restricted");

    private final String ratingDescription;

    Rating(String ratingDescription) {
        this.ratingDescription = ratingDescription;
    }
}
