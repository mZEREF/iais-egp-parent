package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author junyu
 * @date 2020/1/16 16:05
 */
@Slf4j
@Delegator("rfiFileRepoDownloadDelegator")
public class RfiFileRepoDownloadDelegator {
    private LicPremisesReqForInfoDto licPremisesReqForInfoDto=new LicPremisesReqForInfoDto();

    @Autowired
    private RequestForInformationService requestForInformationService;


    public  void start (BaseProcessClass bpc){
        logAbout("start");

    }

    public  void prepareData(BaseProcessClass bpc) throws Exception {
         logAbout("preparetionData");
        requestForInformationService.delete();
        requestForInformationService.compress(licPremisesReqForInfoDto);

    }
/*******************************/
    private void logAbout(String name){
        log.debug(StringUtil.changeForLog("****The***** " +name +" ******Start ****"));
    }

}
