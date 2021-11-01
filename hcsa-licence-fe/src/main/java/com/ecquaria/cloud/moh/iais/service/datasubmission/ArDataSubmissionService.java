package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;

import java.util.Map;

public interface ArDataSubmissionService {

    Map<String,AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName);

    CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode, String hciCOde);

    ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveBeArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveDataSubmissionDraft(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    String getSubmissionNo(String submisisonType, String cycleStage, DataSubmissionDto lastDataSubmissionDto);

    /**
      * @author: shicheng
      * @Date 2021/11/1
      * @Param: ArSubFreezingStageDto
      * @return: arSubFreezingStageDto, cryopreservedNum, cryopreservationDate
      * @Descripation: set cryopreservedNum and cryopreservationDate
      */
    ArSubFreezingStageDto setFreeCryoNumAndDate(ArSubFreezingStageDto arSubFreezingStageDto, String cryopreservedNum, String cryopreservationDate);
}
