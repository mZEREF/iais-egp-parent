package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
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
    @Autowired
    private LicenceViewService licenceViewService;
    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData start ..."));
        String licencId= ParamUtil.getRequestString(bpc.request,"licenceId");
        LicenceViewDto licenceViewDto = licenceViewService.getLicenceViewDtoByLicenceId(licencId);
        ParamUtil.setSessionAttr(bpc.request,"licenceViewDto",licenceViewDto);
        log.info(StringUtil.changeForLog("The LicenceViewDelegator prepareData end ..."));

    }
}
