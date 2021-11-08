package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArDonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Oocyte Retrieval Stage</strong>");
    }

    @Override
    public void preparePage(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        if (oocyteRetrievalStageDto == null) {
            oocyteRetrievalStageDto = new OocyteRetrievalStageDto();
            oocyteRetrievalStageDto.setMatureRetrievedNum(0);
            oocyteRetrievalStageDto.setImmatureRetrievedNum(0);
            oocyteRetrievalStageDto.setOtherRetrievedNum(0);
            arSuperDataSubmissionDto.setOocyteRetrievalStageDto(oocyteRetrievalStageDto);
            List<ArDonorDto> arDonorDtoList = arSuperDataSubmissionDto.getArDonorDtos();
            if (IaisCommonUtils.isNotEmpty(arDonorDtoList)) {
                for (ArDonorDto arDonorDto : arDonorDtoList) {
                    if (arDonorDto.isDirectedDonation()) {
                        oocyteRetrievalStageDto.setIsFromDonor(true);
                        break;
                    }
                }
            }
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmissionDto, bpc.request);
        }
        ParamUtil.setRequestAttr(bpc.request, "totalRetrievedNum", oocyteRetrievalStageDto.getTotalNum());
    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        int totalNum = oocyteRetrievalStageDto.getTotalNum();
        ParamUtil.setRequestAttr(bpc.request, "totalRetrievedNum", totalNum);
        PatientInventoryDto patientInventoryDto = DataSubmissionHelper.initPatientInventoryTable(bpc.request);
        patientInventoryDto.setChangeFreshOocytes(totalNum);
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
        int matureRetrievedNum = getInt(request, "matureRetrievedNum");
        int immatureRetrievedNum = getInt(request, "immatureRetrievedNum");
        int otherRetrievedNum = getInt(request, "otherRetrievedNum");
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

    private int getInt(HttpServletRequest request, String param){
        String s = ParamUtil.getString(request, param);
        int result = 0;
        if (StringUtil.isNotEmpty(s)){
            try {
                result = Integer.parseInt(s);
            }catch (NumberFormatException ignored){

            }
        }
        return result;
    }
}
