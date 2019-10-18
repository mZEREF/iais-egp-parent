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
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeView;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * MasterCodeUtil
 *
 * @author Jinhua
 * @date 2019/7/25 16:20
 */
@Slf4j
public final class MasterCodeUtil {
    //Cache names
    private static final String CACHE_NAME_CATE_MAP                = "iaisMcCateMap";
    private static final String CACHE_NAME_FILTER                  = "iaisMcFilterMap";
    private static final String CACHE_NAME_CODE                    = "iaisMcCode";
    private static final String SEQUENCE                           = "sequence";
    private static final String WEBCOMMON                          = "webcommon";
    private static final String RETRIEVE_MASTER_CODES              = "retrieveMasterCodes";
    private static final String MASTERCODE_CACHES                  = "system-admin-service:8886/iais-mastercode/caches";
    //Code Categorys
    public static final String CATE_ID_NATIONALITY                 = "8215D856-84AD-48F2-8590-1654DBAB1B40";
    public static final String CATE_ID_RISK_LEVEL                  = "33C073B6-5F62-4BE7-951A-06DBB881C127";
    public static final String CATE_ID_APP_TYPE                    = "09FF3DBF-8C6F-4509-9BAB-086F0F916F3A";
    public static final String CATE_ID_INSPEC_REQUIRED_TYPE        = "42CCE0B3-1D7E-40D5-BC2B-F31F34AC7BDF";
    public static final String CATE_ID_PRE_OR_POST_INSPEC          = "7E21590E-05AF-47D7-9EF8-F8AACAFDE181";
    public static final String CATE_ID_RISK_RATING                 = "FAF512A7-79D9-4DCE-AEBC-00A720DB5FB7";
    public static final String CATE_ID_INS_OR_RRA_SOURCE           = "94E95C79-ED51-4B93-97BE-2496044A837D";
    public static final String CATE_ID_LEAD_AND_GOVE_TAG           = "677C61D3-D4E5-4F91-80C7-1808147E6FFC";
    public static final String CATE_ID_APP_STATUS                  = "E8E722B5-7761-4EC0-AD06-E05ECA94F7F8";
    public static final String CATE_ID_ADDRESS_TYPE                = "D6FAD1CB-DF43-4CDE-AF97-45F88566D7EF";
    public static final String CATE_ID_ANSWER_TYPE                  = "14C0B134-1369-430B-B036-B1BF56A9DFBB";
    public static final String CATE_ID_COMMON_STATUS                  = "95FF02D9-2C34-4662-8AC1-FFC8A5288C1F";


    /**
     * @description: refresh the master codes into cache
     *
     * @author: Jinhua on 2019/7/26 11:04
     * @param: []
     * @return: void
     */
    public static void refreshCache() {
        SearchParam param = new SearchParam(MasterCodeView.class.getName());
        param.setSort(SEQUENCE, SearchParam.ASCENDING);
        QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
        SearchResult sr = RestApiUtil.query(MASTERCODE_CACHES, param);
        if (sr == null || sr.getRowCount() <= 0)
            return;

        List<MasterCodeView> list = new ArrayList<>();
        sr.getRows().forEach(obj -> {
            if (obj instanceof MasterCodeView) {
                list.add((MasterCodeView) obj);
            } else if (obj instanceof Map) {
                MasterCodeView mcv = MiscUtil.transferDtoFromMap((Map) obj, MasterCodeView.class);
                list.add(mcv);
            }
        });
        Map<String, List<MasterCodeView>> cateMap = new LinkedHashMap<>();
        Map<String, List<MasterCodeView>> filterMap = new HashMap<>();
        list.forEach(mc ->
            RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, mc.getCode(), mc.getDescription())
        );
        list.forEach(mc -> {
            String cateStr = String.valueOf(mc.getCategory());
            if (cateMap.get(cateStr) == null) {
                List<MasterCodeView> codes = new ArrayList<>();
                codes.add(mc);
                cateMap.put(cateStr, codes);
            } else {
                List<MasterCodeView> codes = cateMap.get(cateStr);
                codes.add(mc);
            }
        });
        saveInCache(CACHE_NAME_CATE_MAP, cateMap);
        list.forEach(mc -> {
            if (StringUtil.isEmpty(mc.getFilterValue())) {
                //Do nothing
            } else if (filterMap.get(mc.getFilterValue()) == null) {
                List<MasterCodeView> codes = new ArrayList<>();
                codes.add(mc);
                filterMap.put(mc.getFilterValue(), codes);
            } else {
                List<MasterCodeView> codes = filterMap.get(mc.getFilterValue());
                codes.add(mc);
            }
        });
        saveInCache(CACHE_NAME_FILTER, filterMap);
    }

    /**
     * @description: The method to get the category id by constant name
     *
     * @author: Jinhua on 2019/7/29 16:12
     * @param: [cateKey] -- CATE_ID_NATIONALITY
     * @return: java.lang.String -- 1
     */
    public static String getCategoryId(String cateKey){
        try {
            Field field = MasterCodeUtil.class.getDeclaredField(cateKey);
            return (String) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return cateKey;
        }
    }

    /**
     * @description: The method to retrieve Master codes by Category
     *
     * @author: Jinhua on 2019/7/29 17:42
     * @param: [cateId]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.dto.MasterCodeView>
     */
    public static List<MasterCodeView> retrieveByCategory(String cateId) {
        List<MasterCodeView> list = retrieveCateSource(cateId);
        List<MasterCodeView> mcList = new ArrayList<>();
        list.forEach(m ->
            mcList.add(MiscUtil.transferEntityDto(m, MasterCodeView.class))
        );

        return mcList;
    }

    /**
     * @description: The method to retrieve Select Options of Master codes by Category
     *
     * @author: Jinhua on 2019/7/29 17:44
     * @param: [cateId]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.tags.SelectOption>
     */
    public static List<SelectOption> retrieveOptionsByCate(String cateId) {
        List<MasterCodeView> list = retrieveCateSource(cateId);
        List<SelectOption> opts = new ArrayList<>();
        list.forEach(m ->
            opts.add(new SelectOption(m.getCode(), m.getDescription()))
        );

        return opts;
    }

    /**
     * @description: The method to get master code value by code
     *
     * @author: Jinhua on 2019/7/29 17:50
     * @param: [code]
     * @return: java.lang.String
     */
    public static String getCodeDesc(String code) {
        String desc = RedisCacheHelper.getInstance().get(CACHE_NAME_CODE, code);
        if (StringUtil.isEmpty(desc)) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.addFilter("codeFilter", code, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            SearchResult<MasterCodeView> sr = RestApiUtil.query(MASTERCODE_CACHES, param);
            if (sr.getRowCount() > 0) {
                MasterCodeView mc = sr.getRows().get(0);
                desc = mc.getDescription();
                addMcToCache(mc);
            } else {
                return "";
            }
        }

        return desc;
    }

    /**
     * @description: The method to retrieve Select Options of Master codes by Filter
     *
     * @author: Jinhua on 2019/7/29 17:51
     * @param: [filter]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.tags.SelectOption>
     */
    public static List<SelectOption> retrieveOptionsByFilter(String filter) {
        List<MasterCodeView> list = retrieveFilterSource(filter);
        List<SelectOption> opts = new ArrayList<>();
        list.forEach(m ->
            opts.add(new SelectOption(m.getCode(), m.getDescription()))
        );

        return opts;
    }

    /**
     * @description: The method to retrieve Master codes by Filter
     *
     * @author: Jinhua on 2019/7/29 17:53
     * @param: [filter]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.dto.MasterCodeView>
     */
    public static List<MasterCodeView> retrieveByFilter(String filter) {
        List<MasterCodeView> list = retrieveFilterSource(filter);
        List<MasterCodeView> mcList = new ArrayList<>();
        list.forEach(m ->
            mcList.add(MiscUtil.transferEntityDto(m, MasterCodeView.class))
        );

        return mcList;
    }

    /**
     * @description: The method to retrieve Select Options of Master codes by Master Codes
     *
     * @author: Jinhua on 2019/7/29 17:52
     * @param: [filter]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.tags.SelectOption>
     */
    public static List<SelectOption> retrieveOptionsByCodes(String[] codes) {
        List<SelectOption> opts = new ArrayList<>();
        if (codes == null)
            return opts;

        for (String c : codes) {
            opts.add(new SelectOption(c, getCodeDesc(c)));
        }

        return opts;
    }

    /******************************************************************************************************************
         Private methods
     ******************************************************************************************************************/
    private static List<MasterCodeView> retrieveCateSource(String cateId) {
        List<MasterCodeView> list = RedisCacheHelper.getInstance().get(CACHE_NAME_CATE_MAP, cateId);
        if (list == null) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.setSort(SEQUENCE, SearchParam.ASCENDING);
            param.addFilter("cateFilter", cateId, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            SearchResult<MasterCodeView> sr = RestApiUtil.query(MASTERCODE_CACHES, param);
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m ->
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getDescription())
                );
                RedisCacheHelper.getInstance().set(CACHE_NAME_CATE_MAP, cateId, list);
            } else {
                return new ArrayList<>();
            }
        }

        return list;
    }

    private static List<MasterCodeView> retrieveFilterSource(String filter) {
        List<MasterCodeView> list = RedisCacheHelper.getInstance().get(CACHE_NAME_FILTER, filter);
        if (list == null) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.setSort(SEQUENCE, SearchParam.ASCENDING);
            param.addFilter("filterAttr", filter, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            SearchResult<MasterCodeView> sr = RestApiUtil.query(MASTERCODE_CACHES, param);
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m ->
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getDescription())
                );
                RedisCacheHelper.getInstance().set(CACHE_NAME_FILTER, filter, list);
            } else {
                return new ArrayList<>();
            }
        }

        return list;
    }

    private static void saveInCache(String cacheName, Map<String, List<MasterCodeView>> conMap) {
        RedisCacheHelper rch = RedisCacheHelper.getInstance();
        rch.clear(cacheName);
        conMap.entrySet().forEach(ent ->
            rch.set(cacheName, ent.getKey(), ent.getValue())
        );
    }

    private static void addMcToCache(MasterCodeView mc) {
        RedisCacheHelper rch = RedisCacheHelper.getInstance();
        rch.set(CACHE_NAME_CODE, mc.getCode(), mc.getDescription());
        String cate = String.valueOf(mc.getCategory());
        List<MasterCodeView> list = rch.get(CACHE_NAME_CATE_MAP, cate);
        if (list == null)
            list = new ArrayList<>();

        list.add(mc);
        rch.set(CACHE_NAME_CATE_MAP, cate, list);
        if (StringUtil.isEmpty(mc.getFilterValue()))
            return;

        list = rch.get(CACHE_NAME_FILTER, mc.getFilterValue());
        if (list == null)
            list = new ArrayList<>();

        list.add(mc);
        rch.set(CACHE_NAME_FILTER, mc.getFilterValue(), list);
    }

    private MasterCodeUtil() {throw new IllegalStateException("Util class");}
}
