package io.demo.MovieCatalog.mapper;

import io.demo.MovieCatalog.dto.MovieCastDTO;
import io.demo.MovieCatalog.dto.MovieCastRequest;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieCast;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class MovieCastMapper {

    public MovieCastDTO toDTO(MovieCast cast) {
        if (cast == null) {
            return null;
        }

        return MovieCastDTO.builder()
                .id(cast.getId())
                .movieId(cast.getMovie() != null ? cast.getMovie().getId() : null)
                .actorName(cast.getActorName())
                .characterName(cast.getCharacterName())
                .role(cast.getRole())
                .profileImageUrl(cast.getProfileImageUrl())
                .description(cast.getDescription())
                .createdAt(cast.getCreatedAt())
                .updatedAt(cast.getUpdatedAt())
                .deleted(cast.getDeletedAt() == null ? null : true)
                .build();
    }

    public MovieCast toEntity(MovieCastRequest request) {
        if (request == null) {
            return null;
        }

        MovieCast cast = MovieCast.builder()
                .id(UUID.randomUUID())
                .actorName(request.getActorName())
                .characterName(request.getCharacterName())
                .role(request.getRole())
                .profileImageUrl(request.getProfileImageUrl())
                .description(request.getDescription())
                .build();

        // Set the movie reference if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            cast.setMovie(movie);
        }

        return cast;
    }

    public List<MovieCastDTO> toDTOList(List<MovieCast> castList) {
        if (castList == null) {
            return Collections.emptyList();
        }

        return castList.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public void updateEntityFromRequest(MovieCast cast, MovieCastRequest request) {
        if (cast == null || request == null) {
            return;
        }

        cast.setActorName(request.getActorName());
        cast.setCharacterName(request.getCharacterName());
        cast.setRole(request.getRole());
        cast.setProfileImageUrl(request.getProfileImageUrl());
        cast.setDescription(request.getDescription());

        // Update movie association if movieId is provided
        if (request.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(request.getMovieId());
            cast.setMovie(movie);
        }

        cast.setUpdatedAt(LocalDateTime.now());
    }

}
