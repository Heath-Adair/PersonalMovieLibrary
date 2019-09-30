package com.hadair.personalmovielib;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

	private MovieRepository movieRepository;

	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public Long addMovie(Movie newMovie) {
		if(!movieRepository.findByTitleIgnoreCaseAndYearReleased(newMovie.getTitle(), newMovie.getYearReleased()).isEmpty()) {
            //Movie already exists
		    return -1L;
		} else {
			Movie savedMovie = movieRepository.save(newMovie);
			if (savedMovie == null) {
				//Failed to save movie
				return -2L;
			} else {
				return savedMovie.getId();
			}
		}
	}

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

	public Movie getMovieByID(Long movieId) {
		return movieRepository.findOne(movieId);
	}

	public int deleteMovie(Long movieId) {
		if (!movieRepository.exists(movieId)) {
			return 0;
		} else {
			movieRepository.delete(movieId);
			return 1;
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