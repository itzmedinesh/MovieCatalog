package io.demo.MovieCatalog.mapper;

import io.demo.MovieCatalog.dto.MovieGenreDTO;
import io.demo.MovieCatalog.dto.MovieGenreRequest;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieGenre;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class MovieGenreMapper {

    public MovieGenreDTO toDTO(MovieGenre genre) {
        if (genre == null) {
            return null;
        }

        return MovieGenreDTO.builder()
                .id(genre.getId())
                .movieId(genre.getMovie() != null ? genre.getMovie().getId() : null)
                .name(genre.getName())
                .description(genre.getDescription())
                .createdAt(genre.getCreatedAt())
                .updatedAt(genre.getUpdatedAt())
                .deleted(genre.getDeletedAt() == null ? null : true)
                .build();
    }

    public MovieGenre toEntity(MovieGenreRequest request) {
        if (request == null) {
            return null;
        }

        MovieGenre genre = MovieGenre.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .build();

        // Set the movie reference if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            genre.setMovie(movie);
        }

        return genre;
    }

    public void updateEntityFromRequest(MovieGenre genre, MovieGenreRequest request) {
        if (genre == null || request == null) {
            return;
        }

        genre.setName(request.getName());
        genre.setDescription(request.getDescription());

        // Update movie association if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            genre.setMovie(movie);
        }

        genre.setUpdatedAt(LocalDateTime.now());
    }
}
