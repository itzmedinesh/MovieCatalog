package io.demo.MovieCatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.MovieCatalog.dto.MovieCastDTO;
import io.demo.MovieCatalog.dto.MovieCastRequest;
import io.demo.MovieCatalog.mapper.MovieCastMapper;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieCast;
import io.demo.MovieCatalog.service.MovieCastService;
import io.demo.MovieCatalog.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieCastController.class)
public class MovieCastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieCastService movieCastService;

    @MockBean
    private MovieService movieService;

    @MockBean
    private MovieCastMapper movieCastMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID castId;
    private UUID movieId;
    private Movie movie;
    private MovieCast cast;
    private MovieCastDTO castDTO;
    private MovieCastRequest castRequest;

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

        castDTO = MovieCastDTO.builder()
                .id(castId)
                .movieId(movieId)
                .actorName("Leonardo DiCaprio")
                .characterName("Cobb")
                .role("Lead")
                .profileImageUrl("https://example.com/images/leonardo.jpg")
                .description("Main character")
                .createdAt(now)
                .updatedAt(now)
                .build();

        castRequest = MovieCastRequest.builder()
                .movieId(movieId)
                .actorName("Leonardo DiCaprio")
                .characterName("Cobb")
                .role("Lead")
                .profileImageUrl("https://example.com/images/leonardo.jpg")
                .description("Main character")
                .build();
    }

    @Test
    void getAllCasts_ShouldReturnCastsList() throws Exception {
        // Arrange
        List<MovieCast> casts = Arrays.asList(cast);
        when(movieCastService.getAllCasts()).thenReturn(casts);
        when(movieCastMapper.toDTO(any(MovieCast.class))).thenReturn(castDTO);

        // Act & Assert
        mockMvc.perform(get("/api/movie-casts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.casts", hasSize(1)))
                .andExpect(jsonPath("$._embedded.casts[0].actorName", is("Leonardo DiCaprio")))
                .andExpect(jsonPath("$._embedded.casts[0].characterName", is("Cobb")))
                .andExpect(jsonPath("$._embedded.casts[0].role", is("Lead")))
                .andExpect(jsonPath("$._embedded.casts[0].profileImageUrl", is("https://example.com/images/leonardo.jpg")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-casts")));

        verify(movieCastService).getAllCasts();
        verify(movieCastMapper).toDTO(any(MovieCast.class));
    }

    @Test
    void getCastById_WithValidId_ShouldReturnCast() throws Exception {
        // Arrange
        when(movieCastService.getCastById(castId)).thenReturn(Optional.of(cast));
        when(movieCastMapper.toDTO(any(MovieCast.class))).thenReturn(castDTO);

        // Act & Assert
        mockMvc.perform(get("/api/movie-casts/{id}", castId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorName", is("Leonardo DiCaprio")))
                .andExpect(jsonPath("$.characterName", is("Cobb")))
                .andExpect(jsonPath("$.role", is("Lead")))
                .andExpect(jsonPath("$.profileImageUrl", is("https://example.com/images/leonardo.jpg")))
                .andExpect(jsonPath("$.description", is("Main character")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-casts/" + castId)))
                .andExpect(jsonPath("$._links.collection.href", containsString("/api/movie-casts")));

        verify(movieCastService).getCastById(castId);
        verify(movieCastMapper).toDTO(cast);
    }

    @Test
    void getCastById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(movieCastService.getCastById(castId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/movie-casts/{id}", castId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(movieCastService).getCastById(castId);
        verify(movieCastMapper, never()).toDTO(any(MovieCast.class));
    }

    @Test
    void getCastsByMovieId_ShouldReturnCastsForMovie() throws Exception {
        // Arrange
        List<MovieCast> casts = Arrays.asList(cast);
        when(movieCastService.getCastsByMovieId(movieId)).thenReturn(casts);
        when(movieCastMapper.toDTO(any(MovieCast.class))).thenReturn(castDTO);

        // Act & Assert
        mockMvc.perform(get("/api/movie-casts/movies/{movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.casts", hasSize(1)))
                .andExpect(jsonPath("$._embedded.casts[0].actorName", is("Leonardo DiCaprio")))
                .andExpect(jsonPath("$._embedded.casts[0].characterName", is("Cobb")))
                .andExpect(jsonPath("$._embedded.casts[0].role", is("Lead")))
                .andExpect(jsonPath("$._embedded.casts[0].profileImageUrl", is("https://example.com/images/leonardo.jpg")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-casts/movies/" + movieId)))
                .andExpect(jsonPath("$._links.collection.href", containsString("/api/movie-casts")));

        verify(movieCastService).getCastsByMovieId(movieId);
        verify(movieCastMapper).toDTO(any(MovieCast.class));
    }

    @Test
    void createCast_ShouldReturnCreatedCast() throws Exception {
        // Arrange
        when(movieCastMapper.toEntity(any(MovieCastRequest.class))).thenReturn(cast);
        when(movieCastService.createCast(any(MovieCast.class))).thenReturn(cast);
        when(movieCastMapper.toDTO(any(MovieCast.class))).thenReturn(castDTO);

        // Act & Assert
        mockMvc.perform(post("/api/movie-casts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(castRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.actorName", is("Leonardo DiCaprio")))
                .andExpect(jsonPath("$.characterName", is("Cobb")))
                .andExpect(jsonPath("$.role", is("Lead")))
                .andExpect(jsonPath("$.profileImageUrl", is("https://example.com/images/leonardo.jpg")))
                .andExpect(jsonPath("$.description", is("Main character")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-casts/" + castId)));

        verify(movieCastMapper).toEntity(any(MovieCastRequest.class));
        verify(movieCastService).createCast(any(MovieCast.class));
        verify(movieCastMapper).toDTO(any(MovieCast.class));
    }

    @Test
    void updateCast_WithValidId_ShouldReturnUpdatedCast() throws Exception {
        // Arrange
        when(movieCastService.getCastById(castId)).thenReturn(Optional.of(cast));
        when(movieCastService.updateCast(eq(castId), any(MovieCast.class))).thenReturn(Optional.of(cast));
        when(movieCastMapper.toDTO(any(MovieCast.class))).thenReturn(castDTO);

        // Act & Assert
        mockMvc.perform(put("/api/movie-casts/{id}", castId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(castRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actorName", is("Leonardo DiCaprio")))
                .andExpect(jsonPath("$.characterName", is("Cobb")))
                .andExpect(jsonPath("$.role", is("Lead")))
                .andExpect(jsonPath("$.profileImageUrl", is("https://example.com/images/leonardo.jpg")))
                .andExpect(jsonPath("$.description", is("Main character")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-casts/" + castId)));

        verify(movieCastService).getCastById(castId);
        verify(movieCastMapper).updateEntityFromRequest(any(MovieCast.class), any(MovieCastRequest.class));
        verify(movieCastService).updateCast(eq(castId), any(MovieCast.class));
        verify(movieCastMapper).toDTO(any(MovieCast.class));
    }

    @Test
    void updateCast_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(movieCastService.getCastById(castId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(put("/api/movie-casts/{id}", castId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(castRequest)))
                .andExpect(status().isNotFound());

        verify(movieCastService).getCastById(castId);
        verify(movieCastMapper, never()).updateEntityFromRequest(any(MovieCast.class), any(MovieCastRequest.class));
        verify(movieCastService, never()).updateCast(any(UUID.class), any(MovieCast.class));
    }

    @Test
    void deleteCast_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        when(movieCastService.deleteCast(castId)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/movie-casts/{id}", castId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(movieCastService).deleteCast(castId);
    }

    @Test
    void deleteCast_WithInvalidId_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(movieCastService.deleteCast(castId)).thenReturn(false);

        // Act & Assert
        mockMvc.perform(delete("/api/movie-casts/{id}", castId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(movieCastService).deleteCast(castId);
    }
}