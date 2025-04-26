package io.demo.MovieCatalog.service.impl;

import io.demo.MovieCatalog.model.MovieCast;
import io.demo.MovieCatalog.repository.MovieCastRepository;
import io.demo.MovieCatalog.service.MovieCastService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class MovieCastServiceImpl implements MovieCastService {

    @Autowired
    private MovieCastRepository movieCastRepository;

    @Override
    public List<MovieCast> getAllCasts() {
        return movieCastRepository.findAll();
    }

    @Override
    public List<MovieCast> getCastsByMovieId(UUID movieId) {
        return movieCastRepository.findByMovieId(movieId);
    }

    @Override
    public Optional<MovieCast> getCastById(UUID id) {
        return movieCastRepository.findById(id);
    }

    @Override
    public MovieCast createCast(MovieCast cast) {
        return movieCastRepository.save(cast);
    }

    @Override
    public Optional<MovieCast> updateCast(UUID id, MovieCast cast) {
        Optional<MovieCast> existingCast = movieCastRepository.findById(id);
        if (existingCast.isPresent()) {
            MovieCast castToUpdate = existingCast.get();
            castToUpdate.setActorName(cast.getActorName());
            castToUpdate.setCharacterName(cast.getCharacterName());
            castToUpdate.setRole(cast.getRole());
            castToUpdate.setProfileImageUrl(cast.getProfileImageUrl());
            castToUpdate.setDescription(cast.getDescription());
            return Optional.of(movieCastRepository.save(castToUpdate));
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteCast(UUID id) {
        Optional<MovieCast> cast = movieCastRepository.findById(id);
        if (cast.isPresent()) {
            MovieCast castToDelete = cast.get();
            castToDelete.setDeletedAt(LocalDateTime.now());
            movieCastRepository.save(castToDelete);
            return true;
        }
        return false;
    }
}
