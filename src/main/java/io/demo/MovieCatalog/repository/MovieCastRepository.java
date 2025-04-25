package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.MovieCast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieCastRepository extends JpaRepository<MovieCast, UUID> {
    List<MovieCast> findByMovieId(UUID movieId);
}