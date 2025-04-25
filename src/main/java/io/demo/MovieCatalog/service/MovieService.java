package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieService {
    List<Movie> getAllMovies();
    Page<Movie> getAllMovies(Pageable pageable);
    Optional<Movie> getMovieById(UUID id);
    Movie createMovie(Movie movie);
    Optional<Movie> updateMovie(UUID id, Movie movieDetails);
    boolean deleteMovie(UUID id);
}
