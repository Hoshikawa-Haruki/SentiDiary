/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.se.SentiDiary.Service;

import deu.se.SentiDiary.Entity.User;
import deu.se.SentiDiary.Repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Haruki
 */
@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // 전체 유저 조회
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(String userId) {
        log.info("[회원 탈퇴 요청] userId={}", userId);
        userRepository.findById(userId).ifPresentOrElse(
                userRepository::delete,
                () -> {
                    throw new NoSuchElementException("존재하지 않는 사용자입니다.");
                }
        );
    }
}
