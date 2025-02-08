package com.moviesforyou.service;

import com.moviesforyou.model.Movie;
import com.moviesforyou.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
  private final MovieRepository movieRepository;

  public MovieService(MovieRepository movieRepository) {
    this.movieRepository = movieRepository;
  }

  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  public Movie addMovie(Movie movie) {
    return movieRepository.save(movie);
  }
}
