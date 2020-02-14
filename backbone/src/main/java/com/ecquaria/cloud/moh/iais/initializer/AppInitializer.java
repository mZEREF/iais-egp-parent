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

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.ErrorMsgClient;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MessageCode;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamCacheHelper;
import com.ecquaria.cloud.moh.iais.sql.SqlMapLoader;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import lombok.extern.slf4j.Slf4j;

/**
 * AppInitializer
 *
 * @author Jinhua
 * @date 2019/7/18 18:03
 */
@WebListener
@Slf4j
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Nothing to do
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("---------- Application is initializing... ----------");
        try {
            SqlMapLoader sqlMapLoader = new SqlMapLoader();
            sqlMapLoader.loadSqlMap();
            initMessages();
            MasterCodeUtil.refreshCache();
            HcsaServiceCacheHelper.receiveServiceMapping();
            SystemParamCacheHelper.receiveAllSystemParam();
        } catch (Exception ex) {
            log.error("Failed to initialize the application.", ex);
        }
        log.info("---------- Initialization done. ----------");
    }

    private void initMessages() {
        SearchParam param = new SearchParam(MessageCode.class.getName());
        QueryHelp.setMainSql("initializer", "retrieveAllMsg", param);
        ErrorMsgClient client = SpringContextHelper.getContext().getBean(ErrorMsgClient.class);
        SearchResult<MessageCode> sr = client.retrieveErrorMsgs(param).getEntity();

        if (sr.getRowCount() > 0) {
            Map<String, String> map = new HashMap<>();
            for (MessageCode mc : sr.getRows()) {
                map.put(mc.getCodeKey(), mc.getDescription());
            }
            MessageUtil.loadMessages(map);
        }
    }
}
