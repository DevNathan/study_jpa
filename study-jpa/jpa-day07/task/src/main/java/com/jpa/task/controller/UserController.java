package com.jpa.task.controller;

import com.jpa.task.domain.dto.UserDto;
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
    public RedirectView join(UserDto userDto){
        String url = "/user/login";

        User user = new User();
        user.setLoginId(userDto.getLoginId());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setGender(userDto.getGender());
        user.setAddress(new Address(userDto.getAddress(), userDto.getAddressDetail(), userDto.getZipcode()));

        System.out.println("user = " + user);

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
    public RedirectView login(@RequestParam("userLoginId") String userLoginId, @RequestParam("userPassword") String userPassword, HttpServletRequest req){
        Long userId = userService.login(userLoginId, userPassword);
        req.getSession().setAttribute("userId", userId);
        return new RedirectView("/board/list");
    }
}









