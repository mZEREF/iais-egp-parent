package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
public class NotificationHelper {
	public static final String RECEIPT_TYPE_APP_GRP 			  	 = "GRP";
	public static final String RECEIPT_TYPE_APP 				 	 = "APP";
	public static final String RECEIPT_TYPE_LICENCE_ID               = "LIC";

	public static final String RECEIPT_ROLE_LICENSEE                			= "EM-LIC";
	public static final String RECEIPT_ROLE_AUTHORISED_PERSON       			= "EM-AP";
	public static final String RECEIPT_ROLE_ASSIGNED_ASO            			= "EM-A-ASO";
	public static final String RECEIPT_ROLE_ASSIGNED_PSO            			= "EM-A-PSO";
	public static final String RECEIPT_ROLE_ASSIGNED_AO1            			= "EM-A-AO1";
	public static final String RECEIPT_ROLE_ASSIGNED_AO2            			= "EM-A-AO2";
	public static final String RECEIPT_ROLE_ASSIGNED_AO3            			= "EM-A-AO3";
	public static final String RECEIPT_ROLE_ASSIGNED_INSPECTOR      			= "EM-A-INSPECTOR";
	public static final String RECEIPT_ROLE_ASSIGNED_INSPECTOR_LEAD 			= "EM-A-INSPECTOR_LEAD";
	public static final String RECEIPT_ROLE_MOH_OFFICER             			= "EM-AO";
	public static final String RECEIPT_ROLE_SVC_PERSONNEL                       = "EM-SVCPSN";
	public static final String RECEIPT_ROLE_SYSTEM_ADMIN                        = "EM-SYS_ADM";
	public static final String RECEIPT_ROLE_AO                                  = "EM-AO";
	public static final String RECEIPT_ROLE_ASO                                 = "EM-ASO";
	public static final String RECEIPT_ROLE_PSO                                 = "EM-PSO";
	public static final String RECEIPT_ROLE_AO1                                 = "EM-AO1";
	public static final String RECEIPT_ROLE_AO2                                 = "EM-AO2";
	public static final String RECEIPT_ROLE_AO3                                 = "EM-AO3";
	public static final String RECEIPT_ROLE_INSPECTOR                           = "EM-INSPECTOR";
	public static final String RECEIPT_ROLE_INSPECTOR_LEAD                      = "EM-INSPECTOR_LEAD";

	@Autowired
	private Environment env;
	@Autowired
	private IaisSystemClient iaisSystemClient;
	@Autowired
	private LicenseeClient licenseeClient;
	@Value("${iais.email.sender}")
	private String mailSender;
	@Autowired
	private EmailSmsClient emailSmsClient;
	@Autowired
	TaskOrganizationClient taskOrganizationClient;
	@Autowired
	private HcsaAppClient hcsaAppClient;
	@Autowired
	private HcsaLicenceClient hcsaLicenceClient;
	@Value("${iais.current.domain}")
	private String currentDomain;
	@Value("${iais.hmac.keyId}")
	private String keyId;
	@Value("${iais.hmac.second.keyId}")
	private String secKeyId;
	@Value("${iais.hmac.secretKey}")
	private String secretKey;
	@Value("${iais.hmac.second.secretKey}")
	private String secSecretKey;

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

	@Async("emailAsyncExecutor")
	public void sendEmail(String templateId, Map<String, Object> templateContent, String queryCode,
						  String reqRefNum, String refIdType, String refId) {
		sendEmailWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, null);
	}

	@Async("emailAsyncExecutor")
	public void sendEmailWithJobTrack(String templateId, Map<String, Object> templateContent, String queryCode,
									  String reqRefNum, String refIdType, String refId, JobRemindMsgTrackingDto jrDto) {
		log.info(StringUtil.changeForLog("sendemail start... ref type is " + StringUtil.nullToEmptyStr(refIdType)
				+ " ref Id is " + StringUtil.nullToEmptyStr(refId)
				+ "templateId is "+ templateId+"thread name is " + Thread.currentThread().getName()));
		try {
			List<String> receiptemail = IaisCommonUtils.genNewArrayList();
			List<String> ccemail = IaisCommonUtils.genNewArrayList();
			List<String> bccemail = IaisCommonUtils.genNewArrayList();
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
			if (queryCode != null) {
				emailDto.setClientQueryCode(queryCode);
			} else {
				emailDto.setClientQueryCode("no queryCode");
			}
			if (reqRefNum != null) {
				emailDto.setReqRefNum(reqRefNum);
			} else {
				emailDto.setReqRefNum("no reqRefNum");
			}
			//send
			if (!IaisCommonUtils.isEmpty(emailDto.getReceipts())) {
				if (AppConsts.DOMAIN_INTERNET.equals(currentDomain)) {
					String gatewayUrl = env.getProperty("iais.inter.gateway.url");
					HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
					HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
					IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/no-attach-emails", HttpMethod.POST, emailDto,
							MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
							signature2.date(), signature2.authorization(), String.class);
				} else {
					emailSmsClient.sendEmail(emailDto,null);
				}
				if (jrDto != null) {
					List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
					jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
					jobList.add(jrDto);
					iaisSystemClient.createJobRemindMsgTracking(jobList);
				}
			} else {
				log.info("No receipts. Won't send email.");
			}
		} catch (Exception e) {
			log.error("Error when sending email ==>", e);
			if (jrDto != null) {
				List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
				jrDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
				jobList.add(jrDto);
				iaisSystemClient.createJobRemindMsgTracking(jobList);
			}
		}
		log.info(StringUtil.changeForLog("sendemail end... queryCode is"+queryCode + "templateId is "
				+ templateId+"thread name is " + Thread.currentThread().getName()));
	}

	public List<String> getRecript(List<String> role, String refType, String refId) {
		List<String> all = IaisCommonUtils.genNewArrayList();
		if (RECEIPT_TYPE_APP_GRP.equals(refType)) {
			all.addAll(getRecriptAppGrp(role, refId));
		} else if (RECEIPT_TYPE_APP.equals(refType)) {
			all.addAll(getRecriptApp(role, refId));
		} else {

		}

		return all;
	}

	private Collection<String> getRecriptAppGrp(List<String> roles, String appGrpId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpById(appGrpId).getEntity();
		List<ApplicationDto> appList = hcsaAppClient.getAppsByGrpId(appGrpId).getEntity();
		for (ApplicationDto app : appList) {
			set.addAll(getAssignedOfficer(roles, app.getApplicationNo()));
		}
		set.addAll(getRecrptLicensee(roles, grpDto.getLicenseeId()));
		set.addAll(getOfficer(roles));

		return set;
	}

	private Collection<String> getRecriptApp(List<String> roles, String appNo) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpByAppNo(appNo).getEntity();
		set.addAll(getRecrptLicensee(roles, grpDto.getLicenseeId()));
		set.addAll(getAssignedOfficer(roles, appNo));
		set.addAll(getOfficer(roles));

		return set;
	}

	private Collection<String> getRecrptLicensee(List<String> roles, String licenseeId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		for (String role : roles) {
			if (RECEIPT_ROLE_LICENSEE.equals(role)) {
				List<String> mails = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
				set.addAll(mails);
			} else if (RECEIPT_ROLE_AUTHORISED_PERSON.equals(role)) {
				List<LicenseeKeyApptPersonDto> pers = licenseeClient.getPersonByid(licenseeId).getEntity();
				if (!IaisCommonUtils.isEmpty(pers)) {
					pers.forEach(p -> {
						if (StringUtil.isEmpty(p.getEmailAddr())) {
							set.add(p.getEmailAddr());
						}
					});
				}
			}
		}

		return set;
	}

	private Collection<String> getAssignedOfficer(List<String> roles, String appNo) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		Set<String> userIds = IaisCommonUtils.genNewHashSet();
		if (!AppConsts.DOMAIN_INTRANET.equals(currentDomain)) {
			return set;
		}
		List<AppPremisesRoutingHistoryDto> hisList = hcsaAppClient.getAppPremisesRoutingHistorysByAppNo(appNo).getEntity();
		if (IaisCommonUtils.isEmpty(hisList)) {
			return set;
		}
		Map<String, List<String>> userMap = IaisCommonUtils.genNewHashMap();
		for (AppPremisesRoutingHistoryDto his : hisList) {
			if (userMap.get(his.getRoleId()) == null) {
				userMap.put(his.getRoleId(), IaisCommonUtils.genNewArrayList());
			}
			userMap.get(his.getRoleId()).add(his.getActionby());
		}
		for (String role : roles) {
			if (RECEIPT_ROLE_ASSIGNED_ASO.equals(role) && userMap.get(RoleConsts.USER_ROLE_ASO) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_ASO));
			} else if (RECEIPT_ROLE_ASSIGNED_PSO.equals(role) && userMap.get(RoleConsts.USER_ROLE_PSO) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_PSO));
			} else if (RECEIPT_ROLE_ASSIGNED_AO1.equals(role) && userMap.get(RoleConsts.USER_ROLE_AO1) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_AO1));
			} else if (RECEIPT_ROLE_ASSIGNED_AO2.equals(role) && userMap.get(RoleConsts.USER_ROLE_AO2) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_AO2));
			} else if (RECEIPT_ROLE_ASSIGNED_AO3.equals(role) && userMap.get(RoleConsts.USER_ROLE_AO3) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_AO3));
			} else if (RECEIPT_ROLE_ASSIGNED_INSPECTOR.equals(role) && userMap.get(RoleConsts.USER_ROLE_INSPECTIOR) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_INSPECTIOR));
			} else if (RECEIPT_ROLE_ASSIGNED_INSPECTOR_LEAD.equals(role) && userMap.get(RoleConsts.USER_ROLE_INSPECTION_LEAD) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_INSPECTION_LEAD));
			}
		}
		List<OrgUserDto> userList = taskOrganizationClient.retrieveOrgUsers(userIds).getEntity();
		if (!IaisCommonUtils.isEmpty(userList)) {
			for (OrgUserDto u : userList) {
				if (!StringUtil.isEmpty(u.getEmail())) {
					set.add(u.getEmail());
				}
			}
		}

		return set;
	}

	private Collection<String> getOfficer(List<String> roles) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		List<String> adminRoles = IaisCommonUtils.genNewArrayList();
		List<String> passRoles = IaisCommonUtils.genNewArrayList();
		adminRoles.add(RoleConsts.USER_ROLE_ASO);
		adminRoles.add(RoleConsts.USER_ROLE_PSO);
		adminRoles.add(RoleConsts.USER_ROLE_AO1);
		adminRoles.add(RoleConsts.USER_ROLE_AO2);
		adminRoles.add(RoleConsts.USER_ROLE_AO3);
		adminRoles.add(RoleConsts.USER_ROLE_INSPECTIOR);
		adminRoles.add(RoleConsts.USER_ROLE_INSPECTION_LEAD);
		adminRoles.add(RoleConsts.USER_ROLE_AUDIT_PLAN);
		if (roles.contains(RECEIPT_ROLE_MOH_OFFICER)) {
			passRoles.addAll(adminRoles);
		} else {
			roles.forEach(r -> {
				String role = r.substring(3, r.length());
				if (adminRoles.contains(role)) {
					passRoles.add(role);
				}
			});
		}

		if (!IaisCommonUtils.isEmpty(passRoles)){
			List<OrgUserDto> userList = taskOrganizationClient.retrieveOrgUserByroleId(passRoles).getEntity();
			if (!IaisCommonUtils.isEmpty(userList)) {
				for (OrgUserDto u : userList) {
					if (!StringUtil.isEmpty(u.getEmail())) {
						set.add(u.getEmail());
					}
				}
			}
		}

		return set;
	}
}
