package com.hadair.personalmovielib;

import com.hadair.exceptions.ElementNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

//JUnit testing with Mockito
@RunWith(SpringRunner.class)
@WebMvcTest(value = MovieController.class)
//TODO refactor
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;


//    // ------------------- Return the current local time -----------------
//    @GetMapping("/api/timeOfDay")
//    public String getTimeOfDay() {
//        return movieService.getTimeOfDay();
//    }
//
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
    public void testRetrieveDetailsForMovieByID() throws Exception {
        Movie mockMovie = new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        when(movieService.getMovieByID(anyLong())).thenReturn(mockMovie);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movies/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"title\":\"Spider-Man: Homecoming\",\"genre\":\"Action\",\"yearReleased\":2017,\"rating\":\"PG-13\",\"duration\":\"2h 13m\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testRetrieveDetailsForMovieByTitle() throws Exception {
        Movie mockMovie = new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        List<Movie> mockMovieList = new ArrayList<>();
        mockMovieList.add(mockMovie);
        when(movieService.getMovieByTitle(anyString())).thenReturn(mockMovieList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movies/search/Spider-Man: Homecoming").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1,\"title\":\"Spider-Man: Homecoming\",\"genre\":\"Action\",\"yearReleased\":2017,\"rating\":\"PG-13\",\"duration\":\"2h 13m\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testDeleteMovieAccepted() throws Exception {
        doNothing().when(movieService).deleteMovie(anyLong());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/movies/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //202 Accepted
        Assert.assertEquals(202, result.getResponse().getStatus());
    }

    @Test
    public void testDeleteMovieNotFound() throws Exception {
        doThrow(new ElementNotFoundException("")).when(movieService).deleteMovie(any());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/movies/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //404 Not Found
        Assert.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    public void testGetMovieListMultipleMovies() throws Exception {
        List<Movie> mockMovieList = new ArrayList<>();
        mockMovieList.add(new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m"));
        mockMovieList.add(new Movie(2L, "Dr. Strange", "Action", 2016, "PG-13", "1h 55m"));
        when(movieService.getMovies()).thenReturn(mockMovieList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movies/list").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1,\"title\":\"Spider-Man: Homecoming\",\"genre\":\"Action\",\"yearReleased\":2017,\"rating\":\"PG-13\",\"duration\":\"2h 13m\"},{\"id\":2,\"title\":\"Dr. Strange\",\"genre\":\"Action\",\"yearReleased\":2016,\"rating\":\"PG-13\",\"duration\":\"1h 55m\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetMovieListNoMovies() throws Exception {
        List<Movie> mockMovieList = new ArrayList<>();
        when(movieService.getMovies()).thenReturn(mockMovieList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movies/list").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }
}

