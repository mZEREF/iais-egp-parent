package com.ecquaria.cloud.moh.iais.helper;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * QueryHelp
 *
 * @author suocheng
 * @date 8/14/2019
 */
@Slf4j
public class QueryHelp {
    private QueryHelp() {
        throw new IllegalStateException("Utility class");
    }
    private static String getMainSql(String catalog, String key, SearchParam param) {
        String sql = null;
        try {
            sql = SqlMap.INSTANCE.getSql(catalog, key, param.getParams());
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(), e);
            throw new IaisRuntimeException(e);
        }

        return sql;
    }
    public static  void setMainSql(String catalog, String key, SearchParam param){
        String sql =getMainSql(catalog,key,param);
        param.setMainSql(sql);
    }
}
