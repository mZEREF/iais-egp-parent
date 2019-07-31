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

package sop.webflow.rt.java.code;

import com.ecquaria.cloud.helper.EngineHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * IAIS___OrgUserAccount___1Test
 *
 * @author suocheng
 * @date 7/30/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({IAIS___OrgUserAccount___1.class,EngineHelper.class})
public class IAIS___OrgUserAccount___1Test {
    @InjectMocks
    private IAIS___OrgUserAccount___1 orgUserAccount___1;

    @Test
    public void testMethod() throws Exception {
        PowerMockito.mockStatic(EngineHelper.class);
        orgUserAccount___1.prepareData_OnStepProcess_0();
        orgUserAccount___1.prepareSwitch_OnStepProcess_0();
        orgUserAccount___1.doSearch_OnStepProcess_0();
        orgUserAccount___1.doSorting_OnStepProcess_0();
        orgUserAccount___1.doPaging_OnStepProcess_0();
        orgUserAccount___1.doDelete_OnStepProcess_0();
        orgUserAccount___1.start_OnStepProcess_0();
        orgUserAccount___1.prepareCreate_OnStepProcess_0();
        orgUserAccount___1.doCreate_OnStepProcess_0();
        orgUserAccount___1.prepareEdit_OnStepProcess_0();
        orgUserAccount___1.doEdit_OnStepProcess_0();
        Assert.assertTrue(true);
    }

}
