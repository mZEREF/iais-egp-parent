package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsConfigHelper;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelPropertyDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.DpDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopDataSubmissionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.VssDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description Data Submission Helper
 * @Auther chenlei on 10/21/2021.
 */
@Slf4j
public final class DataSubmissionHelper {

    public static void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(DataSubmissionConstant.AR_DATA_LIST);
        session.removeAttribute(DataSubmissionConstant.DP_DATA_LIST);
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
        session.removeAttribute(DataSubmissionConstant.LDT_IS_GUIDE);
        session.removeAttribute(DataSubmissionConstant.AR_TRANSFER_OUT_IN_PREMISES_SEL);
        session.removeAttribute(DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_ID);
        session.removeAttribute(DataSubmissionConstant.AR_TRANSFER_BIND_STAGE_SUPER_DTO);
        // clear session title
        session.removeAttribute("title");
        session.removeAttribute("count");
    }

    public static LoginContext getLoginContext(HttpServletRequest request) {
        if (request == null) {
            log.info("------Request is null------");
            return null;
        }
        return (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
    }

    public static String getLicenseeId(HttpServletRequest request) {
        LoginContext loginContext = getLoginContext(request);
        String licenseeId = "";
        if (loginContext != null) {
            licenseeId = loginContext.getLicenseeId();
        }
        return licenseeId;
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
        newDto.setCentreSel(currentSuper.getCentreSel());
        newDto.setAppType(currentSuper.getAppType());
        newDto.setOrgId(currentSuper.getOrgId());
        newDto.setLicenseeId(currentSuper.getLicenseeId());
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
        currentSuper.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return newDto;
    }

    public static DpSuperDataSubmissionDto dpReNew(DpSuperDataSubmissionDto currentSuper) {
        DpSuperDataSubmissionDto newDto = new DpSuperDataSubmissionDto();
        newDto.setAppType(currentSuper.getAppType());
        newDto.setOrgId(currentSuper.getOrgId());
        newDto.setSubmissionType(currentSuper.getSubmissionType());
        newDto.setPremisesDto(currentSuper.getPremisesDto());
        newDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        newDto.setDraftId(currentSuper.getDraftId());
        newDto.setDraftNo(currentSuper.getDraftNo());
        newDto.setPremises(currentSuper.getPremises());
        DataSubmissionDto dataSubmissionDto = DataSubmissionHelper.initDataSubmission(newDto, true);
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        dataSubmissionDto.setDeclaration(null);
        newDto.setDataSubmissionDto(dataSubmissionDto);
        newDto.setCycleDto(DataSubmissionHelper.initCycleDto(newDto, currentSuper.getCycleDto().getLicenseeId(), true));
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

    public static TopSuperDataSubmissionDto getCurrentTopDataSubmission(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = (TopSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.TOP_DATA_SUBMISSION);
        if (topSuperDataSubmissionDto == null) {
            log.info("------------------------------------TOP_SUPER_DATA_SUBMISSION_DTO is null-----------------");
        }
        return topSuperDataSubmissionDto;
    }

    public static void setCurrentTopDataSubmission(TopSuperDataSubmissionDto topSuperDataSubmissionDto, HttpServletRequest request) {
        DataSubmissionHelper.setTopPremisesMap(request);
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_DATA_SUBMISSION, topSuperDataSubmissionDto);
    }

    public static LdtSuperDataSubmissionDto getCurrentLdtSuperDataSubmissionDto(HttpServletRequest request) {
        LdtSuperDataSubmissionDto LdtSuperDataSubmissionDto = (LdtSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.LAB_SUPER_DATA_SUBMISSION);
        if (LdtSuperDataSubmissionDto == null) {
            log.info("------------------------------------LdtSuperDataSubmissionDto is null-----------------");
        }
        return LdtSuperDataSubmissionDto;
    }

    public static void setCurrentLdtSuperDataSubmissionDto(LdtSuperDataSubmissionDto LdtSuperDataSubmissionDto,
            HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.LAB_SUPER_DATA_SUBMISSION, LdtSuperDataSubmissionDto);
    }

    public static LdtSuperDataSubmissionDto getOldLdtSuperDataSubmissionDto(HttpServletRequest request) {
        LdtSuperDataSubmissionDto LdtSuperDataSubmissionDto = (LdtSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.LDT_OLD_DATA_SUBMISSION);
        if (LdtSuperDataSubmissionDto == null) {
            log.info("------------------------------------getOldLdtSuperDataSubmissionDto is null-----------------");
        }
        return LdtSuperDataSubmissionDto;
    }

    public static DpSuperDataSubmissionDto getOldDpSuperDataSubmissionDto(HttpServletRequest request) {
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = (DpSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.DP_OLD_DATA_SUBMISSION);
        if (dpSuperDataSubmissionDto == null) {
            log.info("------------------------------------getOldDpSuperDataSubmissionDto is null-----------------");
        }
        return dpSuperDataSubmissionDto;
    }

    public static VssSuperDataSubmissionDto getOldVssSuperDataSubmissionDto(HttpServletRequest request) {
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.VSS_OLD_DATA_SUBMISSION);
        if (vssSuperDataSubmissionDto == null) {
            log.info("------------------------------------getOldDpSuperDataSubmissionDto is null-----------------");
        }
        return vssSuperDataSubmissionDto;
    }

    public static TopSuperDataSubmissionDto getOldTopSuperDataSubmissionDto(HttpServletRequest request) {
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = (TopSuperDataSubmissionDto) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.TOP_OLD_DATA_SUBMISSION);
        if (topSuperDataSubmissionDto == null) {
            log.info("------------------------------------getOldTopSuperDataSubmissionDto is null-----------------");
        }
        return topSuperDataSubmissionDto;
    }

    /**
     * Cycle Stages
     * <p>
     * Spec: 3.3.3 Stage Selection Business Rules
     *
     * @param selectionDto
     * @return
     */
    public static List<String> getNextStageForAR(CycleStageSelectionDto selectionDto) {
        if (selectionDto == null || StringUtils.isEmpty(selectionDto.getPatientCode())) {
            return Collections.emptyList();
        }
        log.info(StringUtil.changeForLog("----- The Cycle Stage Selection: " + JsonUtil.parseToJson(selectionDto) + " ----- "));
        String lastCycle = selectionDto.getLastCycle();
        String lastStage = selectionDto.getLastStage();
        String lastStatus = selectionDto.getLastStatus();
        boolean undergoingCycle = selectionDto.isUndergoingCycle();
        boolean frozenOocyte = selectionDto.isFrozenOocyte();
        boolean frozenEmbryo = selectionDto.isFrozenEmbryo();
        boolean freshNatural = selectionDto.isFreshNatural();
        boolean freshStimulated = selectionDto.isFreshStimulated();
        // 3.3.3.2 (4) If the predecessor stage is AR Treatment Co-funding or Transfer In & Out,
        // available stages for selection will be based on the stage prior to it
        // disposal, donation
        if (DataSubmissionConsts.DS_CYCLE_AR.equals(lastCycle) && DsHelper.isSpecialStage(lastStage)) {
            lastStage = selectionDto.getAdditionalStage();
        }
        List<String> result = getNextStagesForAr(lastCycle, lastStage, lastStatus, undergoingCycle, frozenOocyte, frozenEmbryo, freshNatural, freshStimulated);
        log.info(StringUtil.changeForLog("----- The Next Stages: " + result + " ----- "));
        return result;
    }

    public static List<String> getNextStagesForAr(String lastCycle, String lastStage, String lastStatus, boolean undergoingCycle, boolean frozenOocyte, boolean frozenEmbryo, boolean freshNatural, boolean freshStimulated) {
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (StringUtils.isEmpty(lastCycle)) {
            addStartStages(result);
            //TODO Need to reorganize
//        } else if (StringUtils.isEmpty(lastStage)
//                || DataSubmissionConsts.AR_STAGE_END_CYCLE.equals(lastStage)
//                || DsHelper.isCycleFinalStatus(lastStatus)) {
//            if (!undergoingCycle) {
//                addStartStages(result);
//            }
//            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
//            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        } else if (DataSubmissionConsts.DS_CYCLE_AR.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_AR.equals(lastStage)) {
                if (freshNatural || freshStimulated) {
                    result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
                }
                if (frozenOocyte || frozenEmbryo) {
                    result.add(DataSubmissionConsts.AR_STAGE_THAWING);
                }
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                if (frozenOocyte) {
                    result.add(DataSubmissionConsts.AR_STAGE_THAWING);
                }
            } else if (DataSubmissionConsts.AR_STAGE_THAWING.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                if (frozenEmbryo) {
                    result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                    result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                }
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
                if (!undergoingCycle && DsHelper.isSpecialFinalStatus(lastStatus)) {
                    addStartStages(result);
                }
            } else if (DataSubmissionConsts.AR_STAGE_FREEZING.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            }  else if (DataSubmissionConsts.AR_STAGE_DONATION.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            }
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
            result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
        } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_IUI.equals(lastStage) || StringUtil.isEmpty(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(lastStage)) {
                if (!undergoingCycle && DsHelper.isSpecialFinalStatus(lastStatus)) {
                    addStartStages(result);
                }
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED);
                result.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
            } else if (DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED);
            }
        } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_EFO.equals(lastStage) || StringUtil.isEmpty(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(lastStage)
                    || DataSubmissionConsts.AR_STAGE_DONATION.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            }
        } else if (DataSubmissionConsts.DS_CYCLE_SFO.equals(lastCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_SFO.equals(lastStage) || StringUtil.isEmpty(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            } else if (DataSubmissionConsts.AR_STAGE_DONATION.equals(lastStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            }
        }
        return result;
    }

    private static void addStartStages(List<String> result) {
        result.add(DataSubmissionConsts.AR_CYCLE_AR);
        result.add(DataSubmissionConsts.AR_CYCLE_IUI);
        result.add(DataSubmissionConsts.AR_CYCLE_EFO);
        result.add(DataSubmissionConsts.AR_CYCLE_SFO);
    }

    public static CycleDto initCycleDto(CycleStageSelectionDto selectionDto, String serviceName, String hciCode, String licenseeId) {
        String stage = selectionDto.getStage();
        String cycle;
        String cycleId = null;
        CycleDto cycleDto = null;
        // 3.3.3.3.1 Transfer In & Out stage will always tagged under Non-cycles.
        if (DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_NON;
        } else if (selectionDto.isUndergoingCycle() && !DsHelper.isCycleFinalStatusWithSpec(selectionDto.getLastStatus())) {
            cycleDto = selectionDto.getLastCycleDto();
            cycle = cycleDto.getCycleType();
            cycleId = cycleDto.getId();
        } else if (StringUtil.isIn(stage, new String[]{
                DataSubmissionConsts.AR_STAGE_DONATION,
                DataSubmissionConsts.AR_STAGE_DISPOSAL})) {
            cycle = DataSubmissionConsts.DS_CYCLE_NON;
        } else if (DataSubmissionConsts.AR_CYCLE_AR.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_AR;
        } else if (DataSubmissionConsts.AR_CYCLE_IUI.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_IUI;
        } else if (DataSubmissionConsts.AR_CYCLE_EFO.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_EFO;
        } else if (DataSubmissionConsts.AR_CYCLE_SFO.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_SFO;
        } else if (DsHelper.isSpecialFinalStatus(selectionDto.getLastStatus())) {
            cycleDto = selectionDto.getLastCycleDto();
            cycle = cycleDto.getCycleType();
            cycleId = cycleDto.getId();
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
        cycleDto.setLicenseeId(licenseeId);
        cycleDto.setDsType(DataSubmissionConsts.DS_AR);
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ONGOING);
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
        String licenseeId = currentArDataSubmission.getLicenseeId();
        if (StringUtil.isEmpty(licenseeId)) {
            licenseeId = getLicenseeId(MiscUtil.getCurrentRequest());
            currentArDataSubmission.setLicenseeId(licenseeId);
        }
        cycleDto.setLicenseeId(licenseeId);
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

    public static CycleDto initCycleDto(DpSuperDataSubmissionDto dpSuperDataSubmissionDto, String licenseeId, boolean reNew) {
        CycleDto cycleDto = dpSuperDataSubmissionDto.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setSvcName(dpSuperDataSubmissionDto.getSvcName());
        cycleDto.setHciCode(dpSuperDataSubmissionDto.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_DRP);
        String cycleType = cycleDto.getCycleType();
        if (DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO.equals(dpSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_PATIENT_DRP;
        } else if (DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED.equals(dpSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_DRP_PRESCRIBED;
        } else if (DataSubmissionConsts.DP_TYPE_SBT_SOVENOR_INVENTORY.equals(dpSuperDataSubmissionDto.getSubmissionType())
                || StringUtil.isEmpty(cycleType)) {
            cycleType = DataSubmissionConsts.DS_CYCLE_SOVENOR_INVENTORY;
        }
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        cycleDto.setLicenseeId(licenseeId);
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

    public static CycleDto initCycleDto(VssSuperDataSubmissionDto vssSuperDataSubmissionDto, String licenseeId, boolean reNew) {
        CycleDto cycleDto = vssSuperDataSubmissionDto.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setSvcName(vssSuperDataSubmissionDto.getSvcName());
        cycleDto.setHciCode(vssSuperDataSubmissionDto.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_VSS);
        String cycleType = cycleDto.getCycleType();
        if (DataSubmissionConsts.VSS_TYPE_SBT_VSS.equals(vssSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_VSS;
        }
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        cycleDto.setLicenseeId(licenseeId);
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

    public static CycleDto initCycleDto(TopSuperDataSubmissionDto topSuperDataSubmissionDto, String licenseeId, boolean reNew) {
        CycleDto cycleDto = topSuperDataSubmissionDto.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        cycleDto.setSvcName(topSuperDataSubmissionDto.getSvcName());
        cycleDto.setHciCode(topSuperDataSubmissionDto.getHciCode());
        cycleDto.setDsType(DataSubmissionConsts.DS_TOP);
        String cycleType = cycleDto.getCycleType();
        /*if (DataSubmissionConsts.TOP_TYPE_SBT_PATIENT_INFO.equals(topSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_PATIENT_TOP;
        }else*/
        if (DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE.equals(topSuperDataSubmissionDto.getSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_ERMINATION_TOP;
        }
        if (StringUtil.isEmpty(cycleDto.getStatus())) {
            cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        }
        cycleDto.setCycleType(cycleType);
        cycleDto.setLicenseeId(licenseeId);
        return cycleDto;
    }

    public static DataSubmissionDto initDataSubmission(TopSuperDataSubmissionDto topSuperDataSubmissionDto, boolean reNew) {
        DataSubmissionDto dataSubmission = topSuperDataSubmissionDto.getDataSubmissionDto();
        if (dataSubmission == null || reNew) {
            dataSubmission = new DataSubmissionDto();
            topSuperDataSubmissionDto.setDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(topSuperDataSubmissionDto.getSubmissionType());
        String cycleStage = null;
        /*if (DataSubmissionConsts.TOP_TYPE_SBT_PATIENT_INFO.equals(topSuperDataSubmissionDto.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_TOPPATIENT;
        }else */
        if (DataSubmissionConsts.TOP_TYPE_SBT_TERMINATION_OF_PRE.equals(topSuperDataSubmissionDto.getSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_TERMINATION;
        }
        dataSubmission.setCycleStage(cycleStage);
        dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        dataSubmission.setAppType(topSuperDataSubmissionDto.getAppType());
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
        Collections.sort(cycleDtos, Comparator.comparing(CycleDto::getCreatedDate).reversed());
        for (CycleDto cycleDto : cycleDtos) {
            if (!isNormalCycle(cycleDto.getCycleType())) {
                continue;
            }
            opts.add(new SelectOption(StringUtil.obscured(cycleDto.getId()),
                    Formatter.formatDate(cycleDto.getCreatedDate()) + ", " + MasterCodeUtil.getCodeDesc(cycleDto.getCycleType())));
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

    public static List<String> getAllARCycleStages() {
        List<String> stages = new ArrayList<>(14);
        stages.add(DataSubmissionConsts.AR_CYCLE_AR);
        stages.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
        stages.add(DataSubmissionConsts.AR_STAGE_FREEZING);
        stages.add(DataSubmissionConsts.AR_STAGE_THAWING);
        stages.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
        stages.add(DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED);
        stages.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
        stages.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
        stages.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED);//todo may change OutCome
//        combine to all ar cycle stage after
//        stages.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
        stages.add(DataSubmissionConsts.AR_STAGE_DONATION);
        stages.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        stages.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
        stages.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);

        return stages;
    }

    public static List<String> getAllIUICycleStages(){
        List<String> stages = new ArrayList<>(3);
        stages.add(DataSubmissionConsts.AR_CYCLE_IUI);
        stages.add(DataSubmissionConsts.AR_STAGE_OUTCOME);
        stages.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
        return stages;
    }

    public static List<String> getAllOFOCycleStages(){
        List<String> stages = new ArrayList<>(4);
        stages.add(DataSubmissionConsts.AR_CYCLE_EFO);
        stages.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
        stages.add(DataSubmissionConsts.AR_STAGE_DONATION);
        return stages;
    }

    public static List<String> getAllSFOCycleStages(){
        List<String> stages = new ArrayList<>(3);
        stages.add(DataSubmissionConsts.AR_CYCLE_SFO);
        stages.add(DataSubmissionConsts.AR_STAGE_DONATION);
        return stages;
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

    public static String getAgeMessage(String person) {
        String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
        String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
        Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
        repMap.put("0", age1);
        repMap.put("1", age2);
        repMap.put("2", person);
        return MessageUtil.getMessageDesc("DS_MSG005", repMap);
    }

    public static ArCurrentInventoryDto getCurrentArCurrentInventoryDto(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = getCurrentArDataSubmission(request);
        return arSuperDataSubmissionDto != null ? arSuperDataSubmissionDto.getArCurrentInventoryDto() : null;
    }

    public static ArChangeInventoryDto getCurrentArChangeInventoryDto(HttpServletRequest request) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = getCurrentArDataSubmission(request);
        ArChangeInventoryDto arChangeInventoryDto = arSuperDataSubmissionDto != null ? arSuperDataSubmissionDto.getArChangeInventoryDto() : new ArChangeInventoryDto();
        if (arChangeInventoryDto == null) {
            arChangeInventoryDto = new ArChangeInventoryDto();
            arSuperDataSubmissionDto.setArChangeInventoryDto(arChangeInventoryDto);
        }
        return arChangeInventoryDto;
    }

    public static String getLicenseeEmailAddrs(HttpServletRequest request) {
        return getEmailAddrsByRoleIdsAndLicenseeId(request, null);
    }

    public static String getEmailAddrsByRoleIdsAndLicenseeId(HttpServletRequest request, List<String> roleIds) {
        LoginContext loginContext = getLoginContext(request);
        List<String> emailAddresses;
        if(IaisCommonUtils.isEmpty(roleIds)){
            emailAddresses = IaisEGPHelper.getLicenseeEmailAddrs(loginContext.getLicenseeId());
        } else {
            List<OrgUserDto> orgUserDtoList = IaisEGPHelper.getLicenseeAccountByRolesAndLicenseeId(loginContext.getLicenseeId(), roleIds);
            emailAddresses = orgUserDtoList != null ?orgUserDtoList.stream().map(OrgUserDto::getEmail).distinct().collect(Collectors.toList()) : new ArrayList<>();
        }
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

    public static String getSmallTitle(String dsType, String appType, String submissionType) {
        StringBuilder title = new StringBuilder();
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(appType)) {
            title.append("You are amending for <strong>");
        } else {
            title.append("You are submitting for <strong>");
        }
        switch (dsType) {
            case DataSubmissionConsts.DS_AR:
                if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(submissionType)) {
                    title.append(DataSubmissionConstant.DS_TITLE_PATIENT);
                } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(submissionType)) {
                    title.append(DataSubmissionConstant.DS_TITLE_DONOR_SAMPLE);
                } else {
                    title.append(DataSubmissionConstant.DS_TITLE_CYCEL_STAGE);
                }
                break;
            case DataSubmissionConsts.DS_DRP:
                title.append(DataSubmissionConstant.DS_TITLE_DRP);
                break;
            case DataSubmissionConsts.DS_LDT:
                title.append(DataSubmissionConstant.DS_TITLE_LDT);
                break;
            case DataSubmissionConsts.DS_TOP:
                title.append(DataSubmissionConstant.DS_TITLE_TOP);
                break;
        }
        title.append("</strong>");
        return title.toString();
    }

    public static String getCode(String codeValue, List<MasterCodeView> masterCodes) {
        if (masterCodes == null || StringUtil.isEmpty(codeValue)) {
            return null;
        }
        return masterCodes.stream()
                .filter(dto -> codeValue.equals(dto.getCodeValue()))
                .map(MasterCodeView::getCode)
                .findAny()
                .orElse(DataSubmissionConstant.DFT_ERROR_MC);
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
                    log.info("The file length is 0!!!");
                    errorMap.put(showErrorField, "MCUPERR004");
                }
                String filename = file.getName();
                if (!FileUtils.isCsv(filename) && !FileUtils.isExcel(filename)) {
                    log.info(StringUtil.changeForLog("Invalid file - " + filename));
                    errorMap.put(showErrorField, MessageUtil.replaceMessage("GENERAL_ERR0018", "XLSX, CSV","fileType"));
                }
            }
        }
        return errorMap;
    }

    public static int getRow(int i) {
        return i + 2;
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String profile,
            Map<String, ExcelPropertyDto> fieldCellMap) {
        return validateExcelList(objList, profile, getRow(0) - 1, fieldCellMap);
    }

    public static <T> List<FileErrorMsg> validateExcelList(List<T> objList, String profile, int startRowIndex,
            Map<String, ExcelPropertyDto> fieldCellMap) {
        return ExcelValidatorHelper.validateExcelList(objList, profile, startRowIndex, fieldCellMap);
    }

    public static String initAction(String dsType, String defaultAction, HttpServletRequest request) {
        DsConfig dsConfig = DsConfigHelper.getCurrentConfig(dsType, request);
        if (dsConfig != null) {
            return dsConfig.getCode();
        }
        List<DsConfig> configs = DsConfigHelper.initDsConfig(dsType, request);
        return configs.stream()
                .filter(DsConfig::isActive)
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

    public static Map<String, PremisesDto> setArPremisesMap(HttpServletRequest request) {
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.AR_PREMISES_MAP);
        if (IaisCommonUtils.isEmpty(premisesMap)) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId = loginContext != null ? loginContext.getLicenseeId() : null;
            premisesMap = SpringContextHelper.getContext().getBean(ArDataSubmissionService.class).getArCenterPremises(licenseeId);
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_PREMISES_MAP, (Serializable) premisesMap);
        }
        return premisesMap;
    }

    public static Map<String, PremisesDto> setTopPremisesMap(HttpServletRequest request) {
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.TOP_PREMISES_MAP);
        if (IaisCommonUtils.isEmpty(premisesMap)) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId = loginContext != null ? loginContext.getLicenseeId() : null;
            premisesMap = SpringContextHelper.getContext().getBean(TopDataSubmissionService.class).getTopCenterPremises(licenseeId);
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.TOP_PREMISES_MAP, (Serializable) premisesMap);
        }
        return premisesMap;
    }
    public static Map<String, PremisesDto> setDpPremisesMap(HttpServletRequest request) {
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.DP_PREMISES_MAP);
        if (IaisCommonUtils.isEmpty(premisesMap)) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId = loginContext != null ? loginContext.getLicenseeId() : null;
            premisesMap = SpringContextHelper.getContext().getBean(DpDataSubmissionService.class).getDpCenterPremises(licenseeId);
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.DP_PREMISES_MAP, (Serializable) premisesMap);
        }
        return premisesMap;
    }

    public static Map<String, PremisesDto> setVsPremisesMap(HttpServletRequest request) {
        Map<String, PremisesDto> premisesMap = (Map<String, PremisesDto>) ParamUtil.getSessionAttr(request,
                DataSubmissionConstant.VSS_PREMISES_MAP);
        if (IaisCommonUtils.isEmpty(premisesMap)) {
            LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
            String licenseeId = loginContext != null ? loginContext.getLicenseeId() : null;
            premisesMap = SpringContextHelper.getContext().getBean(VssDataSubmissionService.class).getVssCenterPremises(licenseeId);
            ParamUtil.setSessionAttr(request, DataSubmissionConstant.VSS_PREMISES_MAP, (Serializable) premisesMap);
        }
        return premisesMap;
    }

    public static CycleDto initCycleDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto, String licenseeId, boolean reNew) {
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
        cycleDto.setLicenseeId(licenseeId);
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

    public static void setGoBackUrl(HttpServletRequest request){
        String URL = InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox";
        StringBuilder url = new StringBuilder();
        url.append(InboxConst.URL_HTTPS)
                .append(request.getServerName())
                .append(URL);
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        ParamUtil.setRequestAttr(request,"goBackUrl",tokenUrl);
    }

}
