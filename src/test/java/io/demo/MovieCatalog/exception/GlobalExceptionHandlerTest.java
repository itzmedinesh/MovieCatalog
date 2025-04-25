package io.demo.MovieCatalog.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.demo.MovieCatalog.service.MovieService;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    public void whenResourceNotFound_thenReturnNotFoundWithErrorResponse() throws Exception {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(movieService.getMovieById(any(UUID.class))).thenReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/movies/{id}", nonExistentId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    public void whenIllegalArgumentExceptionThrown_thenReturnBadRequestWithErrorResponse() throws Exception {
        // Given
        UUID movieId = UUID.randomUUID();
        when(movieService.getMovieById(any(UUID.class))).thenThrow(new IllegalArgumentException("Invalid argument"));

        // When/Then
        mockMvc.perform(get("/api/movies/{id}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid argument provided"))
                .andExpect(jsonPath("$.debugMessage").value("Invalid argument"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}