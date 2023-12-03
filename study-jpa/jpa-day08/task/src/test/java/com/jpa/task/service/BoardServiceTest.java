package com.jpa.task.service;

import com.jpa.task.domain.entity.Board;
import com.jpa.task.repository.BoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    Board board;

    @BeforeEach
    void setup() {
        board = new Board();
        board.setTitle("test");
        board.setContent("test content");
    }

    @Test
    @DisplayName("저장")
    void write(){
        // given
        doReturn(board).when(boardRepository).save(any());

        // when
        Board savedBoard = boardService.boardWrite(board);

        // then
        assertThat(savedBoard).isNotNull();
        assertThat(savedBoard.getTitle()).isEqualTo(board.getTitle());
        assertThat(savedBoard).isNotNull().extracting(Board::getTitle).isEqualTo(board.getTitle());
    }

    @Test
    @DisplayName("게시물 조회")
    void findById(){
        // given
        doReturn(Optional.of(board)).when(boardRepository).findById(any());

        // when
        Board foundBoard = boardService.detail(1L);

        // then
        assertThat(foundBoard.getTitle()).isEqualTo(board.getTitle());
    }

    @Test
    @DisplayName("전체 조회")
    void findAll(){
        // given
        doReturn(List.of(board)).when(boardRepository).findAll();

        // when
        List<Board> boardList = boardService.findAll();

        // then
        assertThat(boardList).isNotEmpty().contains(board);
    }
    
    @Test
    @DisplayName("페이징 처리")
    void findAllWithPagination(){
        // given
        doReturn(List.of(board)).when(boardRepository).findAllWithPagination(anyInt(), anyInt());
        
        // when
        List<Board> boardList = boardService.findAllWithPagination(1, 1232);

        // then
        assertThat(boardList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("삭제")
    void delete(){
        // given
        doNothing().when(boardRepository).delete(any());

        // when
        boardService.delete(1L);

        // then
        verify(boardRepository, times(1)).delete(1L);
    }
}