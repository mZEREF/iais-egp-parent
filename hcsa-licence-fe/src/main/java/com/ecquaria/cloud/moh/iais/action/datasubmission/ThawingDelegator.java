package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
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

        PatientInventoryDto patientInventoryDto = DataSubmissionHelper.initPatientInventoryTable(bpc.request);
        if (thawingStageDto.getHasOocyte()) {
            patientInventoryDto.setChangeFrozenOocytes(-1 * thawingStageDto.getThawedOocytesNum());
            patientInventoryDto.setChangeThawedOocytes(thawingStageDto.getThawedOocytesSurvivedMatureNum());
        }
        if (thawingStageDto.getHasEmbryo()) {
            patientInventoryDto.setChangeFrozenEmbryos(-1 * thawingStageDto.getThawedEmbryosNum());
            patientInventoryDto.setChangeThawedEmbryos(thawingStageDto.getThawedEmbryosSurvivedNum());
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
        int thawedOocytesNum = ParamUtil.getInt(request, "thawedOocytesNum", -1);
        int thawedOocytesSurvivedMatureNum = ParamUtil.getInt(request, "thawedOocytesSurvivedMatureNum", -1);
        int thawedOocytesSurvivedImmatureNum = ParamUtil.getInt(request, "thawedOocytesSurvivedImmatureNum", -1);
        int thawedOocytesSurvivedOtherNum = ParamUtil.getInt(request, "thawedOocytesSurvivedOtherNum", -1);
        int thawedEmbryosNum = ParamUtil.getInt(request, "thawedEmbryosNum", -1);
        int thawedEmbryosSurvivedNum = ParamUtil.getInt(request, "thawedEmbryosSurvivedNum", -1);

        thawingStageDto.setHasOocyte(hasOocyte);
        thawingStageDto.setHasEmbryo(hasEmbryo);
        thawingStageDto.setThawedOocytesNum(thawedOocytesNum);
        thawingStageDto.setThawedOocytesSurvivedMatureNum(thawedOocytesSurvivedMatureNum);
        thawingStageDto.setThawedOocytesSurvivedImmatureNum(thawedOocytesSurvivedImmatureNum);
        thawingStageDto.setThawedOocytesSurvivedOtherNum(thawedOocytesSurvivedOtherNum);
        thawingStageDto.setThawedEmbryosNum(thawedEmbryosNum);
        thawingStageDto.setThawedEmbryosSurvivedNum(thawedEmbryosSurvivedNum);
    }
}
