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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private Movie movie;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
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
        }

        @Test
        void throwElementNotFoundException() {
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