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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsVssEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsVssEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
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
 * OnlinedrpEnquiryDelegator
 *
 * @author junyu
 * @date 2022/5/5
 */
@Delegator(value = "mohVssOnlineEnquiry")
@Slf4j
public class OnlineVssEnquiryDelegator {

    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter vssParameter = new FilterParameter.Builder()
            .clz(DsVssEnquiryResultsDto.class)
            .searchAttr("vssParam")
            .resultAttr("vssResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;
    @Autowired
    private AssistedReproductionClient assistedReproductionClient;

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_VSS);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        vssParameter.setPageSize(pageSize);
        vssParameter.setPageNo(1);
        vssParameter.setSortField("ID");
        vssParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"dsEnquiryVssFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "vssParam",null);

    }

    public void preSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "vssParam");
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions(DataSubmissionConsts.DS_VSS,"null");
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);


        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                vssParameter.setSortType(sortType);
                vssParameter.setSortField(sortFieldName);
            }
            DsVssEnquiryFilterDto drpDto=setDsVssEnquiryFilterDto(request);

            setQueryFilter(drpDto,vssParameter);

            SearchParam vssParam = SearchResultHelper.getSearchParam(request, vssParameter,true);

            if(searchParam!=null){
                vssParam.setPageNo(searchParam.getPageNo());
                vssParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(vssParam,bpc.request);
            QueryHelp.setMainSql("onlineEnquiry","searchByVss",vssParam);
            SearchResult<DsVssEnquiryResultsDto> vssResult = assistedReproductionService.searchDsVssByParam(vssParam);
            ParamUtil.setRequestAttr(request,"vssResult",vssResult);
            ParamUtil.setSessionAttr(request,"vssParam",vssParam);
        }else {
            SearchResult<DsVssEnquiryResultsDto> vssResult = assistedReproductionService.searchDsVssByParam(searchParam);
            ParamUtil.setRequestAttr(request,"vssResult",vssResult);
            ParamUtil.setSessionAttr(request,"vssParam",searchParam);
        }
    }

    private DsVssEnquiryFilterDto setDsVssEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        DsVssEnquiryFilterDto filterDto=new DsVssEnquiryFilterDto();
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
        String maritalStatus=ParamUtil.getString(request,"maritalStatus");
        filterDto.setMaritalStatus(maritalStatus);
        String sterilisationReason=ParamUtil.getString(request,"sterilisationReason");
        filterDto.setSterilisationReason(sterilisationReason);
        Date birthDateFrom= Formatter.parseDate(ParamUtil.getString(request, "birthDateFrom"));
        filterDto.setBirthDateFrom(birthDateFrom);
        Date birthDateTo= Formatter.parseDate(ParamUtil.getString(request, "birthDateTo"));
        filterDto.setBirthDateTo(birthDateTo);
        Date submissionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "submissionDateFrom"));
        filterDto.setSubmissionDateFrom(submissionDateFrom);
        Date submissionDateTo= Formatter.parseDate(ParamUtil.getString(request, "submissionDateTo"));
        filterDto.setSubmissionDateTo(submissionDateTo);
        ParamUtil.setSessionAttr(request,"dsEnquiryVssFilterDto",filterDto);
        return filterDto;
    }

    private void setQueryFilter(DsVssEnquiryFilterDto filterDto, FilterParameter filterParameter){
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        if(filterDto.getCenterName()!=null) {
            filter.put("arCentre", filterDto.getCenterName());
        }
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
        if(filterDto.getMaritalStatus()!=null){
            filter.put("maritalStatus", filterDto.getMaritalStatus());
        }
        if(filterDto.getSterilisationReason()!=null){
            filter.put("sterilisationReason", filterDto.getSterilisationReason());
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

    public void perVssInfo(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        String submissionNo = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);


        VssSuperDataSubmissionDto vssInfo = assistedReproductionClient.getVssSuperDataSubmissionDto(submissionNo).getEntity();
        List<PremisesDto> premisesDtos=assistedReproductionClient.getAllCenterPremisesDtoByPatientCode(DataSubmissionConsts.DS_VSS,"null","null").getEntity();
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
        if(vssInfo.getVssTreatmentDto().getSexualSterilizationDto()!=null){
            if(vssInfo.getVssTreatmentDto().getSexualSterilizationDto().getSterilizationHospital()!=null&&premisesMap.containsKey(vssInfo.getVssTreatmentDto().getSexualSterilizationDto().getSterilizationHospital())){
                vssInfo.getVssTreatmentDto().getSexualSterilizationDto().setSterilizationHospital(premisesMap.get(vssInfo.getVssTreatmentDto().getSexualSterilizationDto().getSterilizationHospital()).getPremiseLabel());
            }
        }

        ParamUtil.setRequestAttr(request,"vssSuperDataSubmissionDto",vssInfo);
    }
}
