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

package com.ecquaria.cloud.moh.iais.initializer;

import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.sql.SqlMapLoader;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.mockito.mockpolicies.Slf4jMockPolicy;
import org.powermock.core.classloader.annotations.MockPolicy;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.doThrow;

/**
 * AppInitializerTest
 *
 * @author Jinhua
 * @date 2019/7/19 17:59
 */
@RunWith(PowerMockRunner.class)
@MockPolicy(Slf4jMockPolicy.class)
@PrepareForTest({AppInitializer.class, MasterCodeUtil.class, QueryHelp.class})
@SuppressStaticInitializationFor("com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil")
@PowerMockIgnore("javax.management.*")
public class AppInitializerTest {
    @Spy
    private AppInitializer init = new AppInitializer();

    @Mock
    private SqlMapLoader sml;

    @Mock
    private ServletContextEvent sce;

    @Before
    public void setup() throws Exception {
        PowerMockito.whenNew(SqlMapLoader.class).withNoArguments().thenReturn(sml);
        PowerMockito.mockStatic(QueryHelp.class);
    }

    @Test
    public void testContextDestroyed() {
        init.contextDestroyed(sce);
        assertNotNull(init);
    }

    @Test
    public void testContextInitialized() throws Exception {
        SearchResult<Map<String, String>> sr = new SearchResult<Map<String, String>>();
        List<Map<String, String>> list = IaisCommonUtils.genNewArrayList();
        Map<String, String> mc = IaisCommonUtils.genNewHashMap();
        mc.put("codeKey", "aaaa");
        mc.put("description", "bbbbbb");
        list.add(mc);
        sr.setRows(list);
        sr.setRowCount(1);
        doNothing().when(sml, "loadSqlMap");
        PowerMockito.mockStatic(MasterCodeUtil.class);
        doNothing().when(MasterCodeUtil.class, "refreshCache");
        init.contextInitialized(sce);
        assertNotNull(init);
    }

    @Test
    public void testContextInitializedExp() throws Exception {
        doThrow(new RuntimeException()).when(sml, "loadSqlMap");
        init.contextInitialized(sce);
        assertNotNull(init);
    }
}
