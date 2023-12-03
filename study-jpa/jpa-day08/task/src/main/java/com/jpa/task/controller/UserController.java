package com.jpa.task.controller;

import com.jpa.task.domain.dto.UserDTO;
import com.jpa.task.domain.embedded.Address;
import com.jpa.task.domain.entity.User;
import com.jpa.task.exception.DuplicateUserException;
import com.jpa.task.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public RedirectView join(UserDTO userDTO){
        String url = "/user/login";

        User user = new User();
        user.setLoginId(userDTO.getLoginId());
        user.setPassword(userDTO.getPassword());
        user.setName(userDTO.getName());
        user.setGender(userDTO.getGender());
        user.setAddress(new Address(userDTO.getAddress(), userDTO.getAddressDetail(), userDTO.getZipcode()));

        try {
            userService.join(user);
        } catch (DuplicateUserException e) {
            url = "/user/join";
            e.printStackTrace();
        }

        return new RedirectView(url);
    }

    @GetMapping("/login")
    public void login(){}

    @PostMapping("/login")
    public RedirectView login(@RequestParam String userLoginId, @RequestParam String userPassword, HttpServletRequest req){
        Long userId = userService.login(userLoginId, userPassword);
        req.getSession().setAttribute("userId", userId);

        return new RedirectView("/board/list");
    }
}
