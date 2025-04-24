package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MovieGenreRepository extends JpaRepository<MovieGenre, UUID> {}

