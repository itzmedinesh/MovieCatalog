package io.demo.MovieCatalog.service.impl;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieGenre;
import io.demo.MovieCatalog.repository.MovieGenreRepository;
import io.demo.MovieCatalog.repository.MovieRepository;
import io.demo.MovieCatalog.service.MovieGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieGenreServiceImpl implements MovieGenreService {

    private final MovieGenreRepository movieGenreRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieGenreServiceImpl(MovieGenreRepository movieGenreRepository, MovieRepository movieRepository) {
        this.movieGenreRepository = movieGenreRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieGenre> getAllGenres() {
        // Return only active genres (not deleted)
        return movieGenreRepository.findAll().stream()
                .filter(genre -> genre.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieGenre> getGenreById(UUID id) {
        return movieGenreRepository.findById(id)
                .filter(genre -> genre.getDeletedAt() == null);
    }

    @Override
    @Transactional
    public MovieGenre createGenre(MovieGenre genre) {
        // If the genre has a movie ID associated, ensure the movie exists
        if (genre.getMovie() != null && genre.getMovie().getId() != null) {
            UUID movieId = genre.getMovie().getId();
            Optional<Movie> movie = movieRepository.findById(movieId);

            if (movie.isPresent()) {
                // Set the actual movie entity to ensure proper association
                genre.setMovie(movie.get());
            } else {
                throw new IllegalArgumentException("Movie with ID " + movieId + " not found");
            }
        }

        return movieGenreRepository.save(genre);
    }

    @Override
    @Transactional
    public Optional<MovieGenre> updateGenre(UUID id, MovieGenre genreDetails) {
        return movieGenreRepository.findById(id)
                .filter(genre -> genre.getDeletedAt() == null)
                .map(existingGenre -> {
                    existingGenre.setName(genreDetails.getName());
                    existingGenre.setDescription(genreDetails.getDescription());

                    // Update movie association if provided
                    if (genreDetails.getMovie() != null && genreDetails.getMovie().getId() != null) {
                        UUID movieId = genreDetails.getMovie().getId();
                        movieRepository.findById(movieId).ifPresent(existingGenre::setMovie);
                    }

                    existingGenre.setUpdatedAt(LocalDateTime.now());
                    return movieGenreRepository.save(existingGenre);
                });
    }

    @Override
    @Transactional
    public boolean deleteGenre(UUID id) {
        return movieGenreRepository.findById(id)
                .filter(genre -> genre.getDeletedAt() == null)
                .map(genre -> {
                    genre.setDeletedAt(LocalDateTime.now());
                    movieGenreRepository.save(genre);
                    return true;
                })
                .orElse(false);
    }
}
