package com.jpa.task.service;

import com.jpa.task.domain.entity.User;
import com.jpa.task.exception.DuplicateUserException;
import com.jpa.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

//    회원가입
    public User join(User user) throws DuplicateUserException{
        checkDuplicateUser(user.getLoginId());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Long login (String loginId, String password){
        return userRepository.findByLoginIdAndPassword(loginId, password)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원 정보입니다."));
    }


    @Transactional(readOnly = true)
    public void checkDuplicateUser(String loginId) throws DuplicateUserException {
        Optional<User> foundUser = userRepository.findByLoginId(loginId);

        if (foundUser.isPresent()){
            throw new DuplicateUserException("중복된 회원 아이디!");
        }
    }

    @Transactional(readOnly = true)
    public User findUser(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원 번호"));
    }
}
