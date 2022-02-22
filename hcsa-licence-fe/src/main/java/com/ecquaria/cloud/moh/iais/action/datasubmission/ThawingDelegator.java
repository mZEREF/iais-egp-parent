package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
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
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Thawing (Oocytes & Embryos) Stage</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        if (thawingStageDto == null) {
            thawingStageDto = new ThawingStageDto();
            arSuperDataSubmissionDto.setThawingStageDto(thawingStageDto);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        ArChangeInventoryDto arChangeInventoryDto = DataSubmissionHelper.getCurrentArChangeInventoryDto(bpc.request);
        if (thawingStageDto.getHasOocyte()) {
            arChangeInventoryDto.setFrozenOocyteNum(-1 * Integer.parseInt(thawingStageDto.getThawedOocytesNum()));
            arChangeInventoryDto.setThawedOocyteNum(Integer.parseInt(thawingStageDto.getThawedOocytesSurvivedMatureNum()));
        }
        if (thawingStageDto.getHasEmbryo()) {
            arChangeInventoryDto.setFrozenEmbryoNum(-1 * Integer.parseInt(thawingStageDto.getThawedEmbryosNum()));
            arChangeInventoryDto.setThawedEmbryoNum(Integer.parseInt(thawingStageDto.getThawedEmbryosSurvivedNum()));
        }
    }

    @Override
    public void submission(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        if (!thawingStageDto.getHasOocyte()) {
            thawingStageDto.setThawedOocytesNum("-1");
            thawingStageDto.setThawedOocytesSurvivedImmatureNum("-1");
            thawingStageDto.setThawedOocytesSurvivedMatureNum("-1");
            thawingStageDto.setThawedOocytesSurvivedOtherNum("-1");
        }
        if (!thawingStageDto.getHasEmbryo()) {
            thawingStageDto.setThawedEmbryosNum("-1");
            thawingStageDto.setThawedEmbryosSurvivedNum("-1");
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(thawingStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(thawingStageDto, "save");
            errorMap = validationResult.retrieveAll();
            verifyRfcCommon(request, errorMap);
            valRFC(request, thawingStageDto);
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private void fromPageData(ThawingStageDto thawingStageDto, HttpServletRequest request) {
        boolean hasOocyte = "true".equals(ParamUtil.getString(request, "hasOocyte"));
        boolean hasEmbryo = "true".equals(ParamUtil.getString(request, "hasEmbryo"));
        String thawedOocytesNum = ParamUtil.getString(request, "thawedOocytesNum");
        String thawedOocytesSurvivedMatureNum = ParamUtil.getString(request, "thawedOocytesSurvivedMatureNum");
        String thawedOocytesSurvivedImmatureNum = ParamUtil.getString(request, "thawedOocytesSurvivedImmatureNum");
        String thawedOocytesSurvivedOtherNum = ParamUtil.getString(request, "thawedOocytesSurvivedOtherNum");
        String thawedEmbryosNum = ParamUtil.getString(request, "thawedEmbryosNum");
        String thawedEmbryosSurvivedNum = ParamUtil.getString(request, "thawedEmbryosSurvivedNum");

        thawingStageDto.setHasOocyte(hasOocyte);
        thawingStageDto.setHasEmbryo(hasEmbryo);
        thawingStageDto.setThawedOocytesNum(thawedOocytesNum);
        thawingStageDto.setThawedOocytesSurvivedMatureNum(thawedOocytesSurvivedMatureNum);
        thawingStageDto.setThawedOocytesSurvivedImmatureNum(thawedOocytesSurvivedImmatureNum);
        thawingStageDto.setThawedOocytesSurvivedOtherNum(thawedOocytesSurvivedOtherNum);
        thawingStageDto.setThawedEmbryosNum(thawedEmbryosNum);
        thawingStageDto.setThawedEmbryosSurvivedNum(thawedEmbryosSurvivedNum);
    }

    protected void valRFC(HttpServletRequest request, ThawingStageDto thawingStageDto) {
        if (isRfc(request)) {
            ArSuperDataSubmissionDto arOldSuperDataSubmissionDto = DataSubmissionHelper.getOldArDataSubmission(request);
            if (arOldSuperDataSubmissionDto != null && arOldSuperDataSubmissionDto.getThawingStageDto() != null && thawingStageDto.equals(arOldSuperDataSubmissionDto.getThawingStageDto())) {
                ParamUtil.setRequestAttr(request, DataSubmissionConstant.RFC_NO_CHANGE_ERROR, AppConsts.YES);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_PAGE);
            }
        }
    }
}
