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
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    //Code Categorys
    public static final String CATE_ID_NATIONALITY                 = "6B201379-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_RISK_LEVEL                  = "2CFD766C-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SERVICE_TYPE                = "0D675C87-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_APP_TYPE                    = "623F4561-1D0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_INSPEC_REQUIRED_TYPE        = "49E66DB0-1F0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_PRE_OR_POST_INSPEC          = "695F9F2B-200C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_RISK_RATING                 = "E9BAC83C-210C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_INS_OR_RRA_SOURCE           = "121589F8-210C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_LEAD_AND_GOVE_TAG           = "C1DD8781-220C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_APP_STATUS                  = "BEE661EE-220C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_ADDRESS_TYPE                = "19DD1192-080C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_ANSWER_TYPE                 = "35AA431C-270C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_COMMON_STATUS               = "8CEB1F80-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SALUTATION                  = "DFF3D597-730B-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_ID_TYPE                     = "73B14DAE-A87F-EA11-BE82-000C29F371DC";
    public static final String CATE_ID_RISK_WEIGHTAGE_MATRIX       = "199B036A-230C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DATE_TYPE                   = "1B1C8815-280C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DATE_RANGE                  = "38E90928-240C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_PAYMENT_STATUS              = "E2194D1C-0A0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_CHECKLIST_TYPE              = "47DD36ED-280C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_CHECKLIST_MODULE            = "9276F7F8-290C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SUB_TYPE_TYPE               = "E214989D-CF0C-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_MSG_TEMPLATE_TYPE           = "427AA14F-4A13-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DELIVERY_MODE               = "754889D0-5213-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_DESIGNATION                 = "136A214B-8611-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_PROFESSIONAL_TYPE           = "444ADC42-8A11-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_RECOMMENDATION_TYPE         = "35829FFC-ED17-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_PROCESSING_DECISION         = "B8A4E683-F517-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_INSPECTION_STATUS           = "D4EA93FF-511A-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_SERVICE_STEP                = "E498C55D-9723-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SERVICE_PERSONNEL_PSN_TYPE  = "D44B5FF6-5028-EA11-BE78-000C29D29DB0";
    public static final String CATE_ID_PROCESS_FILE_TRACK_STATUS   = "38902DD7-1E29-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_CONFIG_EFFECTIVE_DATE       = "A22973DF-2094-EA11-BE7A-000C29D29DB0";
    public static final String CATE_ID_APPLICATION_DRAFT_VALIDITY  = "A89CA599-179F-EA11-BE7A-000C29D29DB0";
    public static final String CATE_ID_LICENCE_STATUS              = "CF69FBD0-B37A-4DD9-9BD6-0A153ED55BF8";

    public static final String CATE_ID_SYSTEM_PARAMETER_MODULE          = "1D3F6F1A-5334-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SYSTEM_PARAMETER_TYPE            = "364C7AFB-5234-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_SYSTEM_PARAMETER_TYPE_OF_VALUE   = "D68BA451-5334-EA11-BE7D-000C29F371DC";
    public static final String CATE_ID_TEMPLATE_ROLE   = "2C594569-0FB9-EA11-BE84-000C29F371DC";

    public static final String CATE_ID_SYSTEM_RISK_TYPE  = "110534B1-4967-EA11-BE7F-000C29F371DC";
    public static final String CATE_ID_INBOx_MESSAGE_TYPE  = "D45AC717-945A-EA11-BE79-000C29D29DB0";
    public static final String CATE_ID_PERSONNEL_PSN_TYPE  = "6BC8C0B4-B182-EA11-BE82-000C29F371DC";
    public static final String CATE_ID_ERR_MSG_MODULE  = "87F2F7C5-ABC5-EA11-BE7A-000C29D29DB0";
    public static final String CATE_ID_ERR_MSG_TYPE  = "FA7B1B4F-ADC5-EA11-BE84-000C29F371DC";
    public static final String CATE_ID_WRONG_TYPE  = "02FC2CBE-ADC5-EA11-BE84-000C29F371DC";
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
        MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
        SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
        if (sr == null || sr.getRowCount() <= 0) {
            return;
        }
        List<MasterCodeView> list = IaisCommonUtils.genNewArrayList();
        sr.getRows().forEach(obj -> {
            list.add((MasterCodeView) obj);
        });
        Map<String, List<MasterCodeView>> cateMap = new LinkedHashMap<>();
        Map<String, List<MasterCodeView>> filterMap = IaisCommonUtils.genNewHashMap();
        list.forEach(mc ->
            RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, mc.getCode(), mc.getCodeValue(),
                    RedisCacheHelper.NOT_EXPIRE)
        );
        list.forEach(mc -> {
            String cateStr = String.valueOf(mc.getCategory());
            if (cateMap.get(cateStr) == null) {
                List<MasterCodeView> codes = IaisCommonUtils.genNewArrayList();
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
                List<MasterCodeView> codes = IaisCommonUtils.genNewArrayList();
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
        List<MasterCodeView> mcList = IaisCommonUtils.genNewArrayList();
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
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
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
        if (StringUtil.isEmpty(desc) && !StringUtil.isEmpty(code)) {
            SearchParam param = new SearchParam(MasterCodeView.class.getName());
            param.addFilter("codeFilter", code, true);
            QueryHelp.setMainSql(WEBCOMMON, RETRIEVE_MASTER_CODES, param);
            MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
            SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
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
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
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
        List<MasterCodeView> mcList = IaisCommonUtils.genNewArrayList();
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
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (codes == null) {
            return opts;
        }
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
            MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
            SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m ->
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getCodeValue(),
                            RedisCacheHelper.NOT_EXPIRE)
                );
                RedisCacheHelper.getInstance().set(CACHE_NAME_CATE_MAP, cateId, list, RedisCacheHelper.NOT_EXPIRE);
            } else {
                return IaisCommonUtils.genNewArrayList();
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
            MasterCodeClient client = SpringContextHelper.getContext().getBean(MasterCodeClient.class);
            SearchResult<MasterCodeView> sr = client.retrieveMasterCodes(param).getEntity();
            if (sr.getRowCount() > 0) {
                list = sr.getRows();
                list.forEach(m ->
                    RedisCacheHelper.getInstance().set(CACHE_NAME_CODE, m.getCode(), m.getCodeValue(),
                            RedisCacheHelper.NOT_EXPIRE)
                );
                RedisCacheHelper.getInstance().set(CACHE_NAME_FILTER, filter, list, RedisCacheHelper.NOT_EXPIRE);
            } else {
                return IaisCommonUtils.genNewArrayList();
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
        if (list == null) {
            list = IaisCommonUtils.genNewArrayList();
        }
        list.add(mc);
        rch.set(CACHE_NAME_CATE_MAP, cate, list);
        if (StringUtil.isEmpty(mc.getFilterValue())) {
            return;
        }
        list = rch.get(CACHE_NAME_FILTER, mc.getFilterValue());
        if (list == null) {
            list = IaisCommonUtils.genNewArrayList();
        }
        list.add(mc);
        rch.set(CACHE_NAME_FILTER, mc.getFilterValue(), list);
    }

    private MasterCodeUtil() {throw new IllegalStateException("Util class");}
}
