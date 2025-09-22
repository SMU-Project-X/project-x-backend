package com.pix.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pix.entity.UsersEntity;

public interface UserRepository extends JpaRepository<UsersEntity, String>{

	// ===== 기존 메서드들 =====
	UsersEntity findByUserId(String userId);

	UsersEntity findByNameAndEmail(String name, String email);
	
	// ===== 🔥 새로 추가된 메서드들 =====
	
	/**
	 * 관리자 권한 사용자 수 조회
	 * @param isAdmin "1" 또는 "Y" 값
	 * @return 관리자 사용자 수
	 */
	long countByIsAdmin(String isAdmin);
	
	/**
	 * 이메일로 사용자 찾기 (이메일 중복 체크용)
	 * @param email 이메일 주소
	 * @return 해당 이메일을 가진 사용자
	 */
	UsersEntity findByEmail(String email);
	
	/**
	 * 이메일 존재 여부 확인
	 * @param email 이메일 주소
	 * @return 존재하면 true, 없으면 false
	 */
	boolean existsByEmail(String email);
	
	/**
	 * 사용자 ID 존재 여부 확인
	 * @param userId 사용자 ID
	 * @return 존재하면 true, 없으면 false
	 */
	boolean existsByUserId(String userId);
	
	/**
	 * 관리자가 아닌 일반 사용자 수 조회 (커스텀 쿼리)
	 * @return 일반 사용자 수
	 */
	@Query("SELECT COUNT(u) FROM UsersEntity u WHERE u.isAdmin != '1' OR u.isAdmin IS NULL")
	long countNormalUsers();
	
	/**
	 * 관리자 사용자 수 조회 (커스텀 쿼리)
	 * @return 관리자 사용자 수
	 */
	@Query("SELECT COUNT(u) FROM UsersEntity u WHERE u.isAdmin = '1'")
	long countAdminUsers();
}