package com.ecquaria.cloud.moh.iais.service.datasubmission;

import sop.webflow.rt.api.BaseProcessClass;

import java.util.Map;

public interface ArCycleBatchUploadService {

    Map<String, String> preBatchUpload(BaseProcessClass bpc, Map<String, String> errorMap);

    void doSubmission(BaseProcessClass bpc);
}
