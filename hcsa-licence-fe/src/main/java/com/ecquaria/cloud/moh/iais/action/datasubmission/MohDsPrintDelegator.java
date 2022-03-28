package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * Process: MohDsPrint
 *
 * @Description MohDsPrintDelegator
 * @Auther chenlei on 11/4/2021.
 */
@Slf4j
@Delegator("mohDsPrintDelegator")
public class MohDsPrintDelegator {

    /**
     * Step: PrepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("--- Print prepareData ---"));
        String printflag = ParamUtil.getString(bpc.request, DataSubmissionConstant.PRINT_FLAG);
        if (StringUtil.isIn(printflag, new String[]{DataSubmissionConstant.PRINT_FLAG_ACKART,
                DataSubmissionConstant.PRINT_FLAG_ACKDRP,DataSubmissionConstant.PRINT_FLAG_ACKLDT,DataSubmissionConstant.PRINT_FLAG_ACKTOP,DataSubmissionConstant.PRINT_FLAG_ACKVSS})) {
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS,
                    DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY,
                    DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        }
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, printflag);
        log.info(StringUtil.changeForLog("--- Print flag: " + printflag + " ---"));
    }

    @RequestMapping(value = "/ds/init-print", method = RequestMethod.POST)
    @ResponseBody
    public String initPrint(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("--- Print init data ---"));
        String printflag = ParamUtil.getString(request, DataSubmissionConstant.PRINT_FLAG);
        log.info(StringUtil.changeForLog("--- Print flag: " + printflag + " ---"));

        if (StringUtil.isIn(printflag, new String[]{DataSubmissionConstant.PRINT_FLAG_PTART,
                DataSubmissionConstant.PRINT_FLAG_ART})) {
            ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
            String[] declaration = ParamUtil.getStrings(request, "declaration");
            if(declaration != null && declaration.length >0){
                dataSubmissionDto.setDeclaration(declaration[0]);
            }else{
                dataSubmissionDto.setDeclaration(null);
            }
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, request);
        }else if(StringUtil.isIn(printflag, new String[]{DataSubmissionConstant.PRINT_FLAG_PTDRP,
                DataSubmissionConstant.PRINT_FLAG_DRP})){
            String declaration = ParamUtil.getString(request, "declaration");
            String remarks=ParamUtil.getRequestString(request, "remarks");
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
            dataSubmissionDto.setDeclaration(declaration);
            dataSubmissionDto.setRemarks(remarks);
            dpSuperDataSubmissionDto.setDataSubmissionDto(dataSubmissionDto);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, request);
        }
        return AppConsts.YES;
    }

}

