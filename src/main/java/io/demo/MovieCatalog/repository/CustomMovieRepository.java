package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomMovieRepository {
    Movie saveAndRefresh(Movie movie);
    List<Movie> findAllActiveMovies();
    Page<Movie> findAllActiveMovies(Pageable pageable);
}


