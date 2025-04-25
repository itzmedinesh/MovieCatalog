package io.demo.MovieCatalog.controller;

import io.demo.MovieCatalog.dto.MovieDTO;
import io.demo.MovieCatalog.dto.MovieRequest;
import io.demo.MovieCatalog.exception.ResourceNotFoundException;
import io.demo.MovieCatalog.mapper.MovieMapper;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
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
    private final PagedResourcesAssembler<Movie> pagedResourcesAssembler;

    @Autowired
    public MovieController(MovieService movieService, MovieMapper movieMapper, 
                          PagedResourcesAssembler<Movie> pagedResourcesAssembler) {
        this.movieService = movieService;
        this.movieMapper = movieMapper;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        
        // If pagination parameters are not provided, use the original method
        if (page == 0 && size == 10 && "name".equals(sort) && "asc".equals(direction)) {
            List<MovieDTO> movies = movieService.getAllMovies().stream()
                    .map(movie -> {
                        MovieDTO dto = movieMapper.toDTO(movie);
                        return addLinks(dto);
                    })
                    .collect(Collectors.toList());

            CollectionModel<MovieDTO> collectionModel = CollectionModel.of(movies);
            collectionModel.add(linkTo(methodOn(MovieController.class).getAllMovies(0, 10, "name", "asc")).withSelfRel());
            return ResponseEntity.ok(collectionModel);
        }
        
        // Create pageable object with sorting
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        // Get paginated movies
        Page<Movie> moviePage = movieService.getAllMovies(pageable);
        
        // Convert to DTOs with links
        Page<MovieDTO> dtoPage = moviePage.map(movie -> {
            MovieDTO dto = movieMapper.toDTO(movie);
            return addLinks(dto);
        });
        
        // Create PagedModel with navigation links
        PagedModel<MovieDTO> pagedModel = PagedModel.of(
            dtoPage.getContent(),
            new PagedModel.PageMetadata(
                dtoPage.getSize(),
                dtoPage.getNumber(),
                dtoPage.getTotalElements(),
                dtoPage.getTotalPages()
            )
        );
        
        // Add navigation links
        pagedModel.add(linkTo(methodOn(MovieController.class).getAllMovies(page, size, sort, direction)).withSelfRel());
        
        if (page > 0) {
            pagedModel.add(linkTo(methodOn(MovieController.class).getAllMovies(0, size, sort, direction)).withRel(IanaLinkRelations.FIRST));
            pagedModel.add(linkTo(methodOn(MovieController.class).getAllMovies(page - 1, size, sort, direction)).withRel(IanaLinkRelations.PREV));
        }
        
        if (page < dtoPage.getTotalPages() - 1) {
            pagedModel.add(linkTo(methodOn(MovieController.class).getAllMovies(page + 1, size, sort, direction)).withRel(IanaLinkRelations.NEXT));
            pagedModel.add(linkTo(methodOn(MovieController.class).getAllMovies(dtoPage.getTotalPages() - 1, size, sort, direction)).withRel(IanaLinkRelations.LAST));
        }
        
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable UUID id) {
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        MovieDTO dto = movieMapper.toDTO(movie);
        return ResponseEntity.ok(addLinks(dto));
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
        Movie movie = movieService.getMovieById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        movieMapper.updateEntityFromRequest(movie, request);
        Movie updatedMovie = movieService.updateMovie(id, movie)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        MovieDTO dto = movieMapper.toDTO(updatedMovie);
        return ResponseEntity.ok(addLinks(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable UUID id) {
        if (!movieService.getMovieById(id).isPresent()) {
            throw new ResourceNotFoundException("Movie", "id", id);
        }
        
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    private MovieDTO addLinks(MovieDTO dto) {
        dto.add(linkTo(methodOn(MovieController.class).getMovieById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieController.class).getAllMovies(0, 10, "name", "asc")).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }
}







