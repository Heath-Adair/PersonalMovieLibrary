package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    private static final String API_MOVIES_URL = "/api/movies";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;
    @MockBean
    private MovieRepository movieRepository;

//    // ------------------- Add a movie by ID -----------------------------
//    @PostMapping("/api/movie/")
//    public ResponseEntity<Void> addMovie(@RequestBody Movie newMovie) {
//        return movieService.addMovie(newMovie);
//    }
//
//    // ------------------- Update a movie by ID -----------------------------
//    @PutMapping("/api/movie/{movieId}")
//    public ResponseEntity<Void> updateMovie(@PathVariable("movieId") Long movieId, @RequestBody Movie newMovie) {
//        return movieService.updateMovie(movieId, newMovie);
//    }

    @Test
    public void getMovieById_movieExists() throws Exception {
        Movie mockMovie = new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        when(movieService.getMovieByID(anyLong())).thenReturn(mockMovie);

        mockMvc.perform(MockMvcRequestBuilders
                .get(API_MOVIES_URL + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Spider-Man: Homecoming"))
                .andExpect(jsonPath("$.genre").value("Action"))
                .andExpect(jsonPath("$.yearReleased").value("2017"))
                .andExpect(jsonPath("$.rating").value("PG-13"))
                .andExpect(jsonPath("$.duration").value("2h 13m"));
    }

    @Test
    public void getMovieByTitle_movieExists() throws Exception {
        Movie mockMovie = new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        List<Movie> mockMovieList = new ArrayList<>();
        mockMovieList.add(mockMovie);
        when(movieService.getMovieByTitle(anyString())).thenReturn(mockMovieList);

        mockMvc.perform(MockMvcRequestBuilders
                .get(API_MOVIES_URL + "/search/Spider-Man: Homecoming")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Spider-Man: Homecoming"))
                .andExpect(jsonPath("$[0].genre").value("Action"))
                .andExpect(jsonPath("$[0].yearReleased").value("2017"))
                .andExpect(jsonPath("$[0].rating").value("PG-13"))
                .andExpect(jsonPath("$[0].duration").value("2h 13m"));
    }

    @Test
    public void deleteMovie_movieExists() throws Exception {
        doNothing().when(movieService).deleteMovie(anyLong());

        mockMvc.perform(MockMvcRequestBuilders
                .delete(API_MOVIES_URL + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void deleteMovie_movieDoesNotExists() throws Exception {
        doThrow(new ElementNotFoundException("")).when(movieService).deleteMovie(any());

        mockMvc.perform(MockMvcRequestBuilders
                .delete(API_MOVIES_URL + "/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getMovies_multipleMoviesExist() throws Exception {
        List<Movie> mockMovieList = new ArrayList<>();
        mockMovieList.add(new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m"));
        mockMovieList.add(new Movie(2L, "Dr. Strange", "Action", 2016, "PG-13", "1h 55m"));
        when(movieService.getMovies()).thenReturn(mockMovieList);

        mockMvc.perform(MockMvcRequestBuilders
                .get(API_MOVIES_URL + "/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("Spider-Man: Homecoming"))
                .andExpect(jsonPath("$[0].genre").value("Action"))
                .andExpect(jsonPath("$[0].yearReleased").value("2017"))
                .andExpect(jsonPath("$[0].rating").value("PG-13"))
                .andExpect(jsonPath("$[0].duration").value("2h 13m"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].title").value("Dr. Strange"))
                .andExpect(jsonPath("$[1].genre").value("Action"))
                .andExpect(jsonPath("$[1].yearReleased").value("2016"))
                .andExpect(jsonPath("$[1].rating").value("PG-13"))
                .andExpect(jsonPath("$[1].duration").value("1h 55m"));
    }

    @Test
    public void getMovies_noMoviesExist() throws Exception {
        List<Movie> mockMovieList = new ArrayList<>();
        when(movieService.getMovies()).thenReturn(mockMovieList);

        mockMvc.perform(MockMvcRequestBuilders
                .get(API_MOVIES_URL + "/list")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}

