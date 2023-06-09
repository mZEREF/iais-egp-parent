package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
                appSubmissionDto.setAppEditSelectDto(new AppEditSelectDto());
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
                ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Licence Details");
            }
        }

        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData end ..."));
    }

}
