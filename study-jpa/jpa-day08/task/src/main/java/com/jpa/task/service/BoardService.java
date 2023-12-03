package com.jpa.task.service;

import com.jpa.task.domain.entity.Board;
import com.jpa.task.repository.BoardRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public Board boardWrite(Board board){
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true) //java꺼 말고 스프링 트랜잭셔널 달면 readOnly 사용가능. => select 성능 향상됨
    public Board detail(Long boardId){
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 게시물"));
    }

    @Transactional(readOnly = true)
    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Board> findAllWithPagination(int page, int pageSize) {
        return boardRepository.findAllWithPagination(page, pageSize);
    }

    public void delete(Long boardId){
        boardRepository.delete(boardId);
    }
}
