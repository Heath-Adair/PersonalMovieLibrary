package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
		return movieRepository.findById(movieId).map(movie -> {
			movie.setTitle(updatedMovie.getTitle() != null ? updatedMovie.getTitle() : movie.getTitle());
			movie.setDuration(updatedMovie.getDuration() != null ? updatedMovie.getDuration() : movie.getDuration());
			movie.setGenre(updatedMovie.getGenre() != null ? updatedMovie.getGenre() : movie.getGenre());
			movie.setRating(updatedMovie.getRating() != null ? updatedMovie.getRating() : movie.getRating());
			movie.setYearReleased(updatedMovie.getYearReleased() != 0 ? updatedMovie.getYearReleased() : movie.getYearReleased());
			return movieRepository.save(movie);
		}).orElseThrow(() -> new ElementNotFoundException(withMovieNotFoundMessage(movieId)));
	}

	@Override
	public Movie getMovieByID(Long movieId) {
		return movieRepository.findById(movieId)
				.orElseThrow(() -> new ElementNotFoundException(withMovieNotFoundMessage(movieId)));
	}

	@Override
	public void deleteMovie(Long movieId) {
		if (movieRepository.existsById(movieId)) {
			movieRepository.deleteById(movieId);
		} else {
			throw new ElementNotFoundException(withMovieNotFoundMessage(movieId));
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

	private String withMovieNotFoundMessage(Long movieId) {
		return "Movie with ID " + movieId + " not found.";
	}
}