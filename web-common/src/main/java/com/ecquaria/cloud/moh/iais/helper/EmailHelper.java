package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.EmailService;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * @author: yichen
 * @date time:2/26/2020 10:56 AM
 * @description:
 */

@Component
@Slf4j
public class EmailHelper {
	public static final String RECEIPT_TYPE_APP_GRP 				= "GRP";
	public static final String RECEIPT_TYPE_APP 					= "APP";
	public static final String RECEIPT_TYPE_LICENCE_ID              = "LIC";

	private static final String RECEIPT_ROLE_LICENSEE               = "EM-LIC";
	private static final String RECEIPT_ROLE_AUTHORISED_PERSON      = "EM-AP";

	@Autowired
	private IaisSystemClient iaisSystemClient;
	@Autowired
	private LicenseeClient licenseeClient;
	@Value("${iais.email.sender}")
	private String mailSender;
	@Autowired
	private EmailService emailService;
	@Autowired
	private EmailSmsClient emailSmsClient;
	@Autowired
	TaskOrganizationClient taskOrganizationClient;
	@Autowired
	private HcsaAppClient hcsaAppClient;
	@Autowired
	private TaskApplicationClient taskApplicationClient;
	@Autowired
	private HcsaLicenceClient hcsaLicenceClient;
	private static List<String> licenceEmailString =  Arrays.asList(
		ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_PO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_DPO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_MAP,
		ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL,
		ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,
		ApplicationConsts.PERSONNEL_PSN_TYPE_AP
			);

	@Bean(name = "emailAsyncExecutor")
	public Executor asyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(50);
		executor.setMaxPoolSize(50);
		executor.setQueueCapacity(500);
		executor.setThreadNamePrefix("EmailAsynchThread-");
		executor.initialize();

		return executor;
	}

	public MsgTemplateDto getMsgTemplate(String templateId){
		return iaisSystemClient.getMsgTemplate(templateId).getEntity();
	}

	public List<String> getEmailAddressListByLicenseeId(List<String> licenseeIdList){
		return licenseeClient.getEmailAddressListByLicenseeId(licenseeIdList).getEntity();
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

	@Async("emailAsyncExecutor")
	public void sendEmail(String templateId, Map<String, Object> templateContent, String queryCode,
						  String reqRefNum, String refIdType, String refId) throws IOException, TemplateException {
		List<String> receiptemail = IaisCommonUtils.genNewArrayList();
		List<String> ccemail = IaisCommonUtils.genNewArrayList();
		List<String> bccemail = IaisCommonUtils.genNewArrayList();
		log.info(StringUtil.changeForLog("sendemail start... ref type is " + StringUtil.nullToEmptyStr(refIdType)
				+ " ref Id is " + StringUtil.nullToEmptyStr(refId)
				+ "templateId is "+ templateId+"thread name is " + Thread.currentThread().getName()));

		MsgTemplateDto msgTemplateDto = iaisSystemClient.getMsgTemplate(templateId).getEntity();
		EmailDto emailDto = new EmailDto();
		String mesContext = "";
		if (templateContent != null && !templateContent.isEmpty()) {
			mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), templateContent);
		} else {
			mesContext = msgTemplateDto.getMessageContent();
		}

		emailDto.setContent(mesContext);
		emailDto.setSubject(msgTemplateDto.getTemplateName());
		emailDto.setSender(this.mailSender);
		if (msgTemplateDto.getRecipient()!= null && msgTemplateDto.getRecipient().size() > 0) {
			receiptemail = getRecript(msgTemplateDto.getRecipient(), refIdType, refId);
			emailDto.setReceipts(receiptemail);
		}
		if (msgTemplateDto.getCcrecipient()!= null && msgTemplateDto.getCcrecipient().size() > 0) {
			ccemail = getRecript(msgTemplateDto.getCcrecipient(), refIdType, refId);
			emailDto.setCcList(ccemail);
		}
		if (msgTemplateDto.getBccrecipient()!= null && msgTemplateDto.getBccrecipient().size() > 0) {
			bccemail = getRecript(msgTemplateDto.getBccrecipient(), refIdType, refId);
			emailDto.setBccList(bccemail);
		}
		if(queryCode != null){
			emailDto.setClientQueryCode(queryCode);
		}else{
			emailDto.setClientQueryCode("no queryCode");
		}
		if(reqRefNum != null){
			emailDto.setReqRefNum(reqRefNum);
		}else{
			emailDto.setReqRefNum("no reqRefNum");
		}
		//send
		emailSmsClient.sendEmail(emailDto,null);
		log.info(StringUtil.changeForLog("sendemail end... queryCode is"+queryCode + "templateId is "
				+ templateId+"thread name is " + Thread.currentThread().getName()));
	}

	public List<String> getRecript(List<String> role, String refType, String refId){
		List<String> all = IaisCommonUtils.genNewArrayList();
		List<String> organizationemail = IaisCommonUtils.genNewArrayList();
		List<String> applicationemail = IaisCommonUtils.genNewArrayList();
		List<String> licenceemail = IaisCommonUtils.genNewArrayList();
		if (RECEIPT_TYPE_APP_GRP.equals(refType)) {

		} else if (RECEIPT_TYPE_APP.equals(refType)) {

		} else {

		}
		for (String item : role) {
			List<String> list = Arrays.asList(item.split("-"));
			if (list.size() > 1) {
				applicationemail.add(list.get(1));
			} else {
				if (licenceEmailString.contains(item)) {
					licenceemail.add(list.get(0));
				} else {
					organizationemail.add(list.get(0));
				}
			}
		}
		if (organizationemail.size() > 0) {
			List<String> email = IaisCommonUtils.genNewArrayList();
			List<OrgUserDto> orgUserDtoList = taskOrganizationClient.retrieveOrgUserByroleId(organizationemail).getEntity();
			for (OrgUserDto item:orgUserDtoList
			) {
				email.add(item.getEmail());
			}
			all.addAll(email.stream().distinct().collect(Collectors.toList()));
		}
		if (applicationemail.size() > 0) {
			List<String> email = IaisCommonUtils.genNewArrayList();
			List<AppGrpPersonnelDto> appGrpPersonnelDtos = taskApplicationClient.getPersonnelByRoleAndGrpid(applicationemail,refId).getEntity();
			for (AppGrpPersonnelDto item:appGrpPersonnelDtos
			) {
				email.add(item.getEmailAddr());
			}
			all.addAll(email.stream().distinct().collect(Collectors.toList()));
		}
		if (licenceemail.size() > 0) {
			List<String> email = IaisCommonUtils.genNewArrayList();
			List<KeyPersonnelDto> keyPersonnelDtos = hcsaLicenceClient.getKeyPersonnelByRole(licenceemail).getEntity();
			for (KeyPersonnelDto item:keyPersonnelDtos ) {
				email.add(item.getEmailAddr());
			}
			all.addAll(email.stream().distinct().collect(Collectors.toList()));
		}

		return all;
	}

	private List<String> getRecriptAppGrp(List<String> roles, String appGrpId) {
		List<String> list = IaisCommonUtils.genNewArrayList();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpById(appGrpId).getEntity();
		for (String role : roles) {
			if (RECEIPT_ROLE_LICENSEE.equals(role)) {
				List<String> mails = IaisEGPHelper.getLicenseeEmailAddrs(grpDto.getLicenseeId());
				list.addAll(mails);
			} else if (RECEIPT_ROLE_AUTHORISED_PERSON.equals(role)) {

			}
		}

		return list;
	}
}
