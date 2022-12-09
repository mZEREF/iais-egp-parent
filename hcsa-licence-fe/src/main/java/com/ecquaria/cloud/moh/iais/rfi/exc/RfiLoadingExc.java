package com.ecquaria.cloud.moh.iais.rfi.exc;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.rfi.RfiLoadingCheck;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Wenkang
 * @date 2021/3/17 14:35
 */
@Component
public class RfiLoadingExc implements RfiLoadingCheck {
    @Autowired
    protected AppSubmissionService appSubmissionService;
    @Autowired
    protected ApplicationFeClient applicationFeClient;

    @Override
    public void beforeSubmitRfi(AppSubmissionDto appSubmissionDto,String appNo) throws Exception {
        AppSubmissionDto submissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = submissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
        submissionDto.setAppGrpPremisesDtoList(appSubmissionDto.getAppGrpPremisesDtoList());
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList1 = submissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> list=new ArrayList<>(appSvcRelatedInfoDtoList1.size());
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
            for(AppSvcRelatedInfoDto svcRelatedInfoDto : appSvcRelatedInfoDtoList1){
                if(appSvcRelatedInfoDto.getServiceId().equals(svcRelatedInfoDto.getServiceId())){
                    list.add(svcRelatedInfoDto);
                }
            }
        }
        List<AppGrpPremisesDto> premisesDtoList=new ArrayList<>(appGrpPremisesDtoList.size());
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            for(AppGrpPremisesDto appGrpPremisesDto1 : appGrpPremisesDtoList1){
                if(appGrpPremisesDto.getPremisesIndexNo().equals(appGrpPremisesDto1.getPremisesIndexNo())){
                    premisesDtoList.add(appGrpPremisesDto);
                }
            }
        }
        appGrpPremisesDtoList.removeAll(premisesDtoList);
        appGrpPremisesDtoList1.addAll(appGrpPremisesDtoList);
        appSvcRelatedInfoDtoList1.removeAll(list);
        appSvcRelatedInfoDtoList.addAll(appSvcRelatedInfoDtoList1);
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
    }
}
