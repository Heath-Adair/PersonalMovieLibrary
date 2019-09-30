package com.hadair.personalmovielib;

import java.util.List;

public interface MovieService {
    Long addMovie(Movie newMovie);

    Movie updateMovie(Long movieId, Movie updatedMovie);

    Movie getMovieByID(Long movieId);

    int deleteMovie(Long movieId);

    Iterable<Movie> getMovies();

    List<Movie> getMovieByTitle(String movieTitle);
}
