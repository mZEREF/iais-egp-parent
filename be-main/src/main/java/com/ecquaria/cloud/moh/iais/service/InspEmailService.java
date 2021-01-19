package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;


/**
 * InspEmailService
 *
 * @author gy
 * @date 2020/05/19
 */
public interface InspEmailService {
    InspectionEmailTemplateDto loadingEmailTemplate(String id);
    LicenseeDto getLicenseeDtoById(String licenseeId);
    LicenceDto getLicenceByAppId(String appId);
    LicenceViewDto getLicenceViewDtoByLicPremCorrId(String licPremCorrId);
    String sendNotification(EmailDto email);
    LicenceDto getLicBylicId(String licenceId);
    InterMessageDto saveInterMessage(InterMessageDto interMessageDto);
    String getMessageNo();
}
