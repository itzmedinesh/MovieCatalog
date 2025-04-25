package io.demo.MovieCatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.MovieCatalog.dto.*;
import io.demo.MovieCatalog.exception.ResourceNotFoundException;
import io.demo.MovieCatalog.mapper.MovieMapper;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @MockitoBean
    private MovieMapper movieMapper;
    
    @MockitoBean
    private PagedResourcesAssembler<Movie> pagedResourcesAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID movieId;
    private Movie movie;
    private MovieDTO movieDTO;
    private MovieRequest movieRequest;

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

        movieDTO = MovieDTO.builder()
                .id(movieId)
                .name("Inception")
                .description("A thief who steals corporate secrets through the use of dream-sharing technology.")
                .durationMinutes(148)
                .imageUrl("https://example.com/images/inception.jpg")
                .trailerUrl("https://example.com/trailers/inception.mp4")
                .createdAt(now)
                .updatedAt(now)
                .languages(Arrays.asList(MovieLanguageDTO.builder().name("English").description("english lang").build(), MovieLanguageDTO.builder().name("French").description("french lang").build()))
                .formats(Arrays.asList(MovieFormatDTO.builder().name("IMAX").description("imax format").build(), MovieFormatDTO.builder().name("Digital").description("digital format").build()))
                .genres(Arrays.asList(MovieGenreDTO.builder().name("Sci-Fi").description("sci-fi genre").build(), MovieGenreDTO.builder().name("Action").description("action genre").build()))
                .cast(Arrays.asList(MovieCastDTO.builder().actorName("Leonardo DiCaprio").characterName("Cobb").role("Lead").description("Main character").build()))
                .build();

        movieRequest = MovieRequest.builder()
                .name("Inception")
                .description("A thief who steals corporate secrets through the use of dream-sharing technology.")
                .durationMinutes(148)
                .imageUrl("https://example.com/images/inception.jpg")
                .trailerUrl("https://example.com/trailers/inception.mp4")
                .languages(Arrays.asList(MovieRequest.MovieLanguageRequest.builder().name("English").description("english lang").build(), MovieRequest.MovieLanguageRequest.builder().name("French").description("french lang").build()))
                .formats(Arrays.asList(MovieRequest.MovieFormatRequest.builder().name("IMAX").description("imax format").build(), MovieRequest.MovieFormatRequest.builder().name("Digital").description("digital format").build()))
                .genres(Arrays.asList(MovieRequest.MovieGenreRequest.builder().name("Sci-Fi").description("sci-fi genre").build(), MovieRequest.MovieGenreRequest.builder().name("Action").description("action genre").build()))
                .cast(Arrays.asList(MovieRequest.MovieCastRequest.builder().actorName("Leonardo DiCaprio").characterName("Cobb").role("Lead").description("Main character").build()))
                .build();
    }

    @Test
    void getAllMovies_WithDefaultParams_ShouldReturnMoviesList() throws Exception {
        List<Movie> movies = Arrays.asList(movie);
        when(movieService.getAllMovies()).thenReturn(movies);
        when(movieMapper.toDTO(any(Movie.class))).thenReturn(movieDTO);

        mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.movies", hasSize(1)))
                .andExpect(jsonPath("$._embedded.movies[0].name", is("Inception")))
                .andExpect(jsonPath("$._embedded.movies[0].imageUrl", is("https://example.com/images/inception.jpg")))
                .andExpect(jsonPath("$._embedded.movies[0].trailerUrl", is("https://example.com/trailers/inception.mp4")))
                .andExpect(jsonPath("$._embedded.movies[0]._links.self.href", containsString("/api/movies/" + movieId)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movies")));

        verify(movieService).getAllMovies();
        verify(movieMapper).toDTO(any(Movie.class));
    }


    @Test
    void getMovieById_WithValidId_ShouldReturnMovie() throws Exception {
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(movie));
        when(movieMapper.toDTO(any(Movie.class))).thenReturn(movieDTO);

        mockMvc.perform(get("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Inception")))
                .andExpect(jsonPath("$.description", is("A thief who steals corporate secrets through the use of dream-sharing technology.")))
                .andExpect(jsonPath("$.durationMinutes", is(148)))
                .andExpect(jsonPath("$.imageUrl", is("https://example.com/images/inception.jpg")))
                .andExpect(jsonPath("$.trailerUrl", is("https://example.com/trailers/inception.mp4")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movies/" + movieId)))
                .andExpect(jsonPath("$._links.collection.href", containsString("/api/movies")));

        verify(movieService).getMovieById(movieId);
        verify(movieMapper).toDTO(movie);
    }

    @Test
    void getMovieById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(String.format("Movie not found with id: '%s'", movieId)))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(movieService).getMovieById(movieId);
        verify(movieMapper, never()).toDTO(any(Movie.class));
    }

    @Test
    void createMovie_ShouldReturnCreatedMovie() throws Exception {
        when(movieMapper.toEntity(any(MovieRequest.class))).thenReturn(movie);
        when(movieService.createMovie(any(Movie.class))).thenReturn(movie);
        when(movieMapper.toDTO(any(Movie.class))).thenReturn(movieDTO);

        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Inception")))
                .andExpect(jsonPath("$.imageUrl", is("https://example.com/images/inception.jpg")))
                .andExpect(jsonPath("$.trailerUrl", is("https://example.com/trailers/inception.mp4")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movies/" + movieId)));

        verify(movieMapper).toEntity(any(MovieRequest.class));
        verify(movieService).createMovie(any(Movie.class));
        verify(movieMapper).toDTO(any(Movie.class));
    }

    @Test
    void updateMovie_WithValidId_ShouldReturnUpdatedMovie() throws Exception {
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(movie));
        when(movieService.updateMovie(eq(movieId), any(Movie.class))).thenReturn(Optional.of(movie));
        when(movieMapper.toDTO(any(Movie.class))).thenReturn(movieDTO);

        mockMvc.perform(put("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Inception")))
                .andExpect(jsonPath("$.imageUrl", is("https://example.com/images/inception.jpg")))
                .andExpect(jsonPath("$.trailerUrl", is("https://example.com/trailers/inception.mp4")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movies/" + movieId)));

        verify(movieService).getMovieById(movieId);
        verify(movieMapper).updateEntityFromRequest(any(Movie.class), any(MovieRequest.class));
        verify(movieService).updateMovie(eq(movieId), any(Movie.class));
        verify(movieMapper).toDTO(any(Movie.class));
    }

    @Test
    void updateMovie_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(String.format("Movie not found with id: '%s'", movieId)))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(movieService).getMovieById(movieId);
        verify(movieMapper, never()).updateEntityFromRequest(any(Movie.class), any(MovieRequest.class));
        verify(movieService, never()).updateMovie(any(UUID.class), any(Movie.class));
    }

    @Test
    void deleteMovie_WithValidId_ShouldReturnNoContent() throws Exception {
        when(movieService.getMovieById(movieId)).thenReturn(Optional.of(movie));
        when(movieService.deleteMovie(movieId)).thenReturn(true);

        mockMvc.perform(delete("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(movieService).getMovieById(movieId);
        verify(movieService).deleteMovie(movieId);
    }

    @Test
    void deleteMovie_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(movieService.getMovieById(movieId)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(String.format("Movie not found with id: '%s'", movieId)))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(movieService).getMovieById(movieId);
        verify(movieService, never()).deleteMovie(movieId);
    }
}





