package com.example.MovieMicroservice.service;

import com.example.MovieMicroservice.model.Movie;
import com.example.MovieMicroservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Film ekleme
    // Burada admin kontrolünü genellikle Security context üzerinden yaparsınız.
    // Örneğin, Spring Security ile kimlik doğrulama yapıyorsanız, 
    // SecurityContextHolder ile current user rolünü kontrol edebilirsiniz.
    public Movie addMovie(Movie movie, String currentUserRole) {
        // İş kuralı: Sadece admin ekleyebilir
        if(!"ROLE_ADMIN".equals(currentUserRole)) {
            throw new SecurityException("Bu işlemi gerçekleştirmek için yetkiniz yok.");
        }

        // Ek bir validasyon: Aynı isimde film var mı?
        boolean exists = movieRepository.findAll().stream()
                .anyMatch(m -> m.getTitle().equalsIgnoreCase(movie.getTitle()));
        if (exists) {
            throw new IllegalArgumentException("Bu isimde bir film zaten mevcut.");
        }

        // Film ekleme işlemi
        return movieRepository.save(movie);
    }

    // Tüm filmleri listeleme
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // ID ile film bulma
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film bulunamadı: " + id));
    }

    // Film güncelleme
    public Movie updateMovie(Long id, Movie updatedMovie, String currentUserRole) {
        if(!"ROLE_ADMIN".equals(currentUserRole)) {
            throw new SecurityException("Güncelleme işlemi için yetkiniz yok.");
        }

        Movie existing = movieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Film bulunamadı: " + id));

        existing.setTitle(updatedMovie.getTitle());
        existing.setGenre(updatedMovie.getGenre());
        existing.setReleaseDate(updatedMovie.getReleaseDate());
        existing.setDirector(updatedMovie.getDirector());
        existing.setDescription(updatedMovie.getDescription());
        // Rating burada güncellenmez, rating servisi üzerinden işlenir.

        return movieRepository.save(existing);
    }

    // Film silme
    public void deleteMovie(Long id, String currentUserRole) {
        if(!"ROLE_ADMIN".equals(currentUserRole)) {
            throw new SecurityException("Silme işlemi için yetkiniz yok.");
        }

        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Film bulunamadı: " + id));

        movieRepository.delete(movie);
    }
}
