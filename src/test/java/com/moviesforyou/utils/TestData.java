package com.moviesforyou.utils;

import com.moviesforyou.model.Movie;

public class TestData {
  public static Movie getTestMovie() {
    return new Movie(1L, "Test Movie", "Test Description");
  }
}
