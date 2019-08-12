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

package com.ecquaria.cloud.moh.iais.querydao;

import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.common.exception.IaisRuntimeException;
import sg.gov.moh.iais.common.utils.StringUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * BaseQueryDao
 *
 * @author Jinhua
 * @date 2019/7/19 16:16
 */
@Component
@Slf4j
public class QueryDao<E extends Serializable> {
    @PersistenceContext
    private EntityManager entityManager;

    public SearchResult<E> doQuery(SearchParam param, String catalog, String key){
        String mainSql = getMainSql(catalog, key, param);
        log.debug("[QueryDao doQuery]   mainSql: ---->>> " + mainSql);
        return doQueryBySql(param, mainSql);
    }

    public  SearchResult<E> doQueryBySql(SearchParam param, String mainSql){
        String querySql = getQuerySql(mainSql, param);
        String countSql = getCountSql(mainSql);
        Query query = entityManager.createNativeQuery(querySql, param.getEntityCls());
        Query count = entityManager.createNativeQuery(countSql);
        for (Map.Entry<String, Serializable> ent : param.getFilters().entrySet()) {
            query.setParameter(ent.getKey(), ent.getValue());
            count.setParameter(ent.getKey(), ent.getValue());
        }
        List<E> list = query.getResultList();
        Integer num = list.size();
        if (param.getPageSize() > 0 && param.getPageNo() > 0)
            num = (Integer) count.getSingleResult();

        SearchResult<E> reslt = new SearchResult<>();
        reslt.setRows(list);
        reslt.setRowCount(num);

        return reslt;
    }

    private String getMainSql(String catalog, String key, SearchParam param) {
        String sql = null;
        try {
            sql = SqlMap.INSTANCE.getSql(catalog, key, param.getParams());
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return sql;
    }

    private String getQuerySql(String mainSql, SearchParam param) {
        String querySql;
        // order by clause
        String orderStr = "";
        if (!param.getSortMap().isEmpty()) {
            StringBuilder orderBySql = new StringBuilder();
            orderBySql.append(" ORDER BY ");
            for (Map.Entry<String, String> ent : param.getSortMap().entrySet()) {
                orderBySql.append(ent.getKey()).append(" ").append(ent.getValue()).append(",");
            }
            orderStr = orderBySql.substring(0, orderBySql.length() - 1);
        }
        // paging, it's only for SQL Server
        if (param.getPageSize() > 0 && param.getPageNo() > 0) {
            if (StringUtil.isEmpty(orderStr))
                throw new IaisRuntimeException("If used paging, there must be a default sorting column!!");

            int from = (param.getPageNo() - 1) * param.getPageSize() + 1;
            int to = param.getPageNo() * param.getPageSize();
            querySql = "SELECT * FROM (SELECT ROW_NUMBER() OVER(" + orderStr + ") AS ROWNUM, a.* FROM ("
                    + mainSql + ") a) b WHERE b.ROWNUM <= " + to + " AND b.ROWNUM >= " + from;
        } else {
            querySql = mainSql + orderStr;
        }

        return querySql;
    }

    private String getCountSql(String mainSql) {
        return "SELECT COUNT(*) FROM (" + mainSql + ") b";
    }
}
