package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.arca.uen.GenerateUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.arca.uen.IaisUENDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.AcraUenBeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.security.SecureRandom;

/**
 * @author yichen
 * @Date:2021/3/31
 */
@Delegator("postEntityDelegator")
@Slf4j
public class PostEntityDelegator {

    @Autowired
    private AcraUenBeClient acraUenBeClient;

    public void postEntityToEDH(BaseProcessClass bpc) {
        IaisUENDto iaisUENDto = new IaisUENDto();
        String licenseeId = ParamUtil.getString(bpc.request, "licenseeId");
        iaisUENDto.setLicenseeId(licenseeId);
        GenerateUENDto generateUENDto = new GenerateUENDto();
        int sequenceNumber = 1;
        generateUENDto.setSequenceNumber(sequenceNumber);
        iaisUENDto.setGenerateUENDto(generateUENDto);

        PremisesDto premise = new PremisesDto();
        SecureRandom secureRandom = new SecureRandom();
        premise.setHciCode("Hci" + secureRandom.nextInt(100));
        premise.setHciName("Name" + secureRandom.nextInt(100));
        iaisUENDto.setPremises(premise);
        log.info(StringUtil.changeForLog("Iais UEN Dto: " + JsonUtil.parseToJson(iaisUENDto)));
        acraUenBeClient.generateUen(iaisUENDto);
    }

}
