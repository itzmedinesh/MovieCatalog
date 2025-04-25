package io.demo.MovieCatalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"Movie\"", schema = "moviecatalog", indexes = {
        @Index(name = "idx_movie_name", columnList = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {

    @Id
    @Column(columnDefinition = "BINARY(16)", insertable = false, updatable = false)
    private UUID id;

    private String name;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @Column(name = "trailer_url")
    private String trailerUrl;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieLanguage> languages;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieFormat> formats;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieGenre> genres;
    
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MovieCast> cast;
}

