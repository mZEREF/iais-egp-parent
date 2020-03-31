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

package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * SqlHelper
 *
 * @author Jinhua
 * @date 2019/7/18 17:17
 */
@Slf4j
public class SqlHelper {

    /**
     * @description: The method to get in SQL with param
     *
     * @author: Jinhua on 2019/7/23 10:48
     * @param: [fieldName, size]
     * @return: java.lang.String
     */
    public static String constructInCondition(String fieldName, int size) {
        StringBuilder sBuilder = new StringBuilder();
        if (size > 0){
            sBuilder.append(" ((").append(fieldName).append(" in (:").append(fieldName).append(0);

            int counter = 1;
            for (int i = 1; i < size; i++) {
                if (counter < 900) {
                    sBuilder.append(", :").append(fieldName).append(i);
                    counter++;
                } else {
                    //split for every 900 elements
                    sBuilder.append(")) or (").append(fieldName).append(" in (:").append(fieldName).append(i);
                    counter = 0;
                }
            }

            sBuilder.append(")))");
        }else{
            sBuilder.append(" 1 = 2 ");
        }

        return sBuilder.toString();
    }


    public static String constructNotInCondition(String fieldName, int size) {
        StringBuilder sBuilder = new StringBuilder();
        if (size > 0){
            sBuilder.append(" ((").append(fieldName).append(" not in (:").append(fieldName).append(0);

            int counter = 1;
            for (int i = 1; i < size; i++) {
                if (counter < 900) {
                    sBuilder.append(", :").append(fieldName).append(i);
                    counter++;
                } else {
                    //split for every 900 elements
                    sBuilder.append(")) or (").append(fieldName).append(" not in (:").append(fieldName).append(i);
                    counter = 0;
                }
            }

            sBuilder.append(")))");
        }else{
            sBuilder.append(" 1 = 2 ");
        }

        return sBuilder.toString();
    }


    public static void builderInSql(SearchParam searchParam, String columnName, String paramName, List<String> values){
        if (searchParam == null || columnName == null|| paramName == null || IaisCommonUtils.isEmpty(values)){
            throw new IllegalArgumentException("Null parameter not allowed");
        }

        //<#if paramName??> and ${paramName} </#if>
        String inSql = SqlHelper.constructInCondition(columnName, values.size());
        searchParam.addParam(paramName, inSql);
        int indx = 0;
        for (String s : values){
            searchParam.addFilter(columnName + indx, s);
            indx++;
        }

    }

    public static void builderNotInSql(SearchParam searchParam, String columnName, String paramName, List<String> values){
        if (searchParam == null || columnName == null|| paramName == null || IaisCommonUtils.isEmpty(values)){
            throw new IllegalArgumentException("Null parameter not allowed");
        }

        //<#if paramName??> and ${paramName} </#if>
        String inSql = SqlHelper.constructNotInCondition(columnName, values.size());
        searchParam.addParam(paramName, inSql);
        int indx = 0;
        for (String s : values){
            searchParam.addFilter(columnName + indx, s);
            indx++;
        }

    }

    private SqlHelper() {throw new IllegalStateException("Utility class");}
}
