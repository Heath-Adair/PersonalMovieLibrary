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
		return movieRepository.findById(movieId).map(existingMovie -> {
			existingMovie.setTitle(
					updatedMovie.getTitle() == null ? existingMovie.getTitle() : updatedMovie.getTitle()
			);
			existingMovie.setDuration(
					updatedMovie.getDuration() == null ? existingMovie.getDuration() : updatedMovie.getDuration()
			);
			existingMovie.setGenre(
					updatedMovie.getGenre() == null ? existingMovie.getGenre() : updatedMovie.getGenre()
			);
			existingMovie.setRating(
					updatedMovie.getRating() == null ? existingMovie.getRating() : updatedMovie.getRating()
			);
			existingMovie.setYearReleased(
					updatedMovie.getYearReleased() == 0 ? existingMovie.getYearReleased() : updatedMovie.getYearReleased()
			);
			return movieRepository.save(existingMovie);
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