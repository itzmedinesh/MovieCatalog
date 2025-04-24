package io.demo.MovieCatalog.service.impl;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.repository.MovieFormatRepository;
import io.demo.MovieCatalog.repository.MovieRepository;
import io.demo.MovieCatalog.service.MovieFormatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MovieFormatServiceImpl implements MovieFormatService {

    private final MovieFormatRepository movieFormatRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public MovieFormatServiceImpl(MovieFormatRepository movieFormatRepository, MovieRepository movieRepository) {
        this.movieFormatRepository = movieFormatRepository;
        this.movieRepository = movieRepository;
    }

    @Override
    public List<MovieFormat> getAllFormats() {
        // Return only active formats (not deleted)
        return movieFormatRepository.findAll().stream()
                .filter(format -> format.getDeletedAt() == null)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MovieFormat> getFormatById(UUID id) {
        return movieFormatRepository.findById(id)
                .filter(format -> format.getDeletedAt() == null);
    }

    @Override
    @Transactional
    public MovieFormat createFormat(MovieFormat format) {
        // If the format has a movie ID associated, ensure the movie exists
        if (format.getMovie() != null && format.getMovie().getId() != null) {
            UUID movieId = format.getMovie().getId();
            Optional<Movie> movie = movieRepository.findById(movieId);
            
            if (movie.isPresent()) {
                // Set the actual movie entity to ensure proper association
                format.setMovie(movie.get());
            } else {
                throw new IllegalArgumentException("Movie with ID " + movieId + " not found");
            }
        }
        
        return movieFormatRepository.save(format);
    }

    @Override
    @Transactional
    public Optional<MovieFormat> updateFormat(UUID id, MovieFormat formatDetails) {
        return movieFormatRepository.findById(id)
                .filter(format -> format.getDeletedAt() == null)
                .map(existingFormat -> {
                    existingFormat.setName(formatDetails.getName());
                    existingFormat.setDescription(formatDetails.getDescription());
                    
                    // Update movie association if provided
                    if (formatDetails.getMovie() != null && formatDetails.getMovie().getId() != null) {
                        UUID movieId = formatDetails.getMovie().getId();
                        movieRepository.findById(movieId).ifPresent(existingFormat::setMovie);
                    }
                    
                    existingFormat.setUpdatedAt(LocalDateTime.now());
                    return movieFormatRepository.save(existingFormat);
                });
    }

    @Override
    @Transactional
    public boolean deleteFormat(UUID id) {
        return movieFormatRepository.findById(id)
                .filter(format -> format.getDeletedAt() == null)
                .map(format -> {
                    format.setDeletedAt(LocalDateTime.now());
                    movieFormatRepository.save(format);
                    return true;
                })
                .orElse(false);
    }
}
