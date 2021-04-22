package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author zixian
 * @date 2021/4/13 17:26
 * @description
 */
@Delegator("fePrintViewDelegator")
@Slf4j
public class FePrintViewDelegator {
    private final String SESSION_VIEW_SUBMISSONS = "viewSubmissons";
    private final String ATTR_PRINT_VIEW = "printView";

    @Autowired
    AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("print view doStart start ..."));
        //remove session
        ParamUtil.setSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS, null);

        String appType = ParamUtil.getString(bpc.request,"appType");
        log.debug("print view appType is {}",appType);
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(appType)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);
            if(appSubmissionDto != null){
                appSubmissionDtoList.add(appSubmissionDto);
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if(renewDto != null){
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if(!IaisCommonUtils.isEmpty(appSubmissionDtos)){
                    appSubmissionDtoList.addAll(appSubmissionDtos);
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO);
            if(appSubmissionDto != null){
                appSubmissionDtoList.add(appSubmissionDto);
            }
        }
        ParamUtil.setSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS, (Serializable) appSubmissionDtoList);

        log.debug(StringUtil.changeForLog("print view doStart end ..."));
    }


    public void prepareData(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("print view prepareData start ..."));
        List<AppSubmissionDto> appSubmissionDtoList = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS);
        for(AppSubmissionDto appSubmissionDto:appSubmissionDtoList){
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
            //set primary doc
            if(appGrpPrimaryDocDtoList != null && appGrpPrimaryDocDtoList.size() > 0){
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtoList.get(0).getSvcComDocId());
                ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, primaryDocConfig);
                Map<String,List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = NewApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,appSubmissionDto.getAppGrpPremisesDtoList(),appGrpPrimaryDocDtoList);
                appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
            }
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String svcId = appSvcRelatedInfoDto.getServiceId();
                    //set po dpo
                    List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    List<AppSvcPrincipalOfficersDto> reloadPoList = IaisCommonUtils.genNewArrayList();
                    List<AppSvcPrincipalOfficersDto> reloadDpoList = IaisCommonUtils.genNewArrayList();
                    if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                        NewApplicationHelper.assignPoDpoDto(appSvcPrincipalOfficersDtos,reloadPoList,reloadDpoList);
                    }
                    appSvcRelatedInfoDto.setReloadPoDtoList(reloadPoList);
                    appSvcRelatedInfoDto.setReloadDpoList(reloadDpoList);
                    //set allocation
                    Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                    appSvcRelatedInfoDto.setReloadDisciplineAllocationMap(reloadDisciplineAllocationMap);
                    //set step
                    List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                    appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                    //set svc doc
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                    appSvcRelatedInfoDto.setSvcDocConfig(svcDocConfig);
                    Map<String,List<AppSvcDocDto>> reloadSvcDocMap = NewApplicationHelper.genSvcDocReloadMap(svcDocConfig,appSubmissionDto.getAppGrpPremisesDtoList(),appSvcRelatedInfoDto);
                    appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS, (Serializable) appSubmissionDtoList);
        ParamUtil.setRequestAttr(bpc.request,ATTR_PRINT_VIEW,"test");
        log.debug(StringUtil.changeForLog("print view prepareData end ..."));
    }

}
