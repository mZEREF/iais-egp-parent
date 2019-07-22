/*
 * This file is generated by ECQ project skeleton automatically.
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

package com.ecquaria.cloud.moh.iais.test.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * DemoQuery
 *
 * @author Jinhua
 * @date 2019/7/19 13:57
 */
@Entity
public class DemoQuery {
    @Id
    @Column(name = "user_id")
    @Getter @Setter private int userId;
    @Column(name = "NRIC_NO")
    @Getter @Setter private String nuicNum;
    @Column(name = "UEN_NO")
    @Getter @Setter private String uenNo;
}
