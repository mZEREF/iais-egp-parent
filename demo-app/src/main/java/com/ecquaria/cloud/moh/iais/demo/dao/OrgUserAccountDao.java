/*
 *   This file is generated by ECQ project skeleton automatically.
 *
 *   Copyright 2019-2049, Ecquaria Technologies Pte Ltd. All rights reserved.
 *
 *   No part of this material may be copied, reproduced, transmitted,
 *   stored in a retrieval system, reverse engineered, decompiled,
 *   disassembled, localised, ported, adapted, varied, modified, reused,
 *   customised or translated into any language in any form or by any means,
 *   electronic, mechanical, photocopying, recording or otherwise,
 *   without the prior written permission of Ecquaria Technologies Pte Ltd.
 */

package com.ecquaria.cloud.moh.iais.demo.dao;

import com.ecquaria.cloud.moh.iais.demo.entity.OrgUserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Org User Account Dao
 *
 * @author suocheng
 * @date 7/12/2019
 */
public interface OrgUserAccountDao extends JpaRepository<OrgUserAccount,Integer> {
    public OrgUserAccount findById(Object id);
    @Query(value = "select * from ORG_USER_ACCOUNT where NRIC_NO = :userIdNo",nativeQuery = true)
    public OrgUserAccount findByIdNo(@Param("userIdNo") String userIdNo);
}
