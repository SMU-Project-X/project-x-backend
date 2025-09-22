package com.pix.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "my_idol_member")
@Getter
@Setter
@ToString
public class MyIdolMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idol_member_id")
    private Long idolMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "myidol_id", nullable = false)
    private MyIdolEntity myIdol;

    private String position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private MemberEntity memberInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "psycho_type_name")
    private psychoTypeEntity psychoType;

    private String mbti;
}
