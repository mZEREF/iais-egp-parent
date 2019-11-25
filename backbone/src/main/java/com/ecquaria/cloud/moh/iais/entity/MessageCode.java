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

package com.ecquaria.cloud.moh.iais.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * MessageCode
 *
 * @author Jinhua
 * @date 2019/7/24 17:58
 */
public class MessageCode implements Serializable {
    private static final long serialVersionUID = 1971470358993800500L;

    @Getter @Setter private String msgId;
    @Getter @Setter private String codeKey;
    @Getter @Setter private String description;
}
