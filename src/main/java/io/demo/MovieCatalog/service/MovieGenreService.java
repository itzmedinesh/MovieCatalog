package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.MovieGenre;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieGenreService {
    List<MovieGenre> getAllGenres();
    Optional<MovieGenre> getGenreById(UUID id);
    MovieGenre createGenre(MovieGenre genre);
    Optional<MovieGenre> updateGenre(UUID id, MovieGenre genreDetails);
    boolean deleteGenre(UUID id);
}