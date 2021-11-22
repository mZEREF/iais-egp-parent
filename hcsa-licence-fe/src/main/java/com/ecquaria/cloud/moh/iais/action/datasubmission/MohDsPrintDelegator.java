package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * Process: MohDsPrint
 *
 * @Description MohDsPrintDelegator
 * @Auther chenlei on 11/4/2021.
 */
@Slf4j
@Delegator("mohDsPrintDelegator")
public class MohDsPrintDelegator {

    private static final String PRINT_FLAG = "printflag";

    /**
     * Step: PrepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("--- Print prepareData ---"));
        String printflag = ParamUtil.getString(bpc.request, PRINT_FLAG);
        ParamUtil.setRequestAttr(bpc.request, PRINT_FLAG, printflag);
        log.info(StringUtil.changeForLog("--- Print flag: " + printflag + " ---"));
    }

    @RequestMapping(value = "/ds/init-print", method = RequestMethod.POST)
    public @ResponseBody
    String initPrint(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("--- Print init data ---"));
        String printflag = ParamUtil.getString(request, PRINT_FLAG);
        log.info(StringUtil.changeForLog("--- Print flag: " + printflag + " ---"));
        if (DataSubmissionConsts.DS_PATIENT_ART.equals(printflag)) {
            String declaration = ParamUtil.getString(request, "declaration");
            ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
            dataSubmissionDto.setDeclaration(declaration);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, request);
        }
        return AppConsts.YES;
    }

}
