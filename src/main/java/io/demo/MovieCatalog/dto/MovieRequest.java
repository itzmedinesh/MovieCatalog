package io.demo.MovieCatalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieRequest {
    private String name;
    private Integer durationMinutes;
    private String description;
    private List<MovieLanguageRequest> languages;
    private List<MovieFormatRequest> formats;
    private List<MovieGenreRequest> genres;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieLanguageRequest {
        private String name;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieFormatRequest {
        private String name;
        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieGenreRequest {
        private String name;
        private String description;
    }
}