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

    @GetMapping("/api/timeOfDay")
    public String getTimeOfDay() {
        return movieService.getTimeOfDay();
    }

    @PostMapping("/api/movies/")
    public ResponseEntity<Void> addMovie(@RequestBody Movie newMovie) {
        return movieService.addMovie(newMovie);
    }

    @PutMapping("/api/movies/{movieId}")
    public ResponseEntity<Void> updateMovie(@PathVariable("movieId") Long movieId, @RequestBody Movie newMovie) {
        return movieService.updateMovie(movieId, newMovie);
    }

    @GetMapping("/api/movies/{movieId}")
    public ResponseEntity<?> retrieveDetailsForMovieByID(@PathVariable("movieId") Long movieId) {
        return movieService.retrieveDetailsForMovieByID(movieId);
    }

    //Returns List of movies in case there are multiple movies with the same title
    @GetMapping("/api/movies/search/{movieTitle}")
    public List<Movie> retrieveDetailsForMovieByTitle(@PathVariable("movieTitle") String movieTitle) {
        //TODO Might could make this better with @RequestParam in the signature
        //TODO - Address issues for movie titles like "The Mummy" vs "Mummy, The"?
        return movieRepository.findByTitleIgnoreCase(movieTitle.trim());
    }

    @DeleteMapping("/api/movies/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable("movieId") Long movieId) {
        return movieService.deleteMovie(movieId);
    }

    @GetMapping("/api/movies/list")
    public Iterable<Movie> getMovieList() {
        return movieRepository.findAll();
    }
}