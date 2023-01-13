package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;


/**
 * InspEmailService
 *
 * @author gy
 * @date 2020/05/19
 */
public interface InspEmailService {

    LicenceDto getLicBylicId(String licenceId);
    InterMessageDto saveInterMessage(InterMessageDto interMessageDto);
    String getMessageNo();
}
