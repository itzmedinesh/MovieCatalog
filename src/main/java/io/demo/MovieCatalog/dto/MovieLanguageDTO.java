package io.demo.MovieCatalog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Relation(collectionRelation = "languages", itemRelation = "language")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieLanguageDTO extends RepresentationModel<MovieLanguageDTO> {
    private UUID id;
    private UUID movieId;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted;
}