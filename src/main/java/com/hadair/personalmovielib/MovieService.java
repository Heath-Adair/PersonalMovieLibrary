package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import com.hadair.exceptions.ElementSaveFailedException;

import java.util.List;

public interface MovieService {
    Movie addMovie(Movie newMovie) throws ElementAlreadyExistsException, ElementSaveFailedException;

    Movie updateMovie(Long movieId, Movie updatedMovie) throws ElementNotFoundException;

    Movie getMovieByID(Long movieId);

    void deleteMovie(Long movieId) throws ElementNotFoundException;

    Iterable<Movie> getMovies();

    List<Movie> getMovieByTitle(String movieTitle);
}
