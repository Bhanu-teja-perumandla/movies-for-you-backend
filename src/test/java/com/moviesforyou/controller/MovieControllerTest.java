package com.moviesforyou.controller;

import com.moviesforyou.model.Movie;
import com.moviesforyou.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static com.moviesforyou.utils.TestData.getTestMovie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieControllerTest {
  MovieService movieService;
  MovieController movieController;

  @BeforeEach
  void setUp() {
    movieService = Mockito.mock(MovieService.class);
    movieController = new MovieController(movieService);
  }

  @Test
  void shouldGetAllMovies() {
    List<Movie> movies = List.of(getTestMovie());
    when(movieService.getAllMovies()).thenReturn(movies);
    assertThat(movieController.getAllMovies()).isEqualTo(movies);
    Mockito.verify(movieService).getAllMovies();
  }

  @Test
  void shouldAddMovie() {
    Movie movie = getTestMovie();
    when(movieService.addMovie(movie)).thenReturn(movie);
    assertThat(movieController.addMovie(movie)).isEqualTo(movie);
    Mockito.verify(movieService).addMovie(movie);
  }

  @Test
  void shouldLoadSomeMovieDetails() {
    List<Movie> movies = List.of(getTestMovie());
    when(movieService.loadMovies()).thenReturn(movies);
    assertThat(movieController.loadMovies()).isEqualTo(movies);
    Mockito.verify(movieService).loadMovies();
  }

  @Test
  void shouldDeleteMovies() {
    Movie testMovie = getTestMovie();
    List<Long> ids = List.of(testMovie.getId());
    doNothing().when(movieService).deleteMovies(ids);
    assertThat(movieController.deleteMovies(ids)).isTrue();
    verify(movieService).deleteMovies(ids);
  }
}