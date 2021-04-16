package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.arcaUen.IaisUENDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author yichen
 * @Date:2021/3/31
 */
@Delegator("postEntityDelegator")
@Slf4j
public class PostEntityDelegator {
    @Autowired
    private AcraUenBeClient acraUenBeClient;

    public void postEntityToEDH(BaseProcessClass bpc){
        IaisUENDto iaisUENDto = new IaisUENDto();
        String licenseeId = ParamUtil.getString(bpc.request, "licenseeId");
        iaisUENDto.setLicenseeId(licenseeId);
        GenerateUENDto generateUENDto = new GenerateUENDto();
        int sequenceNumber = 1;
        generateUENDto.setSequenceNumber(sequenceNumber);
        iaisUENDto.setGenerateUENDto(generateUENDto);
        acraUenBeClient.generateUen(iaisUENDto);
    }
}
