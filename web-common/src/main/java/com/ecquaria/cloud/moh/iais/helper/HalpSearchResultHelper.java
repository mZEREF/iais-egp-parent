package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.privilege.PrivilegeConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserRoleAccessMatrixDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.privilege.Privilege;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
                    //initMsgControlSearchParam(searchParam,request);
                    List<String> allTypes = HcsaServiceCacheHelper.controlServices(0,userRoleAccessMatrixDtos);
                    List<String> privilegeIds = AccessUtil.getLoginUser(request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
                    allTypes.addAll(getDsTypes(privilegeIds));
                    setMsgParamByField(searchParam,"hcsaServicesShow", allTypes);
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
                    setAppParamByField(searchParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,userRoleAccessMatrixDtos));
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
                    setLicParamByField(searchParam,"serviceTypesShow",HcsaServiceCacheHelper.controlServices(2,userRoleAccessMatrixDtos));
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
                sb.append(" CHARINDEX(").append(':').append(key).append(i).append(',').append(compareField).append(") >0");
                searchParam.addFilter(key + i, values.get(i));
            }
            sb.append(')');
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

    public static void setMasterCodeForSearchParam(SearchParam searchParam,String key,String value, String cateId){
            searchParam.setMasterCode(new MasterCodePair(key, value,
                    MasterCodeUtil.retrieveOptionsByCate(cateId)));
    }

    /*public static void initMsgControlSearchParam(SearchParam searchParam,HttpServletRequest request){
        InterMessageSearchDto interMessageSearchDto =  initInterMessageSearchDto(request);
        if(interMessageSearchDto.getSearchSql() == 1){
            setParamByField(searchParam,"interServiceDsShow",interMessageSearchDto.getServiceCodes());
        }else if(interMessageSearchDto.getSearchSql() == 2){
            setParamByField(searchParam,"interServiceHcsaShow",interMessageSearchDto.getServiceCodes());
        }
    }

    public static InterMessageSearchDto initInterMessageSearchDto(HttpServletRequest request){
        InterMessageSearchDto interMessageSearchDto = new InterMessageSearchDto();
        List<String> privilegeIds = AccessUtil.getLoginUser(request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
        if(privilegeIds.contains(PrivilegeConsts.USER_PRIVILEGE_HALP_HCSA_DASHBOARD)){
            List<String> dsTypes = getDsTypes(privilegeIds);
            if(dsTypes.size() < allDsTypes.size()){
                List<String> allTypes = new ArrayList<>(allDsTypes);
                allTypes.removeAll(dsTypes);
                interMessageSearchDto.setSearchSql(2);// not in
                interMessageSearchDto.setServiceCodes(allTypes);
            }
        }else {
            interMessageSearchDto.setSearchSql(1);// in
            interMessageSearchDto.setServiceCodes(getDsTypes(privilegeIds));
        }
        return interMessageSearchDto;
    }*/

    public static List<String> getDsTypes(List<String> privilegeIds) {
        if (IaisCommonUtils.isEmpty(privilegeIds)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> types = IaisCommonUtils.genNewArrayList(5);
        privilegeIds.forEach(privilegeId -> {
            switch (privilegeId) {
                case PrivilegeConsts.USER_PRIVILEGE_DS_AR:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_AR, types);
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_AR_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_DP:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_DRP, types);
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_DRP_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_TOP:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_TOP, types);
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_TOP_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_VSS:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_VSS, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_LDT:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_LDT, types);
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_LDT_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_AR_CRE:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_AR, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_DP_CRE:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_DRP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_TOP_CRE:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_TOP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_VSS_CRE:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_VSS, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_LDT_CRE:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_LDT, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_AR_RFC:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_AR_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_DP_RFC:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_DRP_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_TOP_RFC:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_TOP_SUP, types);
                    break;
                case PrivilegeConsts.USER_PRIVILEGE_DS_LDT_RFC:
                    IaisCommonUtils.addToList(DataSubmissionConsts.DS_LDT_SUP, types);
                    break;
                default:
                    break;
            }
        });
        return types;
    }

    public static void setParamByField(SearchParam searchParam,String key,List<String> values){
        if(IaisCommonUtils.isNotEmpty(values)){
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < values.size(); i++) {
                sb.append(':').append(key)
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

    public static InterMessageSearchDto initInterDssSearchDto(HttpServletRequest request,String licenseeId,String createdBy){
        InterMessageSearchDto interMessageSearchDto = new InterMessageSearchDto();
        interMessageSearchDto.setLicenseeId(licenseeId);
        interMessageSearchDto.setCreatBy(createdBy);
        interMessageSearchDto.setServiceCodes(getDsTypes(AccessUtil.getLoginUser(request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList())));

        return interMessageSearchDto;
    }

    /**
     * @param serviceCode
     * @return SERVICE_NAME
     * @description Get the value of SERVICE_NAME from SERVICE_CODE
     */
    public static String splitServiceName(String serviceCode){
        if(StringUtil.isEmpty(serviceCode) || StringUtil.isIn(serviceCode, HalpSearchResultHelper.allDsTypes)){
            return "N/A";
        }
        StringBuilder draftServiceName = new StringBuilder();
        String[] serviceName = serviceCode.split("@");
        for (int i=0;i<serviceName.length;i++){
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(serviceName[i]);
            if (hcsaServiceDto != null){
                if (i>0){
                    draftServiceName.append("<br/>")
                            .append(hcsaServiceDto.getSvcName());
                }else{
                    draftServiceName.append(hcsaServiceDto.getSvcName());
                }
            }
        }
        return draftServiceName.toString();
    }
}
