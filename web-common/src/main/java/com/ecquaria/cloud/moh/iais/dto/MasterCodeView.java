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

import java.io.Serializable;
import java.util.Date;

/**
 * MasterCode
 *
 * @author Jinhua
 * @date 2019/7/25 16:49
 */
public class MasterCodeView implements Serializable {
    private static final long serialVersionUID = -7118511997100636148L;

    @Getter @Setter private int masterCodeId;
    @Getter @Setter private String code;
    @Getter @Setter private String codeValue;
    @Getter @Setter private String category;
    @Getter @Setter private String description;
    @Getter @Setter private String filterValue;
    @Getter @Setter private int sequence;
    @Getter @Setter private int status;
    @Getter @Setter private int version;
    @Getter @Setter private Date effectFrom;
    @Getter @Setter private Date effectTo;
}
