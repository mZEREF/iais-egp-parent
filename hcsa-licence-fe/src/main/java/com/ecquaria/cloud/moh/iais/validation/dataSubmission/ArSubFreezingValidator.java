package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/11/1 16:11
 **/
public class ArSubFreezingValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if(arSuperDataSubmission != null) {
            ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
            if(arSubFreezingStageDto != null) {
                Integer cryopreservedNum = arSubFreezingStageDto.getCryopreservedNum();
                Date cryopreservedDate = arSubFreezingStageDto.getCryopreservedDate();
                if(cryopreservedDate != null) {
                    if(cryopreservedDate.after(new Date())) {
                        errMap.put("cryopreservedDate", MessageUtil.replaceMessage("DS_ERR010", "Cryopreservation Date", "field"));
                    }
                }
                if (cryopreservedNum != null){
                    //Cannot be greater than number of fresh oocytes or fresh embryos under patient's inventory currently
                    PatientInventoryDto patientInventoryDto = arSuperDataSubmission.getPatientInventoryDto();
                    String cryopreservedType = arSubFreezingStageDto.getCryopreservedType();
                    int oocytesOrEmbryos = 0;
                    if(patientInventoryDto != null) {
                        if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_OOCYTE.equals(cryopreservedType)) {
                            oocytesOrEmbryos = patientInventoryDto.getCurrentFreshOocytes();
                        } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_FRESH_EMBRYO.equals(cryopreservedType)) {
                            oocytesOrEmbryos = patientInventoryDto.getCurrentFreshEmbryos();
                        } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_OOCYTE.equals(cryopreservedType)) {
                            oocytesOrEmbryos = patientInventoryDto.getCurrentThawedOocytes();
                        } else if (DataSubmissionConsts.FREEZING_CRYOPRESERVED_THAWED_EMBRYO.equals(cryopreservedType)) {
                            oocytesOrEmbryos = patientInventoryDto.getCurrentThawedEmbryos();
                        }
                    }
                    if(cryopreservedNum < 0) {
                        errMap.put("cryopreservedNum", "GENERAL_ERR0027");
                    } else if (cryopreservedNum > 99) {
                        errMap.put("cryopreservedNum", MessageUtil.replaceMessage("DS_ERR009", "No. Cryopreserved", "field"));
                    } else if(cryopreservedNum > oocytesOrEmbryos) {
                        errMap.put("cryopreservedNum", "DS_ERR015");
                    }
                }
            }
        }
        if(errMap.isEmpty()) {
            return null;
        }
        return errMap;
    }
}
