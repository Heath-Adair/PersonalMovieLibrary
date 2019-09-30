package com.hadair.personalmovielib;

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
    public ResponseEntity<Void> addMovie(@RequestBody Movie newMovie) {
        Long result = movieService.addMovie(newMovie);
        if (result == -1) {
            //Status 304
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } else if(result == -2) {
            //Status 204 No Content
            return ResponseEntity.noContent().build();
        } else {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(result).toUri();
            //Status 201 Created
            return ResponseEntity.created(location).build();
        }
    }

    @PutMapping("/api/movies/{movieId}")
    public ResponseEntity<Void> updateMovie(@PathVariable("movieId") Long movieId, @RequestBody Movie newMovie) {
        Movie movie = movieService.updateMovie(movieId, newMovie);
        if(movie == null) {
            return ResponseEntity.noContent().build();
        } else {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(movie.getId()).toUri();
            //Status 201 Created
            return ResponseEntity.created(location).build();
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
        int result = movieService.deleteMovie(movieId);
        if (result == 0) {
            return new ResponseEntity<>("Movie with id " + movieId + " not found", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<Movie>(HttpStatus.ACCEPTED);
        }
    }

    @GetMapping("/api/movies/list")
    public Iterable<Movie> getMovieList() {
        return movieService.getMovies();
    }
}