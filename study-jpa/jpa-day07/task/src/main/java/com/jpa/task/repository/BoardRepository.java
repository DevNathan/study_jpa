package com.jpa.task.repository;

import com.jpa.task.domain.entity.Board;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BoardRepository {
    private final EntityManager entityManager;

    public Board save(Board board){
        entityManager.persist(board);
        return board;
    }

    public Optional<Board> findById(Long boardId){
        return Optional.ofNullable(entityManager.find(Board.class, boardId));
    }

    public List<Board> findAll(){
        return entityManager.createQuery("SELECT b FROM Board b", Board.class)
                .getResultList();
    }

    public List<Board> findAllWithPagination(int page, int pageSize){
        return entityManager.createQuery("SELECT b FROM Board b ORDER BY b.id DESC", Board.class)
                .setFirstResult((page - 1) * pageSize) // 페이징 처리를 위해 시작 인덱스를 지정한다.
                .setMaxResults(pageSize) // 인덱스 번호부터 pageSize 만큼 조회한다.
                .getResultList();
    }

    public void delete(Long boardId){
        Board foundBoard = entityManager.find(Board.class, boardId);

        if(foundBoard != null){
            entityManager.remove(foundBoard);
        }
    }
}

















