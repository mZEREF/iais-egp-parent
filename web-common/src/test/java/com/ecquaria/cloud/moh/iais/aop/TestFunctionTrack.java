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

package com.ecquaria.cloud.moh.iais.aop;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.annotation.LogInfo;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;

/**
 * Class TestFunctionTrack
 *
 * @author Jinhua
 * @date 2019/7/4 14:25
 */

public class TestFunctionTrack {
    @LogInfo(funcName="testSearch", moduleName="modaa")
    @SearchTrack(catalog = "aaa", key = "bbb")
    public void searchForSomething(SearchParam param) {

    }
}
