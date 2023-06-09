package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
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
            String templateId = ParamUtil.getString(bpc.request, "templateId");
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.EMAIL_ADDRESS,DataSubmissionHelper.getEmailAddrsByRoleIdsAndLicenseeId(bpc.request, templateId));
            ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.SUBMITTED_BY,
                    DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        }
        if(DataSubmissionConstant.PRINT_FLAG_LDT.equals(printflag)){
            String title = ParamUtil.getString(bpc.request, "title");
            ParamUtil.setRequestAttr(bpc.request, "title", title);
        }
        ParamUtil.setSessionAttr(bpc.request,"isPrintDoc","yes");
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, printflag);
        //ParamUtil.setRequestAttr(bpc.request, "DeclarationsCheckBox", "hide");
        ParamUtil.setRequestAttr(bpc.request, "headingSigns", "hide");
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
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = dpSuperDataSubmissionDto.getDataSubmissionDto();
            if(dataSubmissionDto.getSubmissionType().equals(DataSubmissionConsts.DP_TYPE_SBT_PATIENT_INFO)){
                String[] declarations = ParamUtil.getStrings(request, "declaration");
                if(declarations != null && declarations.length >0){
                    dataSubmissionDto.setDeclaration(declarations[0]);
                }else{
                    dataSubmissionDto.setDeclaration(null);
                }
            }else if((dataSubmissionDto.getSubmissionType().equals(DataSubmissionConsts.DP_TYPE_SBT_DRUG_PRESCRIBED))){
                String dpLateReasonRadio = ParamUtil.getString(request, "dpLateReasonRadio");
                String remarks=ParamUtil.getString(request, "remarks");
                String[] declaration = ParamUtil.getStrings(request, "declaration");
                dataSubmissionDto.setDpLateReasonRadio(dpLateReasonRadio);
                dataSubmissionDto.setRemarks(remarks);
                if(declaration != null && declaration.length >0){
                    dataSubmissionDto.setDeclaration(declaration[0]);
                }else{
                    dataSubmissionDto.setDeclaration(null);
                }
            }
            dpSuperDataSubmissionDto.setDataSubmissionDto(dataSubmissionDto);
            DataSubmissionHelper.setCurrentDpDataSubmission(dpSuperDataSubmissionDto, request);

        }else if(StringUtil.isIn(printflag, new String[]{DataSubmissionConstant.PRINT_FLAG_PTVSS,DataSubmissionConstant.PRINT_FLAG_VSS})){
            VssSuperDataSubmissionDto vssSuperDataSubmissionDto = DataSubmissionHelper.getCurrentVssDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = vssSuperDataSubmissionDto.getDataSubmissionDto();
            String[] declaration = ParamUtil.getStrings(request, "declaration");
            if(declaration != null && declaration.length >0){
                dataSubmissionDto.setDeclaration(declaration[0]);
            }else{
                dataSubmissionDto.setDeclaration(null);
            }
            DataSubmissionHelper.setCurrentVssDataSubmission(vssSuperDataSubmissionDto, request);
            ParamUtil.setSessionAttr(request,"isPrintDoc","yes");
        }else if(StringUtil.isIn(printflag, new String[]{DataSubmissionConstant.PRINT_FLAG_PTTOP,DataSubmissionConstant.PRINT_FLAG_TOP})) {
            TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
            DataSubmissionDto dataSubmissionDto = topSuperDataSubmissionDto.getDataSubmissionDto();
            String[] declaration = ParamUtil.getStrings(request, "declaration");
            if (declaration != null && declaration.length > 0) {
                dataSubmissionDto.setDeclaration(declaration[0]);
            } else {
                dataSubmissionDto.setDeclaration(null);
            }
            String amendReason = ParamUtil.getString(request, "amendReason");
            String amendReasonOther = ParamUtil.getString(request, "amendReasonOther");
            dataSubmissionDto.setAmendReason(amendReason);
            dataSubmissionDto.setAmendReasonOther(amendReasonOther);
            DataSubmissionHelper.setCurrentTopDataSubmission(topSuperDataSubmissionDto, request);
        }else if(StringUtil.isIn(printflag, new String[]{DataSubmissionConstant.PRINT_FLAG_LDT})) {
            LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto = DataSubmissionHelper.getCurrentLdtSuperDataSubmissionDto(request);
            DataSubmissionDto dataSubmissionDto = ldtSuperDataSubmissionDto.getDataSubmissionDto();
            String[] declaration = ParamUtil.getStrings(request, "declaration");
            if (declaration != null && declaration.length > 0) {
                dataSubmissionDto.setDeclaration(declaration[0]);
            } else {
                dataSubmissionDto.setDeclaration(null);
            }
            DataSubmissionHelper.setCurrentLdtSuperDataSubmissionDto(ldtSuperDataSubmissionDto, request);
        }
        return AppConsts.YES;
    }

}

