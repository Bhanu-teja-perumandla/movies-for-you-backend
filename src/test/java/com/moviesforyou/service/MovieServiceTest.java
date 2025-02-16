package com.moviesforyou.service;

import com.moviesforyou.model.Movie;
import com.moviesforyou.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.moviesforyou.service.MovieService.getLoadData;
import static com.moviesforyou.utils.TestData.getTestMovie;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MovieServiceTest {
  MovieService movieService;
  MovieRepository movieRepository;

  @BeforeEach
  void setUp() {
    movieRepository = mock(MovieRepository.class);
    movieService = new MovieService(movieRepository);
  }

  @Test
  void shouldGetAllMovies() {
    List<Movie> movies = List.of(getTestMovie());
    when(movieRepository.findAll()).thenReturn(movies);
    assertThat(movieService.getAllMovies()).isEqualTo(movies);
    verify(movieRepository).findAll();
  }

  @Test
  void shouldAddMovie() {
    Movie movie = getTestMovie();
    when(movieRepository.save(movie)).thenReturn(movie);
    assertThat(movieService.addMovie(movie)).isEqualTo(movie);
    verify(movieRepository).save(movie);
  }

  @Test
  void shouldLoadDefaultMovies() {
    List<Movie> movies = getLoadData();
    when(movieRepository.saveAll(movies)).thenReturn(movies);
    assertThat(movieService.loadMovies()).isEqualTo(movies);
    verify(movieRepository).saveAll(movies);
  }

  @Test
  void shouldDeleteMovies() {
    List<Long> ids = List.of(getTestMovie().getId());
    doNothing().when(movieRepository).deleteAllById(ids);
    movieService.deleteMovies(ids);
    verify(movieRepository).deleteAllById(ids);
  }
}