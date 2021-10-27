package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ThawingDelegator
 *
 * @author jiawei
 * @date 2021/10/27
 */

@Delegator("thawingDelegator")
@Slf4j

public class ThawingDelegator extends CommonDelegator {
    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, new ArSuperDataSubmissionDto());
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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        if (thawingStageDto == null) {
            thawingStageDto = new ThawingStageDto();
            arSuperDataSubmissionDto.setThawingStageDto(thawingStageDto);
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();

        String changeFrozenOocytes = thawingStageDto.getThawedOocytesNum() > 0 ? "+" + thawingStageDto.getThawedOocytesNum() : "0";
        String changeThawedOocytes = thawingStageDto.getThawedOocytesSurvivedMatureNum() > 0 ? "+" + thawingStageDto.getThawedOocytesSurvivedMatureNum() : "0";
        String changeFrozenEmbryos = thawingStageDto.getThawedEmbryosNum() > 0 ? "+" + thawingStageDto.getThawedEmbryosNum() : "0";
        String changeThawedEmbryos = thawingStageDto.getThawedEmbryosSurvivedNum() > 0 ? "+" + thawingStageDto.getThawedEmbryosSurvivedNum() : "0";
        ParamUtil.setRequestAttr(bpc.request, "changeFrozenOocytes", changeFrozenOocytes);
        ParamUtil.setRequestAttr(bpc.request, "changeThawedOocytes", changeThawedOocytes);
        ParamUtil.setRequestAttr(bpc.request, "changeFrozenEmbryos", changeFrozenEmbryos);
        ParamUtil.setRequestAttr(bpc.request, "changeThawedEmbryos", changeThawedEmbryos);
    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(thawingStageDto, request);

        ValidationResult validationResult = WebValidationHelper.validateProperty(thawingStageDto, "save");
        Map<String, String> errorMap = validationResult.retrieveAll();

        if (!errorMap.isEmpty() || validationResult.isHasErrors()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
            ParamUtil.setSessionAttr(bpc.request, "thawingStageDto", thawingStageDto);
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.CRUD_ACTION_TYPE, "confirm");
    }

    private void fromPageData(ThawingStageDto thawingStageDto, HttpServletRequest request) {
        boolean hasOocyte = "true".equals(ParamUtil.getString(request, "hasOocyte"));
        boolean hasEmbryo = "true".equals(ParamUtil.getString(request, "hasEmbryo"));
        int thawedOocytesNum = ParamUtil.getInt(request, "thawedOocytesNum");
        int thawedOocytesSurvivedMatureNum = ParamUtil.getInt(request, "thawedOocytesSurvivedMatureNum");
        int thawedOocytesSurvivedImmatureNum = ParamUtil.getInt(request, "thawedOocytesSurvivedImmatureNum");
        int thawedOocytesSurvivedOtherNum = ParamUtil.getInt(request, "thawedOocytesSurvivedOtherNum");
        int thawedEmbryosNum = ParamUtil.getInt(request, "thawedEmbryosNum");
        int thawedEmbryosSurvivedNum = ParamUtil.getInt(request, "thawedEmbryosSurvivedNum");

        thawingStageDto.setHasOocyte(hasOocyte);
        thawingStageDto.setHasEmbryo(hasEmbryo);
        thawingStageDto.setThawedOocytesNum(thawedOocytesNum);
        thawingStageDto.setThawedOocytesSurvivedMatureNum(thawedOocytesSurvivedMatureNum);
        thawingStageDto.setThawedOocytesSurvivedImmatureNum(thawedOocytesSurvivedImmatureNum);
        thawingStageDto.setThawedOocytesSurvivedOtherNum(thawedOocytesSurvivedOtherNum);
        thawingStageDto.setThawedEmbryosNum(thawedEmbryosNum);
        thawingStageDto.setThawedEmbryosSurvivedNum(thawedEmbryosSurvivedNum);
    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
