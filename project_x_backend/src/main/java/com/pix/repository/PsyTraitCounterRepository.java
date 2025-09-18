package com.pix.repository;

import com.pix.domain.PsyTraitCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Oracle 환경에서 동시성 안전하게 +1 하기 위해 MERGE 사용
 * (다른 DB면 find/save로 변경)
 */
public interface PsyTraitCounterRepository extends JpaRepository<PsyTraitCounter, String> {

    @Modifying
    @Query(value = """
        MERGE INTO PSY_TRAIT_COUNTER t
        USING (SELECT ?1 AS TRAIT FROM dual) s
           ON (t.TRAIT = s.TRAIT)
        WHEN MATCHED THEN
          UPDATE SET t.CNT = t.CNT + 1
        WHEN NOT MATCHED THEN
          INSERT (TRAIT, CNT) VALUES (?1, 1)
        """, nativeQuery = true)
    int upsertInc(String trait);
}
