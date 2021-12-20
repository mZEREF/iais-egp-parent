package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleFilterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * OnlineEnquiryDonorSampleDelegator
 *
 * @author junyu
 * @date 2021/12/20
 */
@Delegator(value = "onlineEnquiryDonorSampleDelegator")
@Slf4j
public class OnlineEnquiryDonorSampleDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter donorSampleParameter = new FilterParameter.Builder()
            .clz(ArEnquiryDonorSampleDto.class)
            .searchAttr("donorSampleParam")
            .resultAttr("donorSampleResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;
    
    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        donorSampleParameter.setPageSize(pageSize);
        donorSampleParameter.setPageNo(1);
        ParamUtil.setSessionAttr(bpc.request,"arEnquiryDonorSampleFilterDto",null);

    }
    private ArEnquiryDonorSampleFilterDto setArEnquiryDonorSampleFilterDto(HttpServletRequest request)  {
        ArEnquiryDonorSampleFilterDto dsFilterDto=new ArEnquiryDonorSampleFilterDto();
        String arCentre=ParamUtil.getString(request,"arCentre");
        dsFilterDto.setArCentre(arCentre);
       
        ParamUtil.setSessionAttr(request,"arEnquiryDonorSampleFilterDto",dsFilterDto);

        return dsFilterDto;

    }
    public void preSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String back = (String) ParamUtil.getRequestAttr(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "donorSampleParam");

        if(!"back".equals(back)||searchParam==null){
            ArEnquiryDonorSampleFilterDto arDto=setArEnquiryDonorSampleFilterDto(request);

            Map<String,Object> filter= IaisCommonUtils.genNewHashMap();

            if(arDto.getArCentre()!=null) {
                filter.put("arCentre", arDto.getArCentre());
            }

            donorSampleParameter.setFilters(filter);

            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                donorSampleParameter.setSortType(sortType);
                donorSampleParameter.setSortField(sortFieldName);
            }

            SearchParam donorSampleParam = SearchResultHelper.getSearchParam(request, donorSampleParameter,true);
            CrudHelper.doPaging(donorSampleParam,bpc.request);

            QueryHelp.setMainSql("onlineEnquiry","searchDonorSampleByAssistedReproduction",donorSampleParam);
            SearchResult<ArEnquiryDonorSampleDto> donorSampleResult = assistedReproductionService.searchDonorSampleByParam(donorSampleParam);
            ParamUtil.setRequestAttr(request,"donorSampleResult",donorSampleResult);
            ParamUtil.setSessionAttr(request,"donorSampleParam",donorSampleParam);
        }else {
            SearchResult<ArEnquiryDonorSampleDto> donorSampleResult = assistedReproductionService.searchDonorSampleByParam(searchParam);
            ParamUtil.setRequestAttr(request,"donorSampleResult",donorSampleResult);
            ParamUtil.setSessionAttr(request,"donorSampleParam",searchParam);
        }


    }

    public void nextStep(BaseProcessClass bpc){

    }

    public void perDonorInfo(BaseProcessClass bpc){

    }

    public void backStep(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request,"back","back");
    }
}
