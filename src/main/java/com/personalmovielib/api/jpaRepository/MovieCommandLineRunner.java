package com.personalmovielib.api.jpaRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MovieCommandLineRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(MovieCommandLineRunner.class);

    private MovieRepository repository;

    public MovieCommandLineRunner(MovieRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        //Save some movies to the database
//        repository.save(new Movie("Spider-Man: Homecoming", "Action", 2017, "PG-13", "2h 13m"));
//        repository.save(new Movie("Spider-Man: Far From Home", "Action", 2019, "PG-13", "2h 15m"));
//        repository.save(new Movie("Captain America: Civil War", "Action", 2016, "PG-13", "2h 28m"));
//        repository.save(new Movie("Dr. Strange", "Action", 2016, "PG-13", "1h 55m"));
//
//        log.info("-------------------------------");
//        log.info("Finding all movies");
//        log.info("-------------------------------");
//        for (Movie movie : repository.findAll()) {
//            log.info(movie.toString());
//        }
    }
}