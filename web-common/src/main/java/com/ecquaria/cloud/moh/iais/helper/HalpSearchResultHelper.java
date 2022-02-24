package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserRoleAccessMatrixDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class HalpSearchResultHelper {
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
        LoginContext lc = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = lc.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        switch (searchClassName) {
            case "inboxMsg":
                searchParam = (SearchParam) ParamUtil.getSessionAttr(request, InboxConst.INBOX_PARAM);
                if (searchParam == null || isNew) {
                    searchParam = new SearchParam(InboxQueryDto.class.getName());
                    searchParam.setPageSize(defaultPageSize);
                    searchParam.setPageNo(1);
                    searchParam.setSort("created_dt", SearchParam.DESCENDING);
                    setMsgParamByField(searchParam,"hcsaServicesShow",HcsaServiceCacheHelper.controlServices(0,null,userRoleAccessMatrixDtos).getServiceCodes());
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
                    setAppParamByField(searchParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,null,userRoleAccessMatrixDtos).getServiceCodes());
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
                    setLicParamByField(searchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,null,userRoleAccessMatrixDtos).getServiceCodes());
                    ParamUtil.setSessionAttr(request,InboxConst.LIC_PARAM, searchParam);
                }
                break;
        }
        return searchParam;
    }
    public static void setMsgParamByField(SearchParam searchParam, String key, List<String> values){
            setParamByFieldOrSearch(searchParam,key,values,"inbox.service_codes");
    }
    public static void setAppParamByField(SearchParam searchParam, String key, List<String> values){
        setParamByFieldOrSearch(searchParam,key,values,"B.code");
    }
    public static void setLicParamByField(SearchParam searchParam,String key,List<String> values){
         if(IaisCommonUtils.isEmpty(values)){
             searchParam.addParam(key," ('None data') ");
             return;
         }
        setParamByField(searchParam,key,values);
    }

    public static void setParamByFieldOrSearch(SearchParam searchParam,String key,List<String> values,String compareField){
        if(IaisCommonUtils.isNotEmpty(values)){
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < values.size(); i++) {
                if(i>0){
                    sb.append(" or ");
                }
                sb.append(" CHARINDEX(").append(":").append(key).append(i).append(",").append(compareField).append(") >0");
                searchParam.addFilter(key + i, values.get(i));
            }
            sb.append(")");
            searchParam.addParam(key,sb.toString());
        }else {
            searchParam.addParam(key," 1 = 2 ");
        }
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
