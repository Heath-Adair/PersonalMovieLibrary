package com.hadair.personalmovielib;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class MovieService {

	private MovieRepository movieRepository;

	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public ResponseEntity<Void> addMovie(Movie newMovie) {
		if(!movieRepository.findByTitleIgnoreCaseAndYearReleased(newMovie.getTitle(), newMovie.getYearReleased()).isEmpty()) {
			return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
		} else {
			Movie savedMovie = movieRepository.save(newMovie);
			if (savedMovie == null) {
				//Status 204 No Content
				return ResponseEntity.noContent().build();
			} else {
				URI location = ServletUriComponentsBuilder.fromCurrentRequest()
						.path("/{id}").buildAndExpand(savedMovie.getId()).toUri();
				//Status 201 Created
				return ResponseEntity.created(location).build();
			}
		}
	}

	public ResponseEntity<Void> updateMovie(Long movieId, Movie newMovie) {
		Movie movie = movieRepository.findOne(movieId);

		if(movie == null) {
			return ResponseEntity.noContent().build();
		}

		movie.setTitle(newMovie.getTitle());
		movie.setDuration(newMovie.getDuration());
		movie.setGenre(newMovie.getGenre());
		movie.setRating(newMovie.getRating());
		movie.setYearReleased(newMovie.getYearReleased());
		Movie savedMovie = movieRepository.save(movie);

		if(savedMovie == null) {
			//Status 204 No Content
			return ResponseEntity.noContent().build();
		} else {
			URI location = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}").buildAndExpand(movie.getId()).toUri();
			//Status 201 Created
			return ResponseEntity.created(location).build();
		}
	}

	public ResponseEntity<?> retrieveDetailsForMovieByID(Long movieId) {
		Movie movie = movieRepository.findOne(movieId);
		if (movie == null) {
			return new ResponseEntity<>("Movie with id " + movieId + " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(movie, HttpStatus.OK);
	}

	public ResponseEntity<?> deleteMovie(Long movieId) {
		if (!movieRepository.exists(movieId)) {
			return new ResponseEntity<>("Movie with id " + movieId + " not found", HttpStatus.NOT_FOUND);
		} else {
			movieRepository.delete(movieId);
			return new ResponseEntity<Movie>(HttpStatus.ACCEPTED);
		}
	}

	public Iterable<Movie> getMovies() {
		return movieRepository.findAll();
	}

	public List<Movie> getMovieByTitle(String movieTitle) {
		//TODO - Address issues for movie titles like "The Mummy" vs "Mummy, The"?
		return movieRepository.findByTitleIgnoreCase(movieTitle.trim());
	}
}