package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * LicenceViewDelegator
 *
 * @author suocheng
 * @date 2/18/2020
 */
@Delegator("licenceViewDelegator")
@Slf4j
public class LicenceViewDelegator {

    private static final String LICENCE_ID = "licenceId";

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
        bpc.request.setAttribute("appeal", appeal);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE, null);
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
        ParamUtil.setRequestAttr(bpc.request, HcsaAppConst.DASHBOARDTITLE, "Licence Details");
        String licencId = ParamUtil.getRequestString(bpc.request, LICENCE_ID);
        if (StringUtil.isEmpty(licencId)) {
            licencId = (String) ParamUtil.getSessionAttr(bpc.request, LICENCE_ID);
        }
        if (!StringUtil.isEmpty(licencId)) {
            AppSubmissionDto appSubmissionDto = appSubmissionService.viewAppSubmissionDto(licencId);
            if (appSubmissionDto != null) {
                DealSessionUtil.initView(appSubmissionDto);
                //set audit trail licNo
                AuditTrailHelper.setAuditLicNo(appSubmissionDto.getLicenceNo());
                // appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                //remove edit btn from page
                appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
                }
                if (appSvcRelatedInfoDto != null) {
                    appSvcRelatedInfoDtos.add(appSvcRelatedInfoDto);
                    appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
                    ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
                }
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
                ParamUtil.setRequestAttr(bpc.request, RfcConst.FIRSTVIEW, AppConsts.TRUE);
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Licence Details");
            }
        }


        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData end ..."));

    }

}
