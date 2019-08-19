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

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.MasterCodeView;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
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
    private static final String CACHE_NAME_CATE_MAP                     = "iaisMcCateMap";
    private static final String CACHE_NAME_FILTER                       = "iaisMcFilterMap";
    private static final String CACHE_NAME_CODE                         = "iaisMcCode";
    private static final String SEQUENCE                                = "sequence";
    private static final String WEBCOMMON                               = "webcommon";
    private static final String RETRIEVE_MASTER_CODES                     = "retrieveMasterCodes";
    //Code Categorys
    public static final String CATE_ID_NATIONALITY                      = "1";

    private static QueryDao queryDao = SpringContextHelper.getContext().getBean(QueryDao.class);

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
        SearchResult<MasterCodeView> sr = queryDao.doQuery(param,null);
        if (sr == null || sr.getRowCount() <= 0)
            return;

        Map<String, List<MasterCodeView>> cateMap = new LinkedHashMap<>();
        Map<String, List<MasterCodeView>> filterMap = new HashMap<>();
        List<MasterCodeView> list = sr.getRows();
        list.forEach(mc ->
            RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, mc.getCode(), mc.getCodeValue())
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
            opts.add(new SelectOption(m.getCode(), m.getCodeValue()))
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
            SearchResult<MasterCodeView> sr = queryDao.doQuery(param,null);
            if (sr.getRowCount() > 0) {
                MasterCodeView mc = sr.getRows().get(0);
                desc = mc.getCodeValue();
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
            opts.add(new SelectOption(m.getCode(), m.getCodeValue()))
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
            SearchResult<MasterCodeView> sr = queryDao.doQuery(param,null);
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m ->
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getCodeValue())
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
            SearchResult<MasterCodeView> sr = queryDao.doQuery(param,null);
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m ->
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getCodeValue())
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
        rch.set(CACHE_NAME_CODE, mc.getCode(), mc.getCodeValue());
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
