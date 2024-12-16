package com.example.MovieMicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.MovieMicroservice.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {

}
