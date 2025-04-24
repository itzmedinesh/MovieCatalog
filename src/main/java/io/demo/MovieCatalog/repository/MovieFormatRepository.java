package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.MovieFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MovieFormatRepository extends JpaRepository<MovieFormat, UUID> {}
