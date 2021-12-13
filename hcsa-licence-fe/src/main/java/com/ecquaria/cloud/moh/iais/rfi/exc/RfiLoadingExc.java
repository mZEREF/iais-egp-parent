package com.ecquaria.cloud.moh.iais.rfi.exc;

import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.rfi.RfiLoadingCheck;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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

    /*
    public void checkPremiseInfo(HttpServletRequest request, AppSubmissionDto appSubmissionDto, String appNo) {
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionService.getAppGrpPremisesDto(appNo);
        List<String> ids = IaisCommonUtils.genNewArrayList();
        String premisesIndexNo = null;
        for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
            for (AppGrpPremisesDto appGrpPremisesDto1 : appGrpPremisesDtoList) {
                if (appGrpPremisesDto.getId().equals(appGrpPremisesDto1.getId())) {
                    appGrpPremisesDto1.setPremisesIndexNo(appGrpPremisesDto.getPremisesIndexNo());
                }
            }
        }
        if (appGrpPremisesDtoList != null && !appGrpPremisesDtoList.isEmpty()) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                ids.add(appGrpPremisesDto.getId());
                premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                //70309
                if ("newPremise".equals(appGrpPremisesDto.getPremisesSelect())) {
                    appGrpPremisesDto.setPremisesSelect(NewApplicationHelper.getPremisesKey(appGrpPremisesDto));
                    List<AppGrpPremisesDto> applicationAppGrpPremisesDtoList = (List<AppGrpPremisesDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.RFC_APP_GRP_PREMISES_DTO_LIST);
                    if (IaisCommonUtils.isEmpty(applicationAppGrpPremisesDtoList)){
                        applicationAppGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
                    }
                    applicationAppGrpPremisesDtoList.add(appGrpPremisesDto);
                    ParamUtil.setSessionAttr(request, NewApplicationDelegator.RFC_APP_GRP_PREMISES_DTO_LIST, (Serializable) applicationAppGrpPremisesDtoList);
                }
            }
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        ApplicationDto entity = applicationFeClient.getApplicationDtoByVersion(appNo).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
            if (entity.getServiceId().equals(appSvcRelatedInfoDto.getServiceId())) {
                *//*  appSvcRelatedInfoDto.setAppSvcDocDtoLit(null);*//*
                appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtos = IaisCommonUtils.genNewArrayList();
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                    if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(premisesIndexNo)) {
                        appSvcLaboratoryDisciplinesDtos.add(appSvcLaboratoryDisciplinesDto);
                    }

                }
                appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtos);
            }
        }
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
    }
    */
}
