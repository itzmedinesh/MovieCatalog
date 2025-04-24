package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.Movie;

import java.util.List;

public interface CustomMovieRepository {
    Movie saveAndRefresh(Movie movie);
    List<Movie> findAllActiveMovies();
}

