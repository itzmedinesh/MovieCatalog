package io.demo.MovieCatalog.mapper;

import io.demo.MovieCatalog.dto.MovieFormatDTO;
import io.demo.MovieCatalog.dto.MovieFormatRequest;
import io.demo.MovieCatalog.dto.MovieLanguageDTO;
import io.demo.MovieCatalog.dto.MovieLanguageRequest;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.model.MovieLanguage;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MovieLanguageMapper {

    public MovieLanguageDTO toDTO(MovieLanguage language) {
        if (language == null) {
            return null;
        }

        return MovieLanguageDTO.builder()
                .id(language.getId())
                .movieId(language.getMovie() != null ? language.getMovie().getId() : null)
                .name(language.getName())
                .description(language.getDescription())
                .createdAt(language.getCreatedAt())
                .updatedAt(language.getUpdatedAt())
                .deleted(language.getDeletedAt() == null ? null : true)
                .build();
    }

    public MovieLanguage toEntity(MovieLanguageRequest request) {
        if (request == null) {
            return null;
        }

        MovieLanguage language = MovieLanguage.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        
        // Set the movie reference if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            language.setMovie(movie);
        }
        
        return language;
    }

    public void updateEntityFromRequest(MovieLanguage language, MovieLanguageRequest request) {
        if (language == null || request == null) {
            return;
        }

        language.setName(request.getName());
        language.setDescription(request.getDescription());
        
        // Update movie association if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            language.setMovie(movie);
        }

        language.setUpdatedAt(LocalDateTime.now());
    }
}
