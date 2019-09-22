package com.hadair.personalmovielib;

import com.hadair.Application;
import com.hadair.personalmovielib.Movie;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

//Integration Testing
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerIT {

    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate = new TestRestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void before() {
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void testGetTimeOfDay(){
        HttpEntity entity = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/timeOfDay"),
                HttpMethod.GET, entity, String.class);
        assertTrue(response.getBody().contains("Current server-side time is "));
    }

    @Test
    public void testGetEmptyMovieList() throws Exception {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                createURLWithPort("/api/movie/list"),
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                new ParameterizedTypeReference<List<Movie>>() {});
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void addQuestion() {
        Movie movie = new Movie("Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        HttpEntity<Movie> entity = new HttpEntity<>(movie, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/movie/"),
                HttpMethod.POST, entity, String.class);
        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertTrue(actual.contains("/api/movie/"));
    }
}