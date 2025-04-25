package io.demo.MovieCatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieCastRequest {
    private UUID movieId;
    private String actorName;
    private String characterName;
    private String role;
    private String profileImageUrl;
    private String description;
}
