package com.example.MovieMicroservice.controller;

import com.example.MovieMicroservice.model.Movie;
import com.example.MovieMicroservice.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;
    
    // Film ekleme - Sadece admin rolü
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {
        String currentUserRole = getCurrentUserRole();
        Movie saved = movieService.addMovie(movie, currentUserRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Tüm filmleri listeleme - Herkes erişebilir
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    // ID ile film getirme - Herkes erişebilir
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }

    // Film güncelleme - Sadece admin
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        String currentUserRole = getCurrentUserRole();
        return ResponseEntity.ok(movieService.updateMovie(id, updatedMovie, currentUserRole));
    }

    // Film silme - Sadece admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        String currentUserRole = getCurrentUserRole();
        movieService.deleteMovie(id, currentUserRole);
        return ResponseEntity.noContent().build();
    }

    // Örneğin, rating servisi çağırdıktan sonra film ortalamasını güncelleyecek bir endpoint
    @PutMapping("/{id}/rating")
    public ResponseEntity<Void> updateMovieRating(@PathVariable Long id, @RequestParam double average) {
        // Bu metot rating servisi tarafından çağrılır. Burada rol kontrolü gerekmeyebilir.
        Movie movie = movieService.getMovieById(id);
        movie.setRating(average);
        movieService.updateMovie(id, movie, "ROLE_ADMIN"); // Burada "ROLE_ADMIN" veriyoruz, çünkü kendi iç çağrımız.
        return ResponseEntity.ok().build();
    }

    private String getCurrentUserRole() {
        // Mevcut kullanıcının rolünü al
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            // İlk yetkiyi dönelim
            return userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(a -> a.getAuthority())
                    .orElse("ROLE_USER");
        }
        return "ROLE_USER";
    }
}
