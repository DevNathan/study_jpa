package com.jpa.data01.dataRepository;

import com.jpa.data01.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDataRepository extends JpaRepository<Book, Long> {
}
