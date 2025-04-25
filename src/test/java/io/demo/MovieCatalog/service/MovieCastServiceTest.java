package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieCast;
import io.demo.MovieCatalog.repository.MovieCastRepository;
import io.demo.MovieCatalog.service.impl.MovieCastServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieCastServiceTest {

    @Mock
    private MovieCastRepository movieCastRepository;

    @InjectMocks
    private MovieCastServiceImpl movieCastService;

    private UUID castId;
    private UUID movieId;
    private Movie movie;
    private MovieCast cast;

    @BeforeEach
    void setUp() {
        castId = UUID.randomUUID();
        movieId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        movie = Movie.builder()
                .id(movieId)
                .name("Inception")
                .description("A thief who steals corporate secrets through the use of dream-sharing technology.")
                .durationMinutes(148)
                .imageUrl("https://example.com/images/inception.jpg")
                .trailerUrl("https://example.com/trailers/inception.mp4")
                .createdAt(now)
                .updatedAt(now)
                .build();

        cast = MovieCast.builder()
                .id(castId)
                .movie(movie)
                .actorName("Leonardo DiCaprio")
                .characterName("Cobb")
                .role("Lead")
                .profileImageUrl("https://example.com/images/leonardo.jpg")
                .description("Main character")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void getAllCasts_ShouldReturnAllCasts() {
        // Arrange
        List<MovieCast> expectedCasts = Arrays.asList(cast);
        when(movieCastRepository.findAll()).thenReturn(expectedCasts);

        // Act
        List<MovieCast> actualCasts = movieCastService.getAllCasts();

        // Assert
        assertThat(actualCasts).isEqualTo(expectedCasts);
        verify(movieCastRepository).findAll();
    }

    @Test
    void getCastsByMovieId_ShouldReturnCastsForMovie() {
        // Arrange
        List<MovieCast> expectedCasts = Arrays.asList(cast);
        when(movieCastRepository.findByMovieId(movieId)).thenReturn(expectedCasts);

        // Act
        List<MovieCast> actualCasts = movieCastService.getCastsByMovieId(movieId);

        // Assert
        assertThat(actualCasts).isEqualTo(expectedCasts);
        verify(movieCastRepository).findByMovieId(movieId);
    }

    @Test
    void getCastById_WithExistingId_ShouldReturnCast() {
        // Arrange
        when(movieCastRepository.findById(castId)).thenReturn(Optional.of(cast));

        // Act
        Optional<MovieCast> result = movieCastService.getCastById(castId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(cast);
        verify(movieCastRepository).findById(castId);
    }

    @Test
    void getCastById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(movieCastRepository.findById(castId)).thenReturn(Optional.empty());

        // Act
        Optional<MovieCast> result = movieCastService.getCastById(castId);

        // Assert
        assertThat(result).isEmpty();
        verify(movieCastRepository).findById(castId);
    }

    @Test
    void createCast_ShouldSaveAndReturnCast() {
        // Arrange
        MovieCast castToCreate = MovieCast.builder()
                .movie(movie)
                .actorName("Leonardo DiCaprio")
                .characterName("Cobb")
                .role("Lead")
                .profileImageUrl("https://example.com/images/leonardo.jpg")
                .description("Main character")
                .build();

        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(cast);

        // Act
        MovieCast createdCast = movieCastService.createCast(castToCreate);

        // Assert
        assertThat(createdCast).isEqualTo(cast);
        verify(movieCastRepository).save(any(MovieCast.class));
    }

    @Test
    void updateCast_WithExistingId_ShouldUpdateAndReturnCast() {
        // Arrange
        MovieCast updatedCast = MovieCast.builder()
                .id(castId)
                .movie(movie)
                .actorName("Leonardo DiCaprio Updated")
                .characterName("Cobb Updated")
                .role("Lead Updated")
                .profileImageUrl("https://example.com/images/leonardo_updated.jpg")
                .description("Updated description")
                .build();

        when(movieCastRepository.findById(castId)).thenReturn(Optional.of(cast));
        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(updatedCast);

        // Act
        Optional<MovieCast> result = movieCastService.updateCast(castId, updatedCast);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedCast);
        verify(movieCastRepository).findById(castId);
        verify(movieCastRepository).save(any(MovieCast.class));
    }

    @Test
    void updateCast_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        MovieCast updatedCast = MovieCast.builder()
                .id(castId)
                .movie(movie)
                .actorName("Leonardo DiCaprio Updated")
                .characterName("Cobb Updated")
                .role("Lead Updated")
                .profileImageUrl("https://example.com/images/leonardo_updated.jpg")
                .description("Updated description")
                .build();

        when(movieCastRepository.findById(castId)).thenReturn(Optional.empty());

        // Act
        Optional<MovieCast> result = movieCastService.updateCast(castId, updatedCast);

        // Assert
        assertThat(result).isEmpty();
        verify(movieCastRepository).findById(castId);
        verify(movieCastRepository, never()).save(any(MovieCast.class));
    }

    @Test
    void deleteCast_WithExistingId_ShouldMarkAsDeletedAndReturnTrue() {
        // Arrange
        when(movieCastRepository.findById(castId)).thenReturn(Optional.of(cast));
        when(movieCastRepository.save(any(MovieCast.class))).thenReturn(cast);

        // Act
        boolean result = movieCastService.deleteCast(castId);

        // Assert
        assertThat(result).isTrue();
        assertThat(cast.getDeletedAt()).isNotNull();
        verify(movieCastRepository).findById(castId);
        verify(movieCastRepository).save(cast);
    }

    @Test
    void deleteCast_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(movieCastRepository.findById(castId)).thenReturn(Optional.empty());

        // Act
        boolean result = movieCastService.deleteCast(castId);

        // Assert
        assertThat(result).isFalse();
        verify(movieCastRepository).findById(castId);
        verify(movieCastRepository, never()).save(any(MovieCast.class));
    }
}