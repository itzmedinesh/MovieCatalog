package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.MovieCast;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieCastService {
    List<MovieCast> getAllCasts();
    List<MovieCast> getCastsByMovieId(UUID movieId);
    Optional<MovieCast> getCastById(UUID id);
    MovieCast createCast(MovieCast cast);
    Optional<MovieCast> updateCast(UUID id, MovieCast cast);
    boolean deleteCast(UUID id);
}