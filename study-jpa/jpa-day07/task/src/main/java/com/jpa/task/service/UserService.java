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

    public User join(User user) throws DuplicateUserException{
        checkDuplicateUser(user.getLoginId());
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Long login(String loginId, String password){
        return userRepository.findByLoginIdAndPassword(loginId, password)
                .orElseThrow(() -> new IllegalStateException("존재하지 않는 회원 정보입니다."));
    }

    /**
     * 중복된 아이디가 있다면 {@link DuplicateUserException} 발생됨!
     * @param loginId
     * @throws DuplicateUserException
     */
    @Transactional(readOnly = true)
    public void checkDuplicateUser(String loginId) throws DuplicateUserException{
        Optional<User> foundUser = userRepository.findByLoginId(loginId);

        if (foundUser.isPresent()){
            throw new DuplicateUserException("중복된 회원 아이디!");
        }
    }
}
















