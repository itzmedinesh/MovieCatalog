package io.demo.MovieCatalog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.demo.MovieCatalog.dto.MovieFormatDTO;
import io.demo.MovieCatalog.dto.MovieFormatRequest;
import io.demo.MovieCatalog.mapper.MovieFormatMapper;
import io.demo.MovieCatalog.model.Movie;
import io.demo.MovieCatalog.model.MovieFormat;
import io.demo.MovieCatalog.service.MovieFormatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieFormatController.class)
public class MovieFormatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieFormatService movieFormatService;

    @MockitoBean
    private MovieFormatMapper movieFormatMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private UUID formatId;
    private UUID movieId;
    private MovieFormat movieFormat;
    private MovieFormatDTO movieFormatDTO;
    private MovieFormatRequest movieFormatRequest;
    private Movie movie;

    @BeforeEach
    void setUp() {
        formatId = UUID.randomUUID();
        movieId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        movie = Movie.builder()
                .id(movieId)
                .name("Inception")
                .build();

        movieFormat = MovieFormat.builder()
                .id(formatId)
                .movie(movie)
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .createdAt(now)
                .updatedAt(now)
                .build();

        movieFormatDTO = MovieFormatDTO.builder()
                .id(formatId)
                .movieId(movieId)
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .createdAt(now)
                .updatedAt(now)
                .build();

        movieFormatRequest = MovieFormatRequest.builder()
                .movieId(movieId)
                .name("IMAX")
                .description("IMAX format with enhanced picture and sound quality")
                .build();
    }

    @Test
    void getAllFormats_ShouldReturnFormatsList() throws Exception {
        List<MovieFormat> formats = Arrays.asList(movieFormat);
        when(movieFormatService.getAllFormats()).thenReturn(formats);
        when(movieFormatMapper.toDTO(any(MovieFormat.class))).thenReturn(movieFormatDTO);

        mockMvc.perform(get("/api/movie-formats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.formats", hasSize(1)))
                .andExpect(jsonPath("$._embedded.formats[0].name", is("IMAX")))
                .andExpect(jsonPath("$._embedded.formats[0].movieId", is(movieId.toString())))
                .andExpect(jsonPath("$._embedded.formats[0]._links.self.href", containsString("/api/movie-formats/" + formatId)))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-formats")));

        verify(movieFormatService).getAllFormats();
        verify(movieFormatMapper).toDTO(any(MovieFormat.class));
    }

    @Test
    void getFormatById_WithValidId_ShouldReturnFormat() throws Exception {
        when(movieFormatService.getFormatById(formatId)).thenReturn(Optional.of(movieFormat));
        when(movieFormatMapper.toDTO(any(MovieFormat.class))).thenReturn(movieFormatDTO);

        mockMvc.perform(get("/api/movie-formats/{id}", formatId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("IMAX")))
                .andExpect(jsonPath("$.movieId", is(movieId.toString())))
                .andExpect(jsonPath("$.description", is("IMAX format with enhanced picture and sound quality")))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-formats/" + formatId)))
                .andExpect(jsonPath("$._links.collection.href", containsString("/api/movie-formats")));

        verify(movieFormatService).getFormatById(formatId);
        verify(movieFormatMapper).toDTO(movieFormat);
    }

    @Test
    void getFormatById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(movieFormatService.getFormatById(formatId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movie-formats/{id}", formatId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(movieFormatService).getFormatById(formatId);
        verify(movieFormatMapper, never()).toDTO(any(MovieFormat.class));
    }

    @Test
    void createFormat_WithMovieId_ShouldReturnCreatedFormat() throws Exception {
        when(movieFormatMapper.toEntity(any(MovieFormatRequest.class))).thenReturn(movieFormat);
        when(movieFormatService.createFormat(any(MovieFormat.class))).thenReturn(movieFormat);
        when(movieFormatMapper.toDTO(any(MovieFormat.class))).thenReturn(movieFormatDTO);

        mockMvc.perform(post("/api/movie-formats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieFormatRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("IMAX")))
                .andExpect(jsonPath("$.movieId", is(movieId.toString())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-formats/" + formatId)));

        verify(movieFormatMapper).toEntity(any(MovieFormatRequest.class));
        verify(movieFormatService).createFormat(any(MovieFormat.class));
        verify(movieFormatMapper).toDTO(any(MovieFormat.class));
    }

    @Test
    void updateFormat_WithValidId_ShouldReturnUpdatedFormat() throws Exception {
        when(movieFormatService.getFormatById(formatId)).thenReturn(Optional.of(movieFormat));
        when(movieFormatService.updateFormat(eq(formatId), any(MovieFormat.class))).thenReturn(Optional.of(movieFormat));
        when(movieFormatMapper.toDTO(any(MovieFormat.class))).thenReturn(movieFormatDTO);

        mockMvc.perform(put("/api/movie-formats/{id}", formatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieFormatRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("IMAX")))
                .andExpect(jsonPath("$.movieId", is(movieId.toString())))
                .andExpect(jsonPath("$._links.self.href", containsString("/api/movie-formats/" + formatId)));

        verify(movieFormatService).getFormatById(formatId);
        verify(movieFormatMapper).updateEntityFromRequest(any(MovieFormat.class), any(MovieFormatRequest.class));
        verify(movieFormatService).updateFormat(eq(formatId), any(MovieFormat.class));
        verify(movieFormatMapper).toDTO(any(MovieFormat.class));
    }

    @Test
    void updateFormat_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(movieFormatService.getFormatById(formatId)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/movie-formats/{id}", formatId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieFormatRequest)))
                .andExpect(status().isNotFound());

        verify(movieFormatService).getFormatById(formatId);
        verify(movieFormatMapper, never()).updateEntityFromRequest(any(MovieFormat.class), any(MovieFormatRequest.class));
        verify(movieFormatService, never()).updateFormat(any(UUID.class), any(MovieFormat.class));
    }

    @Test
    void deleteFormat_WithValidId_ShouldReturnNoContent() throws Exception {
        when(movieFormatService.deleteFormat(formatId)).thenReturn(true);

        mockMvc.perform(delete("/api/movie-formats/{id}", formatId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(movieFormatService).deleteFormat(formatId);
    }

    @Test
    void deleteFormat_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(movieFormatService.deleteFormat(formatId)).thenReturn(false);

        mockMvc.perform(delete("/api/movie-formats/{id}", formatId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(movieFormatService).deleteFormat(formatId);
    }
}