package io.demo.MovieCatalog.mapper;

import io.demo.MovieCatalog.controller.MovieFormatController;
import io.demo.MovieCatalog.controller.MovieGenreController;
import io.demo.MovieCatalog.controller.MovieLanguageController;
import io.demo.MovieCatalog.dto.*;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieCast;
import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.model.MovieGenre;
import io.demo.MovieCatalog.model.MovieLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class MovieMapper {

    @Autowired
    private MovieCastMapper movieCastMapper;

    public MovieDTO toDTO(Movie movie) {
        if (movie == null) {
            return null;
        }

        return MovieDTO.builder()
                .id(movie.getId())
                .name(movie.getName())
                .durationMinutes(movie.getDurationMinutes())
                .description(movie.getDescription())
                .imageUrl(movie.getImageUrl())
                .trailerUrl(movie.getTrailerUrl())
                .createdAt(movie.getCreatedAt())
                .updatedAt(movie.getUpdatedAt())
                .deleted(movie.getDeletedAt() == null ? null : true)
                .languages(movie.getLanguages() != null ?
                        movie.getLanguages().stream()
                                .map(lang -> addLinks(MovieLanguageDTO.builder()
                                        .id(lang.getId())
                                        .movieId(movie.getId())
                                        .name(lang.getName())
                                        .description(lang.getDescription())
                                        .createdAt(lang.getCreatedAt())
                                        .updatedAt(lang.getUpdatedAt())
                                        .deleted(lang.getDeletedAt() == null ? null : true)
                                        .build()))
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .formats(movie.getFormats() != null ?
                        movie.getFormats().stream()
                                .map(format -> addLinks(MovieFormatDTO.builder()
                                        .id(format.getId())
                                        .movieId(movie.getId())
                                        .name(format.getName())
                                        .description(format.getDescription())
                                        .createdAt(format.getCreatedAt())
                                        .updatedAt(format.getUpdatedAt())
                                        .deleted(format.getDeletedAt() == null ? null : true)
                                        .build()))
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .genres(movie.getGenres() != null ?
                        movie.getGenres().stream()
                                .map(genre -> addLinks(MovieGenreDTO.builder()
                                        .id(genre.getId())
                                        .movieId(movie.getId())
                                        .name(genre.getName())
                                        .description(genre.getDescription())
                                        .createdAt(genre.getCreatedAt())
                                        .updatedAt(genre.getUpdatedAt())
                                        .deleted(genre.getDeletedAt() == null ? null : true)
                                        .build()))
                                .collect(Collectors.toList()) :
                        Collections.emptyList())
                .cast(movie.getCast() != null ?
                        movieCastMapper.toDTOList(movie.getCast()) :
                        Collections.emptyList())
                .build();
    }

    public Movie toEntity(MovieRequest request) {
        if (request == null) {
            return null;
        }

        Movie movie = Movie.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .description(request.getDescription())
                .durationMinutes(request.getDurationMinutes())
                .imageUrl(request.getImageUrl())
                .trailerUrl(request.getTrailerUrl())
                .build();

        // Create languages
        if (request.getLanguages() != null && !request.getLanguages().isEmpty()) {
            List<MovieLanguage> languages = new ArrayList<>();
            for (MovieRequest.MovieLanguageRequest lang : request.getLanguages()) {
                languages.add(MovieLanguage.builder()
                        .id(UUID.randomUUID())
                        .movie(movie)
                        .name(lang.getName())
                        .description(lang.getDescription())
                        .build());
            }
            movie.setLanguages(languages);
        }

        // Create formats
        if (request.getFormats() != null && !request.getFormats().isEmpty()) {
            List<MovieFormat> formats = new ArrayList<>();
            for (MovieRequest.MovieFormatRequest format : request.getFormats()) {
                formats.add(MovieFormat.builder()
                        .id(UUID.randomUUID())
                        .movie(movie)
                        .name(format.getName())
                        .description(format.getDescription())
                        .build());
            }
            movie.setFormats(formats);
        }

        // Create genres
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            List<MovieGenre> genres = new ArrayList<>();
            for (MovieRequest.MovieGenreRequest genre : request.getGenres()) {
                genres.add(MovieGenre.builder()
                        .id(UUID.randomUUID())
                        .movie(movie)
                        .name(genre.getName())
                        .description(genre.getDescription())
                        .build());
            }
            movie.setGenres(genres);
        }
        
        // Create cast
        if (request.getCast() != null && !request.getCast().isEmpty()) {
            List<MovieCast> cast = new ArrayList<>();
            for (MovieRequest.MovieCastRequest castMember : request.getCast()) {
                cast.add(MovieCast.builder()
                        .id(UUID.randomUUID())
                        .movie(movie)
                        .actorName(castMember.getActorName())
                        .characterName(castMember.getCharacterName())
                        .role(castMember.getRole())
                        .profileImageUrl(castMember.getProfileImageUrl())
                        .description(castMember.getDescription())
                        .build());
            }
            movie.setCast(cast);
        }

        return movie;
    }

    public void updateEntityFromRequest(Movie movie, MovieRequest request) {
        if (movie == null || request == null) {
            return;
        }

        movie.setName(request.getName());
        movie.setDurationMinutes(request.getDurationMinutes());
        movie.setDescription(request.getDescription());
        movie.setImageUrl(request.getImageUrl());
        movie.setTrailerUrl(request.getTrailerUrl());
        movie.setUpdatedAt(LocalDateTime.now());
    }

    private MovieFormatDTO addLinks(MovieFormatDTO dto) {
        dto.add(linkTo(methodOn(MovieFormatController.class).getFormatById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieFormatController.class).getAllFormats()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }

    private MovieLanguageDTO addLinks(MovieLanguageDTO dto) {
        dto.add(linkTo(methodOn(MovieLanguageController.class).getLanguageById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieLanguageController.class).getAllLanguages()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }

    private MovieGenreDTO addLinks(MovieGenreDTO dto) {
        dto.add(linkTo(methodOn(MovieGenreController.class).getGenreById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieGenreController.class).getAllGenres()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }
}