package io.demo.MovieCatalog.mapper;

import io.demo.MovieCatalog.dto.MovieFormatDTO;
import io.demo.MovieCatalog.dto.MovieFormatRequest;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MovieFormatMapper {

    public MovieFormatDTO toDTO(MovieFormat format) {
        if (format == null) {
            return null;
        }

        return MovieFormatDTO.builder()
                .id(format.getId())
                .movieId(format.getMovie() != null ? format.getMovie().getId() : null)
                .name(format.getName())
                .description(format.getDescription())
                .createdAt(format.getCreatedAt())
                .updatedAt(format.getUpdatedAt())
                .deleted(format.getDeletedAt() == null ? null : true)
                .build();
    }

    public MovieFormat toEntity(MovieFormatRequest request) {
        if (request == null) {
            return null;
        }

        MovieFormat format = MovieFormat.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        
        // Set the movie reference if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            format.setMovie(movie);
        }
        
        return format;
    }

    public void updateEntityFromRequest(MovieFormat format, MovieFormatRequest request) {
        if (format == null || request == null) {
            return;
        }

        format.setName(request.getName());
        format.setDescription(request.getDescription());
        
        // Update movie association if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            format.setMovie(movie);
        }
        
        format.setUpdatedAt(LocalDateTime.now());
    }
}
