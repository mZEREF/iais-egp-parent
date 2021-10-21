package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description Ajax
 * @Auther chenlei on 10/21/2021.
 */
@Controller
@Slf4j
public class ArAjaxController {

    @GetMapping(value = "/ar-cycle-stage")
    public @ResponseBody String genArCycleStages(HttpServletRequest request) {
        String currCycle = ParamUtil.getString(request, "currCycle");
        String currStage = ParamUtil.getString(request, "currStage");
        return DataSubmissionHelper.getOptions(DataSubmissionHelper.getNextStageForAr(currCycle, currStage));
    }
}
