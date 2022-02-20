package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.privilege.PrivilegeConsts;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.privilege.Privilege;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

public class HalpSearchResultHelper {

    public final static List<String> allDsTypes = Arrays.asList(DataSubmissionConsts.DS_AR,DataSubmissionConsts.DS_DRP,
            DataSubmissionConsts.DS_LDT,DataSubmissionConsts.DS_TOP,DataSubmissionConsts.DS_VSS);

    public static SearchParam getSearchParam(HttpServletRequest request, String searchClassName) {
        return getSearchParam(request, searchClassName,false);
    }



    public static SearchParam gainSearchParam(HttpServletRequest request,String paramName,String searchClassName,String sortField,String sortRule,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, paramName);
        if(searchParam == null || isNew){
            searchParam = new SearchParam(searchClassName);
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            searchParam.setPageNo(1);
            searchParam.setSort(sortField, sortRule);
            ParamUtil.setSessionAttr(request,paramName, searchParam);
        }
        return searchParam;
    }

    public static SearchParam getSearchParam(HttpServletRequest request,String searchClassName,boolean isNew) {
        int defaultPageSize = SystemParamUtil.getDefaultPageSize();
        SearchParam searchParam = null;
        switch (searchClassName) {
            case "inboxMsg":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.INBOX_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxQueryDto.class.getName());
                    searchParam.setPageSize(defaultPageSize);
                    searchParam.setPageNo(1);
                    searchParam.setSort("created_dt", SearchParam.DESCENDING);
                    initMsgControlSearchParam(searchParam,request);
                    ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, searchParam);
                }
                break;
            case "inboxApp":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.APP_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxAppQueryDto.class.getName());
                    searchParam.setPageNo(1);
                    searchParam.setPageSize(defaultPageSize);
                    searchParam.setSort("created_dt", SearchParam.DESCENDING);
                    ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, searchParam);
                }
                break;
            case "inboxLic":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.LIC_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxLicenceQueryDto.class.getName());
                    searchParam.setSort("START_DATE", SearchParam.DESCENDING);
                    searchParam.setPageNo(1);
                    searchParam.setPageSize(defaultPageSize);
                    ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, searchParam);
                }
                break;
        }
        return searchParam;
    }

    public static void doSort(HttpServletRequest request,SearchParam searchParam){
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            searchParam.setSort(sortFieldName,sortType);
        }
    }

    public static void doSearch(SearchParam searchParam,Map<String,Object> SearchMap){
        if (SearchMap != null && SearchMap.size()>0){
            for (Map.Entry<String, Object> entry : SearchMap.entrySet()) {
                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
                searchParam.addFilter(mapKey, mapValue,true);
            }
        }
    }

    public static void doPage(HttpServletRequest request,SearchParam searchParam){
        String pageNo = ParamUtil.getString(request, "pageJumpNoTextchangePage");
        String pageSize = ParamUtil.getString(request, "pageJumpNoPageSize");
        if (!StringUtil.isEmpty(pageNo)) {
            searchParam.setPageNo(Integer.parseInt(pageNo));
        }
        if (!StringUtil.isEmpty(pageSize)) {
            searchParam.setPageSize(Integer.parseInt(pageSize));
        }
    }

    public static void setMasterCodeForSearchParam(SearchParam searchParam,String key,String value, String cateId){
            searchParam.setMasterCode(new MasterCodePair(key, value,
                    MasterCodeUtil.retrieveOptionsByCate(cateId)));
    }

    public static void initMsgControlSearchParam(SearchParam searchParam,HttpServletRequest request){
        List<String> privilegeIds = AccessUtil.getLoginUser(request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
        if(privilegeIds.contains(PrivilegeConsts.USER_PRIVILEGE_HALP_HCSA_DASHBOARD)){
            List<String> dsTypes = getDsTypes(privilegeIds);
            if(dsTypes.size() < allDsTypes.size()){
                 List<String> allTypes = new ArrayList<>(allDsTypes);
                 allTypes.removeAll(dsTypes);
                setParamByField(searchParam,"interServiceHcsaShow",allTypes);
            }
        }else {
            setParamByField(searchParam,"interServiceDsShow", getDsTypes(privilegeIds));
        }
    }

    public static List<String> getDsTypes(List<String> privilegeIds){
        if(IaisCommonUtils.isEmpty(privilegeIds)){
            return null;
        }
        List<String> types = IaisCommonUtils.genNewArrayList(5);
        privilegeIds.stream().forEach(privilegeId ->{
            switch(privilegeId){
                case PrivilegeConsts.USER_PRIVILEGE_DS_AR :
                    types.add(DataSubmissionConsts.DS_AR);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_DP :
                    types.add(DataSubmissionConsts.DS_DRP);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_TOP:
                    types.add(DataSubmissionConsts.DS_TOP);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_VSS:
                    types.add(DataSubmissionConsts.DS_VSS);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_LDT:
                    types.add(DataSubmissionConsts.DS_LDT);
                    break;
                default: break;
            }
        });
        return types;
    }

    public static void setParamByField(SearchParam searchParam,String key,List<String> values){
        if(IaisCommonUtils.isNotEmpty(values)){
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < values.size(); i++) {
                sb.append(":").append(key)
                        .append(i)
                        .append(',');
                searchParam.addFilter(key + i, values.get(i));
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam(key, inSql);
        }else {
            searchParam.removeFilter(key);
        }
    }
}
