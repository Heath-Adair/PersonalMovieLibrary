package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

	private final MovieRepository movieRepository;

	public MovieServiceImpl(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Override
	public Movie addMovie(Movie newMovie) {
		if(!movieRepository.findByTitleIgnoreCaseAndYearReleased(newMovie.getTitle(), newMovie.getYearReleased()).isEmpty()) {
			throw new ElementAlreadyExistsException("Movie with title " + newMovie.getTitle()
					+ ", and released in the year " + newMovie.getYearReleased() + " already exists");
		} else {
			return movieRepository.save(newMovie);
		}
	}

	@Override
	public Movie updateMovie(Long movieId, Movie updatedMovie) {
		//TODO Fix this Optional abuse
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			movie.setTitle(updatedMovie.getTitle() != null ? updatedMovie.getTitle() : movie.getTitle());
			movie.setDuration(updatedMovie.getDuration() != null ? updatedMovie.getDuration() : movie.getDuration());
			movie.setGenre(updatedMovie.getGenre() != null ? updatedMovie.getGenre() : movie.getGenre());
			movie.setRating(updatedMovie.getRating() != null ? updatedMovie.getRating() : movie.getRating());
			movie.setYearReleased(updatedMovie.getYearReleased() != 0 ? updatedMovie.getYearReleased() : movie.getYearReleased());
			return movieRepository.save(movie);
		} else {
			throw new ElementNotFoundException("Movie with id: " + movieId + " not found.");
		}
	}

	@Override
	public Movie getMovieByID(Long movieId) {
		Optional<Movie> movie = movieRepository.findById(movieId);
		if(movie.isPresent()) {
			return movie.get();
		} else {
			throw new ElementNotFoundException("Movie with id: " + movieId + " not found.");
		}
	}

	@Override
	public void deleteMovie(Long movieId) {
		if (!movieRepository.existsById(movieId)) {
			throw new ElementNotFoundException("Movie with ID: " + movieId + ", was not found");
		} else {
			movieRepository.deleteById(movieId);
		}
	}

	@Override
	public Iterable<Movie> getMovies() {
		return movieRepository.findAll();
	}

	@Override
	public List<Movie> getMovieByTitle(String movieTitle) {
		//TODO - Address issues for movie titles like "The Mummy" vs "Mummy, The"?
		return movieRepository.findByTitleIgnoreCase(movieTitle.trim());
	}
}