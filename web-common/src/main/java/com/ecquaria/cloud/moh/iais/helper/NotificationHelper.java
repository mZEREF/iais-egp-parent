package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.service.client.IaisSystemClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

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
	public static final String RECEIPT_TYPE_SMS		                 = "SMS";
	public static final String RECEIPT_TYPE_NOTIFICATION 			 = "MESTYPE001";
	public static final String RECEIPT_TYPE_ANNONUCEMENT			 = "MESTYPE002";
	public static final String RECEIPT_TYPE_ACTION_REQUIRED			 = "MESTYPE003";

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

	/**
	 * Get different Officer according to different types
	 */
	public static final String OFFICER_MODULE_TYPE_SCHEDULING                   = "Appt-Scheduling";
	public static final String OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK    = "inspector-current-task";

	@Autowired
	private Environment env;

	@Autowired
	private IaisSystemClient iaisSystemClient;
	@Autowired
	private EicClient eicClient;

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
	private MasterCodeClient masterCodeClient;
	@Autowired
	private HcsaServiceClient hcsaServiceClient;
	@Autowired
	private HcsaLicenceClient hcsaLicenceClient;
	@Autowired
	private EmailHistoryCommonClient emailHistoryCommonClient;
	@Value("${iais.current.domain}")
	private String currentDomain;
	@Value("${spring.application.name}")
	private String currentApp;
	@Value("${iais.hmac.keyId}")
	private String keyId;
	@Value("${iais.hmac.second.keyId}")
	private String secKeyId;
	@Value("${iais.hmac.secretKey}")
	private String secretKey;
	@Value("${iais.hmac.second.secretKey}")
	private String secSecretKey;

	@Autowired
	private SystemParamConfig systemParamConfig;

	/*private static List<String> licenceEmailString =  Arrays.asList(
		ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_PO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_DPO,
		ApplicationConsts.PERSONNEL_PSN_TYPE_MAP,
		ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL,
		ApplicationConsts.PERSONNEL_PSN_TYPE_LICENSEE,
		ApplicationConsts.PERSONNEL_PSN_TYPE_AP
			);*/

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

	/**
	 * Method to retrieve All officers by role
	 */
	public List<String> getEmailAddressListByRole(List<String> role){
		List<String> email = IaisCommonUtils.genNewArrayList();
		List<OrgUserDto> orgUserDtoList = taskOrganizationClient.retrieveOrgUserByroleId(role).getEntity();
		for (OrgUserDto item:orgUserDtoList
		) {
			email.add(item.getEmail());
		}
		return email;
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode,
								 String reqRefNum) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, null, null, null, null, null,
				true, null);
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode,
								 String reqRefNum, String refIdType, String refId) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, null, null, null,
				true, null);
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode,
								 String reqRefNum, String refIdType, String refId, String subject) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, null, subject, null,
				true, null);
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode,
								 String reqRefNum, String refIdType, String refId, JobRemindMsgTrackingDto jrDto, String subject) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, jrDto, subject, null,
				true, null);
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode, String reqRefNum,
								 String refIdType, String refId, String subject, HashMap<String, String> maskParams) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, null, subject, null,
				true, maskParams);
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode, String reqRefNum,
								 String refIdType, String refId, JobRemindMsgTrackingDto jrDto, String subject, HashMap<String, String> maskParams) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, jrDto, subject, null,
				true, maskParams);
	}

	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode,
								 String reqRefNum, String refIdType, String refId, JobRemindMsgTrackingDto jrDto) {
		sendNotificationWithJobTrack(templateId, templateContent, queryCode, reqRefNum, refIdType, refId, jrDto, null, null,
				true, null);
	}

	@Async("emailAsyncExecutor")
	private void sendNotificationWithJobTrack(String templateId, Map<String, Object> templateContent, String queryCode, String reqRefNum, String refIdType,
											 String refId, JobRemindMsgTrackingDto jrDto, String subject, String moduleType, boolean smsOnlyOfficerHour,
											 HashMap<String, String> maskParams) {
		log.info(StringUtil.changeForLog("sendemail start... ref type is " + StringUtil.nullToEmptyStr(refIdType)
				+ " ref Id is " + StringUtil.nullToEmptyStr(refId)
				+ "templateId is "+ templateId+"thread name is " + Thread.currentThread().getName()));
		try {
			List<String> receiptEmail;
			List<String> ccEmail;
			List<String> bccEmail;
			MsgTemplateDto msgTemplateDto = iaisSystemClient.getMsgTemplate(templateId).getEntity();
			if(AppConsts.COMMON_STATUS_IACTIVE.equals(msgTemplateDto.getStatus())){
				return;
			}
			//get mesContext
			String mesContext;
			String emailTemplate = msgTemplateDto.getMessageContent();
			//replace num
			emailTemplate = replaceNum(emailTemplate);
			if (templateContent != null && !templateContent.isEmpty()) {
				if(templateContent.get("msgContent") != null ){
					mesContext = (String) templateContent.get("msgContent");
				}else {
					mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateContent);
				}
			} else {
				mesContext = emailTemplate;
			}
			if(RECEIPT_TYPE_SMS.equals(refIdType)) {
				int smsFlag = systemParamConfig.getEgpSmsNotifications();
				if(0 == smsFlag){
					return;
				}
				sendSms(templateId, mesContext, refId, smsOnlyOfficerHour);
				if (jrDto != null) {
					List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
					jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
					jobList.add(jrDto);
					iaisSystemClient.createJobRemindMsgTracking(jobList);
				}
			} else if (RECEIPT_TYPE_NOTIFICATION.equals(refIdType) ||
					RECEIPT_TYPE_ANNONUCEMENT.equals(refIdType) ||
					RECEIPT_TYPE_ACTION_REQUIRED.equals(refIdType)) {
				// send message
				if(StringUtil.isEmpty(subject)){
					subject = msgTemplateDto.getTemplateName();
				}
				sendMessage(mesContext, refId, refIdType, subject, maskParams);
				if (jrDto != null) {
					List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
					jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
					jobList.add(jrDto);
					iaisSystemClient.createJobRemindMsgTracking(jobList);
				}
			} else {
				int emailFlag = systemParamConfig.getEgpEmailNotifications();
				if(0 == emailFlag){
					log.info("please turn on email param.......");
					return;
				}
				EmailDto emailDto = new EmailDto();
				//get assign officer address and other address
				InspectionEmailTemplateDto inspectionEmailTemplateDto = new InspectionEmailTemplateDto();
				if (msgTemplateDto.getRecipient() != null && msgTemplateDto.getRecipient().size() > 0) {
					inspectionEmailTemplateDto = getRecript(msgTemplateDto.getRecipient(), refIdType, refId, moduleType, inspectionEmailTemplateDto);
					receiptEmail = inspectionEmailTemplateDto.getReceiptEmails();
					if (!IaisCommonUtils.isEmpty(receiptEmail)) {
						emailDto.setReceipts(receiptEmail);
					}
				}
				if (msgTemplateDto.getCcrecipient() != null && msgTemplateDto.getCcrecipient().size() > 0) {
					inspectionEmailTemplateDto = getRecript(msgTemplateDto.getCcrecipient(), refIdType, refId, moduleType, inspectionEmailTemplateDto);
					ccEmail = inspectionEmailTemplateDto.getReceiptEmails();
					if (!IaisCommonUtils.isEmpty(ccEmail)) {
						emailDto.setCcList(ccEmail);
					}
				}
				if (msgTemplateDto.getBccrecipient() != null && msgTemplateDto.getBccrecipient().size() > 0) {
					inspectionEmailTemplateDto = getRecript(msgTemplateDto.getBccrecipient(), refIdType, refId, moduleType, inspectionEmailTemplateDto);
					bccEmail = inspectionEmailTemplateDto.getReceiptEmails();
					if (!IaisCommonUtils.isEmpty(bccEmail)) {
						emailDto.setBccList(bccEmail);
					}
				}
				emailDto.setContent(mesContext);
				if (StringUtil.isEmpty(subject)) {
					msgTemplateDto.setTemplateName(MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), templateContent));
					emailDto.setSubject(msgTemplateDto.getTemplateName());
				} else {
					emailDto.setSubject(subject);
				}
				emailDto.setSender(this.mailSender);
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
				boolean jobRemindFlag = false;
				//send other address
				if (!IaisCommonUtils.isEmpty(emailDto.getReceipts())) {
					if (AppConsts.DOMAIN_INTERNET.equals(currentDomain)) {
						String gatewayUrl = env.getProperty("iais.inter.gateway.url");
						HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
						HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
						IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/no-attach-emails", HttpMethod.POST, emailDto,
								MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
								signature2.date(), signature2.authorization(), String.class);
					} else {
						emailSmsClient.sendEmail(emailDto, null);
					}
					if (jrDto != null) {
						List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
						jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
						jobList.add(jrDto);
						iaisSystemClient.createJobRemindMsgTracking(jobList);
						jobRemindFlag = true;
					}
				} else {
					log.info("No receipts. Won't send email.");
				}
				//send officer address
				Map<String, String> officerNameMap = inspectionEmailTemplateDto.getOfficerNameMap();
				Map<String, String> emailAddressMap = inspectionEmailTemplateDto.getEmailAddressMap();
				if (officerNameMap != null && emailAddressMap != null) {
					for (Map.Entry<String, String> onMap : officerNameMap.entrySet()) {
						String orgUserKey = onMap.getKey();
						String orgName = onMap.getKey();
						List<String> officerEmails = IaisCommonUtils.genNewArrayList();
						String officerEmail = emailAddressMap.get(orgUserKey);
						officerEmails.add(officerEmail);
						if (templateContent != null && !templateContent.isEmpty()) {
							boolean officerFlag = templateContent.containsKey("officer_name");
							if(officerFlag) {
								templateContent.put("officer_name", orgName);
								mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateContent);
							}
						}
						emailDto.setContent(mesContext);
						emailDto.setReceipts(officerEmails);
						if (AppConsts.DOMAIN_INTERNET.equals(currentDomain)) {
							String gatewayUrl = env.getProperty("iais.inter.gateway.url");
							HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
							HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
							IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/no-attach-emails", HttpMethod.POST, emailDto,
									MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
									signature2.date(), signature2.authorization(), String.class);
						} else {
							emailSmsClient.sendEmail(emailDto, null);
						}
					}
					if (!jobRemindFlag && jrDto != null) {
						List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
						jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
						jobList.add(jrDto);
						iaisSystemClient.createJobRemindMsgTracking(jobList);
					}
				}
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

	private void sendMessage(String mesContext, String appNo, String refIdType, String subject, HashMap<String, String> maskParams) {
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpByAppNo(appNo).getEntity();
		ApplicationDto applicationDto = hcsaAppClient.getAppByNo(appNo).getEntity();
		String serviceId = applicationDto.getServiceId();
		HcsaServiceDto hcsaServiceDto = hcsaServiceClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
		String serviceCode = hcsaServiceDto.getSvcCode();
		InterMessageDto interMessageDto = new InterMessageDto();
		interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
		interMessageDto.setSubject(subject);
		interMessageDto.setMessageType(refIdType);
		String mesNO = getHelperMessageNo();
		interMessageDto.setRefNo(mesNO);
		interMessageDto.setService_id(serviceCode+"@");
		interMessageDto.setUserId(grpDto.getLicenseeId());
		interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
		interMessageDto.setMsgContent(mesContext);
		interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
		interMessageDto.setMaskParams(maskParams);
		saveInterMessage(interMessageDto);
	}

	private void saveInterMessage(InterMessageDto interMessageDto) {
		String moduleName = currentApp + "-" + currentDomain;
		EicRequestTrackingDto dto = new EicRequestTrackingDto();
		dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
		dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
		dto.setActionClsName(this.getClass().getName());
		dto.setActionMethod("callEicInterMsg");
		dto.setDtoClsName(interMessageDto.getClass().getName());
		dto.setDtoObject(JsonUtil.parseToJson(interMessageDto));
		dto.setRefNo(interMessageDto.getRefNo());
		dto.setModuleName(moduleName);
		eicClient.saveEicTrack(dto);
		callEicInterMsg(interMessageDto);
		dto = eicClient.getPendingRecordByReferenceNumber(interMessageDto.getRefNo()).getEntity();
		Date now = new Date();
		dto.setProcessNum(1);
		dto.setFirstActionAt(now);
		dto.setLastActionAt(now);
		dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
		List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
		list.add(dto);
		eicClient.updateStatus(list);
	}

	public void callEicInterMsg(InterMessageDto interMessageDto) {
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		emailSmsClient.saveInboxMessage(interMessageDto, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization()).getEntity();
	}

	private String getHelperMessageNo() {
		return masterCodeClient.messageID().getEntity();
	}

	private void sendSms(String templateId, String mesContext, String refId, boolean smsOnlyOfficerHour) {
		try{
			SmsDto smsDto = new SmsDto();
			smsDto.setSender(mailSender);
			smsDto.setContent(mesContext);
			smsDto.setOnlyOfficeHour(smsOnlyOfficerHour);
			String refNo = templateId;
			List<String> mobile = hcsaLicenceClient.getMobileByRole(refId).getEntity();
			if (!IaisCommonUtils.isEmpty(mobile)) {
				emailHistoryCommonClient.sendSMS(mobile, smsDto, refNo);
			}
		}catch (Exception e){
			log.error(StringUtil.changeForLog("error"));
		}
	}

	private String replaceNum(String emailTemplate) {
		int index = 1;
		String replaceStr = "num_rep";
		while(emailTemplate.contains(replaceStr)){
			emailTemplate = emailTemplate.replaceFirst(replaceStr,  index + ".");
			index++;
		}
		return emailTemplate;
	}

	public InspectionEmailTemplateDto getRecript(List<String> role, String refType, String refId, String moduleType, InspectionEmailTemplateDto inspectionEmailTemplateDto) {
		if (RECEIPT_TYPE_APP_GRP.equals(refType)) {
			inspectionEmailTemplateDto = getRecriptAppGrp(role, refId, moduleType, inspectionEmailTemplateDto);
		} else if (RECEIPT_TYPE_APP.equals(refType)) {
			inspectionEmailTemplateDto = getRecriptApp(role, refId, moduleType, inspectionEmailTemplateDto);
		} else if (RECEIPT_TYPE_LICENCE_ID.equals(refType)){
			inspectionEmailTemplateDto = getRecriptLic(role, refId, inspectionEmailTemplateDto);
		} else {
			inspectionEmailTemplateDto = getOfficer(role, inspectionEmailTemplateDto);
		}
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getRecriptLic(List<String> roles, String licenceId, InspectionEmailTemplateDto inspectionEmailTemplateDto) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		set.addAll(getRecrptPersonnel(roles, licenceId));
		LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licenceId).getEntity();
		if(licenceDto != null){
			String licenseeId = licenceDto.getLicenseeId();
			if(!StringUtil.isEmpty(licenseeId)) {
				set.addAll(getRecrptLicensee(roles, licenseeId));
			}
		}
		inspectionEmailTemplateDto = getOfficer(roles, inspectionEmailTemplateDto);//NOSONAR
		List<String> receiptEmails = new ArrayList<>(set);
		inspectionEmailTemplateDto.setReceiptEmails(receiptEmails);//NOSONAR
		return inspectionEmailTemplateDto;
	}

	private Collection<String> getRecrptPersonnel(List<String> roles, String licenceId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		for (String role : roles) {
			if (RECEIPT_ROLE_SVC_PERSONNEL.equals(role)) {
				List<PersonnelsDto> personnelsDtos = hcsaLicenceClient.getPersonnelDtoByLicId(licenceId).getEntity();
				log.info(StringUtil.changeForLog("PersonnelsDto Size = " + personnelsDtos.size()));
				if(!IaisCommonUtils.isEmpty(personnelsDtos)){
					for(PersonnelsDto personnelsDto : personnelsDtos){
						KeyPersonnelDto keyPersonnelDto = personnelsDto.getKeyPersonnelDto();
						if(keyPersonnelDto != null){
							String emailAddress = keyPersonnelDto.getEmailAddr();
							if(!StringUtil.isEmpty(emailAddress)){
								set.add(emailAddress);
							}
						}
					}
				}
			}
		}
		return set;
	}

	private InspectionEmailTemplateDto getRecriptAppGrp(List<String> roles, String appGrpId, String moduleType,
														InspectionEmailTemplateDto inspectionEmailTemplateDto) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpById(appGrpId).getEntity();
		List<ApplicationDto> appList = hcsaAppClient.getAppsByGrpId(appGrpId).getEntity();
		for (ApplicationDto app : appList) {
			inspectionEmailTemplateDto = getAssignedOfficer(roles, app.getApplicationNo(), moduleType, inspectionEmailTemplateDto);//NOSONAR
		}
		set.addAll(getRecrptLicensee(roles, grpDto.getLicenseeId()));
		inspectionEmailTemplateDto = getOfficer(roles, inspectionEmailTemplateDto);//NOSONAR
		List<String> receiptEmails = new ArrayList<>(set);
		inspectionEmailTemplateDto.setReceiptEmails(receiptEmails);//NOSONAR
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getRecriptApp(List<String> roles, String appNo, String moduleType, InspectionEmailTemplateDto inspectionEmailTemplateDto) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpByAppNo(appNo).getEntity();
		set.addAll(getRecrptLicensee(roles, grpDto.getLicenseeId()));
		inspectionEmailTemplateDto = getAssignedOfficer(roles, appNo, moduleType, inspectionEmailTemplateDto);//NOSONAR
		inspectionEmailTemplateDto = getOfficer(roles, inspectionEmailTemplateDto);//NOSONAR
		List<String> receiptEmails = new ArrayList<>(set);
		inspectionEmailTemplateDto.setReceiptEmails(receiptEmails);//NOSONAR
		return inspectionEmailTemplateDto;
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
						if (!StringUtil.isEmpty(p.getEmailAddr())) {
							set.add(p.getEmailAddr());
						}
					});
				}
			}
		}

		return set;
	}

	private InspectionEmailTemplateDto getAssignedOfficer(List<String> roles, String appNo, String moduleType, InspectionEmailTemplateDto inspectionEmailTemplateDto) {
		if (OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK.equals(moduleType)){
			inspectionEmailTemplateDto = getCurrentTaskAssignedInspector(inspectionEmailTemplateDto, appNo);//NOSONAR
		}else {
			//The default function
			inspectionEmailTemplateDto = getDefaultAssignedOfficer(roles, inspectionEmailTemplateDto, appNo);//NOSONAR
		}
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getCurrentTaskAssignedInspector(InspectionEmailTemplateDto inspectionEmailTemplateDto, String appNo) {
		if (StringUtil.isEmpty(appNo) || inspectionEmailTemplateDto == null) {
			return inspectionEmailTemplateDto;
		}

		String processUrl = TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION;
		List<String> taskStatus = new ArrayList<>(Arrays.asList(TaskConsts.TASK_STATUS_PENDING, TaskConsts.TASK_STATUS_READ));
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

		Map<String, Object> params = IaisCommonUtils.genNewHashMap();
		params.put("processUrl", processUrl);
		params.put("taskStatus", taskStatus);
		String gatewayUrl = env.getProperty("iais.inter.gateway.url");
		List<OrgUserDto> orgUserList = IaisEGPHelper.callEicGatewayWithBodyForList(gatewayUrl + "/v1/inspector-by-task", HttpMethod.POST, params,
				MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization(), OrgUserDto.class).getEntity();

		if (IaisCommonUtils.isEmpty(orgUserList)) {
			return inspectionEmailTemplateDto;
		}

		Map<String, String> officerNameMap = inspectionEmailTemplateDto.getOfficerNameMap();
		Map<String, String> emailAddressMap = inspectionEmailTemplateDto.getEmailAddressMap();

		if (officerNameMap == null) {
			officerNameMap = IaisCommonUtils.genNewHashMap();
		}

		if (emailAddressMap == null) {
			emailAddressMap = IaisCommonUtils.genNewHashMap();
		}

		int index = officerNameMap.size();
		for (OrgUserDto u : orgUserList) {
			if (!StringUtil.isEmpty(u.getEmail())) {
				officerNameMap.put(String.valueOf(index), u.getDisplayName());
				emailAddressMap.put(String.valueOf(index), u.getEmail());
				index++;
			}
		}

		inspectionEmailTemplateDto.setOfficerNameMap(officerNameMap);
		inspectionEmailTemplateDto.setEmailAddressMap(emailAddressMap);
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getDefaultAssignedOfficer(List<String> roles, InspectionEmailTemplateDto inspectionEmailTemplateDto, String appNo) {
		Set<String> userIds = IaisCommonUtils.genNewHashSet();
		List<AppPremisesRoutingHistoryDto> hisList;
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		if (AppConsts.DOMAIN_INTRANET.equals(currentDomain)) {
			hisList = hcsaAppClient.getAppPremisesRoutingHistorysByAppNo(appNo).getEntity();
		} else {
			String gatewayUrl = env.getProperty("iais.inter.gateway.url");
			Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
			params.put("appNo", appNo);
			hisList = IaisEGPHelper.callEicGatewayWithParamForList(gatewayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
					MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
					signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class).getEntity();
		}
		if (IaisCommonUtils.isEmpty(hisList)) {
			return inspectionEmailTemplateDto;
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

		if (IaisCommonUtils.isEmpty(userIds)) {
			return inspectionEmailTemplateDto;
		}

		List<OrgUserDto> userList = null;
		if (AppConsts.DOMAIN_INTRANET.equals(currentDomain)) {
			userList = taskOrganizationClient.retrieveOrgUsers(userIds).getEntity();
		} else {
			String gatewayUrl = env.getProperty("iais.inter.gateway.url");
			userList = IaisEGPHelper.callEicGatewayWithBodyForList(gatewayUrl + "/v1/moh-officer-info", HttpMethod.POST, userIds,
					MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
					signature2.date(), signature2.authorization(), OrgUserDto.class).getEntity();
		}
		if (!IaisCommonUtils.isEmpty(userList)) {
			Map<String, String> officerNameMap = inspectionEmailTemplateDto.getOfficerNameMap();
			Map<String, String> emailAddressMap = inspectionEmailTemplateDto.getEmailAddressMap();
			if(officerNameMap == null){
				officerNameMap = IaisCommonUtils.genNewHashMap();
			}
			if(emailAddressMap == null){
				emailAddressMap = IaisCommonUtils.genNewHashMap();
			}
			int index = officerNameMap.size();
			for (OrgUserDto u : userList) {
				if (!StringUtil.isEmpty(u.getEmail())) {
					officerNameMap.put(index + "", u.getDisplayName());
					emailAddressMap.put(index + "", u.getEmail());
					index++;
				}
			}
			inspectionEmailTemplateDto.setOfficerNameMap(officerNameMap);
			inspectionEmailTemplateDto.setEmailAddressMap(emailAddressMap);
		}
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getOfficer(List<String> roles, InspectionEmailTemplateDto inspectionEmailTemplateDto) {
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
			List<OrgUserDto> userList = null;
			if (AppConsts.DOMAIN_INTRANET.equals(currentDomain)) {
				userList = taskOrganizationClient.retrieveOrgUserByroleId(passRoles).getEntity();
			}
			if (!IaisCommonUtils.isEmpty(userList)) {
				Map<String, String> officerNameMap = inspectionEmailTemplateDto.getOfficerNameMap();
				Map<String, String> emailAddressMap = inspectionEmailTemplateDto.getEmailAddressMap();
				if(officerNameMap == null){
					officerNameMap = IaisCommonUtils.genNewHashMap();
				}
				if(emailAddressMap == null){
					emailAddressMap = IaisCommonUtils.genNewHashMap();
				}
				int index = officerNameMap.size();
				for (OrgUserDto u : userList) {
					if (!StringUtil.isEmpty(u.getEmail())) {
						officerNameMap.put(index + "", u.getDisplayName());
						emailAddressMap.put(index + "", u.getEmail());
						index++;
					}
				}
				inspectionEmailTemplateDto.setOfficerNameMap(officerNameMap);
				inspectionEmailTemplateDto.setEmailAddressMap(emailAddressMap);
			}
		}

		return inspectionEmailTemplateDto;
	}
}
