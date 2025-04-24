package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MovieRepositoryImpl implements CustomMovieRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Movie saveAndRefresh(Movie movie) {
        entityManager.persist(movie);
        entityManager.flush();
        entityManager.refresh(movie);
        return movie;
    }

    @Override
    public List<Movie> findAllActiveMovies() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);

        query.select(root).where(cb.isNull(root.get("deletedAt")));

        return entityManager.createQuery(query).getResultList();

    }


}
