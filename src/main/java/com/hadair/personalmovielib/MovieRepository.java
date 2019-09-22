package com.hadair.personalmovielib;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

//@RepositoryRestResource(path = "movie", collectionResourceRel = "movie") //Not going with this solution because it creates more api than desired
public interface MovieRepository extends PagingAndSortingRepository<Movie, Long> {

    @RestResource(path="searchByTitle", rel="searchByTitle")
    List<Movie> findByTitleIgnoreCase(@Param("title") String title);

    List<Movie> findByTitleIgnoreCaseAndYearReleased(@Param("title") String title, @Param("yearReleased") int yearReleased);
}