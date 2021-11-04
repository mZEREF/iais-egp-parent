package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ARCycleStagesManualDelegator
 *
 * @author suocheng
 * @date 10/21/2021
 */
@Delegator("arCycleStagesManualDelegator")
@Slf4j
public class ArCycleStagesManualDelegator {

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info("----- ArCycleStagesManualDelegator Start -----");
    }

    /**
     * StartStep: PrepareCycleStageSelection
     *
     * @param bpc
     * @throws
     */
    public void doPrepareCycleStageSelection(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        CycleStageSelectionDto selectionDto = currentArDataSubmission.getSelectionDto();
        List<String> nextStages = null;
        if (selectionDto != null) {
            nextStages = DataSubmissionHelper.getNextStageForAR(selectionDto);
        }
        bpc.request.setAttribute("stage_options", DataSubmissionHelper.genOptions(nextStages));

        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "cycle-stage-selection");
        ParamUtil.setRequestAttr(bpc.request, "title", "New Assisted Reproduction Submission");
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
    }

    /**
     * StartStep: PrepareStage
     *
     * @param bpc
     * @throws
     */
    public void doPrepareStage(BaseProcessClass bpc) {
        CycleStageSelectionDto selectionDto = getSelectionDtoFromPage(bpc.request);
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        currentArDataSubmission.setSelectionDto(selectionDto);
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, bpc.request);
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{"return", "back"})) {
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, "back");
            return;
        }
        // validation
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String profile = StringUtil.isIn(crudype, new String[]{"draft", "patient"}) ? "ART" : "save";
        ValidationResult result = WebValidationHelper.validateProperty(selectionDto, profile);
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
        String stage;
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            stage = "current";
        } else {
            if ("patient".equals(crudype)) {
                stage = checkPatient(currentArDataSubmission, bpc.request);
            } else if ("draft".equals(crudype)) {
                stage = "draft";
            } else {
                stage = checkCycleStage(currentArDataSubmission, selectionDto, bpc.request);
            }
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, stage);
    }

    private String checkPatient(ArSuperDataSubmissionDto currentArDataSubmission, HttpServletRequest request) {
        String stage = "patient";
        String orgId = currentArDataSubmission.getOrgId();
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("Action Type: " + actionValue));
        if (StringUtil.isEmpty(actionValue) || "patient".equals(actionValue)) {
            ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                    orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO, null);
            if (dataSubmissionDraft != null) {
                currentArDataSubmission.setDraftId(dataSubmissionDraft.getDraftId());
                currentArDataSubmission.setArSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
                stage = "current";
                ParamUtil.setRequestAttr(request, "hasDraft", true);
            }
        } else if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentArDataSubmission.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
            } else {
                log.warn(StringUtil.changeForLog("The draft is null for " + currentArDataSubmission.getDraftId()));
            }
        } else if ("delete".equals(actionValue)) {
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO,
                    null);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
        return stage;
    }

    private String checkCycleStage(ArSuperDataSubmissionDto currentArDataSubmission,
            CycleStageSelectionDto selectionDto, HttpServletRequest request) {
        String stage = selectionDto.getStage();
        String orgId = currentArDataSubmission.getOrgId();
        String hciCode = Optional.ofNullable(currentArDataSubmission.getAppGrpPremisesDto())
                .map(AppGrpPremisesDto::getHciCode)
                .orElse("");
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("Action Type: " + actionValue));
        if (StringUtil.isEmpty(actionValue)) {
            ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                    selectionDto.getPatientIdType(), selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                    orgId, hciCode);
            if (dataSubmissionDraft != null) {
                currentArDataSubmission.setDraftId(dataSubmissionDraft.getDraftId());
                DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
                stage = "current";
                ParamUtil.setRequestAttr(request, "hasDraft", true);
            } else {
                handleArSuperDataSubmissionDto(currentArDataSubmission, selectionDto, request);
            }
        } else if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentArDataSubmission.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
            } else {
                log.warn(StringUtil.changeForLog("The draft is null for " + currentArDataSubmission.getDraftId()));
                handleArSuperDataSubmissionDto(currentArDataSubmission, selectionDto, request);
            }
        } else if ("delete".equals(actionValue)) {
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(selectionDto.getPatientIdType(),
                    selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                    orgId, hciCode);
            handleArSuperDataSubmissionDto(currentArDataSubmission, selectionDto, request);
        }
        return stage;
    }

    private void handleArSuperDataSubmissionDto(ArSuperDataSubmissionDto currentArDataSubmission,
            CycleStageSelectionDto selectionDto, HttpServletRequest request) {
        String hicCode = Optional.ofNullable(currentArDataSubmission.getAppGrpPremisesDto())
                .map(premises -> premises.getHciCode())
                .orElse("");
        DataSubmissionDto dataSubmission = currentArDataSubmission.getCurrentDataSubmissionDto();
        if (dataSubmission == null) {
            dataSubmission = new DataSubmissionDto();
        }
        dataSubmission.setSubmissionType(DataSubmissionConsts.DATA_SUBMISSION_TYPE_AR);
        String stage = selectionDto.getStage();
        dataSubmission.setCycleStage(stage);
        currentArDataSubmission.setCurrentDataSubmissionDto(dataSubmission);
        ArSuperDataSubmissionDto newDto = arDataSubmissionService.getArSuperDataSubmissionDto(selectionDto.getPatientCode(),
                hicCode);
        if (newDto != null) {
            log.info("-----Retieve ArSuperDataSubmissionDto from DB-----");
            currentArDataSubmission.setPatientInfoDto(newDto.getPatientInfoDto());
            selectionDto = newDto.getSelectionDto();
            selectionDto.setStage(stage);
            currentArDataSubmission.setSelectionDto(selectionDto);
            currentArDataSubmission.setCycleDto(DataSubmissionHelper.genCycleDto(selectionDto, hicCode));
        } else {
            // for test
            log.info("-----No ArSuperDataSubmissionDto found from DB-----");
            currentArDataSubmission.setCycleDto(DataSubmissionHelper.genCycleDto(selectionDto, hicCode));
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
    }

    private CycleStageSelectionDto getSelectionDtoFromPage(HttpServletRequest request) {
        CycleStageSelectionDto selectionDto = new CycleStageSelectionDto();
        selectionDto.setPatientIdType(ParamUtil.getString(request, "patientIdType"));
        selectionDto.setPatientIdNumber(ParamUtil.getString(request, "patientIdNumber"));
        selectionDto.setPatientNationality(ParamUtil.getString(request, "patientNationality"));
        selectionDto.setPatientCode(ParamUtil.getString(request, "patientCode"));
        //selectionDto.setPatientName(ParamUtil.getString(request, "patientName"));
        selectionDto.setRetrieveData(StringUtil.getNonNull(ParamUtil.getString(request, "retrieveData")));
        selectionDto.setPatientName(StringUtil.getNonNull(ParamUtil.getString(request, "patientName")));
        selectionDto.setUndergoingCycle("1".equals(ParamUtil.getString(request, "undergoingCycle")));
        selectionDto.setLastStage(ParamUtil.getString(request, "lastStage"));
        selectionDto.setStage(ParamUtil.getString(request, "stage"));
        return selectionDto;
    }

    /**
     * StartStep: Back
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc) {
        log.info("----- Back -----");
    }

    /**
     * StartStep: PrepareRegisterPatient
     *
     * @param bpc
     * @throws
     */
    public void doPrepareRegisterPatient(BaseProcessClass bpc) {
        log.info("-----Prepare Register Patient-----");
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        currentArDataSubmission.setArSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
        currentArDataSubmission.setSubmissionMethod(DataSubmissionConsts.DATA_SUBMISSION_METHOD_MANUAL_ENTRY);
        currentArDataSubmission.setCurrentDataSubmissionDto(null);
        currentArDataSubmission.setCycleDto(null);
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, bpc.request);
    }

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (arSuperDataSubmission != null) {
            arSuperDataSubmission = arDataSubmissionService.saveDataSubmissionDraft(arSuperDataSubmission);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The arSuperDataSubmission is null"));
        }
    }

    /**
     * StartStep: PrepareARCycle
     *
     * @param bpc
     * @throws
     */
    public void doPrepareARCycle(BaseProcessClass bpc) {
        log.info("----- doPrepareARCycle -----");
    }

    /**
     * StartStep: PrepareIUICycle
     *
     * @param bpc
     * @throws
     */
    public void doPrepareIUICycle(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareEFOCycle
     *
     * @param bpc
     * @throws
     */
    public void doPrepareEFOCycle(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareOocyteRetrieval
     *
     * @param bpc
     * @throws
     */
    public void doPrepareOocyteRetrieval(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareFertilisation
     *
     * @param bpc
     * @throws
     */
    public void doPrepareFertilisation(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareEmbryoCreated
     *
     * @param bpc
     * @throws
     */
    public void doPrepareEmbryoCreated(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareThawing
     *
     * @param bpc
     * @throws
     */
    public void doPrepareThawing(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PreparePreimplantation
     *
     * @param bpc
     * @throws
     */
    public void doPreparePreimplantation(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareEmbryoTransfer
     *
     * @param bpc
     * @throws
     */
    public void doPrepareEmbryoTransfer(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareARTreatmentSubsidies
     *
     * @param bpc
     * @throws
     */
    public void doPrepareARTreatmentSubsidies(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareIUITreatmentSubsidies
     *
     * @param bpc
     * @throws
     */
    public void doPrepareIUITreatmentSubsidies(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareOutcomeEmbryoTransferred
     *
     * @param bpc
     * @throws
     */
    public void doPrepareOutcomeEmbryoTransferred(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareOutcome
     *
     * @param bpc
     * @throws
     */
    public void doPrepareOutcome(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareOutcomePregnancy
     *
     * @param bpc
     * @throws
     */
    public void doPrepareOutcomePregnancy(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareFreezing
     *
     * @param bpc
     * @throws
     */
    public void doPrepareFreezing(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareDonation
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDonation(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareDisposal
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDisposal(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareEndCycle
     *
     * @param bpc
     * @throws
     */
    public void doPrepareEndCycle(BaseProcessClass bpc) {
    }

    /**
     * StartStep: PrepareTransferInOut
     *
     * @param bpc
     * @throws
     */
    public void doPrepareTransferInOut(BaseProcessClass bpc) {
    }

}
