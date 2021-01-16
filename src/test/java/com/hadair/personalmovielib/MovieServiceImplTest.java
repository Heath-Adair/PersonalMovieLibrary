package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementAlreadyExistsException;
import com.hadair.exceptions.ElementNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;
    private Movie movie;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movie = new Movie("Captain America: Civil War", "Action", 2016, "PG-13", "2h 28m");
    }

    @Nested
    public class AddMovie {
        @Test
        void saveAndReturn() {
            when(movieRepository.findByTitleIgnoreCaseAndYearReleased(eq(movie.getTitle()), anyInt()))
                    .thenReturn(Collections.emptyList());
            when(movieRepository.save(movie)).thenReturn(movie);

            Movie returnedMovie = movieService.addMovie(movie);

            assertNotNull(returnedMovie);
            verify(movieRepository, times(1)).save(movie);
        }

        @Test
        void throwElementAlreadyExistsException() {
            when(movieRepository.findByTitleIgnoreCaseAndYearReleased(eq(movie.getTitle()), anyInt()))
                    .thenReturn(Collections.singletonList(movie));

            assertThrows(ElementAlreadyExistsException.class, () -> movieService.addMovie(movie));
        }
    }

    @Nested
    public class UpdateMovie {
        @Test
        void saveAndReturn() {
            when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
            when(movieRepository.save(movie)).thenReturn(movie);

            Movie returnedMovie = movieService.updateMovie(anyLong(), movie);

            assertNotNull(returnedMovie);
            verify(movieRepository, times(1)).save(movie);
        }

        @Test
        void throwElementNotFoundException() {
            when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

            ElementNotFoundException exception = assertThrows(ElementNotFoundException.class,
                    () -> movieService.updateMovie(anyLong(), movie));

            assertEquals("Movie with ID 0 not found.", exception.getMessage());
        }
    }

    @Nested
    public class GetMovieById {
        @Test
        void returnMovie() {
        }

        @Test
        void throwElementNotFoundException() {
        }
    }

    @Nested
    public class DeleteMovie {
        @Test
        void movieDeleted() {
        }

        @Test
        void throwElementNotFoundException() {
        }
    }

    @Nested
    public class GetMovies {
        @Test
        void emptyListReturned() {
        }

        @Test
        void movieListReturned() {
        }
    }

    @Nested
    public class GetMovieByTitle {
        @Test
        void emptyListReturned() {
        }

        @Test
        void movieListReturned() {
        }
    }
}