package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;

import java.util.Map;

public interface ArDataSubmissionService {

    Map<String,AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName);

    CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idNumber, String nationality, String orgId);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode);

    ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveBeArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveDataSubmissionDraft(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    String getSubmissionNo(String submisisonType, String cycleStage, DataSubmissionDto lastDataSubmissionDto);

}
