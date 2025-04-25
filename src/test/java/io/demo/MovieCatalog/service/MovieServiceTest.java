package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.repository.CustomMovieRepository;
import io.demo.MovieCatalog.repository.MovieRepository;
import io.demo.MovieCatalog.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CustomMovieRepository customMovieRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private UUID movieId;
    private Movie movie;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void getAllMovies_ShouldReturnAllMovies() {
        // Arrange
        List<Movie> expectedMovies = Arrays.asList(movie);
        when(customMovieRepository.findAllActiveMovies()).thenReturn(expectedMovies);

        // Act
        List<Movie> actualMovies = movieService.getAllMovies();

        // Assert
        assertThat(actualMovies).isEqualTo(expectedMovies);
        verify(customMovieRepository).findAllActiveMovies();
    }
    
    @Test
    void getAllMovies_WithPagination_ShouldReturnPagedMovies() {
        // Arrange
        List<Movie> movies = Arrays.asList(movie);
        Page<Movie> expectedPage = new PageImpl<>(movies, PageRequest.of(0, 10), 1);
        Pageable pageable = PageRequest.of(0, 10);
        
        when(customMovieRepository.findAllActiveMovies(pageable)).thenReturn(expectedPage);
        
        // Act
        Page<Movie> actualPage = movieService.getAllMovies(pageable);
        
        // Assert
        assertThat(actualPage).isEqualTo(expectedPage);
        assertThat(actualPage.getContent()).hasSize(1);
        assertThat(actualPage.getContent().get(0)).isEqualTo(movie);
        assertThat(actualPage.getTotalElements()).isEqualTo(1);
        verify(customMovieRepository).findAllActiveMovies(pageable);
    }

    @Test
    void getMovieById_WithExistingId_ShouldReturnMovie() {
        // Arrange
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // Act
        Optional<Movie> result = movieService.getMovieById(movieId);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(movie);
        verify(movieRepository).findById(movieId);
    }

    @Test
    void getMovieById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act
        Optional<Movie> result = movieService.getMovieById(movieId);

        // Assert
        assertThat(result).isEmpty();
        verify(movieRepository).findById(movieId);
    }

    @Test
    void createMovie_ShouldSaveAndReturnMovie() {
        // Arrange
        Movie movieToCreate = Movie.builder()
                .name("Inception")
                .description("A thief who steals corporate secrets through the use of dream-sharing technology.")
                .durationMinutes(148)
                .imageUrl("https://example.com/images/inception.jpg")
                .trailerUrl("https://example.com/trailers/inception.mp4")
                .build();

        when(customMovieRepository.saveAndRefresh(any(Movie.class))).thenReturn(movie);

        // Act
        Movie createdMovie = movieService.createMovie(movieToCreate);

        // Assert
        assertThat(createdMovie).isEqualTo(movie);
        verify(customMovieRepository).saveAndRefresh(any(Movie.class));
    }

    @Test
    void updateMovie_WithExistingId_ShouldUpdateAndReturnMovie() {
        // Arrange
        Movie updatedMovie = Movie.builder()
                .id(movieId)
                .name("Inception Updated")
                .description("Updated description")
                .durationMinutes(150)
                .imageUrl("https://example.com/images/inception-updated.jpg")
                .trailerUrl("https://example.com/trailers/inception-updated.mp4")
                .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);

        // Act
        Optional<Movie> result = movieService.updateMovie(movieId, updatedMovie);

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(updatedMovie);
        verify(movieRepository).findById(movieId);
        verify(movieRepository).save(any(Movie.class));
    }

    @Test
    void updateMovie_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        Movie updatedMovie = Movie.builder()
                .id(movieId)
                .name("Inception Updated")
                .description("Updated description")
                .durationMinutes(150)
                .imageUrl("https://example.com/images/inception-updated.jpg")
                .trailerUrl("https://example.com/trailers/inception-updated.mp4")
                .build();

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act
        Optional<Movie> result = movieService.updateMovie(movieId, updatedMovie);

        // Assert
        assertThat(result).isEmpty();
        verify(movieRepository).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_WithExistingId_ShouldMarkAsDeletedAndReturnTrue() {
        // Arrange
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(customMovieRepository.saveAndRefresh(any(Movie.class))).thenReturn(movie);

        // Act
        boolean result = movieService.deleteMovie(movieId);

        // Assert
        assertThat(result).isTrue();
        assertThat(movie.getDeletedAt()).isNotNull();
        verify(movieRepository).findById(movieId);
        verify(customMovieRepository).saveAndRefresh(movie);
    }

    @Test
    void deleteMovie_WithNonExistingId_ShouldReturnFalse() {
        // Arrange
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        // Act
        boolean result = movieService.deleteMovie(movieId);

        // Assert
        assertThat(result).isFalse();
        verify(movieRepository).findById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }
}

