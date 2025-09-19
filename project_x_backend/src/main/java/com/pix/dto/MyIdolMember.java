package com.pix.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "my_idol_member")
public class MyIdolMember {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idol_member_id")
    private Long idolMemberId;

    @ManyToOne
    @JoinColumn(name = "myidol_id")
    private MyIdol myIdol;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private MemberInfo memberInfo;

    private String position;

	public Long getIdolMemberId() {
		return idolMemberId;
	}

	public void setIdolMemberId(Long idolMemberId) {
		this.idolMemberId = idolMemberId;
	}

	public MyIdol getMyIdol() {
		return myIdol;
	}

	public void setMyIdol(MyIdol myIdol) {
		this.myIdol = myIdol;
	}

	public MemberInfo getMemberInfo() {
		return memberInfo;
	}

	public void setMemberInfo(MemberInfo memberInfo) {
		this.memberInfo = memberInfo;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}   
}