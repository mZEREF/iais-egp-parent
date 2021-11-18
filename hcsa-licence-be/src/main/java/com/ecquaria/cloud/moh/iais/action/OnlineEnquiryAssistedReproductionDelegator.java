package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * OnlineEnquiryAssistedReproductionDelegator
 *
 * @author junyu
 * @date 2021/11/17
 */
@Delegator(value = "onlineEnquiryAssistedReproductionDelegator")
@Slf4j
public class OnlineEnquiryAssistedReproductionDelegator {
    public void start(BaseProcessClass bpc){
        AssistedReproductionEnquiryFilterDto assistedReproductionEnquiryFilterDto=new AssistedReproductionEnquiryFilterDto();
        assistedReproductionEnquiryFilterDto.setSearchBy("1");
        ParamUtil.setSessionAttr(bpc.request,"assistedReproductionEnquiryFilterDto",assistedReproductionEnquiryFilterDto);

    }
    public void baseSearch(BaseProcessClass bpc){
        List<SelectOption> submissionTypeOptions= IaisCommonUtils.genNewArrayList();
        submissionTypeOptions.add(new SelectOption("AR_TP001","Patient Information"));
        submissionTypeOptions.add(new SelectOption("AR_TP002","Cycle Stages"));
        submissionTypeOptions.add(new SelectOption("AR_TP003","Donor Samples"));
        ParamUtil.setRequestAttr(bpc.request,"submissionTypeOptions",submissionTypeOptions);

    }
    public void changePagination(BaseProcessClass bpc){

    }
    public void advNextStep(BaseProcessClass bpc){

    }
    public void perAdvancedSearch(BaseProcessClass bpc){

    }
    public void preViewFullDetails(BaseProcessClass bpc){

    }
    public void searchCycle(BaseProcessClass bpc){

    }
    public void perStep(BaseProcessClass bpc){

    }

    public void perStageInfo(BaseProcessClass bpc){

    }

    public void searchInventory(BaseProcessClass bpc){

    }
}
