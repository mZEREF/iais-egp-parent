package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;

import java.util.List;

/**
 * EMailService
 *
 * @author guyin
 * @date 11/20/2019
 */

public interface EmailService {
    public void callEicSendEmail(EmailDto emailDto);

    List<OrgUserDto> retrieveOrgUserByroleId(List<String> roleId);
    List<OrgUserDto> retrieveOrgUser();
}
