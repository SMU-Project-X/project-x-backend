package com.pix.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    private String userId;

    @Column(nullable = false, length = 100)
    @JsonIgnore // 응답 JSON에 비밀번호 제외
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String nickname;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 10)
    @ColumnDefault("'남자'")
    private String gender;

    @Column(nullable = false)
    @Builder.Default
    private boolean isAdmin = false;

    private LocalDate birthday;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    // 엔티티 생성 시 자동 값 세팅
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    // 엔티티 수정 시 updatedAt 갱신
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
