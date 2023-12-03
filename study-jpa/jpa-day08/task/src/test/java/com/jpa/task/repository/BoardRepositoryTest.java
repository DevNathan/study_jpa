package com.jpa.task.repository;

import com.jpa.task.domain.entity.Board;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    Board board;

    @BeforeEach
    void setup(){
        board = new Board();
        board.setTitle("test title");
        board.setContent("test content");
    }

    @Test
    @DisplayName("게시글 작성")
    void save(){
        // given
        // when
        Board savedBoard = boardRepository.save(board);

        // then
        assertThat(savedBoard.getTitle()).isEqualTo(board.getTitle());
    }

    @Test
    @DisplayName("게시물 조회")
    void findById(){
        // given
        Board savedBoard = boardRepository.save(board);

        // when
        Optional<Board> foundBoard = boardRepository.findById(savedBoard.getId());

        // then
        foundBoard.ifPresentOrElse(
                b -> assertThat(b).isNotNull().extracting(Board::getTitle).isEqualTo(board.getTitle()),
                () -> fail()
        );
    }

    @Test
    @DisplayName("전체 조회")
    void findAll(){
        // given
        boardRepository.save(board);

        // when
        List<Board> boardList = boardRepository.findAll();
        // then
        assertThat(boardList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("전체 조회 페이징")
    void findAllWithPagination(){
        // given
        Board board2 = new Board();
        board2.setTitle("test2");
        board2.setContent("test2");

        Board board3 = new Board();
        board3.setTitle("test3");
        board3.setContent("test3");

        List.of(board, board2, board3).forEach(boardRepository::save);

        // when
        List<Board> boardList = boardRepository.findAllWithPagination(1, 2);

        // then
        assertThat(boardList.size()).isEqualTo(2);
        assertThat(boardList).extracting(Board::getTitle).contains("test3", "test2");
    }

    @Test
    @DisplayName("삭제")
    void delete(){
        // given
        boardRepository.save(board);

        // when
        boardRepository.delete(board.getId());

        // then
        List<Board> boardList = boardRepository.findAll();
        assertThat(boardList.size()).isEqualTo(0);
    }
}