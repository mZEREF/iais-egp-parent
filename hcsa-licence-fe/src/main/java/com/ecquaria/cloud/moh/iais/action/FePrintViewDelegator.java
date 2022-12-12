package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.AppDataHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author zixian
 * @date 2021/4/13 17:26
 * @description
 */
@Delegator("fePrintViewDelegator")
@Slf4j
public class FePrintViewDelegator {
    private final static String SESSION_VIEW_SUBMISSONS = "viewSubmissons";
    private final static  String ATTR_PRINT_VIEW = "printView";
    private final static String LICENCE_VIEW="licenceView";
    private final static String RFC_EQHCINAMECHANGE ="RFC_eqHciNameChange";
    private final static String GROUP_RENEW_APP_RFC ="group_renewal_app_rfc";
    @Autowired
    AppSubmissionService appSubmissionService;

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("print view doStart start ..."));
        HttpServletRequest request = bpc.request;
        //remove session
        ParamUtil.setSessionAttr(request,SESSION_VIEW_SUBMISSONS, null);
        // View and Print
        String viewPrint = (String) ParamUtil.getSessionAttr(request,HcsaAppConst.IS_VIEW);
        String appType = ParamUtil.getString(request,"appType");
        log.debug("print view appType is {}",appType);
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        String licenceView = request.getParameter(LICENCE_VIEW);
        if(LICENCE_VIEW.equals(licenceView)){
            request.setAttribute(LICENCE_VIEW,LICENCE_VIEW);
        }
        ParamUtil.setRequestAttr(request,"serviceNameMiss",ParamUtil.getRequestString(request,"serviceNameMiss"));
        if(StringUtil.isEmpty(appType)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, HcsaAppConst.APPSUBMISSIONDTO);
            if (appSubmissionDto != null) {
                AppSubmissionDto newAppSubmissionDto = null;
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                    String rfcEqHciNameChange = request.getParameter(RFC_EQHCINAMECHANGE);
                    log.info(StringUtil.changeForLog("hciNameChange: " + rfcEqHciNameChange));
                    request.setAttribute(RFC_EQHCINAMECHANGE, rfcEqHciNameChange);
                    if (RFC_EQHCINAMECHANGE.equals(rfcEqHciNameChange)) {
                        ParamUtil.setRequestAttr(request, GROUP_RENEW_APP_RFC,
                                ParamUtil.getRequestString(request, GROUP_RENEW_APP_RFC));
                    }
                }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                    //inbox view dec
                    RenewDto renewDto=new RenewDto();
                    renewDto.setAppSubmissionDtos(Collections.singletonList(appSubmissionDto));
                    request.setAttribute(RenewalConstants.RENEW_DTO, renewDto);
                } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    String rfiAppNo = appSubmissionDto.getRfiAppNo();
                    if (!StringUtil.isEmpty(rfiAppNo)) {
                        newAppSubmissionDto = CopyUtil.copyMutableObject(appSubmissionDto);
                        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = newAppSubmissionDto.getAppSvcRelatedInfoDtoList();
                        if (appSvcRelatedInfoDtoList != null && !appSvcRelatedInfoDtoList.isEmpty()) {
                            List<AppSvcRelatedInfoDto> newList = IaisCommonUtils.genNewArrayList(1);
                            appSvcRelatedInfoDtoList.stream()
                                    .filter(dto -> rfiAppNo.equals(dto.getAppNo()))
                                    .findAny()
                                    .ifPresent(newList::add);
                            newAppSubmissionDto.setAppSvcRelatedInfoDtoList(newList);
                        }

                    }
                }
                if (newAppSubmissionDto != null) {
                    appSubmissionDtoList.add(newAppSubmissionDto);
                } else {
                    appSubmissionDtoList.add(appSubmissionDto);
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(request, RenewalConstants.RENEW_DTO);
            if(renewDto != null){
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if(!IaisCommonUtils.isEmpty(appSubmissionDtos)){
                    if(appSubmissionDtos.size()==1){
                        if (StringUtil.isEmpty(viewPrint)) {
                            AppDeclarationMessageDto appDeclarationMessageDto = AppDataHelper.getAppDeclarationMessageDto(request,
                                    ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                            appSubmissionDtos.get(0).setAppDeclarationMessageDto(appDeclarationMessageDto);
                            appSubmissionDtos.get(0).setAppDeclarationDocDtos(
                                    AppDataHelper.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_RENEWAL, request));
                        }
                    }
                    appSubmissionDtoList.addAll(appSubmissionDtos);
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, RfcConst.RFCAPPSUBMISSIONDTO);
            if(appSubmissionDto != null){
                appSubmissionDtoList.add(appSubmissionDto);
            }
        }
        ParamUtil.setRequestAttr(request, HcsaAppConst.IS_VIEW, "Y");
        ParamUtil.setRequestAttr(request, HcsaAppConst.IS_PRINT, "Y");
        ParamUtil.setSessionAttr(request,SESSION_VIEW_SUBMISSONS, (Serializable) appSubmissionDtoList);
        log.debug(StringUtil.changeForLog("print view doStart end ..."));
    }


    public void prepareData(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("print view prepareData start ..."));
        List<AppSubmissionDto> appSubmissionDtoList = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS);
        for(AppSubmissionDto appSubmissionDto:appSubmissionDtoList){
            DealSessionUtil.initView(appSubmissionDto);
        }
        ParamUtil.setSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS, (Serializable) appSubmissionDtoList);
        ParamUtil.setRequestAttr(bpc.request,ATTR_PRINT_VIEW,"test");
        log.debug(StringUtil.changeForLog("print view prepareData end ..."));
    }

    @RequestMapping(value = "/init-print", method = RequestMethod.POST)
    public @ResponseBody String initPrint(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, HcsaAppConst.APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            log.info(StringUtil.changeForLog("init-print"));
            appSubmissionDto.setAppDeclarationMessageDto(
                    AppDataHelper.getAppDeclarationMessageDto(request, appSubmissionDto.getAppType()));
            appSubmissionDto.setAppDeclarationDocDtos(
                    AppDataHelper.getDeclarationFiles(appSubmissionDto.getAppType(), request, true));
            ParamUtil.setSessionAttr(request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
            String verifyInfoCheckbox = ParamUtil.getString(request, "verifyInfoCheckbox");
            appSubmissionDto.setUserAgreement(AppConsts.YES.equals(verifyInfoCheckbox));
            String effectiveDateStr = ParamUtil.getString(request, "rfcEffectiveDate");
            appSubmissionDto.setEffectiveDateStr(effectiveDateStr);
            if (!StringUtil.isEmpty(effectiveDateStr) && CommonValidator.isDate(effectiveDateStr)) {
                appSubmissionDto.setEffectiveDate(DateUtil.parseDate(effectiveDateStr, Formatter.DATE));
            } else {
                appSubmissionDto.setEffectiveDate(null);
            }
        }
        return AppConsts.YES;
    }

}
