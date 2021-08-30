package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ApplyGiroFromDelegateo
 *
 * @author junyu
 * @date 2021/3/8
 */
@Delegator("viewGIROArrangementsDelegator")
@Slf4j
public class ViewGIROArrangementsDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();
    FilterParameter giroAccountParameter = new FilterParameter.Builder()
            .clz(GiroAccountInfoQueryDto.class)
            .searchAttr("giroAcctSearch")
            .resultAttr("giroAcctResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private LicenceClient licenceClient;
    public void start(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"DashboardTitle","View GIRO Arrangements");
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        giroAccountParameter.setPageNo(1);
        giroAccountParameter.setPageSize(pageSize);
    }

    public void info(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String orgId = loginContext.getOrgId();
        filter.put("org_id", orgId);
        giroAccountParameter.setFilters(filter);
        SearchParam giroAccountParam = SearchResultHelper.getSearchParam(request, giroAccountParameter,true);
        CrudHelper.doPaging(giroAccountParam,bpc.request);
        String sortFieldName = ParamUtil.getString(request,"crud_action_value");
        String sortType = ParamUtil.getString(request,"crud_action_additional");
        String actionType=ParamUtil.getString(request,"crud_action_type");
        if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
            giroAccountParameter.setSortType(sortType);
            giroAccountParameter.setSortField(sortFieldName);
            //giroAccountParameter.setPageNo(1);
        }
        if("back".equals(actionType)){
            giroAccountParam= (SearchParam) ParamUtil.getSessionAttr(request,"searchGiroAccountParam");
        }
        QueryHelp.setMainSql("giroPayee","searchByGiroAcctInfo",giroAccountParam);
        SearchResult<GiroAccountInfoQueryDto> giroAccountResult = licenceClient.searchGiroInfoByParam(giroAccountParam).getEntity();
        if(giroAccountResult.getRowCount()!=0){
            SearchResult<GiroAccountInfoViewDto> searchGiroDtoResult=new SearchResult<>();
            searchGiroDtoResult.setRowCount(giroAccountResult.getRowCount());
            List<GiroAccountInfoViewDto> giroAccountInfoViewDtos= IaisCommonUtils.genNewArrayList();
            for (GiroAccountInfoQueryDto gai:
                    giroAccountResult.getRows()) {
                GiroAccountInfoViewDto giroAccountInfoViewDto=new GiroAccountInfoViewDto();
                giroAccountInfoViewDto.setAcctName(gai.getAcctName());
                giroAccountInfoViewDto.setAcctNo(gai.getAcctNo());
                giroAccountInfoViewDto.setBankCode(gai.getBankCode());
                giroAccountInfoViewDto.setBankName(gai.getBankName());
                giroAccountInfoViewDto.setBranchCode(gai.getBranchCode());
                giroAccountInfoViewDto.setLicenceNo(gai.getLicenceNo());
                giroAccountInfoViewDto.setLicenseeName(gai.getLicenseeName());
                giroAccountInfoViewDto.setSvcName(gai.getSvcName());
                giroAccountInfoViewDto.setUen(gai.getUen());
                giroAccountInfoViewDto.setId(gai.getId());
                giroAccountInfoViewDto.setCustomerReferenceNo(gai.getCustomerReferenceNo());
                giroAccountInfoViewDto.setRemarks(gai.getInternetRemarks());
                giroAccountInfoViewDtos.add(giroAccountInfoViewDto);
            }
            searchGiroDtoResult.setRows(giroAccountInfoViewDtos);
            ParamUtil.setSessionAttr(request,"searchGiroAccountParam",giroAccountParam);
            ParamUtil.setRequestAttr(request,"searchGiroDtoResult",searchGiroDtoResult);

        }
    }

    public void sort(BaseProcessClass bpc){
// 		sort->OnStepProcess
    }
}
