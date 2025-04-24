package io.demo.MovieCatalog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "movies", itemRelation = "movie")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieDTO extends RepresentationModel<MovieDTO> {
    private UUID id;
    private String name;
    private Integer durationMinutes;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted;
    private List<MovieLanguageDTO> languages;
    private List<MovieFormatDTO> formats;
    private List<MovieGenreDTO> genres;
}