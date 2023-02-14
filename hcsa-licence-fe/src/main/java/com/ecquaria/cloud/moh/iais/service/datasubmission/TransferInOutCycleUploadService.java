package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface TransferInOutCycleUploadService {

    Map<String ,String> getTransferInOutUploadFile(HttpServletRequest request, Map<String, String> errorMap,int fileItemSize);

    void saveTransferInOutCycleUploadFile(HttpServletRequest request, ArSuperDataSubmissionDto arSuperDto);
}
