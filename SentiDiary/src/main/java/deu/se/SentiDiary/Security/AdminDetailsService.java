package deu.se.SentiDiary.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
/*
 * AdminDetailsService가 아이디를 받아 AdminDetails 객체를 생성
 *
 *
 */
@Service
public class AdminDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 관리자 계정 적용
        if ("admin".equals(username)) {
            return new AdminDetails("admin", "{noop}1234"); // {noop}은 비밀번호 인코딩 없음
        }
        throw new UsernameNotFoundException("관리자 계정이 존재하지 않습니다.");
    }
}
