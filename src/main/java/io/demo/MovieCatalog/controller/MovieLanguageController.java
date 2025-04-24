package io.demo.MovieCatalog.controller;

import io.demo.MovieCatalog.dto.MovieLanguageDTO;
import io.demo.MovieCatalog.dto.MovieLanguageRequest;
import io.demo.MovieCatalog.mapper.MovieLanguageMapper;
import io.demo.MovieCatalog.model.MovieLanguage;
import io.demo.MovieCatalog.service.MovieLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/movie-languages")
public class MovieLanguageController {

    private final MovieLanguageService movieLanguageService;
    private final MovieLanguageMapper movieLanguageMapper;

    @Autowired
    public MovieLanguageController(MovieLanguageService movieLanguageService, MovieLanguageMapper movieLanguageMapper) {
        this.movieLanguageService = movieLanguageService;
        this.movieLanguageMapper = movieLanguageMapper;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MovieLanguageDTO>> getAllLanguages() {
        List<MovieLanguageDTO> languages = movieLanguageService.getAllLanguages().stream()
                .map(format -> {
                    MovieLanguageDTO dto = movieLanguageMapper.toDTO(format);
                    return addLinks(dto);
                })
                .collect(Collectors.toList());

        CollectionModel<MovieLanguageDTO> collectionModel = CollectionModel.of(languages);
        collectionModel.add(linkTo(methodOn(MovieLanguageController.class).getAllLanguages()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieLanguageDTO> getLanguageById(@PathVariable UUID id) {
        return movieLanguageService.getLanguageById(id)
                .map(format -> {
                    MovieLanguageDTO dto = movieLanguageMapper.toDTO(format);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<MovieLanguageDTO> createLanguage(@RequestBody MovieLanguageRequest request) {
        MovieLanguage language = movieLanguageMapper.toEntity(request);
        MovieLanguage createdLanguage = movieLanguageService.createLanguage(language);
        MovieLanguageDTO dto = movieLanguageMapper.toDTO(createdLanguage);

        return ResponseEntity
                .created(linkTo(methodOn(MovieLanguageController.class).getLanguageById(createdLanguage.getId())).toUri())
                .body(addLinks(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieLanguageDTO> updateLanguage(@PathVariable UUID id, @RequestBody MovieLanguageRequest request) {
        return movieLanguageService.getLanguageById(id)
                .map(language -> {
                    movieLanguageMapper.updateEntityFromRequest(language, request);
                    MovieLanguage updatedLanguage = movieLanguageService.updateLanguage(id, language).orElse(language);
                    MovieLanguageDTO dto = movieLanguageMapper.toDTO(updatedLanguage);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable UUID id) {
        if (movieLanguageService.deleteLanguage(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private MovieLanguageDTO addLinks(MovieLanguageDTO dto) {
        dto.add(linkTo(methodOn(MovieLanguageController.class).getLanguageById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieLanguageController.class).getAllLanguages()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }
}