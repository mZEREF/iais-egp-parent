package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
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
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, null);
        String appeal = bpc.request.getParameter("appeal");
        bpc.request.setAttribute("appeal",appeal);
        ParamUtil.setSessionAttr(bpc.request,HcsaAppConst.DASHBOARDTITLE,null);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.PRIMARY_DOC_CONFIG, null);
        log.info(StringUtil.changeForLog("The LicenceViewDelegator doStart end ..."));

    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData start ..."));
        ParamUtil.setRequestAttr(bpc.request,HcsaAppConst.DASHBOARDTITLE,"Licence Details");
        String licencId= ParamUtil.getRequestString(bpc.request,LICENCE_ID);
        if(StringUtil.isEmpty(licencId)){
            licencId = (String)ParamUtil.getSessionAttr(bpc.request,LICENCE_ID);
        }
        if(!StringUtil.isEmpty(licencId)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.viewAppSubmissionDto(licencId);
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
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
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
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(hcsaServiceDto.getId());
                        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SVC_DOC_CONFIG, (Serializable) svcDocConfig);
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        ApplicationHelper.setDocInfo(appSvcDocDtos, svcDocConfig);
                        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        //svc doc add align for dup for prem
                        ApplicationHelper.addPremAlignForSvcDoc(svcDocConfig,appSvcDocDtos,appGrpPremisesDtos);
                        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                        //set svc doc title
                        Map<String,List<AppSvcDocDto>> reloadSvcDocMap = ApplicationHelper.genSvcDocReloadMap(svcDocConfig,appGrpPremisesDtos,appSvcRelatedInfoDto);
                        appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);

                        //set po dpo
                        appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                        ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
                        //set DisciplineAllocationMap
                        //get lic_app svc id
                        String licAlignAppSvcId = "";
                        List<LicAppCorrelationDto> licAppCorrelationDtos =  appSubmissionService.getLicDtoByLicId(appSubmissionDto.getLicenceId());
                        if(licAppCorrelationDtos != null && licAppCorrelationDtos.size() >0){
                            LicAppCorrelationDto licAppCorrelationDto = licAppCorrelationDtos.get(0);
                            ApplicationDto applicationDto = appSubmissionService.getAppById(licAppCorrelationDto.getApplicationId());
                            if(applicationDto != null){
                                licAlignAppSvcId = applicationDto.getServiceId();
                            }
                        }
                        if(!StringUtil.isEmpty(licAlignAppSvcId)){
                            appSvcRelatedInfoDto.setServiceId(licAlignAppSvcId);
                            //692590
                            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                        }
                    }else{
                        log.info(StringUtil.changeForLog("current svc name:"+appSvcRelatedInfoDto.getServiceName()+" can not found hcsaServiceDto"));
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Licence Details");
            }
        }


        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData end ..."));

    }
}
