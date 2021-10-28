package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
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
    public void start(BaseProcessClass bpc) {
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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        if (thawingStageDto == null) {
            thawingStageDto = new ThawingStageDto();
            thawingStageDto.setThawedOocytesNum(0);
            thawingStageDto.setThawedOocytesSurvivedMatureNum(0);
            thawingStageDto.setThawedOocytesSurvivedImmatureNum(0);
            thawingStageDto.setThawedOocytesSurvivedOtherNum(0);
            thawingStageDto.setThawedEmbryosNum(0);
            thawingStageDto.setThawedEmbryosSurvivedNum(0);
            arSuperDataSubmissionDto.setThawingStageDto(thawingStageDto);
            ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmissionDto);
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
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
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ThawingStageDto thawingStageDto = arSuperDataSubmissionDto.getThawingStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(thawingStageDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(thawingStageDto, "save");
            errorMap = validationResult.retrieveAll();
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
            ParamUtil.setSessionAttr(request, "thawingStageDto", thawingStageDto);
        }
    }

    private void fromPageData(ThawingStageDto thawingStageDto, HttpServletRequest request) {
        boolean hasOocyte = "true".equals(ParamUtil.getString(request, "hasOocyte"));
        boolean hasEmbryo = "true".equals(ParamUtil.getString(request, "hasEmbryo"));
        int thawedOocytesNum = getInt(request, "thawedOocytesNum");
        int thawedOocytesSurvivedMatureNum = getInt(request, "thawedOocytesSurvivedMatureNum");
        int thawedOocytesSurvivedImmatureNum = getInt(request, "thawedOocytesSurvivedImmatureNum");
        int thawedOocytesSurvivedOtherNum = getInt(request, "thawedOocytesSurvivedOtherNum");
        int thawedEmbryosNum = getInt(request, "thawedEmbryosNum");
        int thawedEmbryosSurvivedNum = getInt(request, "thawedEmbryosSurvivedNum");

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

    private int getInt(HttpServletRequest request, String param){
        String s = ParamUtil.getString(request, param);
        int result = 0;
        if (StringUtil.isNotEmpty(s)){
            result = Integer.valueOf(s);
        }
        return result;
    }
}
