package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArChangeInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    public void prepareSwitch(BaseProcessClass bpc) {
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        if (oocyteRetrievalStageDto == null) {
            oocyteRetrievalStageDto = new OocyteRetrievalStageDto();
            arSuperDataSubmissionDto.setOocyteRetrievalStageDto(oocyteRetrievalStageDto);
            List<DonorDto> arDonorDtoList = arDataSubmissionService.getAllDonorDtoByCycleId(arSuperDataSubmissionDto.getCycleDto().getId());
            if (IaisCommonUtils.isNotEmpty(arDonorDtoList)) {
                for (DonorDto donorDto : arDonorDtoList) {
                    if (donorDto.isDirectedDonation()) {
                        oocyteRetrievalStageDto.setIsFromDonor(Boolean.TRUE);
                        break;
                    }
                }
            }
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        int totalNum = oocyteRetrievalStageDto.getTotalNum();
        ArChangeInventoryDto arChangeInventoryDto = DataSubmissionHelper.getCurrentArChangeInventoryDto(bpc.request);
        arChangeInventoryDto.setFreshOocyteNum(totalNum);
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        HttpServletRequest request = bpc.request;
        fromPageData(oocyteRetrievalStageDto, request);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, request);

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_type = ParamUtil.getRequestString(request, IntranetUserConstant.CRUD_ACTION_TYPE);

        if ("confirm".equals(crud_action_type)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(oocyteRetrievalStageDto, "save");
            errorMap = validationResult.retrieveAll();
        }

        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.CRUD_ACTION_TYPE, "page");
        }
    }

    private void fromPageData(OocyteRetrievalStageDto oocyteRetrievalStageDto, HttpServletRequest request) {
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
    }
}
