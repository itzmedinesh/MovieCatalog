package io.demo.MovieCatalog.repository;

import io.demo.MovieCatalog.model.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<Movie> findAllActiveMovies(Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        
        // Query for fetching the movies
        CriteriaQuery<Movie> query = cb.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);
        query.select(root).where(cb.isNull(root.get("deletedAt")));
        
        // Apply sorting if present
        if (pageable.getSort().isSorted()) {
            pageable.getSort().forEach(order -> {
                if (order.isAscending()) {
                    query.orderBy(cb.asc(root.get(order.getProperty())));
                } else {
                    query.orderBy(cb.desc(root.get(order.getProperty())));
                }
            });
        }
        
        // Create the query with pagination
        TypedQuery<Movie> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Movie> movies = typedQuery.getResultList();
        
        // Count query for total elements
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Movie> countRoot = countQuery.from(Movie.class);
        countQuery.select(cb.count(countRoot)).where(cb.isNull(countRoot.get("deletedAt")));
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        
        return new PageImpl<>(movies, pageable, count);
    }
}

