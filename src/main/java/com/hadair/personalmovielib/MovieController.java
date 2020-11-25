package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import com.hadair.exceptions.ElementSaveFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
class MovieController {

    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/api/movies")
    public ResponseEntity<?> addMovie(@RequestBody Movie newMovie) {
        Movie addedMovie;
        try {
            addedMovie = movieService.addMovie(newMovie);
        } catch (ElementAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ElementSaveFailedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NO_CONTENT);
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(addedMovie.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/api/movies/{movieId}")
    public ResponseEntity updateMovie(@PathVariable("movieId") Long movieId, @RequestBody Movie newMovie) {
        try {
            Movie movie = movieService.updateMovie(movieId, newMovie);

            if(movie == null) {
                return ResponseEntity.noContent().build();
            } else {
                URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}").buildAndExpand(movie.getId()).toUri();
                return ResponseEntity.created(location).build();
            }
        } catch(ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/api/movies/{movieId}")
    public ResponseEntity<?> getMovieByID(@PathVariable("movieId") Long movieId) {
        Movie movie = movieService.getMovieByID(movieId);
        if (movie == null) {
            return new ResponseEntity<>("Movie with id " + movieId + " not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    //Returns List of movies in case there are multiple movies with the same title
    @GetMapping("/api/movies/search/{movieTitle}")
    public List<Movie> getMovieByTitle(@PathVariable("movieTitle") String movieTitle) {
        //TODO Might could make this better with @RequestParam in the signature
        return movieService.getMovieByTitle(movieTitle);
    }

    @DeleteMapping("/api/movies/{movieId}")
    public ResponseEntity<?> deleteMovie(@PathVariable("movieId") Long movieId) {
        try {
            movieService.deleteMovie(movieId);
        } catch(ElementNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Movie>(HttpStatus.ACCEPTED);
    }

    @GetMapping("/api/movies/list")
    public Iterable<Movie> getMovieList() {
        return movieService.getMovies();
    }
}