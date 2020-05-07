package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.client.OrganizationBeClient;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: yichen
 * @date time:2/26/2020 10:56 AM
 * @description:
 */

@Component
public class EmailHelper {
	@Autowired
	private IaisSystemClient iaisSystemClient;
	@Autowired
	private OrganizationBeClient organizationBeClient;

	public MsgTemplateDto getMsgTemplate(String templateId){
		return iaisSystemClient.getMsgTemplate(templateId).getEntity();
	}

	public List<String> getEmailAddressListByLicenseeId(List<String> licenseeIdList){
		return organizationBeClient.getEmailAddressListByLicenseeId(licenseeIdList).getEntity();
	}

}
