package com.jpa.data01.repository;

import com.jpa.data01.domain.entity.Book;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookRepository {
    private final EntityManager em;

    public Book save(Book book){
        em.persist(book);
        return book;
    }

    public Optional<Book> findById(Long bookId){
        return Optional.ofNullable(em.find(Book.class, bookId));
    }

    public List<Book> findAll(){
        String query = "select b from Book b";
        return em.createQuery(query, Book.class).getResultList();
    }

    public void delete(Book book){
        em.remove(book);
    }
}











