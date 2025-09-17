package com.pix.service;

import com.pix.dto.UserDto;
import com.pix.entity.User;
import com.pix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto.LoginResponse login(UserDto.LoginRequest request) {
        log.info("로그인 시도: {}", request.getUsername());
        
        try {
            // 사용자명 또는 이메일로 사용자 조회
            User user = userRepository.findByUsernameOrEmail(request.getUsername())
                    .orElse(null);

            if (user == null) {
                log.warn("로그인 실패 - 사용자 없음: {}", request.getUsername());
                return UserDto.LoginResponse.builder()
                        .success(false)
                        .message("사용자를 찾을 수 없습니다.")
                        .build();
            }

            // 단순 문자열 비교 (실제 운영시에는 BCrypt 등 해시 사용 권장)
            if (!user.getPasswordHash().equals(request.getPassword())) {
                log.warn("로그인 실패 - 비밀번호 불일치: {}", request.getUsername());
                return UserDto.LoginResponse.builder()
                        .success(false)
                        .message("비밀번호가 일치하지 않습니다.")
                        .build();
            }

            log.info("로그인 성공: {}", user.getUsername());
            return UserDto.LoginResponse.builder()
                    .success(true)
                    .message("로그인 성공")
                    .user(convertToResponseDto(user))
                    .token("temp_token_" + user.getUserId()) // 임시 토큰
                    .build();

        } catch (Exception e) {
            log.error("로그인 처리 중 오류 발생", e);
            return UserDto.LoginResponse.builder()
                    .success(false)
                    .message("로그인 처리 중 오류가 발생했습니다.")
                    .build();
        }
    }

    @Override
    public UserDto.Response register(UserDto.RegisterRequest request) {
        log.info("회원가입 시도: {}", request.getUsername());

        // 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 사용자명입니다.");
        }

        if (request.getEmail() != null && userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        // User 엔티티 생성
        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(request.getPassword()) // 실제로는 BCrypt 해시 권장
                .email(request.getEmail())
                .name(request.getName())
                .nickname(request.getNickname())
                .age(request.getAge())
                .address(request.getAddress())
                .isAdmin(false) // 기본값: 일반 사용자
                .build();

        User savedUser = userRepository.save(user);
        log.info("회원가입 완료: {}", savedUser.getUsername());

        return convertToResponseDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto.Response getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));
        return convertToResponseDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto.Response getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. Username: " + username));
        return convertToResponseDto(user);
    }

    @Override
    public UserDto.Response updateUser(Long userId, UserDto.UpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId));

        // 수정 가능한 필드만 업데이트
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        if (request.getAge() != null) {
            user.setAge(request.getAge());
        }
        if (request.getAddress() != null) {
            user.setAddress(request.getAddress());
        }
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
            }
            user.setEmail(request.getEmail());
        }

        User updatedUser = userRepository.save(user);
        log.info("사용자 정보 수정 완료: {}", updatedUser.getUsername());

        return convertToResponseDto(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다. ID: " + userId);
        }
        userRepository.deleteById(userId);
        log.info("사용자 삭제 완료: ID {}", userId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto.Response> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto.UserStats getUserStats() {
        long totalUsers = userRepository.count();
        long normalUsers = userRepository.countNormalUsers();
        long adminUsers = userRepository.countAdminUsers();

        return new UserDto.UserStats(totalUsers, normalUsers, adminUsers);
    }

    // Entity → DTO 변환 메서드
    private UserDto.Response convertToResponseDto(User user) {
        return UserDto.Response.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .nickname(user.getNickname())
                .age(user.getAge())
                .address(user.getAddress())
                .isAdmin(user.getIsAdmin())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}