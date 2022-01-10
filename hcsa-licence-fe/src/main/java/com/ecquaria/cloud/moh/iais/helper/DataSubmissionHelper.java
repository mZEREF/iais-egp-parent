package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.annotation.ExcelProperty;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description Data Submission Helper
 * @Auther chenlei on 10/21/2021.
 */
@Slf4j
public final class DataSubmissionHelper {

    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_DATA_LIST);
        session.removeAttribute(DataSubmissionConstant.AR_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.AR_OLD_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.AR_PREMISES);
        session.removeAttribute(DataSubmissionConstant.DP_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.DP_OLD_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.DP_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.DP_PREMISES);
        session.removeAttribute(DataSubmissionConstant.VSS_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.VSS_OLD_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.VSS_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.VSS_PREMISES);
        session.removeAttribute(DataSubmissionConstant.TOP_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.TOP_OLD_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.TOP_PREMISES_MAP);
        session.removeAttribute(DataSubmissionConstant.TOP_PREMISES);
        session.removeAttribute(DataSubmissionConstant.LAB_SUPER_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.LDT_OLD_DATA_SUBMISSION);
        session.removeAttribute(DataSubmissionConstant.LDT_PREMISS_OPTION);
        session.removeAttribute(DataSubmissionConstant.LDT_CANOT_LDT);
    }

    public static LoginContext getLoginContext(HttpServletRequest request) {
        return (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
    }

    public static ArSuperDataSubmissionDto getCurrentArDataSubmission(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.AR_DATA_SUBMISSION);
        if (arSuperDataSubmissionDto == null) {
            log.info("------------------------------------AR_SUPER_DATA_SUBMISSION_DTO is null-----------------");
        }
        return arSuperDataSubmissionDto;
    }

    public static ArSuperDataSubmissionDto getOldArDataSubmission(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.AR_OLD_DATA_SUBMISSION);
        if (arSuperDataSubmissionDto == null) {
            log.info("------------------------------------AR_OLD_DATA_SUBMISSION is null-----------------");
        }
        return arSuperDataSubmissionDto;
    }

    public static void setCurrentArDataSubmission(ArSuperDataSubmissionDto arSuperDataSubmissionDto, HttpServletRequest request) {
        DataSubmissionHelper.setArPremisesMap(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
    }

    public static ArSuperDataSubmissionDto reNew(ArSuperDataSubmissionDto currentSuper) {
        ArSuperDataSubmissionDto newDto = new ArSuperDataSubmissionDto();
        newDto.setAppType(currentSuper.getAppType());
        newDto.setOrgId(currentSuper.getOrgId());
        newDto.setSubmissionType(currentSuper.getSubmissionType());
        newDto.setSubmissionMethod(currentSuper.getSubmissionMethod());
        newDto.setPremisesDto(currentSuper.getPremisesDto());
        newDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        newDto.setPatientInfoDto(currentSuper.getPatientInfoDto());
        newDto.setDraftId(currentSuper.getDraftId());
        newDto.setDraftNo(currentSuper.getDraftNo());
        DataSubmissionDto dataSubmissionDto = DataSubmissionHelper.initDataSubmission(newDto, true);
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        dataSubmissionDto.setDeclaration(null);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setCycleDto(DataSubmissionHelper.initCycleDto(newDto, true));
        return newDto;
    }

    public static DpSuperDataSubmissionDto getCurrentDpDataSubmission(HttpServletRequest request) {
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = (DpSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.DP_DATA_SUBMISSION);
        if (dpSuperDataSubmissionDto == null) {
            log.info("------------------------------------DP_SUPER_DATA_SUBMISSION_DTO is null-----------------");
        }
        return dpSuperDataSubmissionDto;
    }

    public static void setCurrentDpDataSubmission(DpSuperDataSubmissionDto dpSuperDataSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_DATA_SUBMISSION, dpSuperDataSubmissionDto);
    }

    public static VssSuperDataSubmissionDto getCurrentVssDataSubmission(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.VSS_DATA_SUBMISSION);
        if (vssSuperDataSubmissionDto == null) {
            log.info("------------------------------------VSS_SUPER_DATA_SUBMISSION_DTO is null-----------------");
        }
        return vssSuperDataSubmissionDto;
    }

    public static void setCurrentVssDataSubmission(VssSuperDataSubmissionDto vssSuperDataSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION, vssSuperDataSubmissionDto);
    }

    public static TopSuperDataSubmissionDto getCurrentTopDataSubmission(HttpServletRequest request){
        TopSuperDataSubmissionDto topSuperDataSubmissionDto=(TopSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.TOP_DATA_SUBMISSION);
        if (topSuperDataSubmissionDto == null) {
            log.info("------------------------------------TOP_SUPER_DATA_SUBMISSION_DTO is null-----------------");
        }
        return topSuperDataSubmissionDto;
    }

    public static void setCurrentTopDataSubmission(TopSuperDataSubmissionDto topSuperDataSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
    }

    public static LdtSuperDataSubmissionDto getCurrentLdtSuperDataSubmissionDto(HttpServletRequest request) {
        LdtSuperDataSubmissionDto LdtSuperDataSubmissionDto = (LdtSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.LAB_SUPER_DATA_SUBMISSION);
        if (LdtSuperDataSubmissionDto == null) {
            log.info("------------------------------------LdtSuperDataSubmissionDto is null-----------------");
        }
        return LdtSuperDataSubmissionDto;
    }

    public static void setCurrentLdtSuperDataSubmissionDto(LdtSuperDataSubmissionDto LdtSuperDataSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.LAB_SUPER_DATA_SUBMISSION, LdtSuperDataSubmissionDto);
    }

    public static LdtSuperDataSubmissionDto getOldLdtSuperDataSubmissionDto(HttpServletRequest request) {
        LdtSuperDataSubmissionDto LdtSuperDataSubmissionDto = (LdtSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.LDT_OLD_DATA_SUBMISSION);
        if (LdtSuperDataSubmissionDto == null) {
            log.info("------------------------------------getOldLdtSuperDataSubmissionDto is null-----------------");
        }
        return LdtSuperDataSubmissionDto;
    }


    public static List<String> getNextStageForAR(CycleStageSelectionDto selectionDto) {
        if (selectionDto == null || StringUtil.isEmpty(selectionDto.getPatientCode())) {
            return null;
        }
        String lastCycle = selectionDto.getLastCycle();
        String lastStage = selectionDto.getLastStage();
        String lastStatus = selectionDto.getLastStatus();
        String latestCycle = selectionDto.getLatestCycle();
        String latestStage = selectionDto.getLatestStage();
        String additionalStage = selectionDto.getAdditionalStage();
        return DataSubmissionHelper.getNextStageForAR(latestCycle, latestStage, lastCycle, lastStage, additionalStage,
                selectionDto.isUndergoingCycle(), lastStatus);
    }

    /**
     * Cycle Stages
     *
     * Spec: 3.3.3 Stage Selection Business Rules
     *
     * @param latestCycle
     * @param latestStage
     * @param lastCycle
     * @param lastStage
     * @param additionalStage
     * @param lastStatus
     * @return
     */
    private static List<String> getNextStageForAR(String latestCycle, String latestStage, String lastCycle, String lastStage,
            String additionalStage, boolean undergoingCycle, String lastStatus) {
        log.info(StringUtil.changeForLog("----- The latest cycle stage is " + latestCycle + " : " + latestStage));
        log.info(StringUtil.changeForLog("----- The current cycle stage is " + lastCycle + " : " + lastStage
                + " : " + additionalStage + " : " + undergoingCycle + " : " + lastStatus + " -----"));
        // 3.3.3.2 (4) If the predecessor stage is AR Treatment Co-funding or Transfer In & Out,
        // available stages for selection will be based on the stage prior to it
        // disposal, donation
        if (DataSubmissionConsts.DS_CYCLE_AR.equals(lastCycle) && isSpecialStage(lastStage)) {
            lastStage = additionalStage;
        }
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(lastCycle)) {
            result.add(DataSubmissionConsts.AR_CYCLE_AR);
            result.add(DataSubmissionConsts.AR_CYCLE_EFO);
            result.add(DataSubmissionConsts.AR_CYCLE_IUI);
        } else if (StringUtil.isEmpty(lastStage)
                || DataSubmissionConsts.AR_STAGE_END_CYCLE.equals(lastStage)
                || IaisCommonUtils.getDsCycleFinalStatus().contains(lastStatus)) {
            if (!undergoingCycle) {
                result.add(DataSubmissionConsts.AR_CYCLE_AR);
                result.add(DataSubmissionConsts.AR_CYCLE_IUI);
                result.add(DataSubmissionConsts.AR_CYCLE_EFO);
            }
            result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        } else if (DataSubmissionConsts.DS_CYCLE_AR.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_AR.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
                result.add(DataSubmissionConsts.AR_STAGE_THAWING);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                result.add(DataSubmissionConsts.AR_STAGE_THAWING);
            } else if (DataSubmissionConsts.AR_STAGE_THAWING.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else if (DataSubmissionConsts.AR_STAGE_FERTILISATION.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED);
            } else if (DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else if (DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else if (DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
            } else if (DataSubmissionConsts.AR_STAGE_FREEZING.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            } else if (DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            } else if (DataSubmissionConsts.AR_STAGE_DONATION.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            }
            result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
            result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
        } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_IUI.equals(lastStage) || StringUtil.isEmpty(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
                result.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
            } else if (DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
            }
        } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_EFO.equals(lastStage) || StringUtil.isEmpty(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(lastStage)
                    || DataSubmissionConsts.AR_STAGE_DONATION.equals(lastStage)
                    || DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            }
        }
        return result;
    }

    private static boolean isSpecialStage(String cycleStage) {
        return StringUtil.isIn(cycleStage, new String[]{DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES,
                DataSubmissionConsts.AR_STAGE_DISPOSAL, DataSubmissionConsts.AR_STAGE_DONATION,
                DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT});
    }

    public static CycleDto initCycleDto(CycleStageSelectionDto selectionDto, String serviceName, String hciCode) {
        String stage = selectionDto.getStage();
        String cycle;
        String cycleId = null;
        CycleDto cycleDto = null;
        if (selectionDto.isUndergoingCycle()) {
            cycleDto = selectionDto.getLastCycleDto();
            cycle = cycleDto.getCycleType();
            cycleId = cycleDto.getId();
        } else if (StringUtil.isIn(stage, new String[]{DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT,
                DataSubmissionConsts.AR_STAGE_DONATION,
                DataSubmissionConsts.AR_STAGE_DISPOSAL})) {
            cycle = DataSubmissionConsts.DS_CYCLE_NON;
        } else if (DataSubmissionConsts.AR_CYCLE_AR.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_AR;
        } else if (DataSubmissionConsts.AR_CYCLE_IUI.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_IUI;
        } else if (DataSubmissionConsts.AR_CYCLE_EFO.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_EFO;
        } else {
            cycle = DataSubmissionConsts.DS_CYCLE_NON;
        }
        if (cycleDto == null) {
            cycleDto = new CycleDto();
        }
        selectionDto.setCycle(cycle);
        cycleDto.setId(cycleId);
        cycleDto.setCycleType(cycle);
        cycleDto.setPatientCode(selectionDto.getPatientCode());
        cycleDto.setSvcName(serviceName);
        cycleDto.setHciCode(hciCode);
        cycleDto.setDsType(DataSubmissionConsts.DS_AR);
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        return cycleDto;
    }

    public static CycleDto initCycleDto(ArSuperDataSubmissionDto currentArDataSubmission, boolean reNew) {
        CycleDto cycleDto = currentArDataSubmission.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setSvcName(currentArDataSubmission.getSvcName());
        cycleDto.setHciCode(currentArDataSubmission.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_AR);
        String cycleType = cycleDto.getCycleType();
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(currentArDataSubmission.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_PATIENT_ART;
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(currentArDataSubmission.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_DONOR_SAMPLE;
        } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(currentArDataSubmission.getSubmissionType())
                || StringUtil.isEmpty(cycleType)) {
            cycleType = DataSubmissionConsts.DS_CYCLE_STAGE;
        }
        cycleDto.setCycleType(cycleType);
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        return cycleDto;
    }

    public static DataSubmissionDto initDataSubmission(ArSuperDataSubmissionDto currentArDataSubmission, boolean reNew) {
        DataSubmissionDto dataSubmission = currentArDataSubmission.getDataSubmissionDto();
        if (dataSubmission == null || reNew) {
            dataSubmission = new DataSubmissionDto();
        }
        dataSubmission.setSubmissionType(currentArDataSubmission.getSubmissionType());
        String cycleStage = null;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(currentArDataSubmission.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT;
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(currentArDataSubmission.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_DONOR_SAMPLE;
        }
        dataSubmission.setCycleStage(cycleStage);
        dataSubmission.setAppType(currentArDataSubmission.getAppType());
        if (StringUtil.isEmpty(dataSubmission.getStatus())) {
            dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        return dataSubmission;
    }

    public static CycleDto initCycleDto(DpSuperDataSubmissionDto dpSuperDataSubmissionDto, boolean reNew) {
        CycleDto cycleDto = dpSuperDataSubmissionDto.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setHciCode(dpSuperDataSubmissionDto.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_DRP);
        String cycleType = cycleDto.getCycleType();
        if (DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO.equals(dpSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_PATIENT_DRP;
        } else if (DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(dpSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_DRP;
        } else if (DataSubmissionConsts.DP_TYPE_SBT_SOVENOR_INVENTORY.equals(dpSuperDataSubmissionDto.getSubmissionType())
                || StringUtil.isEmpty(cycleType)) {
            cycleType = DataSubmissionConsts.DS_CYCLE_SOVENOR_INVENTORY;
        }
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        cycleDto.setCycleType(cycleType);
        return cycleDto;
    }

    public static DataSubmissionDto initDataSubmission(DpSuperDataSubmissionDto dpSuperDataSubmissionDto, boolean reNew) {
        DataSubmissionDto dataSubmission = dpSuperDataSubmissionDto.getDataSubmissionDto();
        if (dataSubmission == null || reNew) {
            dataSubmission = new DataSubmissionDto();
            dpSuperDataSubmissionDto.setDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(dpSuperDataSubmissionDto.getSubmissionType());
        String cycleStage = null;
        if (DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO.equals(dpSuperDataSubmissionDto.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT;
        } else if (DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(dpSuperDataSubmissionDto.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_PRESCRIBED_DISPENSED;
        }
        dataSubmission.setCycleStage(cycleStage);
        dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        dataSubmission.setAppType(dpSuperDataSubmissionDto.getAppType());
        return dataSubmission;
    }

    public static CycleDto initCycleDto(VssSuperDataSubmissionDto vssSuperDataSubmissionDto, boolean reNew) {
        CycleDto cycleDto = vssSuperDataSubmissionDto.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setHciCode(vssSuperDataSubmissionDto.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_VSS);
        String cycleType = cycleDto.getCycleType();
        if (DataSubmissionConsts.VSS_TYPE_SBT_VSS.equals(vssSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_VSS;
        }
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        cycleDto.setCycleType(cycleType);
        return cycleDto;
    }

    public static DataSubmissionDto initDataSubmission(VssSuperDataSubmissionDto vssSuperDataSubmissionDto, boolean reNew) {
        DataSubmissionDto dataSubmission = vssSuperDataSubmissionDto.getDataSubmissionDto();
        if (dataSubmission == null || reNew) {
            dataSubmission = new DataSubmissionDto();
            vssSuperDataSubmissionDto.setDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(vssSuperDataSubmissionDto.getSubmissionType());
        String cycleStage = null;
        if (DataSubmissionConsts.VSS_TYPE_SBT_VSS.equals(vssSuperDataSubmissionDto.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_VSS;
        }
        dataSubmission.setCycleStage(cycleStage);
        dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        dataSubmission.setAppType(vssSuperDataSubmissionDto.getAppType());
        return dataSubmission;
    }

    public static boolean isNormalCycle(String cycleType) {
        return StringUtil.isIn(cycleType, new String[]{DataSubmissionConsts.DS_CYCLE_AR,
                DataSubmissionConsts.DS_CYCLE_IUI, DataSubmissionConsts.DS_CYCLE_EFO});
    }

    public static List<SelectOption> genCycleStartOptions(List<CycleDto> cycleDtos) {
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(cycleDtos)) {
            return opts;
        }
        //reverse chronological order
        Collections.sort(cycleDtos, Comparator.comparing(CycleDto::getCreatedAt).reversed());
        for (CycleDto cycleDto : cycleDtos) {
            if (!isNormalCycle(cycleDto.getCycleType())) {
                continue;
            }
            opts.add(new SelectOption(StringUtil.obscured(cycleDto.getId()),
                    Formatter.formatDate(cycleDto.getCreatedAt()) + ", " + MasterCodeUtil.getCodeDesc(cycleDto.getCycleType())));
        }
        return opts;
    }

    public static String genCycleStartHtmls(List<CycleDto> cycleDtos) {
        if (IaisCommonUtils.isEmpty(cycleDtos)) {
            return "-";
        }
        StringBuilder data = new StringBuilder();
        data.append("<select name=\"cycleStart\" id=\"cycleStart\" class=\"stageSel\" onchange=\"retriveCycleStageSelection()\">")
                .append(genOptionHtmls(genCycleStartOptions(cycleDtos)))
                .append("</select>");
        return data.toString();
    }

    public static String genOptionHtmlsWithFirst(List<String> options) {
        return genOptionHtmls(genOptions(options, "Please Select"));
    }

    public static String genOptionHtmls(List<SelectOption> options) {
        StringBuilder data = new StringBuilder();
        for (SelectOption opt : options) {
            data.append("<option value=\"").append(opt.getValue()).append("\">").append(opt.getText()).append("</option>");
        }
        return data.toString();
    }

    public static List<SelectOption> genOptions(List<String> options) {
        return genOptions(options, "Please Select");
    }

    public static List<SelectOption> genOptions(List<String> options, String firstOption) {
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isEmpty(options)) {
            if (!StringUtil.isEmpty(firstOption)) {
                opts.add(0, new SelectOption("", firstOption));
            }
            return opts;
        }
        for (String opt : options) {
            opts.add(new SelectOption(opt, MasterCodeUtil.getCodeDesc(opt)));
        }
        opts.sort(SelectOption::compareTo);
        if (!StringUtil.isEmpty(firstOption)) {
            opts.add(0, new SelectOption("", firstOption));
        }
        return opts;
    }

    public static List<SelectOption> genOptions(Map<String, String> map, String firstOption, boolean sort) {
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                opts.add(new SelectOption(entry.getKey(), entry.getValue()));
            }
        }
        if (sort) {
            Collections.sort(opts);
        }
        if (!StringUtil.isEmpty(firstOption)) {
            opts.add(0, new SelectOption("", firstOption));
        }
        return opts;
    }

    public static List<SelectOption> genOptions(Map<String, String> map) {
        return genOptions(map, "Please Select", true);
    }

    public static String getPremisesMapKey(PremisesDto premisesDto) {
        return getPremisesMapKey(premisesDto, null);
    }

    public static String getPremisesMapKey(PremisesDto premisesDto, String dsType) {
        String key = premisesDto.getHciCode();
        if (!DataSubmissionConsts.DS_AR.equals(dsType)) {
            key += ":" + premisesDto.getSvcName();
            key = StringUtil.obscured(key);
        }
        return key;
    }

    public static List<SelectOption> genPremisesOptions(Map<String, PremisesDto> premisesMap) {
        Map<String, String> map = IaisCommonUtils.genNewLinkedHashMap();
        if (premisesMap != null && !premisesMap.isEmpty()) {
            for (Map.Entry<String, PremisesDto> entry : premisesMap.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getPremiseLabel());
            }
        }
        return genOptions(map);
    }

    public static int getFileRecordMaxNumber() {
        return SystemParamUtil.getSystemParamConfig().getArFileRecordMaxNumber();
    }

    public static List<SelectOption> getNumsSelections(int startNum, int endNum) {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList(endNum + 1 - startNum);
        for (int num = startNum; num <= endNum; num++) {
            String key = String.valueOf(num);
            selectOptions.add(new SelectOption(key, key));
        }
        return selectOptions;
    }

    public static List<SelectOption> getNumsSelections(int endNum) {
        return getNumsSelections(0, endNum);
    }

    public static String getPatientAgeMessage(String person) {
        String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
        String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
        Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
        repMap.put("0", age1);
        repMap.put("1", age2);
        repMap.put("2", person);
        return MessageUtil.getMessageDesc("DS_MSG005", repMap);
    }

    public static PatientInventoryDto getCurrentPatientInventory(HttpServletRequest request) {
        return getCurrentArDataSubmission(request).getPatientInventoryDto();
    }

    public static String getLicenseeEmailAddrs(HttpServletRequest request) {
        LoginContext loginContext = getLoginContext(request);
        List<String> emailAddresses = IaisEGPHelper.getLicenseeEmailAddrs(loginContext.getLicenseeId());
        StringBuilder emailAddress = new StringBuilder();
        if (emailAddresses.isEmpty()) {
            return emailAddress.toString();
        }
        if (emailAddresses.size() == 1) {
            emailAddress.append(emailAddresses.get(0));
        } else {
            for (int i = 0; i < emailAddresses.size(); i++) {
                if (i == emailAddresses.size() - 1) {
                    emailAddress.append(emailAddresses.get(i));
                } else {
                    emailAddress.append(emailAddresses.get(i)).append(", ");
                }
            }
        }
        return emailAddress.toString();
    }

    public static String getMainTitle(ArSuperDataSubmissionDto currentSuper) {
        return getMainTitle(currentSuper != null ? currentSuper.getAppType() : null);
    }

    public static String getMainTitle(DpSuperDataSubmissionDto currentSuper) {
        return getMainTitle(currentSuper != null ? currentSuper.getAppType() : null);
    }

    public static String getMainTitle(String type) {
        String title;
        switch (type) {
            case DataSubmissionConsts.DS_APP_TYPE_RFC:
                title = DataSubmissionConstant.DS_TITLE_RFC;
                break;
            default:
                title = DataSubmissionConstant.DS_TITLE_NEW;
        }
        return title;
    }

    public static String getCode(String codeValue, List<MasterCodeView> masterCodes) {
        if (masterCodes == null || StringUtil.isEmpty(codeValue)) {
            return null;
        }
        return masterCodes.stream()
                .filter(dto -> codeValue.equals(dto.getCodeValue()))
                .map(MasterCodeView::getCode)
                .findAny()
                .orElse("");
    }

    private boolean validateCodeValue(String codeValue, List<MasterCodeView> masterCodes) {
        if (StringUtil.isEmpty(codeValue)) {
            return true;
        }
        return masterCodes.stream().anyMatch(dto -> codeValue.equals(dto.getCodeValue()));
    }

    public static Map<String, String> validateFile(String sessionName, HttpServletRequest request) {
        return validateFile(sessionName, "uploadFileError", request);
    }

    public static Map<String, String> validateFile(String sessionName, String showErrorField, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request, sessionName);
        if (fileMap == null || fileMap.isEmpty()) {
            errorMap.put(showErrorField, "GENERAL_ERR0006");
        } else {
            // only one
            Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator();
            if (!iterator.hasNext()) {
                errorMap.put(showErrorField, "GENERAL_ERR0020");
            } else {
                Map.Entry<String, File> next = iterator.next();
                File file = next.getValue();
                long length = file.length();
                if (length == 0) {
                    errorMap.put(showErrorField, "MCUPERR004");
                }
                String filename = file.getName();
                if (!FileUtils.isCsv(filename) && !FileUtils.isExcel(filename)) {

                }
            }
        }
        return errorMap;
    }

    public static int getRow(int i) {
        return i + 2;
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String profile, Map<String, ExcelPropertyDto> fieldCellMap) {
        return validateExcelList(objList, profile, getRow(0), fieldCellMap);
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String profile, int startRow,
            Map<String, ExcelPropertyDto> fieldCellMap) {
        if (objList == null || objList.isEmpty()) {
            return IaisCommonUtils.genNewArrayList(0);
        }
        List<FileErrorMsg> result = IaisCommonUtils.genNewArrayList();
        int row = startRow;
        if (fieldCellMap == null) {
            fieldCellMap = getFieldCellMap(objList.get(0).getClass());
        }
        for (T t : objList) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(t, profile);
            if (validationResult.isHasErrors()) {
                result.addAll(getExcelErrorMsgs(row++, validationResult.retrieveAll(), fieldCellMap));
            }
        }
        return result;
    }

    public static List<FileErrorMsg> getExcelErrorMsgs(int row, Map<String, String> errorMap, Map<String, ExcelPropertyDto> fieldCellMap) {
        List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList(errorMap.size());

        errorMap.forEach((k, v) -> errorMsgs.add(new FileErrorMsg(row, getFieldCell(k, fieldCellMap), v)));
        return errorMsgs;
    }

    private static ExcelPropertyDto getFieldCell(String k, Map<String, ExcelPropertyDto> fieldCellMap) {
        if (fieldCellMap == null || fieldCellMap.isEmpty()) {
            return null;
        }
        return fieldCellMap.getOrDefault(k, new ExcelPropertyDto());
    }

    public static Map<String, ExcelPropertyDto> getFieldCellMap(Class<?> clazz) {
        Map<String, ExcelPropertyDto> map = IaisCommonUtils.genNewHashMap();
        if (clazz == null) {
            return map;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExcelProperty.class)) {
                ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                map.put(field.getName(), new ExcelPropertyDto(excelProperty.cellIndex(), excelProperty.cellName(), field.getName()));
            }
        }
        return map;
    }

    public static String initAction(String dsType, String defaultAction, HttpServletRequest request) {
        DsConfig dsConfig = DsConfigHelper.getCurrentConfig(dsType, request);
        if (dsConfig != null) {
            return dsConfig.getCode();
        }
        List<DsConfig> configs = DsConfigHelper.initDsConfig(dsType, request);
        return configs.stream()
                .filter(config -> config.isActive())
                .map(DsConfig::getCode)
                .filter(Objects::nonNull)
                .findAny()
                .orElse(defaultAction);
    }

    public static String setPreviousAction(String dsType, HttpServletRequest request) {
        String actionType;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(dsType, request);
        if (currentConfig == null || 1 == currentConfig.getSeqNo()) {
            actionType = "return";
        } else {
            DsConfig config = DsConfigHelper.setPreviousActiveConfig(dsType, request);
            if (config == null) {
                actionType = "return";
            } else {
                actionType = config.getCode();
            }
        }
        return actionType;
    }

    public static String setCurrentAction(String dsType, HttpServletRequest request) {
        String actionType;
        DsConfig currentConfig = DsConfigHelper.getCurrentConfig(dsType, request);
        if (currentConfig != null) {
            actionType = currentConfig.getCode();
        } else {
            actionType = "return";
        }
        return actionType;
    }

    public static String setNextAction(String dsType, HttpServletRequest request) {
        String actionType;
        DsConfig config = DsConfigHelper.setNextActiveConfig(dsType, request);
        if (config == null) {
            actionType = "submission";
        } else {
            actionType = config.getCode();
        }
        return actionType;
    }

    public static boolean isToNextAction(HttpServletRequest request) {
        String crudType = ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE);
        if (crudType == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(crudType.substring(4));
            String dsType = crudType.substring(0, 3);
            DsConfig config = DsConfigHelper.getCurrentConfig(dsType, request);
            int j = Integer.parseInt(config.getCode().substring(4));
            return i > j;
        } catch (Exception e) {
            return false;
        }
    }

    public static Map<String, PremisesDto> setArPremisesMap(HttpServletRequest request){
        Map<String, PremisesDto> premisesMap = ( Map<String, PremisesDto>) ParamUtil.getSessionAttr(request,DataSubmissionConstant.AR_PREMISES_MAP);
        if(IaisCommonUtils.isEmpty(premisesMap)){
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId =  loginContext != null ? loginContext.getLicenseeId() : null;
            premisesMap = SpringContextHelper.getContext().getBean( ArDataSubmissionService.class).getArCenterPremises(licenseeId);
            ParamUtil.setSessionAttr(request,DataSubmissionConstant.AR_PREMISES_MAP, (Serializable) premisesMap);
        }
        return premisesMap;
    }

    public static CycleDto initCycleDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto, boolean reNew) {
        CycleDto cycleDto = ldtSuperDataSubmissionDto.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setSvcName(ldtSuperDataSubmissionDto.getSvcName());
        cycleDto.setHciCode(ldtSuperDataSubmissionDto.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_LDT);
        cycleDto.setCycleType(DataSubmissionConsts.DS_CYCLE_LDT);
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        return cycleDto;
    }

    public static DataSubmissionDto initDataSubmission(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto, boolean reNew) {
        DataSubmissionDto dataSubmission = ldtSuperDataSubmissionDto.getDataSubmissionDto();
        if (dataSubmission == null || reNew) {
            dataSubmission = new DataSubmissionDto();
        }
        dataSubmission.setSubmissionType(ldtSuperDataSubmissionDto.getSubmissionType());
        dataSubmission.setCycleStage(DataSubmissionConsts.DS_CYCLE_STAGE_LDT);
        dataSubmission.setAppType(ldtSuperDataSubmissionDto.getAppType());
        if (StringUtil.isEmpty(dataSubmission.getStatus())) {
            dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        return dataSubmission;
    }
}
