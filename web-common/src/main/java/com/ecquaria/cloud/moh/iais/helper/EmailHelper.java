package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.client.OrganizationBeClient;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.EmailService;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
	@Value("${iais.email.sender}")
	private String mailSender;
	@Autowired
	EmailService emailService;
	@Autowired
	EmailSmsClient emailSmsClient;
	@Autowired
	TaskOrganizationClient taskOrganizationClient;
	@Autowired
	TaskApplicationClient taskApplicationClient;
	@Autowired
	HcsaLicenceClient hcsaLicenceClient;
	private static List<String> licenceEmailString =  Arrays.asList(
		ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_PO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_DPO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_MAP,
		ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL,
		ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,
		ApplicationConsts.PERSONNEL_PSN_TYPE_AP
			);
	public MsgTemplateDto getMsgTemplate(String templateId){
		return iaisSystemClient.getMsgTemplate(templateId).getEntity();
	}

	public List<String> getEmailAddressListByLicenseeId(List<String> licenseeIdList){
		return organizationBeClient.getEmailAddressListByLicenseeId(licenseeIdList).getEntity();
	}

	//Method to retrieve All officers by role
	public List<String> getEmailAddressListByRole(List<String> role){
		List<String> email = IaisCommonUtils.genNewArrayList();
		List<OrgUserDto> orgUserDtoList = taskOrganizationClient.retrieveOrgUserByroleId(role).getEntity();
		for (OrgUserDto item:orgUserDtoList
		) {
			email.add(item.getEmail());
		}
		return email;
	}

	//Method to retrieve All effect officers
	public List<String> getEmailAddressList(){
		List<String> email = IaisCommonUtils.genNewArrayList();
		List<OrgUserDto> orgUserDtoList = taskOrganizationClient.retrieveOrgUser().getEntity();
		for (OrgUserDto item:orgUserDtoList
		) {
			email.add(item.getEmail());
		}
		return email;
	}

	public void sendEmail(String templateId, Map<String, Object> templateContent,String QueryCode,String reqRefNum, String applicaitonId){
		if(!StringUtil.isEmpty(templateId)){
			SendEmailThread sendEmailThread = new SendEmailThread(templateId,applicaitonId,templateContent,mailSender,QueryCode,reqRefNum);
			sendEmailThread.start();
		}
	}

	public List<String> getRecript(List<String> role,String application){
		List<String> all = IaisCommonUtils.genNewArrayList();
		List<String> organizationemail = IaisCommonUtils.genNewArrayList();
		List<String> applicationemail = IaisCommonUtils.genNewArrayList();
		List<String> licenceemail = IaisCommonUtils.genNewArrayList();
		for (String item:role
		) {
			List<String> list = Arrays.asList(item.split("-"));
			if(list.size() > 1){
				applicationemail.add(list.get(1));
			}else{
				if(licenceEmailString.contains(item)){
					licenceemail.add(list.get(0));
				}else{
					organizationemail.add(list.get(0));
				}
			}
		}
		if (organizationemail.size() > 0){
			List<String> email = IaisCommonUtils.genNewArrayList();
			List<OrgUserDto> orgUserDtoList =taskOrganizationClient.retrieveOrgUserByroleId(organizationemail).getEntity();
			for (OrgUserDto item:orgUserDtoList
			) {
				email.add(item.getEmail());
			}
			all.addAll(email.stream().distinct().collect(Collectors.toList()));
		}
		if (applicationemail.size() > 0){
			List<String> email = IaisCommonUtils.genNewArrayList();
			List<AppGrpPersonnelDto> appGrpPersonnelDtos = taskApplicationClient.getPersonnelByRoleAndGrpid(applicationemail,application).getEntity();
			for (AppGrpPersonnelDto item:appGrpPersonnelDtos
			) {
				email.add(item.getEmailAddr());
			}
			all.addAll(email.stream().distinct().collect(Collectors.toList()));
		}
		if (licenceemail.size() > 0){
			List<String> email = IaisCommonUtils.genNewArrayList();
			List<KeyPersonnelDto> keyPersonnelDtos = hcsaLicenceClient.getKeyPersonnelByRole(licenceemail).getEntity();
			for (KeyPersonnelDto item:keyPersonnelDtos
			) {
				email.add(item.getEmailAddr());
			}
			all.addAll(email.stream().distinct().collect(Collectors.toList()));
		}
		return all;
	}

	private boolean isOrganizationEmail(String role){
		return true;
	}
}
