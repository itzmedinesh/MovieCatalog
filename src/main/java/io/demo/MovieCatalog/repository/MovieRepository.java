package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}

