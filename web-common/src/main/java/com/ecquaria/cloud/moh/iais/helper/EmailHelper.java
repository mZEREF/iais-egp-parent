package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.client.OrganizationBeClient;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;

import java.util.List;

/**
 * @author: yichen
 * @date time:2/26/2020 10:56 AM
 * @description:
 */

public class EmailHelper {
	public static IaisSystemClient iaisSystemClient = SpringContextHelper.getContext().getBean(IaisSystemClient.class);
	public static OrganizationBeClient organizationBeClient = SpringContextHelper.getContext().getBean(OrganizationBeClient.class);


	private EmailHelper(){
		throw new IllegalStateException("Utility class");
	}

	public static MsgTemplateDto getMsgTemplate(String templateId){
		return iaisSystemClient.getMsgTemplate(templateId).getEntity();
	}

	public static List<String> getEmailAddressListByLicenseeId(List<String> licenseeIdList){
		return organizationBeClient.getEmailAddressListByLicenseeId(licenseeIdList).getEntity();
	}

}
