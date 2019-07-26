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

package com.ecquaria.cloud.moh.iais.test.entity;

import lombok.Getter;
import lombok.Setter;
import sg.gov.moh.iais.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * Org User Account
 *
 * @author suocheng
 * @date 7/11/2019
 */
@Entity
@Table(name = "ORG_USER_ACCOUNT")
public class OrgUserAccount extends BaseEntity {
    private static final long serialVersionUID = 6984899936975396716L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    @Getter @Setter
    private Integer id;

    @Column(name = "ROWGUID", insertable = false)
    @Getter @Setter
    private String rowguid;

    @Column(name = "NAME")
    @Getter @Setter
    private String name;

    @Column(name = "NRIC_NO")
    @Getter @Setter
    private String nircNo;

    @Column(name="CORP_PASS_ID")
    @Getter @Setter
    private String corpPassId;

    @Column(name="STATUS")
    @Getter @Setter
    private String status;

    @Column(name="ORGANIZATION_ID")
    @Getter @Setter
    private Integer orgId;


    @ManyToOne(cascade = {CascadeType.REFRESH},optional = true)
    @JoinColumn(name="ORGANIZATION_ID",insertable = false,updatable = false)
    @Getter @Setter
    private Organization organization;

}
