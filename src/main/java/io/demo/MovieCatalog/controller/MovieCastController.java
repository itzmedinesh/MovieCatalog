package io.demo.MovieCatalog.controller;

import io.demo.MovieCatalog.dto.MovieCastDTO;
import io.demo.MovieCatalog.dto.MovieCastRequest;
import io.demo.MovieCatalog.mapper.MovieCastMapper;
import io.demo.MovieCatalog.model.MovieCast;
import io.demo.MovieCatalog.service.MovieCastService;
import io.demo.MovieCatalog.service.MovieService;
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
@RequestMapping("/api/movie-casts")
public class MovieCastController {

    @Autowired
    private MovieCastService movieCastService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieCastMapper movieCastMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<MovieCastDTO>> getAllCasts() {
        List<MovieCastDTO> castDTOs = movieCastService.getAllCasts().stream()
                .map(movieCastMapper::toDTO)
                .collect(Collectors.toList());

        CollectionModel<MovieCastDTO> collectionModel = CollectionModel.of(castDTOs);
        collectionModel.add(linkTo(methodOn(MovieCastController.class).getAllCasts()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieCastDTO> getCastById(@PathVariable UUID id) {
        return movieCastService.getCastById(id).map(
                        cast ->
                                ResponseEntity.ok(addLinks(movieCastMapper.toDTO(cast))))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/movies/{movieId}")
    public ResponseEntity<CollectionModel<MovieCastDTO>> getCastsByMovieId(@PathVariable UUID movieId) {
        List<MovieCast> casts = movieCastService.getCastsByMovieId(movieId);
        List<MovieCastDTO> castDTOs = casts.stream()
                .map(movieCastMapper::toDTO)
                .collect(Collectors.toList());

        CollectionModel<MovieCastDTO> collectionModel = CollectionModel.of(castDTOs);
        collectionModel.add(linkTo(methodOn(MovieCastController.class).getCastsByMovieId(movieId)).withSelfRel());
        collectionModel.add(linkTo(methodOn(MovieCastController.class).getAllCasts()).withRel(IanaLinkRelations.COLLECTION));

        return ResponseEntity.ok(collectionModel);
    }

    @PostMapping()
    public ResponseEntity<MovieCastDTO> createCast(@RequestBody MovieCastRequest request) {
        MovieCast cast = movieCastMapper.toEntity(request);
        MovieCast createdCast = movieCastService.createCast(cast);
        MovieCastDTO dto = movieCastMapper.toDTO(createdCast);

        return ResponseEntity
                .created(linkTo(methodOn(MovieCastController.class).getCastById(createdCast.getId())).toUri())
                .body(addLinks(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieCastDTO> updateCast(@PathVariable UUID id, @RequestBody MovieCastRequest request) {
        return movieCastService.getCastById(id)
                .map(cast -> {
                    movieCastMapper.updateEntityFromRequest(cast, request);
                    MovieCast updatedCast = movieCastService.updateCast(id, cast).orElse(cast);
                    MovieCastDTO dto = movieCastMapper.toDTO(updatedCast);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCast(@PathVariable UUID id) {
        if (movieCastService.deleteCast(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private MovieCastDTO addLinks(MovieCastDTO castDTO) {
        castDTO.add(linkTo(methodOn(MovieCastController.class).getCastById(castDTO.getId())).withSelfRel());
        castDTO.add(linkTo(methodOn(MovieCastController.class).getAllCasts()).withRel(IanaLinkRelations.COLLECTION));
        return castDTO;
    }
}
