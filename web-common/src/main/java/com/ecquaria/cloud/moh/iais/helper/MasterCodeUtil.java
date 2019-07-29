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
import com.ecquaria.cloud.moh.iais.dto.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.querydao.QueryDao;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.StringUtil;

import java.util.*;

/**
 * MasterCodeUtil
 *
 * @author Jinhua
 * @date 2019/7/25 16:20
 */
@Slf4j
public final class MasterCodeUtil {
    private static final String CACHE_NAME_CATEGORY         = "iaisMcCate";
    private static final String CACHE_NAME_FILTER           = "iaisMcFilter";
    private static final String CACHE_NAME_CODE             = "iaisMcCode";

    private static QueryDao queryDao = SpringContextHelper.getContext().getBean(QueryDao.class);
    /**
     * @description: refresh the master codes into cache
     *
     * @author: Jinhua on 2019/7/26 11:04
     * @param: []
     * @return: void
     */
    public static void refreshCache() {
        SearchParam param = new SearchParam(MasterCodeDto.class);
        param.setSort("sequence", SearchParam.ASCENDING);
        SearchResult<MasterCodeDto> sr = queryDao.doQuery(param, "webcommon", "retrieveMasterCodes");
        if (sr == null || sr.getRowCount() <= 0)
            return;

        Map<String, List<MasterCodeDto>> cateMap = new LinkedHashMap<>();
        Map<String, List<MasterCodeDto>> filterMap = new HashMap<>();
        List<MasterCodeDto> list = sr.getRows();
        list.forEach(mc -> {
            RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, mc.getCode(), mc.getCodeValue());
        });
        list.forEach(mc -> {
            String cateStr = String.valueOf(mc.getCategory());
            if (cateMap.get(cateStr) == null) {
                List<MasterCodeDto> codes = new ArrayList<>();
                codes.add(mc);
                cateMap.put(cateStr, codes);
            } else {
                List<MasterCodeDto> codes = cateMap.get(cateStr);
                codes.add(mc);
            }
        });
        saveInCache(CACHE_NAME_CATEGORY, cateMap);
        list.forEach(mc -> {
            if (StringUtil.isEmpty(mc.getFilterValue())) {
                //Do nothing
            } else if (filterMap.get(mc.getFilterValue()) == null) {
                List<MasterCodeDto> codes = new ArrayList<>();
                codes.add(mc);
                filterMap.put(mc.getFilterValue(), codes);
            } else {
                List<MasterCodeDto> codes = filterMap.get(mc.getFilterValue());
                codes.add(mc);
            }
        });
        saveInCache(CACHE_NAME_FILTER, filterMap);
    }

    public static List<MasterCodeDto> retrieveByCategory(String cateId) {
        List<MasterCodeDto> list = retrieveCateSource(cateId);
        List<MasterCodeDto> mcList = new ArrayList<>();
        list.forEach(m -> {
            mcList.add(MiscUtil.transferEntityDto(m, MasterCodeDto.class));
        });

        return mcList;
    }

    public static List<SelectOption> retrieveOptionsByCate(String cateId) {
        List<MasterCodeDto> list = retrieveCateSource(cateId);
        List<SelectOption> opts = new ArrayList<>();
        list.forEach(m -> {
            opts.add(new SelectOption(m.getCode(), m.getCodeValue()));
        });

        return opts;
    }

    public static String getCodeDesc(String code) {
        String desc = RedisCacheHelper.getInstance().get(CACHE_NAME_CODE, code, String.class);
        if (StringUtil.isEmpty(desc)) {
            SearchParam param = new SearchParam(MasterCodeDto.class);
            param.addFilter("codeFilter", code, true);
            SearchResult<MasterCodeDto> sr = queryDao.doQuery(param, "webcommon", "retrieveMasterCodes");
            if (sr.getRowCount() > 0) {
                MasterCodeDto mc = sr.getRows().get(0);
                addMcToCache(mc);
            } else {
                return "";
            }
        }

        return desc;
    }

    private static List<MasterCodeDto> retrieveCateSource(String cateId) {
        String cate = String.valueOf(cateId);
        List<MasterCodeDto> list = RedisCacheHelper.getInstance().get(CACHE_NAME_CATEGORY, cate, List.class);
        if (list == null) {
            SearchParam param = new SearchParam(MasterCodeDto.class);
            param.setSort("sequence", SearchParam.ASCENDING);
            param.addFilter("cateFilter", cateId, true);
            SearchResult<MasterCodeDto> sr = queryDao.doQuery(param, "webcommon", "retrieveMasterCodes");
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m -> {
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getCodeValue());
                });
                RedisCacheHelper.getInstance().set(CACHE_NAME_CATEGORY, cate, list);
            } else {
                return new ArrayList<>();
            }
        }

        return list;
    }

    private static void saveInCache(String cacheName, Map<String, List<MasterCodeDto>> conMap) {
        RedisCacheHelper rch = RedisCacheHelper.getInstance();
        rch.clear(cacheName);
        conMap.entrySet().forEach(ent -> {
            rch.set(cacheName, ent.getKey(), ent.getValue());
        });
    }

    private static void addMcToCache(MasterCodeDto mc) {
        RedisCacheHelper rch = RedisCacheHelper.getInstance();
        rch.set(CACHE_NAME_CODE, mc.getCode(), mc.getCodeValue());
        String cate = String.valueOf(mc.getCategory());
        List<MasterCodeDto> list = rch.get(CACHE_NAME_CATEGORY, cate, List.class);
        if (list == null)
            list = new ArrayList<>();

        list.add(mc);
        rch.set(CACHE_NAME_CATEGORY, cate, list);
        if (StringUtil.isEmpty(mc.getFilterValue()))
            return;

        list = rch.get(CACHE_NAME_FILTER, mc.getFilterValue(), List.class);
        if (list == null)
            list = new ArrayList<>();

        list.add(mc);
        rch.set(CACHE_NAME_FILTER, mc.getFilterValue(), list);
    }

    private MasterCodeUtil() {throw new IllegalStateException("Util class");}
}
