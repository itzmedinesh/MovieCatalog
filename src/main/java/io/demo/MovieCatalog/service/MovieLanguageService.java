package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.model.MovieLanguage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieLanguageService {
    List<MovieLanguage> getAllLanguages();
    Optional<MovieLanguage> getLanguageById(UUID id);
    MovieLanguage createLanguage(MovieLanguage language);
    Optional<MovieLanguage> updateLanguage(UUID id, MovieLanguage languageDetails);
    boolean deleteLanguage(UUID id);
}