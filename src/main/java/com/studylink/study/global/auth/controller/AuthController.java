package com.studylink.study.global.auth.controller;

import com.studylink.study.domain.user.entity.User;
import com.studylink.study.global.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // --- 안내 GET 엔드포인트 ---
    @GetMapping("/signup")
    public String signupInfo() {
        return "일반 회원가입 API입니다. POST 요청과 JSON Body를 사용하세요.";
    }

    @GetMapping("/signup/admin")
    public String signupAdminInfo() {
        return "관리자 회원가입 API입니다. POST 요청과 JSON Body를 사용하세요.";
    }

    // 일반 사용자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setName(request.getName());
            user.setEmail(request.getEmail());

            authService.signupUser(user);
            return ResponseEntity.ok("일반 회원가입 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 관리자 회원가입
    @PostMapping("/signup/admin")
    public ResponseEntity<?> signupAdmin(@RequestBody SignupRequest request) {
        try {
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(request.getPassword());
            user.setName(request.getName());
            user.setEmail(request.getEmail());

            authService.signupAdmin(user);
            return ResponseEntity.ok("관리자 회원가입 성공");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}

class SignupRequest {
    private String username;
    private String password;
    private String name;
    private String email;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
class JwtResponse {
    private String token;

    public JwtResponse(String token) { this.token = token; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
