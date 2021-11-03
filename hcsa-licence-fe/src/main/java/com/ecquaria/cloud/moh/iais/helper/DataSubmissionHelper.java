package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
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

    public static List<String> getNextStageForAR(String currCycle, String currStage, String lastStatus) {
        log.info(StringUtil.changeForLog("----- The current cycle stage is " + currCycle + " : " + currStage + " : " + lastStatus +
                " -----"));
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(currCycle)
                || DataSubmissionConsts.AR_CYCLE_NON.equals(currCycle)
                || DataSubmissionConsts.AR_STAGE_END_CYCLE.equals(currStage)
                || IaisCommonUtils.getDsFinalStatus().contains(lastStatus)) {
            result.add(DataSubmissionConsts.AR_CYCLE_AR);
            result.add(DataSubmissionConsts.AR_CYCLE_IUI);
            result.add(DataSubmissionConsts.AR_CYCLE_EFO);
            result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
            result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED);
            result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
        } else if (DataSubmissionConsts.AR_CYCLE_NON.equals(currCycle)) {
            result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
            result.add(DataSubmissionConsts.AR_STAGE_DONATION);
            result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
        } else if (DataSubmissionConsts.AR_CYCLE_AR.equals(currCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_AR.equals(currStage) || StringUtil.isEmpty(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
                result.add(DataSubmissionConsts.AR_STAGE_THAWING);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                result.add(DataSubmissionConsts.AR_STAGE_THAWING);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_THAWING.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_FERTILISATION.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_EMBRYO_CREATED.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_FREEZING.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else if (DataSubmissionConsts.AR_STAGE_DONATION.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_FERTILISATION);
                result.add(DataSubmissionConsts.AR_STAGE_PRE_IMPLANTAION_GENETIC_TESTING);
                result.add(DataSubmissionConsts.AR_STAGE_EMBRYO_TRANSFER);
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_TRANSFER_IN_AND_OUT);
                result.add(DataSubmissionConsts.AR_STAGE_AR_TREATMENT_SUBSIDIES);
                result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            } else {
                //result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            }
        } else if (DataSubmissionConsts.AR_CYCLE_IUI.equals(currCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_IUI.equals(currStage) || StringUtil.isEmpty(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY);
                result.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_IUI_TREATMENT_SUBSIDIES);
            } else {
                //result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            }
        } else if (DataSubmissionConsts.AR_CYCLE_EFO.equals(currCycle)) {
            if (DataSubmissionConsts.AR_CYCLE_EFO.equals(currStage) || StringUtil.isEmpty(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL);
            } else if (DataSubmissionConsts.AR_STAGE_OOCYTE_RETRIEVAL.equals(currStage)
                    || DataSubmissionConsts.AR_STAGE_DONATION.equals(currStage)
                    || DataSubmissionConsts.AR_STAGE_DISPOSAL.equals(currStage)) {
                result.add(DataSubmissionConsts.AR_STAGE_DONATION);
                result.add(DataSubmissionConsts.AR_STAGE_DISPOSAL);
                result.add(DataSubmissionConsts.AR_STAGE_FREEZING);
            } else {
                //result.add(DataSubmissionConsts.AR_STAGE_END_CYCLE);
            }
        }
        return result;
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

    public static List<SelectOption> genPremisesOptions(Map<String, AppGrpPremisesDto> appGrpPremisesMap) {
        Map<String, String> map = IaisCommonUtils.genNewLinkedHashMap();
        if (appGrpPremisesMap != null && !appGrpPremisesMap.isEmpty()) {
            for (Map.Entry<String, AppGrpPremisesDto> entry : appGrpPremisesMap.entrySet()) {
                map.put(entry.getKey(), getPremisesLabel(entry.getValue()));
            }
        }
        return genOptions(map);
    }

    public static String getPremisesLabel(AppGrpPremisesDto appGrpPremisesDto) {
        if (appGrpPremisesDto == null) {
            return "";
        }
        return StringUtil.escapeHtml(appGrpPremisesDto.getBusinessName() + ", " + appGrpPremisesDto.getAddress());
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

    public static CycleDto genCycleDto(CycleStageSelectionDto selectionDto, String hciCode) {
        String stage = selectionDto.getStage();
        String cycle;
        String cycleId = null;
        if (selectionDto.isUndergoingCycle()) {
            cycle = selectionDto.getLastCycle();
            cycleId = selectionDto.getLastCycleId();
        } else if (StringUtil.isIn(stage, new String[]{DataSubmissionConsts.AR_CYCLE_AR,
                DataSubmissionConsts.AR_CYCLE_IUI,
                DataSubmissionConsts.AR_CYCLE_EFO})) {
            cycle = stage;
        } else {
            cycle = DataSubmissionConsts.AR_CYCLE_NON;
        }
        CycleDto cycleDto = new CycleDto();
        cycleDto.setCycleType(cycle);
        cycleDto.setPatientCode(selectionDto.getPatientCode());
        cycleDto.setHciCode(hciCode);
        cycleDto.setId(cycleId);
        cycleDto.setStatus(DataSubmissionConsts.DS_STATUS_ACTIVE);
        return cycleDto;
    }

}
