package com.pix.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class MyIdol {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "myidol_id")
    private Long myidolId;

    @Column(name = "user_id")
    private Long userId;

    @OneToMany(mappedBy = "myIdol", fetch = FetchType.LAZY)
    private List<MyIdolMember> members;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
	public Long getMyidolId() {
		return myidolId;
	}
	public void setMyidolId(Long myidolId) {
		this.myidolId = myidolId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public List<MyIdolMember> getMembers() {
		return members;
	}
	public void setMembers(List<MyIdolMember> members) {
		this.members = members;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
    
    
}
