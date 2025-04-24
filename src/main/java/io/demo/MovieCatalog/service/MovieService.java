package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieService {
    List<Movie> getAllMovies();
    Optional<Movie> getMovieById(UUID id);
    Movie createMovie(Movie movie);
    Optional<Movie> updateMovie(UUID id, Movie movieDetails);
    boolean deleteMovie(UUID id);
}