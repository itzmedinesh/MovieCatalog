package io.demo.MovieCatalog.controller;

import io.demo.MovieCatalog.dto.MovieGenreDTO;
import io.demo.MovieCatalog.dto.MovieGenreRequest;
import io.demo.MovieCatalog.mapper.MovieGenreMapper;
import io.demo.MovieCatalog.model.MovieGenre;
import io.demo.MovieCatalog.service.MovieGenreService;
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
@RequestMapping("/api/movie-genres")
public class MovieGenreController {

    private final MovieGenreService movieGenreService;
    private final MovieGenreMapper movieGenreMapper;

    @Autowired
    public MovieGenreController(MovieGenreService movieGenreService, MovieGenreMapper movieGenreMapper) {
        this.movieGenreService = movieGenreService;
        this.movieGenreMapper = movieGenreMapper;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MovieGenreDTO>> getAllGenres() {
        List<MovieGenreDTO> genres = movieGenreService.getAllGenres().stream()
                .map(genre -> {
                    MovieGenreDTO dto = movieGenreMapper.toDTO(genre);
                    return addLinks(dto);
                })
                .collect(Collectors.toList());

        CollectionModel<MovieGenreDTO> collectionModel = CollectionModel.of(genres);
        collectionModel.add(linkTo(methodOn(MovieGenreController.class).getAllGenres()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieGenreDTO> getGenreById(@PathVariable UUID id) {
        return movieGenreService.getGenreById(id)
                .map(format -> {
                    MovieGenreDTO dto = movieGenreMapper.toDTO(format);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<MovieGenreDTO> createGenre(@RequestBody MovieGenreRequest request) {
        MovieGenre genre = movieGenreMapper.toEntity(request);
        MovieGenre createdGenre = movieGenreService.createGenre(genre);
        MovieGenreDTO dto = movieGenreMapper.toDTO(createdGenre);

        return ResponseEntity
                .created(linkTo(methodOn(MovieGenreController.class).getGenreById(createdGenre.getId())).toUri())
                .body(addLinks(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieGenreDTO> updateGenre(@PathVariable UUID id, @RequestBody MovieGenreRequest request) {
        return movieGenreService.getGenreById(id)
                .map(genre -> {
                    movieGenreMapper.updateEntityFromRequest(genre, request);
                    MovieGenre updatedGenre = movieGenreService.updateGenre(id, genre).orElse(genre);
                    MovieGenreDTO dto = movieGenreMapper.toDTO(updatedGenre);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable UUID id) {
        if (movieGenreService.deleteGenre(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private MovieGenreDTO addLinks(MovieGenreDTO dto) {
        dto.add(linkTo(methodOn(MovieGenreController.class).getGenreById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieGenreController.class).getAllGenres()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }
}