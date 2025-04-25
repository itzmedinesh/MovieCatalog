package io.demo.MovieCatalog.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "movie_cast", indexes = {
        @Index(name = "idx_moviecast_movie_id", columnList = "movie_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieCast {

    @Id
    @Column(columnDefinition = "BINARY(16)", insertable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "actor_name")
    private String actorName;

    @Column(name = "character_name")
    private String characterName;

    private String role;
    
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
}
