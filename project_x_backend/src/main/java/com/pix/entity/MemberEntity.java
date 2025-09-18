package com.pix.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "member_info")
public class MemberEntity {
	// 멤버이름, 멤버 아이디, 이미지 정보, 성격

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")         // DB 컬럼명과 매핑
    private Long memberId;				// 자바 필드명 -> JSON 키는 memberId

    @Column(name = "NAME")
    private String memberName;

    @Column(name = "AGE")
    private Integer age;

    @Column(name = "BIRTHDATE")
    private LocalDate birthdate;

    @Column(name = "CONSTELLATION")
    private String constellation;

    @Column(name = "PROFILE_IMAGE_URL")
    private String profileImageUrl;

    @Column(name = "HEIGHT")
    private Integer height;

    @Column(name = "APPEARANCE")
    private String appearance;

    
}
