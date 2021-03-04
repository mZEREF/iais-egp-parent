package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountFormDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.OrganizationPremisesViewQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchListDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.GiroAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * FeeAndPaymentGIROPayeeDelegator
 *
 * @author junyu
 * @date 2021/3/2
 */
@Delegator(value = "feeAndPaymentGIROPayeeDelegator")
@Slf4j
public class FeeAndPaymentGIROPayeeDelegator {
    @Autowired
    private GiroAccountService giroAccountService;

    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter giroAccountParameter = new FilterParameter.Builder()
            .clz(GiroAccountInfoQueryDto.class)
            .searchAttr("giroAcctSearch")
            .resultAttr("giroAcctResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter orgPremParameter = new FilterParameter.Builder()
            .clz(OrganizationPremisesViewQueryDto.class)
            .searchAttr("orgPremParam")
            .resultAttr("orgPremResult")
            .sortField("id").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(pageSize).build();
    @Autowired
    SystemParamConfig systemParamConfig;


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>start>>>>>>>>>>>>>>>>requestForInformation");
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_PAYMENT,  AuditTrailConsts.FUNCTION_ONLINE_PAYMENT);
    }
    public void info(BaseProcessClass bpc) {
        HttpServletRequest request=bpc.request;
        ParamUtil.setSessionAttr(request,"cusRefNo",null);
        ParamUtil.setSessionAttr(request,"hciCode",null);
        ParamUtil.setSessionAttr(request,"hciName",null);
        ParamUtil.setSessionAttr(request,"uenNo",null);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        orgPremParameter.setPageSize(pageSize);
        giroAccountParameter.setPageSize(pageSize);
    }
    public void prePayeeResult(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String hciCode= ParamUtil.getString(request,"hciCode");
        String cusRefNo =ParamUtil.getString(request,"cusRefNo");
        ParamUtil.setSessionAttr(request,"hciCode",hciCode);
        ParamUtil.setSessionAttr(request,"cusRefNo",cusRefNo);

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(hciCode!=null) {
            filter.put("hciCode", hciCode);
        }
        if(cusRefNo!=null) {
            filter.put("cusRefNo", cusRefNo);
        }
        giroAccountParameter.setFilters(filter);
        SearchParam giroAccountParam = SearchResultHelper.getSearchParam(request, giroAccountParameter,true);
        CrudHelper.doPaging(giroAccountParam,bpc.request);
        QueryHelp.setMainSql("giroPayee","searchByGiroAcctInfo",giroAccountParam);
        SearchResult<GiroAccountInfoQueryDto> giroAccountResult = giroAccountService.searchGiroInfoByParam(giroAccountParam);
        if(giroAccountResult.getRowCount()!=0){
            SearchResult<GiroAccountInfoViewDto> searchGiroDtoResult=new SearchResult<>();
            searchGiroDtoResult.setRowCount(giroAccountResult.getRowCount());
            List<GiroAccountInfoViewDto> giroAccountInfoViewDtos=IaisCommonUtils.genNewArrayList();
            for (GiroAccountInfoQueryDto gai:
            giroAccountResult.getRows()) {
                GiroAccountInfoViewDto giroAccountInfoViewDto=new GiroAccountInfoViewDto();
                List<GiroAccountFormDocDto> giroAccountFormDocDtoList=giroAccountService.findGiroAccountFormDocDtoListByAcctId(gai.getId());
                giroAccountInfoViewDto.setAcctName(gai.getAcctName());
                giroAccountInfoViewDto.setAcctNo(gai.getAcctNo());
                giroAccountInfoViewDto.setBankCode(gai.getBankCode());
                giroAccountInfoViewDto.setGiroAccountFormDocDtoList(giroAccountFormDocDtoList);
                giroAccountInfoViewDto.setBankName(gai.getBankName());
                giroAccountInfoViewDto.setBranchCode(gai.getBranchCode());
                giroAccountInfoViewDto.setHciCode(gai.getHciCode());
                giroAccountInfoViewDto.setHciName(gai.getHciName());
                giroAccountInfoViewDto.setId(gai.getId());
                giroAccountInfoViewDto.setCustomerReferenceNo(gai.getCustomerReferenceNo());
                giroAccountInfoViewDtos.add(giroAccountInfoViewDto);
            }
            searchGiroDtoResult.setRows(giroAccountInfoViewDtos);
            ParamUtil.setRequestAttr(request,"searchGiroAccountParam",giroAccountParam);
            ParamUtil.setRequestAttr(request,"searchGiroDtoResult",searchGiroDtoResult);

        }
    }
    public void deletePayee(BaseProcessClass bpc) {

    }
    public void reSearchPayee(BaseProcessClass bpc) {

    }
    public void preOrgResult(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String hciCode= ParamUtil.getString(request,"hciCode");
        String hciName =ParamUtil.getString(request,"hciName");
        String uenNo =ParamUtil.getString(request,"uenNo");
        ParamUtil.setSessionAttr(request,"hciCode",hciCode);
        ParamUtil.setSessionAttr(request,"hciName",hciName);
        ParamUtil.setSessionAttr(request,"uenNo",uenNo);

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(hciCode!=null) {
            filter.put("hciCode", hciCode);
        }
        if(hciName!=null) {
            filter.put("hciName", hciName);
        }
        if(uenNo!=null) {
            filter.put("uenNo", uenNo);
        }
        orgPremParameter.setFilters(filter);
        SearchParam orgPremParam = SearchResultHelper.getSearchParam(request, orgPremParameter,true);
        CrudHelper.doPaging(orgPremParam,bpc.request);
        QueryHelp.setMainSql("giroPayee","searchByOrgPremView",orgPremParam);
        SearchResult<OrganizationPremisesViewQueryDto> orgPremResult = giroAccountService.searchOrgPremByParam(orgPremParam);
        ParamUtil.setRequestAttr(request,"orgPremParam",orgPremParam);
        ParamUtil.setRequestAttr(request,"orgPremResult",orgPremResult);
    }
    public void reSearchOrg(BaseProcessClass bpc) {

    }
    public void doSelect(BaseProcessClass bpc) {

    }
    public void doBack(BaseProcessClass bpc) {

    }
    public void refill(BaseProcessClass bpc) {

    }
    public void doValidate(BaseProcessClass bpc) {

    }
    public void preView(BaseProcessClass bpc) {

    }
    public void doSubmit(BaseProcessClass bpc) {

    }
}
