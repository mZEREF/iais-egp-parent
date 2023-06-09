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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugPrescribedDispensedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryResultsDto;
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
import org.springframework.util.StringUtils;
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
@Delegator(value = "mohDrpOnlineEnquiry")
@Slf4j
public class OnlineDrpEnquiryDelegator {

    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    private static final String DRP_PARAM = "drpParam";
    private static final String DRP_RESULT = "drpResult";

    FilterParameter drpParameter = new FilterParameter.Builder()
            .clz(DsDrpEnquiryResultsDto.class)
            .searchAttr(DRP_PARAM)
            .resultAttr(DRP_RESULT)
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private AssistedReproductionService assistedReproductionService;
    @Autowired
    private AssistedReproductionClient assistedReproductionClient;


    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY_DRP);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        drpParameter.setPageSize(pageSize);
        drpParameter.setPageNo(1);
        drpParameter.setSortField("ID");
        drpParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"dsEnquirydrpFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, DRP_PARAM,null);

    }

    public void preSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, DRP_PARAM);
        List<SelectOption> arCentreSelectOption  = assistedReproductionService.genPremisesOptions(DataSubmissionConsts.DS_DRP,"null");
        ParamUtil.setRequestAttr(bpc.request,"arCentreSelectOption",arCentreSelectOption);


        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                drpParameter.setSortType(sortType);
                drpParameter.setSortField(sortFieldName);
            }
            DsDrpEnquiryFilterDto drpDto=setDsDrpEnquiryFilterDto(request);

            setQueryFilter(drpDto,drpParameter, arCentreSelectOption);

            SearchParam drpParam = SearchResultHelper.getSearchParam(request, drpParameter,true);

            if(searchParam!=null){
                drpParam.setPageNo(searchParam.getPageNo());
                drpParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(drpParam,bpc.request);
            QueryHelp.setMainSql("onlineEnquiry","searchByDrpPatient",drpParam);
            SearchResult<DsDrpEnquiryResultsDto> drpResult = assistedReproductionService.searchDrpByParam(drpParam);
            ParamUtil.setRequestAttr(request,DRP_RESULT,drpResult);
            ParamUtil.setSessionAttr(request,DRP_PARAM,drpParam);
        }else {
            SearchResult<DsDrpEnquiryResultsDto> drpResult = assistedReproductionService.searchDrpByParam(searchParam);
            ParamUtil.setRequestAttr(request,DRP_RESULT,drpResult);
            ParamUtil.setSessionAttr(request,DRP_PARAM,searchParam);
        }
    }

    private DsDrpEnquiryFilterDto setDsDrpEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        DsDrpEnquiryFilterDto filterDto=new DsDrpEnquiryFilterDto();
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
        Date birthDateFrom= Formatter.parseDate(ParamUtil.getString(request, "birthDateFrom"));
        filterDto.setBirthDateFrom(birthDateFrom);
        Date birthDateTo= Formatter.parseDate(ParamUtil.getString(request, "birthDateTo"));
        filterDto.setBirthDateTo(birthDateTo);
        Date submissionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "submissionDateFrom"));
        filterDto.setSubmissionDateFrom(submissionDateFrom);
        Date submissionDateTo= Formatter.parseDate(ParamUtil.getString(request, "submissionDateTo"));
        filterDto.setSubmissionDateTo(submissionDateTo);
        ParamUtil.setSessionAttr(request,"dsEnquiryDrpFilterDto",filterDto);
        return filterDto;
    }

    private void setQueryFilter(DsDrpEnquiryFilterDto filterDto, FilterParameter filterParameter, List<SelectOption> arCentreSelectOption){
        Map<String,Object> filter=IaisCommonUtils.genNewHashMap();
        if(filterDto.getCenterName()!=null) {
//            filter.put("arCentre", filterDto.getCenterName());
            setQueryDpBusinessName(arCentreSelectOption,filterDto.getCenterName(),filter);
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

    private static void setQueryDpBusinessName(List<SelectOption> arCentreSelectOption, String centerName,Map<String,Object> filter){
        if (IaisCommonUtils.isNotEmpty(arCentreSelectOption)){
            List<String> hicCodeList = IaisCommonUtils.genNewArrayList();
            for (SelectOption selectOption : arCentreSelectOption) {
                if (isExistHciCode(selectOption , centerName)){
                    hicCodeList.add(selectOption.getValue());
                }
            }
            filter.put("arCentre",hicCodeList);
        }
    }

    private static Boolean isExistHciCode(SelectOption arCentreSelectOption, String centerName){
        Boolean result = Boolean.FALSE;
        if (arCentreSelectOption != null){
            String compareArText = StringUtils.trimAllWhitespace(arCentreSelectOption.getText()).toLowerCase();
            String compareCenterName = StringUtils.trimAllWhitespace(centerName).toLowerCase();
            String reCompareArText = convHtmlStr(compareArText);
            result = reCompareArText.contains(compareCenterName);
        }
        return  result;
    }

    private static String convHtmlStr(String inStr){
        return inStr
                .replaceAll("&apos;","'")
                .replaceAll("&amp;", "&")
                .replaceAll("&copy;","©");
    }

    public void nextStep(BaseProcessClass bpc){

    }

    public void perDrpInfo(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        String submissionNo = ParamUtil.getString(request, InboxConst.CRUD_ACTION_VALUE);


        DpSuperDataSubmissionDto dpSuper = assistedReproductionClient.getDpSuperDataSubmissionDto(submissionNo).getEntity();
        List<PremisesDto> premisesDtos=assistedReproductionClient.getAllCenterPremisesDtoByPatientCode(DataSubmissionConsts.DS_DRP,"null","null").getEntity();
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
        DrugPrescribedDispensedDto drugPrescribedDispensedDto=dpSuper.getDrugPrescribedDispensedDto();
        if(drugPrescribedDispensedDto!=null){
            DrugSubmissionDto drugSubmissionDto=drugPrescribedDispensedDto.getDrugSubmission();
            drugSubmissionDto.setHspBusinessName(premisesMap.get(drugSubmissionDto.getHspBusinessName()).getPremiseLabel());
            if ("DP_TP002".equals(dpSuper.getSubmissionType()) && StringUtil.isNotEmpty(drugSubmissionDto.getDoctorInformationId())) {
                DoctorInformationDto doctorInformationDto = assistedReproductionClient.getRfcDoctorInformationDtoByConds(drugSubmissionDto.getDoctorInformationId()).getEntity();
                dpSuper.setDoctorInformationDto(doctorInformationDto);
                if (doctorInformationDto != null) {
                    if ("DRPP".equals(doctorInformationDto.getDoctorSource()) || "DRPT".equals(doctorInformationDto.getDoctorSource())) {
                        dpSuper.setDoctorInformationDto(doctorInformationDto);
                        drugSubmissionDto.setDoctorReignNo(doctorInformationDto.getDoctorReignNo());
                        drugSubmissionDto.setDoctorInformations("true");
                    } else if ("DRPE".equals(doctorInformationDto.getDoctorSource())) {
                        drugSubmissionDto.setDoctorName(doctorInformationDto.getName());
                        drugSubmissionDto.setSpecialty(String.valueOf(doctorInformationDto.getSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        drugSubmissionDto.setSubSpecialty(String.valueOf(doctorInformationDto.getSubSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        drugSubmissionDto.setQualification(String.valueOf(doctorInformationDto.getQualification()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        drugSubmissionDto.setDoctorReignNo(doctorInformationDto.getDoctorReignNo());
                        drugSubmissionDto.setDoctorInformations("false");
                        drugSubmissionDto.setDoctorInformationPE("true");
                    }
                }
            }
        }
        ParamUtil.setRequestAttr(request,"dpSuperDataSubmissionDto",dpSuper);
    }
}
