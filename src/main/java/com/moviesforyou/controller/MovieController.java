package com.moviesforyou.controller;

import com.moviesforyou.model.Movie;
import com.moviesforyou.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MovieController {

  private final MovieService movieService;

  @Autowired
  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping("/hello")
  public String hello() {
    return "Hello, World!";
  }

  @GetMapping("/movies")
  public List<Movie> getAllMovies() {
    return movieService.getAllMovies();
  }

  @PostMapping("/add")
  public Movie addMovie(@RequestBody Movie movie) {
    return movieService.addMovie(movie);
  }
}
