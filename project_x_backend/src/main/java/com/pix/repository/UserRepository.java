package com.pix.repository;

import com.pix.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자명으로 사용자 조회
     */
    Optional<User> findByUsername(String username);

    /**
     * 이메일로 사용자 조회
     */
    Optional<User> findByEmail(String email);

    /**
     * 사용자명 중복 체크
     */
    boolean existsByUsername(String username);

    /**
     * 이메일 중복 체크
     */
    boolean existsByEmail(String email);

    /**
     * 사용자명 또는 이메일로 사용자 조회 (로그인용)
     */
    @Query("SELECT u FROM User u WHERE u.username = :identifier OR u.email = :identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * 관리자 사용자 목록 조회
     */
    @Query("SELECT u FROM User u WHERE u.isAdmin = true")
    Optional<User> findAllAdmins();

    /**
     * 일반 사용자 개수 조회 (통계용)
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isAdmin = false")
    long countNormalUsers();

    /**
     * 관리자 개수 조회 (통계용)
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.isAdmin = true")
    long countAdminUsers();
}