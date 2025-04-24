package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.MovieFormat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieFormatService {
    List<MovieFormat> getAllFormats();
    Optional<MovieFormat> getFormatById(UUID id);
    MovieFormat createFormat(MovieFormat format);
    Optional<MovieFormat> updateFormat(UUID id, MovieFormat formatDetails);
    boolean deleteFormat(UUID id);
}