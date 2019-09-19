package com.personalmovielib.api.controller;

import com.personalmovielib.api.jpaRepository.MovieRepository;
import com.personalmovielib.api.model.Movie;
import com.personalmovielib.api.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class MovieController {

    private MovieRepository movieRepository;
    private MovieService movieService;

    public MovieController(MovieRepository movieRepository, MovieService movieService) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
    }

    // ------------------- Return the current local time -----------------
    @GetMapping("/api/timeOfDay")
    public String getTimeOfDay() {
        return movieService.getTimeOfDay();
    }

    // ------------------- Add a movie by ID -----------------------------
    @PostMapping("/api/movie/")
    public ResponseEntity<Void> addMovie(@RequestBody Movie newMovie) {
        return movieService.addMovie(newMovie);
    }

    // ------------------- Update a movie by ID -----------------------------
    @PutMapping("/api/movie/{movieId}")
    public ResponseEntity<Void> updateMovie(@PathVariable("movieId") Long movieId, @RequestBody Movie newMovie) {
        return movieService.updateMovie(movieId, newMovie);
    }

    // ------------------- Get a movie by ID -----------------------------
    @GetMapping("/api/movie/{movieId}")
    public ResponseEntity<?> retrieveDetailsForMovieByID(@PathVariable("movieId") Long movieId) {
        return movieService.retrieveDetailsForMovieByID(movieId);
    }

    // ------------------- Get all movies with a specific title ----------
    //Returns List of movies in case there are multiple movies with the same title
    @GetMapping("/api/movie/search/{movieTitle}")
    public List<Movie> retrieveDetailsForMovieByTitle(@PathVariable("movieTitle") String movieTitle) {
        //TODO Might could make this better with @RequestParam in the signature
        //TODO - Address issues for movie titles like "The Mummy" vs "Mummy, The"?
        return movieRepository.findByTitleIgnoreCase(movieTitle.trim());
    }

    // ------------------- Delete a movie --------------------------------
    @DeleteMapping("/api/movie/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable("movieId") Long movieId) {
        return movieService.deleteMovie(movieId);
    }

    // ------------------- Returns list of movie entries -----------------
    @GetMapping("/api/movie/list")
    public Iterable<Movie> getMovieList() {
        return movieRepository.findAll();
    }
}