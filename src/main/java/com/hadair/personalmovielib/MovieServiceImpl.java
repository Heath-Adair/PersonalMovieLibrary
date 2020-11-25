package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import com.hadair.exceptions.ElementSaveFailedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
	public Movie updateMovie(Long movieId, Movie updatedMovie) throws ElementNotFoundException {
		//TODO if new movie field is null do not update that field, but maybe if blank? Maybe handle this as part of validation
		//TODO Fix this Optional abuse
		Optional<Movie> optionalMovie = movieRepository.findById(movieId);
		if(optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			movie.setTitle(updatedMovie.getTitle());
			movie.setDuration(updatedMovie.getDuration());
			movie.setGenre(updatedMovie.getGenre());
			movie.setRating(updatedMovie.getRating());
			movie.setYearReleased(updatedMovie.getYearReleased());
			movie = movieRepository.save(movie);
			return movie;
		} else {
			throw new ElementNotFoundException("Movie with id: " + movieId + " not found.");
		}
	}

	@Override
	public Movie getMovieByID(Long movieId) {
		return movieRepository.findById(movieId).get();
	}

	@Override
	public void deleteMovie(Long movieId) throws ElementNotFoundException {
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