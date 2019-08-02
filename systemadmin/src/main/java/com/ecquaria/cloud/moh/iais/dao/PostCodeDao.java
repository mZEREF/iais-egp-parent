package com.ecquaria.cloud.moh.iais.dao;

import com.ecquaria.cloud.moh.iais.entity.PostCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PostCodeDao extends JpaRepository<PostCode, Long> {
    @Query(value = "select * from SINGPOST_ADDRESS where POSTAL_CODE =:postCode",nativeQuery = true)
    public PostCode getPostCodeByCode(@Param("postCode") String postCode);
}
