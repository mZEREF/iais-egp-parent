package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.ACTION_TYPE;
import static com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant.JUMP_ACTION_TYPE;

/**
 * ARCycleStagesManualDelegator
 * <p>
 * Process: MohARCycleStagesManual
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
            String stage = selectionDto.getStage();
            if(stage != null && stage.equals("AR_STG013")){
                ParamUtil.setRequestAttr(bpc.request, JUMP_ACTION_TYPE, "jump");
                HttpServletRequest request = bpc.request;
                String crudTypeCt = (String) request.getAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_CT);
                if(crudTypeCt == null || !crudTypeCt.equals("back")){
                    ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, stage);
                }
            }
        }
        String jumpActionType = (String) ParamUtil.getRequestAttr(bpc.request, JUMP_ACTION_TYPE);
        if (StringUtils.isEmpty(jumpActionType)){
            ParamUtil.setRequestAttr(bpc.request, JUMP_ACTION_TYPE,"page");
        }
        bpc.request.setAttribute("stage_options", DataSubmissionHelper.genOptions(nextStages));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "cycle-stage-selection");
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(currentArDataSubmission.getAppType()));
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", DataSubmissionHelper.getSmallTitle(DataSubmissionConsts.DS_AR, currentArDataSubmission.getAppType(),
                currentArDataSubmission.getSubmissionType()));
        ParamUtil.setRequestAttr(bpc.request, "stageList", arDataSubmissionService.genAvailableStageList(bpc.request, true));
    }

    /**
     * StartStep: PrepareStage
     *
     * @param bpc
     * @throws
     */
    public void doPrepareStage(BaseProcessClass bpc) {
        String jumpActionType = (String) ParamUtil.getRequestAttr(bpc.request, JUMP_ACTION_TYPE);
        ArSuperDataSubmissionDto currentSuper = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if ("jump".equals(jumpActionType)) {
            ParamUtil.setRequestAttr(bpc.request, "haveJump", "Y");
        } else {
            String crudype = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
            log.info(StringUtil.changeForLog("------Action Type: " + crudype));
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            CycleStageSelectionDto selectionDto;
            if (!StringUtil.isIn(crudype, new String[]{DataSubmissionConstant.CRUD_TYPE_FROM_DRAFT, "patient",
                    DataSubmissionConstant.CRUD_TYPE_RFC})) {
                // click head step nav
                boolean isNavJump = "jumpStage".equals(ParamUtil.getRequestString(bpc.request, ACTION_TYPE));
                selectionDto = getSelectionDtoFromPage(bpc.request, isNavJump);
                currentSuper.setSelectionDto(selectionDto);
                DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
                if (!isNavJump && StringUtil.isIn(crudype, new String[]{"return", "back"})) {
                    ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, "back");
                    return;
                }
                // validation
                String profile = StringUtil.isIn(crudype, new String[]{"draft", "patient"}) ? "ART" : "save";
                ValidationResult result = WebValidationHelper.validateProperty(selectionDto, profile);
                if (result != null) {
                    errorMap.putAll(result.retrieveAll());
                }

                if (errorMap.isEmpty() && !AppConsts.YES.equals(selectionDto.getRetrieveData())) {
                    errorMap.put("showValidatePT", AppConsts.YES);
                    ParamUtil.setRequestAttr(bpc.request, "showValidatePT", AppConsts.YES);
                }
                if (AppConsts.YES.equals(selectionDto.getRetrieveData()) && StringUtil.isEmpty(selectionDto.getPatientName())) {
                    errorMap.put("showNoFoundMd", AppConsts.YES);
                    ParamUtil.setRequestAttr(bpc.request, "showNoFoundMd", AppConsts.YES);
                }
            } else {
                selectionDto = currentSuper.getSelectionDto();
            }
            String stage;
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                stage = "current";
            } else {
                if ("patient".equals(crudype)) {
                    stage = checkPatient(currentSuper, bpc.request);
                } else if ("draft".equals(crudype)) {
                    stage = "draft";
                    // re-set cycle stage
                    DataSubmissionDto dataSubmissionDto = currentSuper.getDataSubmissionDto();
                    if (dataSubmissionDto != null) {
                        // To reNew super data submission and retrieving draft data
                        if (!Objects.equals(selectionDto.getStage(), dataSubmissionDto.getCycleStage())) {
                            currentSuper = DataSubmissionHelper.reNew(currentSuper);
                            currentSuper.setSelectionDto(selectionDto);
                        }
                        dataSubmissionDto.setCycleStage(null);
                    }
                /*currentSuper.setCycleDto(DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(),
                        currentSuper.getHciCode()));*/
                    DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, bpc.request);
                } else if (StringUtil.isIn(crudype, new String[]{DataSubmissionConstant.CRUD_TYPE_FROM_DRAFT,
                        DataSubmissionConstant.CRUD_TYPE_RFC})) {
                    stage = selectionDto.getStage();
                    if (StringUtil.isEmpty(stage)) {
                        stage = "current";
                    }
                } else {
                    stage = checkCycleStage(currentSuper, selectionDto, bpc.request);
                }
            }
            log.info(StringUtil.changeForLog("Stage: " + stage));
            if (DataSubmissionConsts.AR_CYCLE_SFO.equals(stage)) {
                stage = DataSubmissionConsts.AR_CYCLE_EFO;
            }
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CRUD_ACTION_TYPE_CT, stage);
        }
    }

    private String checkPatient(ArSuperDataSubmissionDto currentArDataSubmission, HttpServletRequest request) {
        String stage = "patient";
        String orgId = currentArDataSubmission.getOrgId();
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
            userId = loginContext.getUserId();
        }
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("Action Value: " + actionValue));
        if (StringUtil.isEmpty(actionValue) || "patient".equals(actionValue)) {
            ArSuperDataSubmissionDto dataSubmissionDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                    orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO, null, userId);
            if (dataSubmissionDraft != null) {
                currentArDataSubmission.setDraftId(dataSubmissionDraft.getDraftId());
                currentArDataSubmission.setDraftNo(dataSubmissionDraft.getDraftNo());
                currentArDataSubmission.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
                stage = "current";
                ParamUtil.setRequestAttr(request, "hasDraft", Boolean.TRUE);
            } else {
                currentArDataSubmission.setDraftId(null);
                currentArDataSubmission.setDraftNo(null);
                currentArDataSubmission.setPatientInfoDto(null);
            }
        } else if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentArDataSubmission.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                currentArDataSubmission = arSuperDataSubmissionDtoDraft;
            } else {
                log.warn(StringUtil.changeForLog("The draft is null for " + currentArDataSubmission.getDraftId()));
            }
        } else if ("delete".equals(actionValue)) {
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(orgId, DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO,
                    null);
            currentArDataSubmission.setDraftId(null);
            currentArDataSubmission.setDraftNo(null);
            currentArDataSubmission.setPatientInfoDto(null);
        }
        DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
        return stage;
    }

    private String checkCycleStage(ArSuperDataSubmissionDto currentArDataSubmission,
            CycleStageSelectionDto selectionDto, HttpServletRequest request) {
        String stage = selectionDto.getStage();
        String orgId = currentArDataSubmission.getOrgId();
        String userId = "";
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            userId = loginContext.getUserId();
        }
        String hciCode = Optional.ofNullable(currentArDataSubmission.getPremisesDto())
                .map(PremisesDto::getHciCode)
                .orElse("");
        String actionValue = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("Action Value: " + actionValue));
        if (StringUtil.isEmpty(actionValue)) {
            List<ArSuperDataSubmissionDto> dataSubmissionDraftList = arDataSubmissionService.getArSuperDataSubmissionDtoDraftByConds(
                    selectionDto.getPatientIdType(), selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                    orgId, hciCode, true, userId);
            if (IaisCommonUtils.isNotEmpty(dataSubmissionDraftList)) {
                dataSubmissionDraftList = dataSubmissionDraftList.stream()
                        .filter(arDraft -> !Objects.isNull(arDraft.getSelectionDto()) && !Objects.isNull(arDraft.getSelectionDto().getStage()))
                        .filter(arDraft -> arDraft.getSelectionDto().getStage().equals(selectionDto.getStage()))
                        .collect(Collectors.toList());
                if (DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT.equals(selectionDto.getStage()) && IaisCommonUtils.isNotEmpty(dataSubmissionDraftList)) {
                    dataSubmissionDraftList = dataSubmissionDraftList.stream()
                            .filter(arSuperDataSubmissionDto1 -> StringUtil.isEmpty(arSuperDataSubmissionDto1.getTransferInOutStageDto().getBindSubmissionId()))
                            .collect(Collectors.toList());
                }
            }
            ArSuperDataSubmissionDto dataSubmissionDraft;
            if (IaisCommonUtils.isEmpty(dataSubmissionDraftList)) {
                dataSubmissionDraft = null;
            }else {
                dataSubmissionDraft = dataSubmissionDraftList.get(0);
            }
            if (dataSubmissionDraft != null/* && !Objects.equals(currentArDataSubmission.getDraftNo(),
                    dataSubmissionDraft.getDraftNo())*/) {
                currentArDataSubmission.setDraftId(dataSubmissionDraft.getDraftId());
                currentArDataSubmission.setDraftNo(dataSubmissionDraft.getDraftNo());
                DataSubmissionHelper.setCurrentArDataSubmission(currentArDataSubmission, request);
                stage = "current";
                ParamUtil.setRequestAttr(request, "hasDraft", Boolean.TRUE);
            } else {
                handleArSuperDataSubmissionDto(currentArDataSubmission, selectionDto, request);
            }
        } else if ("resume".equals(actionValue)) {
            ArSuperDataSubmissionDto arSuperDataSubmissionDtoDraft = arDataSubmissionService.getArSuperDataSubmissionDtoDraftById(
                    currentArDataSubmission.getDraftId());
            if (arSuperDataSubmissionDtoDraft != null) {
                DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDtoDraft, request);
                stage = arSuperDataSubmissionDtoDraft.getSelectionDto().getStage();
            } else {
                log.warn(StringUtil.changeForLog("The draft is null for " + currentArDataSubmission.getDraftId()));
                handleArSuperDataSubmissionDto(currentArDataSubmission, selectionDto, request);
            }
        } else if ("delete".equals(actionValue)) {
            arDataSubmissionService.deleteArSuperDataSubmissionDtoDraftByConds(selectionDto.getPatientIdType(),
                    selectionDto.getPatientIdNumber(), selectionDto.getPatientNationality(),
                    orgId, hciCode);
            currentArDataSubmission = DataSubmissionHelper.reNew(currentArDataSubmission);
            currentArDataSubmission.setDraftNo(null);
            currentArDataSubmission.setDraftId(null);
            currentArDataSubmission.getDataSubmissionDto().setCycleStage(stage);
            handleArSuperDataSubmissionDto(currentArDataSubmission, selectionDto, request);
        }
        return stage;
    }

    private void handleArSuperDataSubmissionDto(ArSuperDataSubmissionDto currentSuper, CycleStageSelectionDto selectionDto,
            HttpServletRequest request) {
        String hciCode = currentSuper.getHciCode();
        DataSubmissionDto dataSubmission = currentSuper.getDataSubmissionDto();
        if (dataSubmission == null) {
            dataSubmission = new DataSubmissionDto();
        }
        dataSubmission.setSubmissionType(currentSuper.getSubmissionType());
        String stage = selectionDto.getStage();
        String navCurrentCycle = selectionDto.getNavCurrentCycle();
        if (!Objects.equals(stage, dataSubmission.getCycleStage())) {
            currentSuper = DataSubmissionHelper.reNew(currentSuper);
        }
        dataSubmission.setCycleStage(stage);
        currentSuper.setDataSubmissionDto(dataSubmission);
        ArSuperDataSubmissionDto newDto = arDataSubmissionService.getArSuperDataSubmissionDto(selectionDto.getPatientCode(),
                hciCode, selectionDto.getCycleId());
        if (newDto != null) {
            log.info("-----Retieve ArSuperDataSubmissionDto from DB-----");
            selectionDto = newDto.getSelectionDto();
            selectionDto.setStage(stage);
            selectionDto.setNavCurrentCycle(navCurrentCycle);
            CycleDto cycleDto = DataSubmissionHelper.initCycleDto(selectionDto, currentSuper.getSvcName(), hciCode,
                    DataSubmissionHelper.getLicenseeId(request));
            currentSuper.setCycleDto(cycleDto);
            currentSuper.setSelectionDto(selectionDto);
            currentSuper.setPatientInfoDto(newDto.getPatientInfoDto());
        } else {
            String msg = "No ArSuperDataSubmissionDto found from DB - " + selectionDto.getPatientCode() + " : " + hciCode;
            log.warn(StringUtil.changeForLog("-----" + msg + "-----"));
            throw new IaisRuntimeException(msg);
        }
        String licenseeId = DataSubmissionHelper.getLicenseeId(request);
        ArCurrentInventoryDto arCurrentInventoryDto = arDataSubmissionService.getArCurrentInventoryDtoByConds(hciCode, licenseeId, selectionDto.getPatientCode(), currentSuper.getSvcName());
        if (arCurrentInventoryDto == null) {
            arCurrentInventoryDto = new ArCurrentInventoryDto();
            arCurrentInventoryDto.setHciCode(hciCode);
            arCurrentInventoryDto.setSvcName(currentSuper.getSvcName());
            arCurrentInventoryDto.setLicenseeId(licenseeId);
            arCurrentInventoryDto.setPatientCode(selectionDto.getPatientCode());
        }
        currentSuper.setArCurrentInventoryDto(arCurrentInventoryDto);
        DataSubmissionHelper.setCurrentArDataSubmission(currentSuper, request);
    }

    private CycleStageSelectionDto getSelectionDtoFromPage(HttpServletRequest request, boolean isNavJump) {
        ArSuperDataSubmissionDto superDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        CycleStageSelectionDto selectionDto = superDto.getSelectionDto();
        String nextStage = isNavJump?ParamUtil.getString(request, "action_value"):ParamUtil.getString(request, "stage");
        selectionDto.setStage(nextStage);
        selectionDto.setCycleId(null);
        CycleDto cycleDto = new CycleDto();
        cycleDto.setId(null);
        cycleDto.setCycleType(selectionDto.getLastCycle());
        selectionDto.setLastCycleDto(cycleDto);
        return selectionDto;
    }

    /**
     * StartStep: Back
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc) throws IOException {
        log.info("----- Back -----");
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(bpc.request.getServerName())
                .append(InboxConst.URL_LICENCE_WEB_MODULE + "MohARAndIUIDataSubmission/PreARIUIDataSubmission");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
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
        currentArDataSubmission.setAppType(DataSubmissionConsts.DS_APP_TYPE_NEW);
        currentArDataSubmission.setSubmissionType(DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO);
        currentArDataSubmission.setSubmissionMethod(DataSubmissionConsts.DS_METHOD_MANUAL_ENTRY);
        DataSubmissionHelper.setCurrentArDataSubmission(DataSubmissionHelper.reNew(currentArDataSubmission), bpc.request);
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
            arSuperDataSubmission.setDraftNo(arDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_AR,
                    arSuperDataSubmission.getDraftNo()));
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
