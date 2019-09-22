package com.hadair.personalmovielib;

import com.hadair.personalmovielib.MovieController;
import com.hadair.personalmovielib.MovieRepository;
import com.hadair.personalmovielib.Movie;
import com.hadair.personalmovielib.MovieService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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

//JUnit testing with Mockito
@RunWith(SpringRunner.class)
@WebMvcTest(value = MovieController.class)
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
        Mockito.when(movieRepository.findOne(Mockito.anyLong())).thenReturn(mockMovie);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movie/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "{\"id\":1,\"title\":\"Spider-Man: Homecoming\",\"genre\":\"Action\",\"yearReleased\":2017,\"rating\":\"PG-13\",\"duration\":\"2h 13m\"}";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testRetrieveDetailsForMovieByTitle() throws Exception {
        Movie mockMovie = new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        List<Movie> mockMovieList = new ArrayList<>();
        mockMovieList.add(mockMovie);
        Mockito.when(movieRepository.findByTitleIgnoreCase(Mockito.anyString())).thenReturn(mockMovieList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movie/search/Spider-Man: Homecoming").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1,\"title\":\"Spider-Man: Homecoming\",\"genre\":\"Action\",\"yearReleased\":2017,\"rating\":\"PG-13\",\"duration\":\"2h 13m\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testDeleteMovieAccepted() throws Exception {
        Mockito.when(movieRepository.exists(Mockito.anyLong())).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/movie/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //200 Accepted
        Assert.assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    public void testDeleteMovieNotFound() throws Exception {
        Mockito.when(movieRepository.exists(Mockito.anyLong())).thenReturn(false);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
                "/api/movie/1").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        //200 Accepted
        Assert.assertEquals(404, result.getResponse().getStatus());
    }

    @Test
    public void testGetMovieListMultipleMovies() throws Exception {
        List<Movie> mockMovieList = new ArrayList<>();
        mockMovieList.add(new Movie(1L,"Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m"));
        mockMovieList.add(new Movie(2L, "Dr. Strange", "Action", 2016, "PG-13", "1h 55m"));
        Mockito.when(movieRepository.findByTitleIgnoreCase(Mockito.anyString())).thenReturn(mockMovieList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movie/search/Spider-Man: Homecoming").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[{\"id\":1,\"title\":\"Spider-Man: Homecoming\",\"genre\":\"Action\",\"yearReleased\":2017,\"rating\":\"PG-13\",\"duration\":\"2h 13m\"},{\"id\":2,\"title\":\"Dr. Strange\",\"genre\":\"Action\",\"yearReleased\":2016,\"rating\":\"PG-13\",\"duration\":\"1h 55m\"}]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void testGetMovieListNoMovies() throws Exception {
        List<Movie> mockMovieList = new ArrayList<>();
        Mockito.when(movieRepository.findByTitleIgnoreCase(Mockito.anyString())).thenReturn(mockMovieList);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/movie/search/Spider-Man: Homecoming").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String expected = "[]";

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }
}

