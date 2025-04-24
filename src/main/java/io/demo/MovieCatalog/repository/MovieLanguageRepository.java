package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.MovieLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MovieLanguageRepository extends JpaRepository<MovieLanguage, UUID> {}
