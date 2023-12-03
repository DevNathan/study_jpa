package com.jpa.task.service;

import com.jpa.task.domain.entity.Board;
import com.jpa.task.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;

    public Board boardWrite(Board board) {
        return boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public Board detail(Long boardId) {
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








