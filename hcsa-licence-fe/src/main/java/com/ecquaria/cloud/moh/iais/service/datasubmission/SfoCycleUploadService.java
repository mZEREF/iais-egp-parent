package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface SfoCycleUploadService {

    Map<String, String> getSfoCycleUploadFile(HttpServletRequest request, Map<String, String> errorMap,int fileItemSize);

    void saveSfoCycleUploadFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto);
}
