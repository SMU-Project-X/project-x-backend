package com.pix.light_stick.repository;

import com.pix.light_stick.domain.LightstickShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LightstickShareRepository extends JpaRepository<LightstickShare, Long> {
    boolean existsByCode(String code);
    Optional<LightstickShare> findByCode(String code);
}
