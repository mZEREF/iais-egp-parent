package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * OnlineEnquiryDonorSampleDelegator
 *
 * @author junyu
 * @date 2021/12/20
 */
@Delegator(value = "onlineEnquiryDonorSampleDelegator")
@Slf4j
public class OnlineEnquiryDonorSampleDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter patientParameter = new FilterParameter.Builder()
            .clz(AssistedReproductionEnquiryResultsDto.class)
            .searchAttr("patientParam")
            .resultAttr("patientResult")
            .sortField("ID_NUMBER").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    public void start(BaseProcessClass bpc){

    }

    public void preSearch(BaseProcessClass bpc){

    }

    public void nextStep(BaseProcessClass bpc){

    }

    public void perDonorInfo(BaseProcessClass bpc){

    }

    public void backStep(BaseProcessClass bpc){

    }
}
