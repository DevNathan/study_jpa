package com.jpa.task.service;

import com.jpa.task.domain.entity.Board;
import com.jpa.task.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {
    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    Board board;

    @BeforeEach
    void setup(){
        board = new Board();
        board.setTitle("test");
        board.setContent("test content");
    }

    @Test
    @DisplayName("저장")
    void write(){
        // given

        // when

        // then
    }
}
















