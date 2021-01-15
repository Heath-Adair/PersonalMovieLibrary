package com.hadair.personalmovielib;

import com.hadair.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerIT {

    @LocalServerPort
    private int port;
    private TestRestTemplate restTemplate;
    private HttpHeaders headers;

    @BeforeEach
    public void setup() {
        restTemplate = new TestRestTemplate();
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    }

    private String createURLWithPort(final String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void testGetEmptyMovieList() throws Exception {
        ResponseEntity<List<Movie>> response = restTemplate.exchange(
                createURLWithPort("/api/movies/list"),
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                new ParameterizedTypeReference<List<Movie>>() {});
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    public void addMovie() {
        Movie movie = new Movie("Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m");
        HttpEntity<Movie> entity = new HttpEntity<>(movie, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/movies"),
                HttpMethod.POST, entity, String.class);
        String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
        assertTrue(actual.contains("/api/movies"));
    }
}