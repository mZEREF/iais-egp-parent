package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
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
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * ARCycleStagesManualDelegator
 *
 * @author suocheng
 * @date 10/21/2021
 */
@Delegator("arCycleStagesManualDelegator")
@Slf4j
public class ArCycleStagesManualDelegator {

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
        List<String> nextStages = DataSubmissionHelper.getNextStageForAr(null, null);
        bpc.request.setAttribute("stage_options", DataSubmissionHelper.genOptions(nextStages));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "cycle-stage-selection");
    }

    /**
     * StartStep: PrepareStage
     *
     * @param bpc
     * @throws
     */
    public void doPrepareStage(BaseProcessClass bpc) {
        String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        if (StringUtil.isIn(crudype, new String[]{"return", "back"})) {
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, "back");
            return;
        }
        CycleStageSelectionDto selectionDto = getSelectionDtoFromPage(bpc.request);
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        currentArDataSubmission.setSelectionDto(selectionDto);
        // validation
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ValidationResult result = WebValidationHelper.validateProperty(selectionDto, "save");
        if (result != null) {
            errorMap.putAll(result.retrieveAll());
        }
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_CT, "invalid");
        } else {
            DataSubmissionDto dataSubmission = currentArDataSubmission.getCurrentDataSubmissionDto();
            if (dataSubmission == null) {
                dataSubmission = new DataSubmissionDto();
                currentArDataSubmission.setCurrentDataSubmissionDto(dataSubmission);
            }
            dataSubmission.setSubmissionType(DataSubmissionConsts.DATA_SUBMISSION_TYPE_AR);
            dataSubmission.setCycleStage(selectionDto.getStage());
            bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_CT, selectionDto.getStage());
        }
    }

    private CycleStageSelectionDto getSelectionDtoFromPage(HttpServletRequest request) {
        CycleStageSelectionDto selectionDto = new CycleStageSelectionDto();
        selectionDto.setPatientIdType(ParamUtil.getString(request, "patientIdType"));
        selectionDto.setPatientIdNumber(ParamUtil.getString(request, "patientIdNumber"));
        selectionDto.setPatientNationality(ParamUtil.getString(request, "patientNationality"));
//        selectionDto.setRetrieveData(StringUtil.getNonNull(ParamUtil.getString(request, "retrieveData")));
//        selectionDto.setPatientName(StringUtil.getNonNull(ParamUtil.getString(request, "patientName")));
        selectionDto.setRetrieveData(ParamUtil.getString(request, "retrieveData"));
        selectionDto.setPatientName(ParamUtil.getString(request, "patientName"));
        selectionDto.setUndergoingCycle("1".equals(ParamUtil.getString(request, "undergoingCycle")));
        selectionDto.setLastStage(ParamUtil.getString(request, "lastStage"));
        selectionDto.setStage(ParamUtil.getString(request, "stage"));
        return selectionDto;
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

    /**
     * StartStep: Back
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc) {
    }

}
