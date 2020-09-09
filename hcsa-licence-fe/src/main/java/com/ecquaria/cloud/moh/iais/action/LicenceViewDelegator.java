package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * LicenceViewDelegator
 *
 * @author suocheng
 * @date 2/18/2020
 */
@Delegator("licenceViewDelegator")
@Slf4j
public class LicenceViewDelegator {

    private static final  String LICENCE_ID = "licenceId";

    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    RequestForChangeService requestForChangeService;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("The LicenceViewDelegator doStart start ..."));
        ParamUtil.setSessionAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,null);
        ParamUtil.setSessionAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,null);
        log.info(StringUtil.changeForLog("The LicenceViewDelegator doStart end ..."));

    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData start ..."));
        ParamUtil.setRequestAttr(bpc.request,HcsaLicenceFeConstant.DASHBOARDTITLE,"empty");
        String licencId= ParamUtil.getRequestString(bpc.request,LICENCE_ID);
        if(StringUtil.isEmpty(licencId)){
            licencId = (String)ParamUtil.getSessionAttr(bpc.request,LICENCE_ID);
        }
        if(!StringUtil.isEmpty(licencId)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licencId);
            if(appSubmissionDto != null){
                //set audit trail licNo
                AuditTrailHelper.setAuditLicNo(appSubmissionDto.getLicenceNo());
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                //remove edit btn from page
                appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
                }
                List<SelectOption> publicHolidayList = serviceConfigService.getPubHolidaySelect();
                if(!IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList())){
                    for(AppGrpPremisesDto appGrpPremisesDto:appSubmissionDto.getAppGrpPremisesDtoList()){
                        NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                        //set ph name
                        NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos,publicHolidayList);
                    }
                }
                if(appSvcRelatedInfoDto != null){
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(appSvcRelatedInfoDto.getServiceName());
                    if(hcsaServiceDto != null){
                        appSvcRelatedInfoDtos = IaisCommonUtils.genNewArrayList();
                        appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                        appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                        appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
                        appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                        //set service step
                        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(hcsaServiceDto.getId());
                        appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                        //set primary doc info
                        requestForChangeService.svcDocToPresmise(appSubmissionDto);
                        //set doc info
                        List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(hcsaServiceDto.getId());
                        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos, appSvcDocDtos, primaryDocConfig, svcDocConfig);
                        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        //set service scope info
                        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(hcsaServiceDto.getId());
                        NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto,hcsaSvcSubtypeOrSubsumedDtos);
                        //set po dpo
                        NewApplicationHelper.setPreviewPo(appSvcRelatedInfoDto,bpc.request);
                        appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                        ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
                        //set DisciplineAllocationMap
                        Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,hcsaServiceDto.getId());
                        ParamUtil.setRequestAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
                    }else{
                        log.info(StringUtil.changeForLog("current svc name:"+appSvcRelatedInfoDto.getServiceName()+" can not found hcsaServiceDto"));
                    }
                }
                ParamUtil.setSessionAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,appSubmissionDto);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
            }
        }


        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData end ..."));

    }
}
