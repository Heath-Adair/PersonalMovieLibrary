package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import com.hadair.exceptions.ElementSaveFailedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

	private MovieRepository movieRepository;

	public MovieServiceImpl(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Override
	public Movie addMovie(Movie newMovie) throws ElementAlreadyExistsException, ElementSaveFailedException {
		if(!movieRepository.findByTitleIgnoreCaseAndYearReleased(newMovie.getTitle(), newMovie.getYearReleased()).isEmpty()) {
			throw new ElementAlreadyExistsException("Movie with title " + newMovie.getTitle() + ", and released in the year " + newMovie.getYearReleased() + " already exists");
		} else {
			Movie savedMovie = movieRepository.save(newMovie);
			if (savedMovie == null) {
				throw new ElementSaveFailedException("The movie " + newMovie.getTitle() + " failed to be saved");
			} else {
				return savedMovie;
			}
		}
	}

	@Override
	public Movie updateMovie(Long movieId, Movie updatedMovie) {
		//TODO if new movie field is null do not update that field, but maybe if blank? Maybe handle this as part of validation
		Movie movie = movieRepository.findOne(movieId);
		if(movie != null) {
			movie.setTitle(updatedMovie.getTitle());
			movie.setDuration(updatedMovie.getDuration());
			movie.setGenre(updatedMovie.getGenre());
			movie.setRating(updatedMovie.getRating());
			movie.setYearReleased(updatedMovie.getYearReleased());
			movie = movieRepository.save(movie);
		}
		return movie;
	}

	@Override
	public Movie getMovieByID(Long movieId) {
		return movieRepository.findOne(movieId);
	}

	@Override
	public void deleteMovie(Long movieId) throws ElementNotFoundException {
		if (!movieRepository.exists(movieId)) {
			throw new ElementNotFoundException("Movie with ID: " + movieId + ", was not found");
		} else {
			movieRepository.delete(movieId);
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