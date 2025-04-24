package io.demo.MovieCatalog.service.impl;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieLanguage;
import io.demo.MovieCatalog.repository.MovieLanguageRepository;
import io.demo.MovieCatalog.repository.MovieRepository;
import io.demo.MovieCatalog.service.MovieLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieLanguageServiceImpl implements MovieLanguageService {

    private final MovieLanguageRepository movieLanguageRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieLanguageServiceImpl(MovieLanguageRepository movieLanguageRepository, MovieRepository movieRepository) {
        this.movieLanguageRepository = movieLanguageRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieLanguage> getAllLanguages() {
        // Return only active languages (not deleted)
        return movieLanguageRepository.findAll().stream()
                .filter(language -> language.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieLanguage> getLanguageById(UUID id) {
        return movieLanguageRepository.findById(id)
                .filter(language -> language.getDeletedAt() == null);
    }

    @Override
    @Transactional
    public MovieLanguage createLanguage(MovieLanguage language) {
        // If the format has a movie ID associated, ensure the movie exists
        if (language.getMovie() != null && language.getMovie().getId() != null) {
            UUID movieId = language.getMovie().getId();
            Optional<Movie> movie = movieRepository.findById(movieId);

            if (movie.isPresent()) {
                // Set the actual movie entity to ensure proper association
                language.setMovie(movie.get());
            } else {
                throw new IllegalArgumentException("Movie with ID " + movieId + " not found");
            }
        }

        return movieLanguageRepository.save(language);
    }

    @Override
    @Transactional
    public Optional<MovieLanguage> updateLanguage(UUID id, MovieLanguage languageDetails) {
        return movieLanguageRepository.findById(id)
                .filter(language -> language.getDeletedAt() == null)
                .map(existingLanguage -> {
                    existingLanguage.setName(languageDetails.getName());
                    existingLanguage.setDescription(languageDetails.getDescription());

                    // Update movie association if provided
                    if (languageDetails.getMovie() != null && languageDetails.getMovie().getId() != null) {
                        UUID movieId = languageDetails.getMovie().getId();
                        movieRepository.findById(movieId).ifPresent(existingLanguage::setMovie);
                    }

                    existingLanguage.setUpdatedAt(LocalDateTime.now());
                    return movieLanguageRepository.save(existingLanguage);
                });
    }

    @Override
    @Transactional
    public boolean deleteLanguage(UUID id) {
        return movieLanguageRepository.findById(id)
                .filter(language -> language.getDeletedAt() == null)
                .map(language -> {
                    language.setDeletedAt(LocalDateTime.now());
                    movieLanguageRepository.save(language);
                    return true;
                })
                .orElse(false);
    }
}
