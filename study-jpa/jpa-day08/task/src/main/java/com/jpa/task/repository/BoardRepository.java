package com.jpa.task.repository;

import com.jpa.task.domain.entity.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BoardRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Board save(Board board){
        entityManager.persist(board);
        return board;
    }

    public Optional<Board> findById(Long boardId){
        return Optional.ofNullable(entityManager.find(Board.class, boardId));
    }

    public List<Board> findAll(){
        return entityManager.createQuery("select b from Board b", Board.class).getResultList();
    }

    public List<Board> findAllWithPagination(int page, int pageSize){
        return entityManager.createQuery("select b from Board b order by b.id desc", Board.class)
                .setFirstResult((page - 1) * pageSize) // 페이징 처리를 위해 시작 인덱스를 지정한다.
                .setMaxResults(pageSize)
                .getResultList();
    }

    public void delete(Long boardId){
        Board foundBoard = entityManager.find(Board.class, boardId);

        if (foundBoard != null){
            entityManager.remove(foundBoard);
        }
    }
}
