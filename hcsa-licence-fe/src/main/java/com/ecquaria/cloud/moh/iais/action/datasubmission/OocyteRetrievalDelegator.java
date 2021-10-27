package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * OocyteRetrievalDelegator
 *
 * @author jiawei
 * @date 2021/10/22
 */

@Delegator("oocyteRetrievalDelegator")
@Slf4j
public class OocyteRetrievalDelegator extends CommonDelegator {
    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, "arSuperDataSubmissionDto",new ArSuperDataSubmissionDto());
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "arSuperDataSubmissionDto");
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        if (oocyteRetrievalStageDto == null) {
            oocyteRetrievalStageDto = new OocyteRetrievalStageDto();
            arSuperDataSubmissionDto.setOocyteRetrievalStageDto(oocyteRetrievalStageDto);
        }
        ParamUtil.setRequestAttr(bpc.request, "totalRetrievedNum", oocyteRetrievalStageDto.getTotalNum());
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "arSuperDataSubmissionDto");
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        String totalNum = oocyteRetrievalStageDto.getTotalNum();
        ParamUtil.setRequestAttr(bpc.request, "totalRetrievedNum", totalNum);
        String freshOocytes = Integer.valueOf(totalNum) > 0 ? "+" + totalNum : "0";
        ParamUtil.setRequestAttr(bpc.request, "freshOocytes",freshOocytes);
    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "arSuperDataSubmissionDto");
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        HttpServletRequest request = bpc.request;
        boolean isFromPatient = "true".equals(ParamUtil.getString(request, "isFromPatient"));
        boolean isFromPatientTissue = "true".equals(ParamUtil.getString(request, "isFromPatientTissue"));
        boolean isFromDonor = "true".equals(ParamUtil.getString(request, "isFromDonor"));
        boolean isFromDonorTissue = "true".equals(ParamUtil.getString(request, "isFromDonorTissue"));
        String matureRetrievedNum = ParamUtil.getString(request, "matureRetrievedNum");
        String immatureRetrievedNum = ParamUtil.getString(request, "immatureRetrievedNum");
        String otherRetrievedNum = ParamUtil.getString(request, "otherRetrievedNum");
        boolean isOvarianSyndrome = "true".equals(ParamUtil.getString(request, "isOvarianSyndrome"));

        oocyteRetrievalStageDto.setIsFromPatient(isFromPatient);
        oocyteRetrievalStageDto.setIsFromPatientTissue(isFromPatientTissue);
        oocyteRetrievalStageDto.setIsFromDonor(isFromDonor);
        oocyteRetrievalStageDto.setIsFromDonorTissue(isFromDonorTissue);
        oocyteRetrievalStageDto.setMatureRetrievedNum(matureRetrievedNum);
        oocyteRetrievalStageDto.setImmatureRetrievedNum(immatureRetrievedNum);
        oocyteRetrievalStageDto.setOtherRetrievedNum(otherRetrievedNum);
        oocyteRetrievalStageDto.setIsOvarianSyndrome(isOvarianSyndrome);

        ValidationResult validationResult = WebValidationHelper.validateProperty(oocyteRetrievalStageDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();

        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
            ParamUtil.setSessionAttr(bpc.request, "oocyteRetrievalStageDto", oocyteRetrievalStageDto);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "confirm");
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
