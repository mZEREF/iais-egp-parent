package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.privilege.PrivilegeConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugPrescribedDispensedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SexualSterilizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TransferInOutStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TreatmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssTreatmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DocInfoService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.LdtDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import com.ecquaria.cloud.privilege.Privilege;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.DP_DOCTOR_INFO_FROM_ELIS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.DP_DOCTOR_INFO_FROM_PRS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.DP_DOCTOR_INFO_USER_NEW_REGISTER;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DOCTOR_INFO_FROM_ELIS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DOCTOR_INFO_FROM_PRS;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.TOP_DOCTOR_INFO_USER_NEW_REGISTER;

/**
 * Process: MohDsAction
 *
 * @Description MohDsActionDelegator
 * @Auther chenlei on 11/16/2021.
 */
@Slf4j
@Delegator("mohDsActionDelegator")
public class MohDsActionDelegator {

    private static final String DEFAULT_URI = "/main-web/eservice/INTERNET/MohDataSubmissionsInbox";
    private static final String ACTION = "ds_action";

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Autowired
    private DpDataSubmissionService dpDataSubmissionService;

    @Autowired
    private LdtDataSubmissionService ldtDataSubmissionService;

    @Autowired
    private VssDataSubmissionService vssDataSubmissionService;

    @Autowired
    private TopDataSubmissionService topDataSubmissionService;

    @Autowired
    private DocInfoService docInfoService;

    @Autowired
    private ArCycleStageDelegator arCycleStageDelegator;
    @Autowired
    private IuiCycleStageDelegator iuiCycleStageDelegator;
    @Autowired
    private TransferInOutDelegator transferInOutDelegator;
    @Autowired
    private DpFeClient dpFeClient;

    @Autowired
    private AppCommService appSubmissionService;


    /**
     * Step: Start
     *
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("------- MohDsActionDelegator Start ------------");
        DataSubmissionHelper.clearSession(bpc.request);
    }

    /**
     * Step: PrepareSwitch
     *
     * @param bpc
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String type = ParamUtil.getString(bpc.request, "type"); // rfc / preview
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        log.info(StringUtil.changeForLog("------DS Type: " + dsType + " --- Submission No: " + submissionNo
                + " --- Type: " + type + " -----"));
        ParamUtil.setRequestAttr(bpc.request, ACTION, type);
    }

    /**
     * Step: PreparePreview
     *
     * @param bpc
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("------ PreparePreview -----" ));
        ParamUtil.setRequestAttr(bpc.request, "isValid", "Y");
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        String submissionType = ParamUtil.getString(bpc.request, "submissionType");
        if(StringUtil.isEmpty(submissionType)){
            bpc.getSession().removeAttribute(HcsaAppConst.DASHBOARDTITLE);
        }

        if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            initDataForView(arSuper, bpc.request);
            arSuper.setDonorSampleDto(setflagMsg(arSuper.getDonorSampleDto()));
            DataSubmissionHelper.setCurrentArDataSubmission(arSuper, bpc.request);
        } else if (DataSubmissionConsts.DS_DRP.equals(dsType)) {
            DpSuperDataSubmissionDto dpSuper = dpDataSubmissionService.getDpSuperDataSubmissionDto(submissionNo);
            if("DP_TP002".equals(dpSuper.getSubmissionType())){
                DrugPrescribedDispensedDto drugPrescribedDispensedDto=dpSuper.getDrugPrescribedDispensedDto();
                DrugSubmissionDto drugSubmissionDto=drugPrescribedDispensedDto.getDrugSubmission();
                DoctorInformationDto doctorInformationDto=docInfoService.getRfcDoctorInformationDtoByConds(dpSuper.getDrugPrescribedDispensedDto().getDrugSubmission().getDoctorInformationId());
                if (doctorInformationDto != null) {
                    if(DP_DOCTOR_INFO_FROM_PRS.equals(doctorInformationDto.getDoctorSource()) || DP_DOCTOR_INFO_USER_NEW_REGISTER.equals(doctorInformationDto.getDoctorSource())){
                        dpSuper.setDoctorInformationDto(doctorInformationDto);
                        drugSubmissionDto.setDoctorReignNo(doctorInformationDto.getDoctorReignNo());
                        drugSubmissionDto.setDoctorInformations("true");
                    }else if(DP_DOCTOR_INFO_FROM_ELIS.equals(doctorInformationDto.getDoctorSource())){
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
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuper, bpc.request);
            dpDataSubmissionService.displayToolTipJudgement(bpc.request);
        } else if (DataSubmissionConsts.DS_LDT.equals(dsType)) {
            LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = ldtDataSubmissionService.getLdtSuperDataSubmissionDto(submissionNo);
            ldtSuperDataSubmissionDto.setAppType(ldtSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
            DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
        } else if (DataSubmissionConsts.DS_VSS.equals(dsType)) {
            VssSuperDataSubmissionDto vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDto(submissionNo);
            VssTreatmentDto vssTreatmentDto=vssSuperDataSubmissionDto.getVssTreatmentDto();
            SexualSterilizationDto sexualSterilizationDto =vssTreatmentDto.getSexualSterilizationDto();
            TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto();
            try {
                int age = -Formatter.compareDateByDay(treatmentDto.getBirthDate())/365;
                int ageNew=-(Formatter.compareDateByDay(treatmentDto.getBirthDate())+age/4) / 365;
                treatmentDto.setAge(ageNew);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            DoctorInformationDto doctorInformationDto=docInfoService.getRfcDoctorInformationDtoByConds(treatmentDto.getDoctorInformationId());
            if(doctorInformationDto!=null){
                vssSuperDataSubmissionDto.setDoctorInformationDto(doctorInformationDto);
                sexualSterilizationDto.setDoctorReignNo(doctorInformationDto.getDoctorReignNo());
                sexualSterilizationDto.setDoctorInformations("true");
            }
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
            vssDataSubmissionService.displayToolTipJudgement(bpc.request);
        }else if (DataSubmissionConsts.DS_TOP.equals(dsType)) {
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDto(submissionNo);
            if(!StringUtil.isEmpty(topSuperDataSubmissionDto.getTerminationOfPregnancyDto().getTerminationDto())){
                DoctorInformationDto doctorInfoDto=docInfoService.getRfcDoctorInformationDtoByConds(topSuperDataSubmissionDto.getTerminationOfPregnancyDto().getTerminationDto().getDoctorInformationId());
                if(doctorInfoDto!=null){
                    ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(doctorInfoDto.getDoctorReignNo());
                    DoctorInformationDto doctorInformationDto=docInfoService.getDoctorInformationDtoByConds(doctorInfoDto.getDoctorReignNo(),"ELIS");

                    if(TOP_DOCTOR_INFO_FROM_PRS.equals(doctorInfoDto.getDoctorSource()) || TOP_DOCTOR_INFO_USER_NEW_REGISTER.equals(doctorInfoDto.getDoctorSource())){
                        TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
                        TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
                        topSuperDataSubmissionDto.setDoctorInformationDto(doctorInfoDto);
                        terminationDto.setTopDoctorInformations("true");
                        terminationDto.setDoctorRegnNo(doctorInfoDto.getDoctorReignNo());
                        if(professionalResponseDto!=null&&doctorInformationDto!=null){
                            ParamUtil.setSessionAttr(bpc.request, "DoctorELISAndPrs",true);
                        }else {
                            ParamUtil.setSessionAttr(bpc.request, "DoctorELISAndPrs",false);
                        }
                    }else if(TOP_DOCTOR_INFO_FROM_ELIS.equals(doctorInfoDto.getDoctorSource())){
                        TerminationOfPregnancyDto terminationOfPregnancyDto=topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
                        TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
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
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
            topDataSubmissionService.displayToolTipJudgement(bpc.request);
        }else {
            ParamUtil.setRequestAttr(bpc.request, "isValid", "N");
        }
        ParamUtil.setRequestAttr(bpc.request, "headingSigns", "hide");
        ParamUtil.setRequestAttr(bpc.request, "dsType", dsType);
    }

    public DonorSampleDto setflagMsg(DonorSampleDto donorSampleDto){
        if(donorSampleDto != null){
            List<DonorSampleAgeDto> donorSampleAgeDtos = donorSampleDto.getDonorSampleAgeDtos();
            if(IaisCommonUtils.isNotEmpty(donorSampleAgeDtos) && !donorSampleDto.isDirectedDonation()){
                for(DonorSampleAgeDto donorSampleAgeDto : donorSampleAgeDtos){
                    int age = donorSampleAgeDto.getAge();
                    if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_SPERM.equals(donorSampleDto.getSampleType())){
                        if(age <21 || age >40){
                            donorSampleDto.setAgeErrorMsg(StringUtil.viewNonNullHtml(MessageUtil.getMessageDesc("DS_ERR044")));
                            break;
                        }
                    }else if(DataSubmissionConsts.DONOR_SAMPLE_TYPE_EMBRYO.equals(donorSampleDto.getSampleType())
                            ||DataSubmissionConsts.DONOR_SAMPLE_TYPE_OOCYTE.equals(donorSampleDto.getSampleType())){
                        if(age <21 || age >35){
                            donorSampleDto.setAgeErrorMsg(StringUtil.viewNonNullHtml(MessageUtil.getMessageDesc("DS_ERR045")));
                            break;
                        }
                    }
                }
            }
        }

        return donorSampleDto;
    }

    public void initDataForView(ArSuperDataSubmissionDto arSuper, HttpServletRequest request) {
        String cycelType = Optional.ofNullable(arSuper)
                .map(ArSuperDataSubmissionDto::getCycleDto)
                .map(CycleDto::getCycleType)
                .orElse(null);
        if (DataSubmissionConsts.DS_CYCLE_PATIENT_ART.equals(cycelType)) {
            DsRfcHelper.handle(arSuper.getPatientInfoDto());
        }
        if (arSuper != null) {
            if (arSuper.getArCycleStageDto() != null) {
                arCycleStageDelegator.init(request);
                arCycleStageDelegator.setCycleAgeByPatientInfoDtoAndHcicode(arSuper.getArCycleStageDto(), arSuper.getPatientInfoDto(),
                        arSuper.getPremisesDto().getHciCode());
                arCycleStageDelegator.setEnhancedCounsellingTipShow(request, arSuper.getArCycleStageDto(), true);
                arCycleStageDelegator.setNumberOfCyclesUndergoneLocally(arSuper.getArCycleStageDto(),arDataSubmissionService.getCycleStageSelectionDtoByConds(arSuper.getPatientInfoDto().getPatient().getPatientCode(),null,null));
            } else if (arSuper.getIuiCycleStageDto() != null) {
                iuiCycleStageDelegator.init(request);
                arDataSubmissionService.setIuiCycleStageDtoDefaultVal(arSuper);
            }else if (arSuper.getTransferInOutStageDto() != null){
                DataSubmissionHelper.setCurrentArDataSubmission(arSuper, request);
                transferInOutDelegator.initSelectOpts(request);
                TransferInOutStageDto transferInOutStageDto = arSuper.getTransferInOutStageDto();
                if (StringUtil.isNotEmpty(transferInOutStageDto.getBindSubmissionId())) {
                    ArSuperDataSubmissionDto bindStageArSuperDto = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionId(transferInOutStageDto.getBindSubmissionId());
                    if (bindStageArSuperDto != null) {
                        TransferInOutDelegator.flagInAndOutDiscrepancy(request, transferInOutStageDto, bindStageArSuperDto);
                    }
                }
            }else if(arSuper.getPgtStageDto() != null){
                String patientCode=arSuper.getPatientInfoDto().getPatient().getPatientCode();

                List<PgtStageDto> oldPgtList=dpFeClient.listPgtStageByPatientCode(patientCode).getEntity();
                int countNo =0;
                if(oldPgtList!=null){
                    for (PgtStageDto pgt:oldPgtList
                    ) {
                        if (pgt.getIsPgtMEbt() + pgt.getIsPgtMCom() + pgt.getIsPgtMRare() > 0 && pgt.getCreatedAt().before(arSuper.getDataSubmissionDto().getSubmitDt())) {
                            countNo += pgt.getIsPgtCoFunding();
                        }
                        if (pgt.getIsPgtSr() > 0 && pgt.getCreatedAt().before(arSuper.getDataSubmissionDto().getSubmitDt())) {
                            countNo += pgt.getIsPgtCoFunding();
                        }

                    }
                }
                ParamUtil.setSessionAttr(request, "count", countNo);
            } else if (arSuper.getEmbryoTransferStageDto() != null) {
                ParamUtil.setRequestAttr(request, "flagTwo", arDataSubmissionService.flagOutEmbryoTransferAgeAndCount(arSuper));
                ParamUtil.setRequestAttr(request, "flagThree", arDataSubmissionService.flagOutEmbryoTransferCountAndPatAge(arSuper));
            }
        }
    }

    /**
     * Step: PrepareRfc
     *
     * @param bpc
     */
    public void prepareRfc(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("------ PrepareRfc -----" ));
        String dsType = ParamUtil.getString(bpc.request, "dsType");
        String submissionNo = ParamUtil.getString(bpc.request, "submissionNo");
        List<String> privilegeIds = AccessUtil.getLoginUser(bpc.request).getPrivileges().stream().map(Privilege::getId).collect(Collectors.toList());
        String uri = "";
        if (StringUtil.isEmpty(dsType) || StringUtil.isEmpty(submissionNo)) {
            uri = DEFAULT_URI;
        } else if (DataSubmissionConsts.DS_AR.equals(dsType) && privilegeIds.contains(PrivilegeConsts.USER_PRIVILEGE_DS_AR_RFC)) {
            uri = prepareArRfc(submissionNo, bpc.request);
        } else if (DataSubmissionConsts.DS_DRP.equals(dsType)&& privilegeIds.contains(PrivilegeConsts.USER_PRIVILEGE_DS_DP_RFC)) {
            uri = prepareDpRfc(submissionNo, bpc.request);
        } else if (DataSubmissionConsts.DS_LDT.equals(dsType)&& privilegeIds.contains(PrivilegeConsts.USER_PRIVILEGE_DS_LDT_RFC)) {
            uri = prepareLdtRfc(submissionNo, bpc.request);
        }else if (DataSubmissionConsts.DS_TOP.equals(dsType)&& privilegeIds.contains(PrivilegeConsts.USER_PRIVILEGE_DS_TOP_RFC)) {
            uri = prepareTopRfc(submissionNo, bpc.request);
        }
        log.info(StringUtil.changeForLog("------URI: " + uri));
        ParamUtil.setRequestAttr(bpc.request, "uri", uri);
    }

    private String prepareDpRfc(String submissionNo, HttpServletRequest request) {
        String uri = "";
        DpSuperDataSubmissionDto dpSuper = dpDataSubmissionService.getDpSuperDataSubmissionDto(submissionNo);
        if (dpSuper == null) {
            uri = DEFAULT_URI;
        } else {
            if (DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PreparePatientInfo?crud_type=" +DataSubmissionConstant.CRUD_TYPE_RFC;
            } else if (DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(dpSuper.getSubmissionType())) {
                DrugPrescribedDispensedDto drugPrescribedDispensedDto=dpSuper.getDrugPrescribedDispensedDto();
                DrugSubmissionDto drugSubmissionDto=drugPrescribedDispensedDto.getDrugSubmission();
                DoctorInformationDto doctorInformationDto=docInfoService.getRfcDoctorInformationDtoByConds(drugSubmissionDto.getDoctorInformationId());
                dpSuper.setDoctorInformationDto(doctorInformationDto);
                /*ProfessionalResponseDto professionalResponseDto=appSubmissionService.retrievePrsInfo(doctorInformationDto.getDoctorReignNo());
                if(professionalResponseDto==null){
                    professionalResponseDto=new ProfessionalResponseDto();
                }*/
                if(doctorInformationDto!=null){
                    if(DP_DOCTOR_INFO_FROM_PRS.equals(doctorInformationDto.getDoctorSource()) || DP_DOCTOR_INFO_USER_NEW_REGISTER.equals(doctorInformationDto.getDoctorSource())){
                        dpSuper.setDoctorInformationDto(doctorInformationDto);
                        drugSubmissionDto.setDoctorReignNo(doctorInformationDto.getDoctorReignNo());
                        drugSubmissionDto.setDoctorInformations("true");
                    }else if(DP_DOCTOR_INFO_FROM_ELIS.equals(doctorInformationDto.getDoctorSource())){
                        drugSubmissionDto.setDoctorName(doctorInformationDto.getName());
                        drugSubmissionDto.setSpecialty(String.valueOf(doctorInformationDto.getSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        drugSubmissionDto.setSubSpecialty(String.valueOf(doctorInformationDto.getSubSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        drugSubmissionDto.setQualification(String.valueOf(doctorInformationDto.getQualification()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        drugSubmissionDto.setDoctorReignNo(doctorInformationDto.getDoctorReignNo());
                        drugSubmissionDto.setDoctorInformations("false");
                        drugSubmissionDto.setDoctorInformationPE("true");
                    }
                }
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareDrugPrecribed?crud_type=" + DataSubmissionConstant.CRUD_TYPE_RFC;
            } else if (DataSubmissionConsts.DP_TYPE_SBT_SOVENOR_INVENTORY.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareSovenorInventory";
            }
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(dpSuper));
            dpSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            dpSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            DataSubmissionDto dataSubmissionDto = dpSuper.getDataSubmissionDto();
            if(dataSubmissionDto ==null){
                dataSubmissionDto = new DataSubmissionDto();
            }
            if (dataSubmissionDto != null) {
                dataSubmissionDto.setDeclaration(null);
                dataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
                dataSubmissionDto.setAmendReason(null);
                dataSubmissionDto.setAmendReasonOther(null);
            }

        }
        DataSubmissionHelper.setCurrentDpDataSubmission(dpSuper, request);
        return uri;
    }

    private String prepareLdtRfc(String submissionNo, HttpServletRequest request) {
        String uri = "";
        LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = ldtDataSubmissionService.getLdtSuperDataSubmissionDto(submissionNo);
        if (ldtSuperDataSubmissionDto == null) {
            uri = DEFAULT_URI;
        } else {
            uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohLDTDataSubmission/PrepareSwitch?crud_type=" + DataSubmissionConstant.CRUD_TYPE_RFC;
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.LDT_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(ldtSuperDataSubmissionDto));
            ldtSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            ldtSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            DataSubmissionDto dataSubmissionDto = ldtSuperDataSubmissionDto.getDataSubmissionDto();
            if (dataSubmissionDto != null) {
                dataSubmissionDto.setDeclaration(null);
                dataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
                dataSubmissionDto.setAmendReason(null);
                dataSubmissionDto.setAmendReasonOther(null);
            }
            String orgId = "";
            String userId = "";
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            if (loginContext != null) {
                orgId = loginContext.getOrgId();
                userId = loginContext.getUserId();
            }
            LdtSuperDataSubmissionDto dataSubmissionDraft = ldtDataSubmissionService.getLdtSuperDataSubmissionDraftByConds(orgId, userId, dataSubmissionDto.getId());
            if (dataSubmissionDraft != null) {
                uri += "&hasDraft=" + Boolean.TRUE;
            }
        }
        DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, request);
        return uri;
    }
    private String prepareTopRfc(String submissionNo, HttpServletRequest request) {
        String uri = "";
        TopSuperDataSubmissionDto topSuper = topDataSubmissionService.getTopSuperDataSubmissionDto(submissionNo);
        if (topSuper == null) {
            uri = DEFAULT_URI;
        } else {
            DsConfigHelper.initTopConfig(request);
            TerminationOfPregnancyDto terminationOfPregnancyDto=topSuper.getTerminationOfPregnancyDto();
            TerminationDto terminationDto=terminationOfPregnancyDto.getTerminationDto();
            if(terminationOfPregnancyDto.getTerminationDto()!=null || terminationOfPregnancyDto.getPostTerminationDto()!=null){
                DoctorInformationDto doctorInformationDto=docInfoService.getRfcDoctorInformationDtoByConds(terminationDto.getDoctorInformationId());
                if(doctorInformationDto!=null){
                    if(TOP_DOCTOR_INFO_FROM_PRS.equals(doctorInformationDto.getDoctorSource())){
                        topSuper.setDoctorInformationDto(doctorInformationDto);
                        terminationDto.setDoctorName(doctorInformationDto.getName());
                        terminationDto.setSpecialty(String.valueOf(doctorInformationDto.getSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setSubSpecialty(String.valueOf(doctorInformationDto.getSubSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setQualification(String.valueOf(doctorInformationDto.getQualification()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setDoctorRegnNo(doctorInformationDto.getDoctorReignNo());
                        terminationDto.setTopDoctorInformations("false");
                    }else if(TOP_DOCTOR_INFO_FROM_ELIS.equals(doctorInformationDto.getDoctorSource())){
                        terminationDto.setDoctorName(doctorInformationDto.getName());
                        terminationDto.setSpecialty(String.valueOf(doctorInformationDto.getSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setSubSpecialty(String.valueOf(doctorInformationDto.getSubSpeciality()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setQualification(String.valueOf(doctorInformationDto.getQualification()).replaceAll("(?:\\[|null|\\]| +)", ""));
                        terminationDto.setDoctorRegnNo(doctorInformationDto.getDoctorReignNo());
                        terminationDto.setTopDoctorInformations("false");
                        terminationDto.setDoctorInformationPE("true");
                    }else if(TOP_DOCTOR_INFO_USER_NEW_REGISTER.equals(doctorInformationDto.getDoctorSource())){
                        topSuper.setDoctorInformationDto(doctorInformationDto);
                        terminationDto.setDoctorRegnNo(doctorInformationDto.getDoctorReignNo());
                        terminationDto.setTopDoctorInformations("true");
                    }
                }
            }
            uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohTOPDataSubmission/PrepareSwitch?crud_type=" + DataSubmissionConstant.CRUD_TYPE_RFC;
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(topSuper));
            topSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            topSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            DsConfigHelper.clearTopSession(request);

            if (topSuper.getDataSubmissionDto() != null) {
                DataSubmissionDto dataSubmissionDto = topSuper.getDataSubmissionDto();
                dataSubmissionDto.setDeclaration(null);
                dataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
                dataSubmissionDto.setAmendReason(null);
                dataSubmissionDto.setAmendReasonOther(null);
            }
        }
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuper, request);
        return uri;
    }

    private String prepareArRfc(String submissionNo, HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                submissionNo);
        String uri;
        if (arSuper == null) {
            uri = DEFAULT_URI;
        } else {
            arSuper = arDataSubmissionService.prepareArRfcData(arSuper,submissionNo,request);
            if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(arSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARPatientInformationManual";
            } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(arSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARSubmitDonor";
            } else if (arSuper.getDataSubmissionDto() == null
                    || StringUtil.isEmpty(arSuper.getDataSubmissionDto().getCycleStage())) {
                uri = DEFAULT_URI;
            } else {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohARCycleStagesManual/PrepareStage?crud_type="
                        + DataSubmissionConstant.CRUD_TYPE_RFC;
            }
        }
        DataSubmissionHelper.setCurrentArDataSubmission(arSuper, request);
        return uri;
    }

    /**
     * Step: Redirection
     *
     * @param bpc
     */
    public void doRedirection(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("------ Redirection -----" ));
        String uri = (String) ParamUtil.getRequestAttr(bpc.request, "uri");
        if (StringUtil.isEmpty(uri)) {
            uri = DEFAULT_URI;
        }
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(uri);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        log.info(StringUtil.changeForLog("------URL: " + tokenUrl));
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

}
