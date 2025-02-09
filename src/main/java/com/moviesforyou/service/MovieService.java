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

  public List<Movie> loadMovies() {
    return movieRepository.saveAll(getLoadData());
  }

  public static List<Movie> getLoadData() {
    return List.of(
        new Movie(1L, "RRR", "A fictitious story about two legendary revolutionaries and their journey away from home before they started fighting for their country in 1920's.", 9.0f, "https://m.media-amazon.com/images/M/MV5BNWMwODYyMjQtMTczMi00NTQ1LWFkYjItMGJhMWRkY2E3NDAyXkEyXkFqcGc@._V1_SX300.jpg"),
        new Movie(2L, "Iron Man", "After being held captive in an Afghan cave, billionaire engineer Tony Stark creates a unique weaponized suit of armor to fight evil.", 7.9f, "https://m.media-amazon.com/images/M/MV5BMTczNTI2ODUwOF5BMl5BanBnXkFtZTcwMTU0NTIzMw@@._V1_SX300.jpg"),
        new Movie(3L, "The Dark Knight", "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.", 9.0f, "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_SX300.jpg"),
        new Movie(4L, "Devara Part 1", "A mighty sea warrior takes a violent stand against the criminal deeds of his village. Years later, his mild-mannered son walks a path of his own.", 8.0f, "https://m.media-amazon.com/images/M/MV5BMzUwZTM1ZDEtZGViOC00YmUxLWEwNWQtNGIyMzE2NWZkYzUyXkEyXkFqcGc@._V1_SX300.jpg"),
        new Movie(5L, "The Shawshank Redemption", "A banker convicted of uxoricide forms a friendship over a quarter century with a hardened convict, while maintaining his innocence and trying to remain hopeful through simple compassion.", 9.3f, "https://m.media-amazon.com/images/M/MV5BMDAyY2FhYjctNDc5OS00MDNlLThiMGUtY2UxYWVkNGY2ZjljXkEyXkFqcGc@._V1_SX300.jpg")
        );
  }
}
