package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * OnlineTopEnquiryDelegator
 *
 * @author junyu
 * @date 2022/5/5
 */
@Delegator(value = "labDevelopedTestsEnquiryDelegator")
@Slf4j
public class OnlineTopEnquiryDelegator {

    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter topParameter = new FilterParameter.Builder()
            .clz(DsTopEnquiryResultsDto.class)
            .searchAttr("topParam")
            .resultAttr("topResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;
    @Autowired
    private AssistedReproductionClient assistedReproductionClient;

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_DS);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        topParameter.setPageSize(pageSize);
        topParameter.setPageNo(1);
        topParameter.setSortField("ID");
        topParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"dsEnquiryTopFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "topParam",null);

    }

    public void preSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "topParam");
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions("null");
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);


        if(!"back".equals(back)||searchParam==null){
            DsTopEnquiryFilterDto arDto=setDsTopEnquiryFilterDto(request);

            Map<String,Object> filter= IaisCommonUtils.genNewHashMap();

            

            topParameter.setFilters(filter);

            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                topParameter.setSortType(sortType);
                topParameter.setSortField(sortFieldName);
            }

            SearchParam topParam = SearchResultHelper.getSearchParam(request, topParameter,true);
            if(topParam.getSortMap().containsKey("SAMPLE_TYPE_DESC")){
                HalpSearchResultHelper.setMasterCodeForSearchParam(topParam,"SAMPLE_TYPE", "SAMPLE_TYPE_DESC", MasterCodeUtil.AR_DONOR_SAMPLE_TYPE);
            }
            if(searchParam!=null){
                topParam.setPageNo(searchParam.getPageNo());
                topParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(topParam,bpc.request);

            QueryHelp.setMainSql("onlineEnquiry","searchtopByAssistedReproduction",topParam);
            SearchResult<DsTopEnquiryResultsDto> topResult = assistedReproductionService.searchDsTopByParam(topParam);
            ParamUtil.setRequestAttr(request,"topResult",topResult);
            ParamUtil.setSessionAttr(request,"topParam",topParam);
        }else {
            SearchResult<DsTopEnquiryResultsDto> topResult = assistedReproductionService.searchDsTopByParam(searchParam);
            ParamUtil.setRequestAttr(request,"topResult",topResult);
            ParamUtil.setSessionAttr(request,"topParam",searchParam);
        }
    }

    private DsTopEnquiryFilterDto setDsTopEnquiryFilterDto(HttpServletRequest request) {
        return new DsTopEnquiryFilterDto();
    }

    public void nextStep(BaseProcessClass bpc){

    }

    public void perTopInfo(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        String submissionNo = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);


        TopSuperDataSubmissionDto topInfo = assistedReproductionClient.getTopSuperDataSubmissionDto(submissionNo).getEntity();
        List<PremisesDto> premisesDtos=assistedReproductionClient.getAllArCenterPremisesDtoByPatientCode("null","null").getEntity();
        Map<String, PremisesDto> premisesMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(premisesDtos)){
            for (PremisesDto premisesDto : premisesDtos) {
                if(premisesDto!=null){
                    premisesMap.put(premisesDto.getHciCode(), premisesDto);
                }
            }
        }

        Map<String, String> map = IaisCommonUtils.genNewLinkedHashMap();
        if (!premisesMap.isEmpty()) {
            for (Map.Entry<String, PremisesDto> entry : premisesMap.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getPremiseLabel());
            }
        }

        ParamUtil.setRequestAttr(request,"topInfoDataSubmissionDto",topInfo);
    }
}
