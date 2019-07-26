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

package com.ecquaria.cloud.moh.iais.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * MasterCode
 *
 * @author Jinhua
 * @date 2019/7/25 16:49
 */
@Entity
public class MasterCodeDto implements Serializable {
    private static final long serialVersionUID = -7118511997100636148L;

    @Id
    @Column(name = "master_code_id")
    @Getter @Setter private int masterCodeId;
    @Column(name = "master_code_key")
    @Getter @Setter private String code;
    @Column(name = "code_value")
    @Getter @Setter private String codeValue;
    @Column(name = "code_category")
    @Getter @Setter private int category;
    @Column(name = "code_description")
    @Getter @Setter private int description;
    @Column(name = "filter_value")
    @Getter @Setter private String filterValue;
    @Column(name = "sequence")
    @Getter @Setter private int sequence;
    @Column(name = "status")
    @Getter @Setter private int status;
    @Column(name = "version")
    @Getter @Setter private int version;
    @Column(name = "effective_from")
    @Getter @Setter private Date effectFrom;
    @Column(name = "effective_to")
    @Getter @Setter private Date effectTo;
}
