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

import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.MessageCode;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.sql.SqlMapLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.common.utils.MessageUtil;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.HashMap;
import java.util.Map;

/**
 * AppInitializer
 *
 * @author Jinhua
 * @date 2019/7/18 18:03
 */
@WebListener
@Slf4j
public class AppInitializer implements ServletContextListener {
    @Autowired
    private QueryDao dao;

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
        } catch (Exception ex) {
            log.error("Failed to initialize the application.", ex);
        }
        log.info("---------- Initialization done. ----------");
    }

    private void initMessages() {
        SearchParam param = new SearchParam(MessageCode.class);
        SearchResult<MessageCode> sr = dao.doQuery(param, "message", "retrieveAllMsg");
        if (sr.getRowCount() > 0) {
            Map<String, String> map = new HashMap<>();
            for (MessageCode mc : sr.getRows()) {
                map.put(mc.getCodeKey(), mc.getDescription());
            }
            MessageUtil.loadMessages(map);
        }
    }
}
