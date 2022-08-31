package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ArDonorSampleService {
    DonorSampleDto genDonorSampleDtoByPage(HttpServletRequest request);

    List<SelectOption> getSampleFromSelOpts(HttpServletRequest request);
}
