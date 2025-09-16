package com.pix.light_stick.repository;

import com.pix.light_stick.domain.LightstickShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LightstickShareRepository extends JpaRepository<LightstickShare, Long> {
    boolean existsByCode(String code);
}
