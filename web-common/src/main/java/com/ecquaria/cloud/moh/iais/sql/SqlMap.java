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

package com.ecquaria.cloud.moh.iais.sql;

import freemarker.cache.StringTemplateLoader;
import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.common.exception.IaisRuntimeException;
import sg.gov.moh.iais.common.utils.StringUtil;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SqlMap
 *
 * @author Jinhua
 * @date 2019/7/18 17:21
 */
@Slf4j
public class SqlMap {
    public static final SqlMap INSTANCE = new SqlMap();
    private Map<String, Sql> mapforSql;
    private static final Configuration cfg = new Configuration(Configuration.VERSION_2_3_28);

    public void initSqlMap(List<Sql> sqls) {
        StringTemplateLoader loader = new StringTemplateLoader();
        for (Sql sql : sqls) {
            String key = getKey(sql.getCatalog(), sql.getKey());
            mapforSql.put(key, sql);
            if (isDynamicSql(sql.getSqlStr())) {
                loader.putTemplate(key, sql.getSqlStr());
            }
        }
        cfg.setTemplateLoader(loader);
        cfg.setNewBuiltinClassResolver(TemplateClassResolver.SAFER_RESOLVER);
    }

    public Sql getSql(String catalog, String key) {
        String ck = getKey(catalog, key);
        Sql sql =  mapforSql.get(ck);
        if (sql == null) {
            String msg = String.format("The SQL [%s] of catalog [%s] is not found in the cache.", key, catalog);
            log.error(msg);
            throw new IaisRuntimeException(msg);
        }

        return sql;
    }

    public String getSql(String catalog, String key, Map<String, Serializable> params) throws IOException, TemplateException {
        Sql sql = getSql(catalog, key);
        String sqlStat = sql.getSqlStr();
        if (isDynamicSql(sqlStat)) {
            StringWriter writer = new StringWriter();
            Template temp = cfg.getTemplate(getKey(sql.getCatalog(), sql.getKey()));
            temp.process(params, writer);
            writer.flush();
            sqlStat = writer.toString();
        }
        return sqlStat;
    }

    private boolean isDynamicSql(String rawSqlStat) {
        return !StringUtil.isEmpty(rawSqlStat)
                && (rawSqlStat.indexOf("<#") != -1 && rawSqlStat.indexOf("</#") != -1
                || rawSqlStat.indexOf("${") != -1);
    }

    private String getKey(String catalog, String key) {
        return catalog + ".-salt-." + key;
    }

    private String getCacheKey(String catalog, String key) {
        return catalog + ".-salt-." + key;
    }

    public String getDynamicSqlStat(Sql sql, Map<String, Object> params) throws IOException, TemplateException {
        updateTemplate(sql);
        StringWriter writer = new StringWriter();
        Template temp = cfg.getTemplate(getCacheKey(sql.getCatalog(), sql.getKey()));
        temp.process(params, writer);
        writer.flush();
        return writer.toString();
    }

    private synchronized void updateTemplate(Sql sql) {
        if (!StringUtil.getBoolean(sql.getCached())) {
            String ck = getCacheKey(sql.getCatalog(), sql.getKey());
            StringTemplateLoader loader = (StringTemplateLoader) cfg.getTemplateLoader();
            if (loader.findTemplateSource(ck) == null) {
                loader.putTemplate(ck, sql.getSqlStr());
            }
        }
    }

    private SqlMap() {
        mapforSql = new HashMap<>();
    }
}
