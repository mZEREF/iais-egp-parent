package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.DsRfcHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    private ArCycleStageDelegator arCycleStageDelegator;
    @Autowired
    private IuiCycleStageDelegator iuiCycleStageDelegator;
    @Autowired
    private TransferInOutDelegator transferInOutDelegator;
    @Autowired
    private DpFeClient dpFeClient;

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
            bpc.getSession().removeAttribute(HcsaLicenceFeConstant.DASHBOARDTITLE);
        }

        if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                    submissionNo);
            initDataForView(arSuper, bpc.request);
            arSuper.setDonorSampleDto(setflagMsg(arSuper.getDonorSampleDto()));
            DataSubmissionHelper.setCurrentArDataSubmission(arSuper, bpc.request);
        } else if (DataSubmissionConsts.DS_DRP.equals(dsType)) {
            DpSuperDataSubmissionDto dpSuper = dpDataSubmissionService.getDpSuperDataSubmissionDto(submissionNo);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuper, bpc.request);
        } else if (DataSubmissionConsts.DS_LDT.equals(dsType)) {
            LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = ldtDataSubmissionService.getLdtSuperDataSubmissionDto(submissionNo);
            ldtSuperDataSubmissionDto.setAppType(ldtSuperDataSubmissionDto.getDataSubmissionDto().getAppType());
            DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "title", "Laboratory Develop Test");
        } else if (DataSubmissionConsts.DS_VSS.equals(dsType)) {
            VssSuperDataSubmissionDto vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDto(submissionNo);
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, bpc.request);
        }else if (DataSubmissionConsts.DS_TOP.equals(dsType)) {
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = topDataSubmissionService.getTopSuperDataSubmissionDto(submissionNo);
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, bpc.request);
        }else {
            ParamUtil.setRequestAttr(bpc.request, "isValid", "N");
        }
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
            } else if (arSuper.getIuiCycleStageDto() != null) {
                iuiCycleStageDelegator.init(request);
                arDataSubmissionService.setIuiCycleStageDtoDefaultVal(arSuper);
            }else if (arSuper.getTransferInOutStageDto() != null){
                DataSubmissionHelper.setCurrentArDataSubmission(arSuper, request);
                transferInOutDelegator.initSelectOpts(request);
            }else if(arSuper.getPgtStageDto() != null){
                String patientCode=arSuper.getPatientInfoDto().getPatient().getPatientCode();

                List<PgtStageDto> oldPgtList=dpFeClient.listPgtStageByPatientCode(patientCode).getEntity();
                int countNo =0;
                if(oldPgtList!=null){
                    for (PgtStageDto pgt:oldPgtList
                    ) {
                        if(pgt.getIsPgtMEbt()+pgt.getIsPgtMCom()+pgt.getIsPgtMRare()+pgt.getIsPgtSr()>0){
                            if(pgt.getIsPgtMEbt()+pgt.getIsPgtMCom()+pgt.getIsPgtMRare()+pgt.getIsPgtSr()>0 && pgt.getCreatedAt().before(arSuper.getDataSubmissionDto().getSubmitDt())){
                                countNo+=pgt.getIsPgtCoFunding();
                            }
                        }

                    }
                }
                ParamUtil.setSessionAttr(request, "count",countNo);
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
        String uri = "";
        if (StringUtil.isEmpty(dsType) || StringUtil.isEmpty(submissionNo)) {
            uri = DEFAULT_URI;
        } else if (DataSubmissionConsts.DS_AR.equals(dsType)) {
            uri = prepareArRfc(submissionNo, bpc.request);
        } else if (DataSubmissionConsts.DS_DRP.equals(dsType)) {
            uri = prepareDpRfc(submissionNo, bpc.request);
        } else if (DataSubmissionConsts.DS_LDT.equals(dsType)) {
            uri = prepareLdtRfc(submissionNo, bpc.request);
        }else if (DataSubmissionConsts.DS_VSS.equals(dsType)) {
            uri = prepareVssRfc(submissionNo, bpc.request);
        }else if (DataSubmissionConsts.DS_TOP.equals(dsType)) {
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
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PreparePatientInfo";
            } else if (DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareDrugPrecribed";
            } else if (DataSubmissionConsts.DP_TYPE_SBT_SOVENOR_INVENTORY.equals(dpSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohDPDataSumission/PrepareSovenorInventory";
            }
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(dpSuper));
            dpSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            dpSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            dpSuper.getDataSubmissionDto().setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
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
            ldtSuperDataSubmissionDto.getDataSubmissionDto().setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        }
        DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, request);
        return uri;
    }

    private String prepareVssRfc(String submissionNo, HttpServletRequest request) {
        String uri = "";
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = vssDataSubmissionService.getVssSuperDataSubmissionDto(submissionNo);
        if (vssSuperDataSubmissionDto == null) {
            uri = DEFAULT_URI;
        } else {
            uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohVSSDataSubmission/PrepareSwitch";
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(vssSuperDataSubmissionDto));
            vssSuperDataSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            vssSuperDataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            vssSuperDataSubmissionDto.getDataSubmissionDto().setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        }
        DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, request);
        return uri;
    }

    private String prepareTopRfc(String submissionNo, HttpServletRequest request) {
        String uri = "";
        TopSuperDataSubmissionDto topSuper = topDataSubmissionService.getTopSuperDataSubmissionDto(submissionNo);
        if (topSuper == null) {
            uri = DEFAULT_URI;
        } else {
            if (DataSubmissionConsts.TOP_TYPE_SBT_PATIENT_INFO.equals(topSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohNewTOPDataSubmission/PatientInformation";
            } else if (DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE.equals(topSuper.getSubmissionType())) {
                uri = InboxConst.URL_LICENCE_WEB_MODULE + "MohNewTOPDataSubmission/TerminationOfPregnancy";
            }
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(topSuper));
            topSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            topSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            topSuper.getDataSubmissionDto().setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
        }
        DataSubmissionHelper.setCurrentTopDataSubmission(topSuper, request);
        return uri;
    }

    private String prepareArRfc(String submissionNo, HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuper = arDataSubmissionService.getArSuperDataSubmissionDtoBySubmissionNo(
                submissionNo);
        arSuper.setArCurrentInventoryDto(arDataSubmissionService.getArCurrentInventoryDtoBySubmissionNo(submissionNo, true));
        String uri;
        if (arSuper == null) {
            uri = DEFAULT_URI;
        } else {
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_OLD_DATA_SUBMISSION,
                    CopyUtil.copyMutableObject(arSuper));
            arSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            arSuper.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
            if (arSuper.getDataSubmissionDto() != null) {
                DataSubmissionDto dataSubmissionDto = arSuper.getDataSubmissionDto();
                dataSubmissionDto.setAppType(DataSubmissionConsts.DS_APP_TYPE_RFC);
                dataSubmissionDto.setAmendReason(null);
                dataSubmissionDto.setAmendReasonOther(null);
                if (arSuper.getSelectionDto() != null) {
                    CycleStageSelectionDto selectionDto = arSuper.getSelectionDto();
                    if (StringUtil.isEmpty(selectionDto.getStage()) || StringUtil.isEmpty(selectionDto.getCycle())) {
                        selectionDto.setStage(dataSubmissionDto.getCycleStage());
                        selectionDto.setCycle(arSuper.getCycleDto().getCycleType());
                    }
                }
            }
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
