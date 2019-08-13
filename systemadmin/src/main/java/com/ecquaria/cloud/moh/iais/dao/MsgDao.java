package com.ecquaria.cloud.moh.iais.dao;

/*
 *File Name: MsgDao
 *Creator: yichen
 *Creation time:2019/8/2 10:44
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MsgDao extends JpaRepository<Message,Integer> {

    Message findByRowguid(String rowguid);

    Message findById(Object id);
}
