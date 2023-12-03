package com.jpa.task.controller;

import com.jpa.task.domain.entity.User;
import com.jpa.task.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// JUNIT을 사용할 때 스프링 컨테이너의 일부 기능을 사용할 수 있는 확장기능
@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class) // 특정 컨트롤러와 관련된 빈만 컨테이너에 등록
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void join() throws Exception{
//        given
        doReturn(new User()).when(userService).join(any(User.class));
//        when
        mockMvc.perform(
                post("/user/join")
                .param("loginId", "test")
                .param("password", "1234")
                .param("name", "test")
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/login")).andDo(print());
//        then
    }
}











