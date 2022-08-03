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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PostTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PreTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
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
        TerminationOfPregnancyDto terminationOfPregnancyDto=topInfo.getTerminationOfPregnancyDto();
        TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
        if(terminationDto!=null){
            String day = MasterCodeUtil.getCodeDesc("TOPDAY001");
            String submitDt=Formatter.formatDateTime(topInfo.getDataSubmissionDto().getSubmitDt(), "dd/MM/yyyy HH:mm:ss");
            ParamUtil.setSessionAttr(request, "topLateSubmit", null);
            try {
                if(Formatter.compareDateByDay(submitDt,terminationDto.getTopDate())>=Integer.parseInt(day)){
                    ParamUtil.setSessionAttr(request, "topLateSubmit", Boolean.TRUE);
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("topLateSubmit is error"));
            }

            if(StringUtil.isNotEmpty(terminationDto.getTopPlace())&&premisesMap.containsKey(terminationDto.getTopPlace())){
                terminationDto.setTopPlace(premisesMap.get(terminationDto.getTopPlace()).getPremiseLabel());
            }
            if(StringUtil.isNotEmpty(terminationDto.getPrescribeTopPlace())&&premisesMap.containsKey(terminationDto.getPrescribeTopPlace())){
                terminationDto.setPrescribeTopPlace(premisesMap.get(terminationDto.getPrescribeTopPlace()).getPremiseLabel());
            }
            if(StringUtil.isNotEmpty(terminationDto.getTopDrugPlace())&&premisesMap.containsKey(terminationDto.getTopDrugPlace())){
                terminationDto.setTopDrugPlace(premisesMap.get(terminationDto.getTopDrugPlace()).getPremiseLabel());
            }
        }
        PostTerminationDto postDto=terminationOfPregnancyDto.getPostTerminationDto();
        if(postDto!=null){
            if(StringUtil.isNotEmpty(postDto.getCounsellingPlace())&&premisesMap.containsKey(postDto.getCounsellingPlace())){
                postDto.setCounsellingPlace(premisesMap.get(postDto.getCounsellingPlace()).getPremiseLabel());
            }
        }
        PreTerminationDto preTerminationDto=terminationOfPregnancyDto.getPreTerminationDto();
        if(preTerminationDto!=null){
            String submitDt=Formatter.formatDateTime(topInfo.getDataSubmissionDto().getSubmitDt(), "dd/MM/yyyy HH:mm:ss");
            try {
                ParamUtil.setSessionAttr(request, "counsellingLateSubmit", null);
                ParamUtil.setSessionAttr(request, "secondLateSubmit", null);
                String dayB = MasterCodeUtil.getCodeDesc("TOPDAY003");
                String dayC = MasterCodeUtil.getCodeDesc("TOPDAY002");
                String dayD = MasterCodeUtil.getCodeDesc("TOPDAY004");
                String dayE = MasterCodeUtil.getCodeDesc("TOPDAY006");
                String dayF = MasterCodeUtil.getCodeDesc("TOPDAY005");
                int dayIntB=Integer.parseInt(dayB);
                int dayIntC=Integer.parseInt(dayC);
                int dayIntD=Integer.parseInt(dayD);
                int dayIntE=Integer.parseInt(dayE);
                int dayIntF=Integer.parseInt(dayF);
                if(preTerminationDto.getCounsellingGiven()){
                    //b.Only 1 pre-TOP counselling session done and decision is not to abort; Data was submitted more than 30 days from the Pre-Counselling Date;
                    if("TOPPCR003".equals(preTerminationDto.getCounsellingResult())){
                        if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntB){
                            ParamUtil.setSessionAttr(request, "counsellingLateSubmit", Boolean.TRUE);
                        }
                    }
                    //c.Only 1 pre-TOP counselling session done and patient was lost to follow-up; Data was submitted more than 37 days from Pre-counselling date;
                    if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                        if("0".equals(preTerminationDto.getPatientAppointment())){
                            if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntC){
                                ParamUtil.setSessionAttr(request, "counsellingLateSubmit", Boolean.TRUE);
                            }
                        }
                    }
                    if("TOPPCR002".equals(preTerminationDto.getCounsellingResult())){
                        if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntC){
                            ParamUtil.setSessionAttr(request, "counsellingLateSubmit", Boolean.TRUE);
                        }
                    }
                    if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                        if("1".equals(preTerminationDto.getPatientAppointment())){
                            //d.More than 1 pre-TOP counselling session done and decision is not to abort; Data was submitted more than 30 days from Pre-Counselling Date;
                            if("TOPSP003".equals(preTerminationDto.getSecCounsellingResult())){
                                if(Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntD){
                                    ParamUtil.setSessionAttr(request, "counsellingLateSubmit", Boolean.TRUE);
                                }
                            }
                            //f.More than 1 pre-TOP counselling session, decision is to abort, Data was submitted more than 30 days from the last Pre-counselling date.
                            if("TOPSP004".equals(preTerminationDto.getSecCounsellingResult())){
                                if(Formatter.compareDateByDay(submitDt,preTerminationDto.getSecCounsellingDate())>=dayIntF){
                                    ParamUtil.setSessionAttr(request, "secondLateSubmit", Boolean.TRUE);
                                }else if(StringUtil.isEmpty(preTerminationDto.getSecCounsellingDate())&& Formatter.compareDateByDay(submitDt,preTerminationDto.getCounsellingDate())>=dayIntF){
                                    ParamUtil.setSessionAttr(request, "counsellingLateSubmit", Boolean.TRUE);
                                }
                            }
                        }
                    }
                    //e.More than 1 pre-TOP counselling session, patient did not return for subsequent appointment; Data was submitted more than 37 days from the Second/Final Pre-Counselling Date
                    if("TOPPCR001".equals(preTerminationDto.getCounsellingResult())){
                        if("1".equals(preTerminationDto.getPatientAppointment())){
                            if("TOPSP001".equals(preTerminationDto.getSecCounsellingResult())){
                                if(Formatter.compareDateByDay(submitDt,preTerminationDto.getSecCounsellingDate())>=dayIntE){
                                    ParamUtil.setSessionAttr(request, "secondLateSubmit", Boolean.TRUE);
                                }
                            }
                        }
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("LateSubmit is error"));
            }
            if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingPlace())&&premisesMap.containsKey(preTerminationDto.getCounsellingPlace())){
                preTerminationDto.setCounsellingPlace(premisesMap.get(preTerminationDto.getCounsellingPlace()).getPremiseLabel());
            }
            if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingDate())){
                try {
                    if(terminationDto!=null&&StringUtil.isNotEmpty(terminationDto.getTopDate())){
                        if(StringUtil.isNotEmpty(preTerminationDto.getCounsellingResult())&&!preTerminationDto.getCounsellingResult().equals("TOPPCR003")){
                            if(preTerminationDto.getCounsellingResult().equals("TOPPCR001")){
                                if(StringUtil.isNotEmpty(preTerminationDto.getSecCounsellingResult())&&!preTerminationDto.getSecCounsellingResult().equals("TOPSP003")&&!preTerminationDto.getSecCounsellingResult().equals("TOPSP001")){
                                    if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                        ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                    }else {
                                        ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                    }
                                }
                                if(preTerminationDto.getPatientAppointment().equals("0")){
                                    if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                        ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                    }else {
                                        ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                    }
                                }
                            }else {
                                if(Formatter.compareDateByDay(terminationDto.getTopDate(),preTerminationDto.getCounsellingDate())<1){
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.TRUE);
                                }else {
                                    ParamUtil.setSessionAttr(request, "topDates", Boolean.FALSE);
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    log.error(StringUtil.changeForLog("CounsellingDate is error"));
                }
            }

        }
        if(!StringUtil.isEmpty(terminationDto)){
            if(!StringUtil.isEmpty(terminationDto)&&StringUtil.isNotEmpty(terminationDto.getDoctorInformationId())){
                DoctorInformationDto doctorInfoDto=assistedReproductionClient.getRfcDoctorInformationDtoByConds(terminationDto.getDoctorInformationId()).getEntity();
                if(doctorInfoDto!=null){
                    ProfessionalResponseDto professionalResponseDto=assistedReproductionService.retrievePrsInfo(doctorInfoDto.getDoctorReignNo());
                    DoctorInformationDto doctorInformationDtoELIS=assistedReproductionClient.getDoctorInformationDtoByConds(doctorInfoDto.getDoctorReignNo(),"ELIS").getEntity();

                    if("TOPP".equals(doctorInfoDto.getDoctorSource()) || "TOPT".equals(doctorInfoDto.getDoctorSource())){
                        topInfo.setDoctorInformationDto(doctorInfoDto);
                        terminationDto.setTopDoctorInformations("true");
                        terminationDto.setDoctorRegnNo(doctorInfoDto.getDoctorReignNo());
                        if(professionalResponseDto!=null&&doctorInformationDtoELIS!=null){
                            ParamUtil.setSessionAttr(request, "DoctorELISAndPrs",true);
                        }else {
                            ParamUtil.setSessionAttr(request, "DoctorELISAndPrs",false);
                        }
                    }else if("TOPE".equals(doctorInfoDto.getDoctorSource())){
                        terminationDto.setTopDoctorInformations("false");
                        terminationDto.setDoctorRegnNo(doctorInfoDto.getDoctorReignNo());
                        terminationDto.setDoctorName(doctorInfoDto.getName());
                        terminationDto.setSpecialty(String.valueOf(doctorInfoDto.getSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setSubSpecialty(String.valueOf(doctorInfoDto.getSubSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setQualification(String.valueOf(doctorInfoDto.getQualification()).replaceAll("(?:\\[|null|\\]| +)", ""));
                    }
                }else {
                    ParamUtil.setSessionAttr(bpc.request, "DoctorELISAndPrs",false);
                }
            }
        }
        ParamUtil.setRequestAttr(request,"topSuperDataSubmissionDto",topInfo);
    }
}
