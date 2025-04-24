package io.demo.MovieCatalog.controller;

import io.demo.MovieCatalog.dto.MovieFormatDTO;
import io.demo.MovieCatalog.dto.MovieFormatRequest;
import io.demo.MovieCatalog.mapper.MovieFormatMapper;
import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.service.MovieFormatService;
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
@RequestMapping("/api/movie-formats")
public class MovieFormatController {

    private final MovieFormatService movieFormatService;
    private final MovieFormatMapper movieFormatMapper;

    @Autowired
    public MovieFormatController(MovieFormatService movieFormatService, MovieFormatMapper movieFormatMapper) {
        this.movieFormatService = movieFormatService;
        this.movieFormatMapper = movieFormatMapper;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<MovieFormatDTO>> getAllFormats() {
        List<MovieFormatDTO> formats = movieFormatService.getAllFormats().stream()
                .map(format -> {
                    MovieFormatDTO dto = movieFormatMapper.toDTO(format);
                    return addLinks(dto);
                })
                .collect(Collectors.toList());

        CollectionModel<MovieFormatDTO> collectionModel = CollectionModel.of(formats);
        collectionModel.add(linkTo(methodOn(MovieFormatController.class).getAllFormats()).withSelfRel());
        
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieFormatDTO> getFormatById(@PathVariable UUID id) {
        return movieFormatService.getFormatById(id)
                .map(format -> {
                    MovieFormatDTO dto = movieFormatMapper.toDTO(format);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<MovieFormatDTO> createFormat(@RequestBody MovieFormatRequest request) {
        MovieFormat format = movieFormatMapper.toEntity(request);
        MovieFormat createdFormat = movieFormatService.createFormat(format);
        MovieFormatDTO dto = movieFormatMapper.toDTO(createdFormat);
        
        return ResponseEntity
                .created(linkTo(methodOn(MovieFormatController.class).getFormatById(createdFormat.getId())).toUri())
                .body(addLinks(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieFormatDTO> updateFormat(@PathVariable UUID id, @RequestBody MovieFormatRequest request) {
        return movieFormatService.getFormatById(id)
                .map(format -> {
                    movieFormatMapper.updateEntityFromRequest(format, request);
                    MovieFormat updatedFormat = movieFormatService.updateFormat(id, format).orElse(format);
                    MovieFormatDTO dto = movieFormatMapper.toDTO(updatedFormat);
                    return ResponseEntity.ok(addLinks(dto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFormat(@PathVariable UUID id) {
        if (movieFormatService.deleteFormat(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private MovieFormatDTO addLinks(MovieFormatDTO dto) {
        dto.add(linkTo(methodOn(MovieFormatController.class).getFormatById(dto.getId())).withSelfRel());
        dto.add(linkTo(methodOn(MovieFormatController.class).getAllFormats()).withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }
}