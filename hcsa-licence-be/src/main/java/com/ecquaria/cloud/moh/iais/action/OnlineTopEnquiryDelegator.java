package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
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
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OnlineTopEnquiryDelegator
 *
 * @author junyu
 * @date 2022/5/5
 */
@Delegator(value = "mohTopOnlineEnquiry")
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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_TOP);
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

    public void preSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "topParam");
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions(DataSubmissionConsts.DS_TOP,"null");
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);


        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                topParameter.setSortType(sortType);
                topParameter.setSortField(sortFieldName);
            }
            DsTopEnquiryFilterDto topDto=setDsTopEnquiryFilterDto(request);

            setQueryFilter(topDto,topParameter);

            SearchParam topParam = SearchResultHelper.getSearchParam(request, topParameter,true);

            if(searchParam!=null){
                topParam.setPageNo(searchParam.getPageNo());
                topParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(topParam,bpc.request);
            QueryHelp.setMainSql("onlineEnquiry","searchByTop",topParam);
            SearchResult<DsTopEnquiryResultsDto> topResult = assistedReproductionService.searchDsTopByParam(topParam);
            ParamUtil.setRequestAttr(request,"topResult",topResult);
            ParamUtil.setSessionAttr(request,"topParam",topParam);
        }else {
            SearchResult<DsTopEnquiryResultsDto> topResult = assistedReproductionService.searchDsTopByParam(searchParam);
            ParamUtil.setRequestAttr(request,"topResult",topResult);
            ParamUtil.setSessionAttr(request,"topParam",searchParam);
        }
    }

    private DsTopEnquiryFilterDto setDsTopEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        DsTopEnquiryFilterDto filterDto=new DsTopEnquiryFilterDto();
        String centerName=ParamUtil.getString(request,"centerName");
        filterDto.setCenterName(centerName);
        String submissionNo=ParamUtil.getString(request,"submissionNo");
        filterDto.setSubmissionNo(submissionNo);
        String patientName=ParamUtil.getString(request,"patientName");
        filterDto.setPatientName(patientName);
        String patientIdType=ParamUtil.getString(request,"patientIdType");
        filterDto.setPatientIdType(patientIdType);
        String patientIdNo=ParamUtil.getString(request,"patientIdNo");
        filterDto.setPatientIdNo(patientIdNo);
        String doctorRegnNo=ParamUtil.getString(request,"doctorRegnNo");
        filterDto.setDoctorRegnNo(doctorRegnNo);
        Date birthDateFrom= Formatter.parseDate(ParamUtil.getString(request, "birthDateFrom"));
        filterDto.setBirthDateFrom(birthDateFrom);
        Date birthDateTo= Formatter.parseDate(ParamUtil.getString(request, "birthDateTo"));
        filterDto.setBirthDateTo(birthDateTo);
        Date submissionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "submissionDateFrom"));
        filterDto.setSubmissionDateFrom(submissionDateFrom);
        Date submissionDateTo= Formatter.parseDate(ParamUtil.getString(request, "submissionDateTo"));
        filterDto.setSubmissionDateTo(submissionDateTo);
        ParamUtil.setSessionAttr(request,"dsEnquiryTopFilterDto",filterDto);
        return filterDto;
    }

    private void setQueryFilter(DsTopEnquiryFilterDto filterDto, FilterParameter filterParameter){
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
//        if(filterDto.getCenterName()!=null) {
//            filter.put("arCentre", filterDto.getCenterName());
//        }
        if(filterDto.getPatientIdType()!=null) {
            filter.put("patientIdType", filterDto.getPatientIdType());
        }
        if(filterDto.getPatientIdNo()!=null){
            filter.put("patientIdNo",filterDto.getPatientIdNo());
        }
        if(filterDto.getSubmissionNo()!=null){
            filter.put("submissionNo",filterDto.getSubmissionNo());
        }
        if(filterDto.getPatientName()!=null){
            filter.put("patientName", filterDto.getPatientName());
        }

        if(filterDto.getDoctorRegnNo()!=null){
            filter.put("doctorRegnNo",filterDto.getDoctorRegnNo());
        }
        if(filterDto.getSubmissionDateFrom()!=null){
            String submissionDateFrom = Formatter.formatDateTime(filterDto.getSubmissionDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("submissionDateFrom", submissionDateFrom);
        }

        if(filterDto.getSubmissionDateTo()!=null){
            String submissionDateTo = Formatter.formatDateTime(filterDto.getSubmissionDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("submissionDateTo", submissionDateTo);
        }
        if(filterDto.getBirthDateFrom()!=null){
            String birthDateFrom = Formatter.formatDateTime(filterDto.getBirthDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("birthDateFrom", birthDateFrom);
        }

        if(filterDto.getBirthDateTo()!=null){
            String birthDateTo = Formatter.formatDateTime(filterDto.getBirthDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("birthDateTo", birthDateTo);
        }
        filterParameter.setFilters(filter);

    }

    public void nextStep(BaseProcessClass bpc){

    }

    public void perTopInfo(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        String submissionNo = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);


        TopSuperDataSubmissionDto topInfo = assistedReproductionClient.getTopSuperDataSubmissionDto(submissionNo).getEntity();
        List<PremisesDto> premisesDtos=assistedReproductionClient.getAllCenterPremisesDtoByPatientCode(DataSubmissionConsts.DS_TOP,"null","null").getEntity();
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
        TerminationDto terminationDto=topInfo.getTerminationOfPregnancyDto().getTerminationDto();
        if(terminationDto!=null){
            if(StringUtil.isNotEmpty(terminationDto.getTopPlace())&&premisesMap.containsKey(terminationDto.getTopPlace())){
                terminationDto.setTopPlace(premisesMap.get(terminationDto.getTopPlace()).getPremiseLabel());
            }
            if(StringUtil.isNotEmpty(terminationDto.getPrescribeTopPlace())&&premisesMap.containsKey(terminationDto.getPrescribeTopPlace())){
                terminationDto.setTopDrugPlace(premisesMap.get(terminationDto.getPrescribeTopPlace()).getPremiseLabel());
            }
            if(StringUtil.isNotEmpty(terminationDto.getTopDrugPlace())&&premisesMap.containsKey(terminationDto.getTopDrugPlace())){
                terminationDto.setTopDrugPlace(premisesMap.get(terminationDto.getTopDrugPlace()).getPremiseLabel());
            }
        }

        ParamUtil.setRequestAttr(request,"topSuperDataSubmissionDto",topInfo);
    }
}
