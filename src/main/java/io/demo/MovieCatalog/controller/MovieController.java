package io.demo.MovieCatalog.controller;

import io.demo.MovieCatalog.dto.MovieDTO;
import io.demo.MovieCatalog.dto.MovieRequest;
import io.demo.MovieCatalog.mapper.MovieMapper;
import io.demo.MovieCatalog.model.Movie;
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
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;
    private final MovieMapper movieMapper;

    @Autowired
    public MovieController(MovieService movieService, MovieMapper movieMapper) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MovieDTO>> getAllMovies() {
        List<MovieDTO> movies = movieService.getAllMovies().stream()
                .map(movie -> {
                    MovieDTO dto = movieMapper.toDTO(movie);
                    return addLinks(dto);
                })
                .collect(Collectors.toList());

        CollectionModel<MovieDTO> collectionModel = CollectionModel.of(movies);
        collectionModel.add(linkTo(methodOn(MovieController.class).getAllMovies()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable UUID id) {
        return movieService.getMovieById(id)
                .map(movie -> {
                    MovieDTO dto = movieMapper.toDTO(movie);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieRequest request) {
        Movie movie = movieMapper.toEntity(request);
        Movie createdMovie = movieService.createMovie(movie);
        MovieDTO dto = movieMapper.toDTO(createdMovie);
        
        return ResponseEntity
                .created(linkTo(methodOn(MovieController.class).getMovieById(createdMovie.getId())).toUri())
                .body(addLinks(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable UUID id, @RequestBody MovieRequest request) {
        return movieService.getMovieById(id)
                .map(movie -> {
                    movieMapper.updateEntityFromRequest(movie, request);
                    Movie updatedMovie = movieService.updateMovie(id, movie).orElse(movie);
                    MovieDTO dto = movieMapper.toDTO(updatedMovie);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID id) {
        if (movieService.deleteMovie(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private MovieDTO addLinks(MovieDTO dto) {
        dto.add(linkTo(methodOn(MovieController.class).getMovieById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieController.class).getAllMovies()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }
}
