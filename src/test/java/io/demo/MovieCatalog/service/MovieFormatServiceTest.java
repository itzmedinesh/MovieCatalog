package io.demo.MovieCatalog.service;

import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.repository.MovieFormatRepository;
import io.demo.MovieCatalog.repository.MovieRepository;
import io.demo.MovieCatalog.service.impl.MovieFormatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieFormatServiceTest {

    @Mock
    private MovieFormatRepository movieFormatRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieFormatServiceImpl movieFormatService;

    private UUID movieId;
    private UUID formatId;
    private Movie movie;
    private MovieFormat movieFormat;

    @BeforeEach
    void setUp() {
        movieId = UUID.randomUUID();
        formatId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        movie = Movie.builder()
                .id(movieId)
                .name("Inception")
                .durationMinutes(148)
                .description("A thief who steals corporate secrets through dream-sharing technology.")
                .createdAt(now)
                .updatedAt(now)
                .build();

        movieFormat = MovieFormat.builder()
                .id(formatId)
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    void createFormat_WithValidMovieId_ShouldAssociateFormatWithMovie() {
        // Arrange
        MovieFormat formatToCreate = MovieFormat.builder()
                .id(UUID.randomUUID())
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .build();
        
        // Set a movie reference with just the ID
        Movie movieRef = new Movie();
        movieRef.setId(movieId);
        formatToCreate.setMovie(movieRef);
        
        // Mock the movie repository to return the full movie entity
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        
        // Mock the format repository to return the saved format
        when(movieFormatRepository.save(any(MovieFormat.class))).thenAnswer(invocation -> {
            MovieFormat savedFormat = invocation.getArgument(0);
            return savedFormat;
        });

        // Act
        MovieFormat createdFormat = movieFormatService.createFormat(formatToCreate);

        // Assert
        assertNotNull(createdFormat);
        assertNotNull(createdFormat.getMovie());
        assertEquals(movieId, createdFormat.getMovie().getId());
        assertEquals("Inception", createdFormat.getMovie().getName());
        
        // Verify that the repositories were called
        verify(movieRepository).findById(movieId);
        verify(movieFormatRepository).save(formatToCreate);
    }

    @Test
    void createFormat_WithInvalidMovieId_ShouldThrowException() {
        // Arrange
        UUID invalidMovieId = UUID.randomUUID();
        
        MovieFormat formatToCreate = MovieFormat.builder()
                .id(UUID.randomUUID())
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .build();
        
        // Set a movie reference with an invalid ID
        Movie movieRef = new Movie();
        movieRef.setId(invalidMovieId);
        formatToCreate.setMovie(movieRef);
        
        // Mock the movie repository to return empty for the invalid ID
        when(movieRepository.findById(invalidMovieId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            movieFormatService.createFormat(formatToCreate);
        });
        
        // Verify that the movie repository was called but not the format repository
        verify(movieRepository).findById(invalidMovieId);
        verify(movieFormatRepository, never()).save(any(MovieFormat.class));
    }

    @Test
    void updateFormat_WithValidMovieId_ShouldUpdateMovieAssociation() {
        // Arrange
        MovieFormat existingFormat = MovieFormat.builder()
                .id(formatId)
                .name("Digital")
                .description("Standard digital format")
                .build();
        
        MovieFormat updatedDetails = MovieFormat.builder()
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .build();
        
        // Set a movie reference with just the ID
        Movie movieRef = new Movie();
        movieRef.setId(movieId);
        updatedDetails.setMovie(movieRef);
        
        // Mock the repositories
        when(movieFormatRepository.findById(formatId)).thenReturn(Optional.of(existingFormat));
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));
        when(movieFormatRepository.save(any(MovieFormat.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Optional<MovieFormat> result = movieFormatService.updateFormat(formatId, updatedDetails);

        // Assert
        assertTrue(result.isPresent());
        MovieFormat updatedFormat = result.get();
        assertEquals("IMAX", updatedFormat.getName());
        assertEquals("IMAX format with enhanced picture and sound quality", updatedFormat.getDescription());
        assertNotNull(updatedFormat.getMovie());
        assertEquals(movieId, updatedFormat.getMovie().getId());
        
        // Verify that the repositories were called
        verify(movieFormatRepository).findById(formatId);
        verify(movieRepository).findById(movieId);
        verify(movieFormatRepository).save(existingFormat);
    }
}