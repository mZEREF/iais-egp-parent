package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description Data Submission Helper
 * @Auther chenlei on 10/21/2021.
 */
@Slf4j
public final class DataSubmissionHelper {

    public static List<String> getNextStageForAr(String currCycle, String currStage) {
        log.info(StringUtil.changeForLog("----- The current cycle stage is: " + currStage + " -----"));
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (StringUtil.isEmpty(currCycle)) {
            result.add(DataSubmissionConsts.AR_CYCLE_AR);
            result.add(DataSubmissionConsts.AR_CYCLE_IUI);
            result.add(DataSubmissionConsts.AR_CYCLE_EFO);
            result.add(DataSubmissionConsts.AR_CYCLE_NON);
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
            } else if (DataSubmissionConsts.AR_CYCLE_AR.equals(currStage)) {
            } else if (DataSubmissionConsts.AR_CYCLE_AR.equals(currStage)) {

            }
        }
        return result;
    }

    public static String getOptions(List<String> options) {
        return getOptions(options, "Please Select");
    }

    public static String getOptions(List<String> options, String firstOption) {
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

}
