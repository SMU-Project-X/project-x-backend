package com.pix.repository;

<<<<<<< HEAD

public interface MyIdolRepository {
=======
import org.springframework.data.jpa.repository.JpaRepository;

import com.pix.entity.MyIdolEntity;


public interface MyIdolRepository extends JpaRepository<MyIdolEntity, Long> {

	MyIdolEntity save(MyIdolEntity myIdol);
>>>>>>> cf79e1e3546d46130804e9de01d5e4c6a059d748

}
