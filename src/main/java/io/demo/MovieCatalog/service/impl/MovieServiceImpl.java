package io.demo.MovieCatalog.service.impl;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.repository.CustomMovieRepository;
import io.demo.MovieCatalog.repository.MovieRepository;
import io.demo.MovieCatalog.service.MovieService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final CustomMovieRepository customMovieRepository;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, CustomMovieRepository customMovieRepository) {
        this.movieRepository = movieRepository;
        this.customMovieRepository = customMovieRepository;
    }

    @Override
    public List<Movie> getAllMovies() {
        log.info("Fetching all movies");

        return customMovieRepository.findAllActiveMovies();
    }
    
    @Override
    public Page<Movie> getAllMovies(Pageable pageable) {
        return customMovieRepository.findAllActiveMovies(pageable);
    }

    @Override
    public Optional<Movie> getMovieById(UUID id) {
        return movieRepository.findById(id);
    }

    @Override
    @Transactional
    public Movie createMovie(Movie movie) {
        return customMovieRepository.saveAndRefresh(movie);
    }

    @Override
    @Transactional
    public Optional<Movie> updateMovie(UUID id, Movie movieDetails) {
        return movieRepository.findById(id)
                .map(existingMovie -> {
                    existingMovie.setName(movieDetails.getName());
                    existingMovie.setDescription(movieDetails.getDescription());
                    existingMovie.setDurationMinutes(movieDetails.getDurationMinutes());
                    existingMovie.setImageUrl(movieDetails.getImageUrl());
                    existingMovie.setTrailerUrl(movieDetails.getTrailerUrl());
                    existingMovie.setUpdatedAt(LocalDateTime.now());
                    return movieRepository.save(existingMovie);
                });
    }

    @Override
    @Transactional
    public boolean deleteMovie(UUID id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movie.setDeletedAt(LocalDateTime.now());
                    customMovieRepository.saveAndRefresh(movie);
                    return true;
                })
                .orElse(false);
    }
}
