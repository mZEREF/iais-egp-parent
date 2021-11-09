package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ThawingStageDtoValidator
 *
 * @author jiawei
 * @date 2021/10/27
 */

public class ThawingStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ThawingStageDto thawingStageDto = (ThawingStageDto) obj;
        if (!(thawingStageDto.getHasEmbryo() || thawingStageDto.getHasOocyte())) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Thawing Oocyte(s) or Embryo(s)", "field");
            errorMap.put("thawings", errMsg);
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        PatientInventoryDto patientInventoryDto = arSuperDataSubmissionDto.getPatientInventoryDto();
        if (patientInventoryDto != null) {
            if (thawingStageDto.getThawedOocytesNum() > patientInventoryDto.getCurrentFrozenOocytes()) {
                errorMap.put("thawedOocytesNum", "No. of Oocytes Thawed cannot be greater than total number of frozen oocytes tagged patient");
            }
            if (thawingStageDto.getThawedEmbryosNum() > patientInventoryDto.getCurrentFrozenEmbryos()) {
                errorMap.put("thawedEmbryosNum", "No. of Embryos Thawed cannot be greater than total number of frozen embryos tagged patient");
            }
        }

        if ((thawingStageDto.getThawedOocytesSurvivedMatureNum() + thawingStageDto.getThawedOocytesSurvivedImmatureNum()
                + thawingStageDto.getThawedOocytesSurvivedOtherNum()) > thawingStageDto.getThawedOocytesNum()) {
            errorMap.put("thawedOocytesNum", "Total sum of Thawing (Mature),Thawing (Immature),Thawing (Others) cannot be greater than No. Thawed");
        }
        if (thawingStageDto.getThawedEmbryosSurvivedNum() > thawingStageDto.getThawedEmbryosNum()) {
            errorMap.put("thawedEmbryosNum", "Survived Thawing cannot be greater than No. Embryo");
        }
        return errorMap;
    }
}
