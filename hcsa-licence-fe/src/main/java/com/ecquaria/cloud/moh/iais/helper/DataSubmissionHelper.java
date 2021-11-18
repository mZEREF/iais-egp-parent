package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Description Data Submission Helper
 * @Auther chenlei on 10/21/2021.
 */
@Slf4j
public final class DataSubmissionHelper {

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

    public static void setCurrentArDataSubmission(ArSuperDataSubmissionDto arSuperDataSubmissionDto, HttpServletRequest request) {
        ParamUtil.setSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
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

    public static List<String> getNextStageForAR(CycleStageSelectionDto selectionDto) {
        if (selectionDto == null || StringUtil.isEmpty(selectionDto.getPatientCode())) {
            return null;
        }
        String currCycle = selectionDto.getLastCycle();
        String currStage = selectionDto.getLastStage();
        String lastStatus = selectionDto.getLastStatus();
        String latestCycle = selectionDto.getLatestCycle();
        return DataSubmissionHelper.getNextStageForAR(latestCycle, currCycle, currStage, lastStatus);
    }

    public static List<String> getNextStageForAR(String latestCycle, String currCycle, String currStage, String lastStatus) {
        log.info(StringUtil.changeForLog("----- The current cycle stage is " +
                currCycle + " : " + currStage + " : " + lastStatus + " -----"));
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(currCycle) || IaisCommonUtils.getDsCycleFinalStatus().contains(lastStatus)
                && !DataSubmissionConsts.DS_CYCLE_NON.equals(latestCycle)) {//3.3.2.1
            result.add(DataSubmissionConsts.AR_CYCLE_AR);
            result.add(DataSubmissionConsts.AR_CYCLE_IUI);
            result.add(DataSubmissionConsts.AR_CYCLE_EFO);
        } else if (DataSubmissionConsts.DS_CYCLE_NON.equals(latestCycle)
                || DataSubmissionConsts.AR_STAGE_END_CYCLE.equals(currStage)) {
            result.add(DataSubmissionConsts.AR_CYCLE_AR);
            result.add(DataSubmissionConsts.AR_CYCLE_IUI);
            result.add(DataSubmissionConsts.AR_CYCLE_EFO);
            result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        } else if (DataSubmissionConsts.DS_CYCLE_AR.equals(currCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_AR.equals(currStage) || StringUtil.isEmpty(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
                result.add(DataSubmissionConsts.AR_STAGE_THAWING);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                result.add(DataSubmissionConsts.AR_STAGE_THAWING);
            } else if (DataSubmissionConsts.AR_STAGE_THAWING.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else if (DataSubmissionConsts.AR_STAGE_FERTILISATION.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED);
            } else if (DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else if (DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else if (DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
            } else if (DataSubmissionConsts.AR_STAGE_FREEZING.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            } else if (DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            } else if (DataSubmissionConsts.AR_STAGE_DONATION.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
            }
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
            result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
        } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(currCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_IUI.equals(currStage) || StringUtil.isEmpty(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
                result.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
            }
        } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(currCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_EFO.equals(currStage) || StringUtil.isEmpty(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(currStage)
                    || DataSubmissionConsts.AR_STAGE_DONATION.equals(currStage)
                    || DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            }
        }
        return result;
    }

    public static CycleDto genCycleDto(CycleStageSelectionDto selectionDto, String hciCode) {
        String stage = selectionDto.getStage();
        String cycle;
        String cycleId = null;
        if (StringUtil.isIn(stage, new String[]{DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT,
                DataSubmissionConsts.AR_STAGE_DONATION,
                DataSubmissionConsts.AR_STAGE_DISPOSAL})) {
            cycle = DataSubmissionConsts.DS_CYCLE_NON;
        } else if (selectionDto.isUndergoingCycle()) {
            cycle = selectionDto.getLastCycle();
            cycleId = selectionDto.getLastCycleId();
        } else if (DataSubmissionConsts.AR_CYCLE_AR.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_AR;
        } else if (DataSubmissionConsts.AR_CYCLE_IUI.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_IUI;
        } else if (DataSubmissionConsts.AR_CYCLE_EFO.equals(stage)) {
            cycle = DataSubmissionConsts.DS_CYCLE_EFO;
        } else {
            cycle = DataSubmissionConsts.DS_CYCLE_NON;
        }
        CycleDto cycleDto = new CycleDto();
        cycleDto.setDsType(DataSubmissionConsts.DS_AR);
        cycleDto.setCycleType(cycle);
        cycleDto.setPatientCode(selectionDto.getPatientCode());
        cycleDto.setHciCode(hciCode);
        cycleDto.setId(cycleId);
        cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        return cycleDto;
    }

    public static CycleDto initCycleDto(ArSuperDataSubmissionDto currentArDataSubmission, boolean reNew) {
        CycleDto cycleDto = currentArDataSubmission.getCycleDto();
        if (cycleDto == null || reNew) {
            cycleDto = new CycleDto();
        }
        String hicCode = Optional.ofNullable(currentArDataSubmission.getPremisesDto())
                .map(premises -> premises.getHciCode())
                .orElse("");
        cycleDto.setHciCode(hicCode);
        cycleDto.setDsType(currentArDataSubmission.getDsType());
        String cycleType = cycleDto.getCycleType();
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(currentArDataSubmission.getArSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_PATIENT_ART;
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(currentArDataSubmission.getArSubmissionType())) {
            cycleType = DataSubmissionConsts.DS_CYCLE_DONOR_SAMPLE;
        } else if (DataSubmissionConsts.AR_TYPE_SBT_CYCLE_STAGE.equals(currentArDataSubmission.getArSubmissionType())
                || StringUtil.isEmpty(cycleType)) {
            cycleType = DataSubmissionConsts.DS_CYCLE_STAGE;
        }
        cycleDto.setCycleType(cycleType);
        return cycleDto;
    }

    public static DataSubmissionDto initDataSubmission(ArSuperDataSubmissionDto currentArDataSubmission, boolean reNew) {
        DataSubmissionDto dataSubmission = currentArDataSubmission.getCurrentDataSubmissionDto();
        if (dataSubmission == null || reNew) {
            dataSubmission = new DataSubmissionDto();
            currentArDataSubmission.setCurrentDataSubmissionDto(dataSubmission);
        }
        dataSubmission.setSubmissionType(currentArDataSubmission.getSubmissionType());
        String cycleStage = null;
        if (DataSubmissionConsts.AR_TYPE_SBT_PATIENT_INFO.equals(currentArDataSubmission.getArSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_PATIENT;
        } else if (DataSubmissionConsts.AR_TYPE_SBT_DONOR_SAMPLE.equals(currentArDataSubmission.getArSubmissionType())) {
            cycleStage = DataSubmissionConsts.DS_CYCLE_STAGE_DONOR_SAMPLE;
        }
        dataSubmission.setCycleStage(cycleStage);
        dataSubmission.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        return dataSubmission;
    }

    public static String genOptionHtmls(List<String> options) {
        return genOptionHtmls(options, "Please Select");
    }

    public static String genOptionHtmls(List<String> options, String firstOption) {
        StringBuilder opts = new StringBuilder();
        if (!StringUtil.isEmpty(firstOption)) {
            opts.append("<option value=\"\">").append(StringUtil.escapeHtml(firstOption)).append("</option>");
        }
        if (options == null || options.isEmpty()) {
            return opts.toString();
        }
        for (String opt : options) {
            opts.append("<option value=\"").append(opt).append("\">").append(MasterCodeUtil.getCodeDesc(opt)).append("</option>");
        }
        return opts.toString();
    }

    public static List<SelectOption> genOptions(List<String> options) {
        return genOptions(options, "Please Select");
    }

    public static List<SelectOption> genOptions(List<String> options, String firstOption) {
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(firstOption)) {
            opts.add(0, new SelectOption("", firstOption));
        }
        if (options == null || options.isEmpty()) {
            return opts;
        }
        for (String opt : options) {
            opts.add(new SelectOption(opt, MasterCodeUtil.getCodeDesc(opt)));
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

    public static List<SelectOption> genPremisesOptions(Map<String, PremisesDto> premisesMap) {
        Map<String, String> map = IaisCommonUtils.genNewLinkedHashMap();
        if (premisesMap != null && !premisesMap.isEmpty()) {
            for (Map.Entry<String, PremisesDto> entry : premisesMap.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getPremiseLabel());
            }
        }
        return genOptions(map);
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

    public static String getPatientAgeMessage() {
        String age1 = MasterCodeUtil.getCodeDesc("PT_AGE_001");
        String age2 = MasterCodeUtil.getCodeDesc("PT_AGE_002");
        Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
        repMap.put("0", age1);
        repMap.put("1", age2);
        return MessageUtil.getMessageDesc("DS_MSG005", repMap);
    }

    public static PatientInventoryDto initPatientInventoryTable(HttpServletRequest request) {
        PatientInventoryDto patientInventoryDto = new PatientInventoryDto();
        ParamUtil.setRequestAttr(request, "patientInventoryDto", patientInventoryDto);
        return patientInventoryDto;
    }

}
