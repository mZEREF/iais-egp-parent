package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
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
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.service.client.CommonFeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailHistoryCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.MasterCodeClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * @author: yichen
 * @date time:2/26/2020 10:56 AM
 * @description:
 */

@Component
@Slf4j
@EnableAsync
public class NotificationHelper {
	public static final String RECEIPT_TYPE_APP_GRP 			  	 = "GRP";
	public static final String RECEIPT_TYPE_APP 				 	 = "APP";
	public static final String RECEIPT_TYPE_LICENCE_ID               = "LIC";
	public static final String RECEIPT_TYPE_LICENSEE_ID               = "LICENSEE";
	public static final String RECEIPT_TYPE_SMS_PSN		             = "SMS_PSN";
	public static final String RECEIPT_TYPE_SMS_APP		             = "SMS_APP";
	public static final String RECEIPT_TYPE_SMS_LICENCE_ID		     = "SMS_LIC";
	public static final String RECEIPT_TYPE_SMS_LICENSEE_ID		     = "SMS_LICENSEE";
	public static final String MESSAGE_TYPE_NOTIFICATION 			 = "MESTYPE001";
	public static final String MESSAGE_TYPE_ANNONUCEMENT			 = "MESTYPE002";
	public static final String MESSAGE_TYPE_ACTION_REQUIRED			 = "MESTYPE003";

	public static final String RECEIPT_ROLE_LICENSEE                			= "EM-LIC";
	public static final String RECEIPT_ROLE_TRANSFEREE_LICENSEE                	= "TRANSFEREE";
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
	private GenerateIdClient generateIdClient;
	@Autowired
	private EicClient eicClient;

	@Value("${iais.email.sender}")
	private String mailSender;
	@Autowired
	private EmailSmsClient emailSmsClient;
	@Autowired
	TaskOrganizationClient taskOrganizationClient;
	@Autowired
	private HcsaAppClient hcsaAppClient;
	@Autowired
	private CommonFeMessageClient commonFeMessageClient;
	@Autowired
	private MasterCodeClient masterCodeClient;
	@Autowired
	private HcsaLicenceCommonClient hcsaLicenceClient;
	@Autowired
	private EmailHistoryCommonClient emailHistoryCommonClient;

	@Autowired
	private MsgCommonUtil msgCommonUtil;

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
		return generateIdClient.getMsgTemplate(templateId).getEntity();
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

	private String replaceText(String text, HashMap<String, String> params){
		for (Map.Entry<String, String> entry : params.entrySet()){
			String sign = entry.getKey();
			String value = entry.getValue();
			if (!StringUtils.isEmpty(text) &&  !StringUtil.isEmpty(sign) && text.indexOf(sign) != -1 && !StringUtil.isEmpty(value)){
				text = text.replace(sign, value);
			}
		}
		return text;
	}

	@Deprecated
	public void sendNotification(String templateId, Map<String, Object> templateContent, String queryCode,
								 String reqRefNum, String refIdType, String refId) {
		EmailParam emailParam = new EmailParam();
		emailParam.setTemplateId(templateId);
		emailParam.setTemplateContent(templateContent);
		emailParam.setQueryCode(queryCode);
		emailParam.setReqRefNum(reqRefNum);
		emailParam.setRefIdType(refIdType);
		emailParam.setRefId(refId);
		sendNotification(emailParam);
	}

	@Async("emailAsyncExecutor")
	public void sendNotification(EmailParam emailParam){
		if (emailParam == null){
			return;
		}

		List<String> svcCodeList = emailParam.getSvcCodeList();
		String templateId = emailParam.getTemplateId();
		Map<String, Object> templateContent = emailParam.getTemplateContent();
		String queryCode = emailParam.getQueryCode();
		String reqRefNum = emailParam.getReqRefNum();
		String refIdType = emailParam.getRefIdType();
		String refId = emailParam.getRefId();
		String recipientType =emailParam.getRecipientType();
		String recipientUserId=emailParam.getRecipientUserId();
		boolean isSendNewLicensee=emailParam.isNeedSendNewLicensee();
		HashMap<String, String> subjectParams = emailParam.getSubjectParams();
		JobRemindMsgTrackingDto jrDto = emailParam.getJobRemindMsgTrackingDto();
		String subject = emailParam.getSubject();
		String moduleType = emailParam.getModuleType();
		boolean smsOnlyOfficerHour = emailParam.isSmsOnlyOfficerHour();
		HashMap<String, String> maskParams = emailParam.getMaskParams();

		log.info(StringUtil.changeForLog("sendemail start... ref type is " + StringUtil.nullToEmptyStr(refIdType)
				+ " ref Id is " + StringUtil.nullToEmptyStr(refId)
				+ "templateId is "+ templateId+ " thread name is " + Thread.currentThread().getName()));
		MsgTemplateDto msgTemplateDto ;
		if(recipientType!=null&&!recipientType.isEmpty()){
			msgTemplateDto = generateIdClient.getMsgTemplate(templateId,recipientType).getEntity();
		}else {
			msgTemplateDto = generateIdClient.getMsgTemplate(templateId).getEntity();
		}
		String deliveryMode = msgTemplateDto.getDeliveryMode();

		if (MessageConstants.TEMPLETE_DELIVERY_MODE_MSG.equals(deliveryMode)) {
			if (AppConsts.COMMON_STATUS_IACTIVE.equals(msgTemplateDto.getStatus())) {
				return;
			}
			//get mesContext
			String mesContext;
			String emailTemplate = msgTemplateDto.getMessageContent();

			if (templateContent != null && !templateContent.isEmpty()) {
				if (templateContent.get("msgContent") != null) {
					mesContext = (String) templateContent.get("msgContent");
				} else {
					try {
						mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateContent);
					}catch (IOException | TemplateException e) {
						log.error(e.getMessage(), e);
						throw new IaisRuntimeException(e);
					}
				}
			} else {
				mesContext = emailTemplate;
			}
			//replace num
			mesContext = MessageTemplateUtil.replaceNum(mesContext);
			// send message
			if(StringUtil.isEmpty(subject)){
				subject = msgTemplateDto.getTemplateName();
			}

			if (!IaisCommonUtils.isEmpty(subjectParams)){
				subject = replaceText(subject, subjectParams);
			}

			sendMessage(mesContext, refId, refIdType, subject, maskParams, svcCodeList,isSendNewLicensee);
			if (jrDto != null) {
				List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
				jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
				jobList.add(jrDto);
				generateIdClient.createJobRemindMsgTracking(jobList);
			}
		} else {
			try {
				List<String> receiptEmail;
				List<String> ccEmail;
				List<String> bccEmail;
				if (AppConsts.COMMON_STATUS_IACTIVE.equals(msgTemplateDto.getStatus())) {
					return;
				}
				//get mesContext
				String mesContext;
				String emailTemplate = msgTemplateDto.getMessageContent();

				if (templateContent != null && !templateContent.isEmpty()) {
					if (templateContent.get("msgContent") != null) {
						mesContext = (String) templateContent.get("msgContent");
					} else {
						mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateContent);
					}
				} else {
					mesContext = emailTemplate;
				}

				if (StringUtil.isEmpty(subject)) {
					subject = msgTemplateDto.getTemplateName();
				}

				if (!IaisCommonUtils.isEmpty(subjectParams)){
					subject = replaceText(subject, subjectParams);
				}

				if (MessageConstants.TEMPLETE_DELIVERY_MODE_SMS.equals(deliveryMode)) {
					int smsFlag = systemParamConfig.getEgpSmsNotifications();
					if (0 == smsFlag) {
						log.info("please turn on sms param.......");
						return;
					}
					sendSms(refIdType, subject, refId, smsOnlyOfficerHour, msgTemplateDto,
							emailParam.getSvcCodeList(),recipientUserId);
					if (jrDto != null) {
						List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
						jrDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
						jobList.add(jrDto);
						generateIdClient.createJobRemindMsgTracking(jobList);
					}
				} else if(MessageConstants.TEMPLETE_DELIVERY_MODE_EMAIL.equals(deliveryMode)) {
					int emailFlag = systemParamConfig.getEgpEmailNotifications();
					if (0 == emailFlag) {
						log.info("please turn on email param.......");
						return;
					}
					EmailDto emailDto = new EmailDto();
					//get assign officer address and other address
					InspectionEmailTemplateDto inspectionEmailTemplateDto = new InspectionEmailTemplateDto();
					inspectionEmailTemplateDto.setEmailTemplateId(templateId);
					if (msgTemplateDto.getRecipient() != null && msgTemplateDto.getRecipient().size() > 0) {
						inspectionEmailTemplateDto = getRecript(msgTemplateDto.getRecipient(), refIdType, refId, moduleType, inspectionEmailTemplateDto,recipientUserId);
						receiptEmail = inspectionEmailTemplateDto.getReceiptEmails();
						if (!IaisCommonUtils.isEmpty(receiptEmail)) {
							emailDto.setReceipts(receiptEmail);
						}
					}

					if(IaisCommonUtils.isEmpty(emailDto.getReceipts()) && StringUtil.isNotEmpty(emailParam.getRecipientEmail())){
						receiptEmail = IaisCommonUtils.genNewArrayList();
						receiptEmail.add(emailParam.getRecipientEmail());
						emailDto.setReceipts(receiptEmail);
						templateContent.put("officer_name",StringUtil.getNonNull(emailParam.getRecipientName()));
						mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateContent);
						//replace num
						mesContext = MessageTemplateUtil.replaceNum(mesContext);
						emailDto.setContent(mesContext);
					}
					if (msgTemplateDto.getCcrecipient() != null && msgTemplateDto.getCcrecipient().size() > 0) {
						inspectionEmailTemplateDto = getRecript(msgTemplateDto.getCcrecipient(), refIdType, refId, moduleType, inspectionEmailTemplateDto,recipientUserId);
						ccEmail = inspectionEmailTemplateDto.getReceiptEmails();
						if (!IaisCommonUtils.isEmpty(ccEmail)) {
							emailDto.setCcList(ccEmail);
						}
					}
					if (msgTemplateDto.getBccrecipient() != null && msgTemplateDto.getBccrecipient().size() > 0) {
						inspectionEmailTemplateDto = getRecript(msgTemplateDto.getBccrecipient(), refIdType, refId, moduleType, inspectionEmailTemplateDto,recipientUserId);
						bccEmail = inspectionEmailTemplateDto.getReceiptEmails();
						if (!IaisCommonUtils.isEmpty(bccEmail)) {
							emailDto.setBccList(bccEmail);
						}
					}
					//replace num
					mesContext = MessageTemplateUtil.replaceNum(mesContext);
					emailDto.setContent(mesContext);
					emailDto.setSubject(subject);
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
					if (!IaisCommonUtils.isEmpty(emailDto.getReceipts()) ||
							!IaisCommonUtils.isEmpty(emailDto.getCcList()) ||
							!IaisCommonUtils.isEmpty(emailDto.getBccList())) {
						if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
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
							generateIdClient.createJobRemindMsgTracking(jobList);
							jobRemindFlag = true;
						}
					} else {
						log.info("No receipts. Won't send email.");
					}
					//send officer address
					Map<String, String> officerNameMap = inspectionEmailTemplateDto.getOfficerNameMap();
					Map<String, String> emailAddressMap = inspectionEmailTemplateDto.getEmailAddressMap();
					if (!IaisCommonUtils.isEmpty(officerNameMap)  && !IaisCommonUtils.isEmpty(emailAddressMap)) {
						for (Map.Entry<String, String> officerEntry : officerNameMap.entrySet()) {
							String key = officerEntry.getKey();
							String officerValue = officerEntry.getValue();
							List<String> officerEmails = IaisCommonUtils.genNewArrayList();
							String officerEmail = emailAddressMap.get(key);
							officerEmails.add(officerEmail);
							if (!IaisCommonUtils.isEmpty(templateContent)) {
								boolean officerFlag = templateContent.containsKey("officer_name");
								if (officerFlag) {
									templateContent.put("officer_name", officerValue);
									mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateContent);
									//replace num
									mesContext = MessageTemplateUtil.replaceNum(mesContext);
									emailDto.setContent(mesContext);
								}
							}
							emailDto.setReceipts(officerEmails);
							if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
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
							generateIdClient.createJobRemindMsgTracking(jobList);
						}
					}
				}
			} catch (Exception e) {
				log.error("Error when sending email ==>", e);
				if (jrDto != null) {
					List<JobRemindMsgTrackingDto> jobList = IaisCommonUtils.genNewArrayList(1);
					jrDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
					jobList.add(jrDto);
					generateIdClient.createJobRemindMsgTracking(jobList);
				}
			}
		}
		log.info(StringUtil.changeForLog("sendemail end... queryCode is"+queryCode + "templateId is "
				+ templateId+"thread name is " + Thread.currentThread().getName()));
	}

	private void sendMessage(String mesContext, String appNo, String refIdType, String subject, HashMap<String, String> maskParams, List<String> svcCodeList,boolean isSendNewLicensee) {
		String licenseeId;
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpByAppNo(appNo).getEntity();
		if(grpDto != null) {
			licenseeId = grpDto.getLicenseeId();
			if(grpDto.getNewLicenseeId()!=null&&isSendNewLicensee){
				licenseeId=grpDto.getNewLicenseeId();
			}
		} else {
			String licenceId = appNo;
			try {
				LicenceDto licenceDto = hcsaLicenceClient.getLicDtoByIdCommon(licenceId).getEntity();
				if(licenceDto!=null) {
					licenseeId = licenceDto.getLicenseeId();
				}else {
					licenseeId=appNo;
				}
			}catch (Exception e){
				licenseeId=appNo;
			}
		}
		InterMessageDto interMessageDto = new InterMessageDto();
		interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
		interMessageDto.setSubject(subject);
		interMessageDto.setMessageType(refIdType);
		String mesNO = getHelperMessageNo();
		interMessageDto.setRefNo(mesNO);
		if(IaisCommonUtils.isEmpty(svcCodeList)){
			interMessageDto.setService_id("");
		} else {
			StringBuilder svcCodeShow = new StringBuilder();
			for(String svcCode : svcCodeList){
				svcCodeShow.append(svcCode);
				svcCodeShow.append('@');
			}
			interMessageDto.setService_id(svcCodeShow.toString());
		}
		interMessageDto.setUserId(licenseeId);
		interMessageDto.setStatus(MessageConstants.MESSAGE_STATUS_UNREAD);
		interMessageDto.setMsgContent(mesContext);
		interMessageDto.setMaskParams(maskParams);
		if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
			commonFeMessageClient.createInboxMessage(interMessageDto);
		} else {
			saveInterMessage(interMessageDto);
		}
	}

	private void saveInterMessage(InterMessageDto interMessageDto) {
		String moduleName = currentApp + "-" + currentDomain;
		EicRequestTrackingDto dto = new EicRequestTrackingDto();
		dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
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
		String gatewayUrl = env.getProperty("iais.intra.gateway.url");
		IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interMessageDto,
				MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization(), InterMessageDto.class);
	}

	private String getHelperMessageNo() {
		if(AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)){
			HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
			HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
			String gatewayUrl = env.getProperty("iais.inter.gateway.url");
			return IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/new-inbox-msg-no", HttpMethod.GET, null,
					MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
					signature2.date(), signature2.authorization(), String.class).getEntity();
		} else {
			return masterCodeClient.messageID().getEntity();
		}
	}

	private void sendSms(String refIdType, String mesContext, String refId, boolean smsOnlyOfficerHour,
						 MsgTemplateDto msgTemplateDto, List<String> svcCodeList,String recipientUserId) {
		try{
			List<String> roles = null;
			if (msgTemplateDto.getRecipient() != null && msgTemplateDto.getRecipient().size() > 0) {
				roles = msgTemplateDto.getRecipient();
			}
			if (msgTemplateDto.getCcrecipient() != null && msgTemplateDto.getCcrecipient().size() > 0) {
				roles = msgTemplateDto.getCcrecipient();
			}
			if (msgTemplateDto.getBccrecipient() != null && msgTemplateDto.getBccrecipient().size() > 0) {
				roles = msgTemplateDto.getBccrecipient();
			}
			SmsDto smsDto = new SmsDto();
			smsDto.setSender(mailSender);
			smsDto.setContent(mesContext);
			smsDto.setOnlyOfficeHour(smsOnlyOfficerHour);
			List<String> mobile  = IaisCommonUtils.genNewArrayList();
			if(!StringUtil.isEmpty(refId)){
				if (RECEIPT_TYPE_SMS_PSN.equals(refIdType)) {
					if(!IaisCommonUtils.isEmpty(svcCodeList)) {
						addPsnMobileByRoleSvc(refId, svcCodeList, mobile);
					}
				} else if (RECEIPT_TYPE_SMS_APP.equals(refIdType)) {
					addMobileAssignedOfficer(roles, refId,recipientUserId, mobile);
					addMobileOfficer(roles, mobile,recipientUserId);
				} else if (RECEIPT_TYPE_SMS_LICENCE_ID.equals(refIdType)) {
					addMobilePersonnel(roles, refId, mobile);
					LicenceDto licenceDto = hcsaLicenceClient.getLicDtoByIdCommon(refId).getEntity();
					if(licenceDto != null){
						String licenseeId = licenceDto.getLicenseeId();
						if(!StringUtil.isEmpty(licenseeId)) {
							addMobileLicensee(roles, licenseeId, mobile);
						}
					}else {
						if(!StringUtil.isEmpty(refId)) {
							addMobileLicensee(roles, refId, mobile);
						}
					}
					addMobileOfficer(roles, mobile,recipientUserId);
				}else if (RECEIPT_TYPE_SMS_LICENSEE_ID.equals(refIdType)){
					addMobileLicensee(roles, refId, mobile);
				}
			} else {
				addMobileOfficer(roles, mobile,recipientUserId);
			}

			if (IaisCommonUtils.isNotEmpty(mobile)) {
				mobile = mobile.stream().distinct().collect(Collectors.toList());
				log.info("SMS-Mobile-Send ........ {}", mobile.size());
				if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
					smsDto.setReceipts(mobile);
					smsDto.setReqRefNum(refId);
					HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
					HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
					String gatewayUrl = env.getProperty("iais.inter.gateway.url");
					IaisEGPHelper.callEicGatewayWithBody(gatewayUrl + "/v1/send-sms", HttpMethod.POST, smsDto,
							MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
							signature2.date(), signature2.authorization(), InterMessageDto.class);
				} else {
					emailHistoryCommonClient.sendSMS(mobile, smsDto, refId);
				}
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}

	private void addPsnMobileByRoleSvc(String refId, List<String> svcCodeList, List<String> mobile) {
		for(String svcCode : svcCodeList){
			if(!StringUtil.isEmpty(svcCode)){
				List<String> mobileSvcList = hcsaLicenceClient.getMobileByRole(refId, HcsaServiceCacheHelper.getServiceByCode(svcCode).getSvcName()).getEntity();
				if(!IaisCommonUtils.isEmpty(mobileSvcList)){
					for(String mobileSvc : mobileSvcList){
						if(!StringUtil.isEmpty(mobileSvc)){
							mobile.add(mobileSvc);
						}
					}
				}
			}
		}
	}

	private void addMobilePersonnel(List<String> roles, String licenceId, List<String> mobile) {
		if(IaisCommonUtils.isEmpty(roles)){
			return;
		}
		for (String role : roles) {
			if (RECEIPT_ROLE_SVC_PERSONNEL.equals(role)) {
				List<PersonnelsDto> personnelsDtos = hcsaLicenceClient.getPersonnelDtoByLicId(licenceId).getEntity();
				log.info(StringUtil.changeForLog("PersonnelsDto Size = " + personnelsDtos.size()));
				if(!IaisCommonUtils.isEmpty(personnelsDtos)){
					for(PersonnelsDto personnelsDto : personnelsDtos){
						KeyPersonnelDto keyPersonnelDto = personnelsDto.getKeyPersonnelDto();
						if(keyPersonnelDto != null){
							String mobileNo = keyPersonnelDto.getMobileNo();
							if(!StringUtil.isEmpty(mobileNo)){
								mobile.add(mobileNo);
							}
						}
					}
				}
			}
		}
	}

	private void addMobileOfficer(List<String> roles, List<String> mobile, String recipientUserId) {
		if(IaisCommonUtils.isEmpty(roles)){
			return;
		}
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
		adminRoles.add(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN);
		if (roles.contains(RECEIPT_ROLE_MOH_OFFICER)) {
			passRoles.addAll(adminRoles);
		} else {
			for (String roleStr : roles) {
				for (String adminRoleStr : adminRoles){
					if (roleStr.contains(adminRoleStr)){
						passRoles.add(adminRoleStr);
						break;
					}
				}
			}
		}

		if (!IaisCommonUtils.isEmpty(passRoles)){
			List<OrgUserDto> userList = null;
			if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)) {
				userList = taskOrganizationClient.retrieveOrgUserByroleId(passRoles).getEntity();
			}
			if (!IaisCommonUtils.isEmpty(userList)) {
				for (OrgUserDto u : userList) {
					if(AppConsts.COMMON_STATUS_ACTIVE.equals(u.getStatus())&&u.getAvailable()) {
						if (!StringUtil.isEmpty(u.getEmail()) && (recipientUserId == null || u.getId().equals(recipientUserId))) {
							mobile.add(u.getMobileNo());
						}
					}
				}
			}
		}
	}

	private void addMobileAssignedOfficer(List<String> roles, String appNo,String recipientUserId, List<String> mobile) {
		if(IaisCommonUtils.isEmpty(roles)){
			return;
		}

		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpByAppNo(appNo).getEntity();
		//applicant

		addMobileApplicant(roles, grpDto, mobile);

		//officer
		Set<String> userIds = IaisCommonUtils.genNewHashSet();
		List<AppPremisesRoutingHistoryDto> hisList;
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)) {
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
			return;
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
			} else if (RECEIPT_ROLE_SYSTEM_ADMIN.equals(role) && userMap.get(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN));
			}
		}
		if (IaisCommonUtils.isEmpty(userIds)) {
			return;
		}
		List<OrgUserDto> userList;
		if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)) {
			userList = taskOrganizationClient.retrieveOrgUsers(userIds).getEntity();
		} else {
			String gatewayUrl = env.getProperty("iais.inter.gateway.url");
			userList = IaisEGPHelper.callEicGatewayWithBodyForList(gatewayUrl + "/v1/moh-officer-info", HttpMethod.POST, userIds,
					MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
					signature2.date(), signature2.authorization(), OrgUserDto.class).getEntity();
		}
		if (!IaisCommonUtils.isEmpty(userList)) {
			for (OrgUserDto u : userList) {
				if(AppConsts.COMMON_STATUS_ACTIVE.equals(u.getStatus())&&u.getAvailable()) {
					if (!StringUtil.isEmpty(u.getEmail()) && (recipientUserId == null || u.getId().equals(recipientUserId))) {
						mobile.add(u.getMobileNo());
					}
				}
			}
		}
	}

	private void addMobileLicensee(List<String> roles, String licenseeId, List<String> mobile) {
		for (String role : roles) {
			if (RECEIPT_ROLE_LICENSEE.equals(role)) {
				List<String> mails = IaisEGPHelper.getLicenseeMobiles(licenseeId);
				mobile.addAll(mails);
			}
		}
	}

	private void addMobileApplicant(List<String> roles, ApplicationGroupDto applicantId, List<String> mobile) {
		for (String role : roles) {
			if (RECEIPT_ROLE_LICENSEE.equals(role)) {
				OrgUserDto orgUserDto = taskOrganizationClient.getUserById(applicantId.getSubmitBy()).getEntity();
				if(orgUserDto != null && AppConsts.COMMON_STATUS_ACTIVE.equals(orgUserDto.getStatus())){
					if(!StringUtil.isEmpty(orgUserDto.getMobileNo())) {
						mobile.add(orgUserDto.getMobileNo());
					}
				}
			}
			if (RECEIPT_ROLE_TRANSFEREE_LICENSEE.equals(role)) {
				List<String> mobiles = IaisEGPHelper.getLicenseeMobiles(applicantId.getNewLicenseeId());
				mobile.addAll(mobiles);
			}
		}
	}

	public InspectionEmailTemplateDto getRecript(List<String> role, String refType, String refId, String moduleType, InspectionEmailTemplateDto inspectionEmailTemplateDto,String recipientUserId) {
		if (RECEIPT_TYPE_APP_GRP.equals(refType)) {
			inspectionEmailTemplateDto = getRecriptAppGrp(role, refId, moduleType, inspectionEmailTemplateDto,recipientUserId);
		} else if (RECEIPT_TYPE_APP.equals(refType)) {
			inspectionEmailTemplateDto = getRecriptApp(role, refId, moduleType, inspectionEmailTemplateDto,recipientUserId);
		} else if (RECEIPT_TYPE_LICENCE_ID.equals(refType)){
			inspectionEmailTemplateDto = getRecriptLic(role, refId, inspectionEmailTemplateDto,recipientUserId);
		} else if (RECEIPT_TYPE_LICENSEE_ID.equals(refType)){
			msgCommonUtil.setRecriptByLicenseeId(refId, inspectionEmailTemplateDto);
		}else {
			inspectionEmailTemplateDto = getOfficer(role, inspectionEmailTemplateDto,recipientUserId);
		}
		return inspectionEmailTemplateDto;
	}





	private InspectionEmailTemplateDto getRecriptLic(List<String> roles, String licenceId, InspectionEmailTemplateDto inspectionEmailTemplateDto,String recipientUserId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		set.addAll(getRecrptPersonnel(roles, licenceId));
		LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(licenceId).getEntity();
		if(licenceDto != null){
			String licenseeId = licenceDto.getLicenseeId();
			if(!StringUtil.isEmpty(licenseeId)) {
				set.addAll(getRecrptLicensee(roles, licenseeId));
			}
		}
		inspectionEmailTemplateDto = getOfficer(roles, inspectionEmailTemplateDto,recipientUserId);
		List<String> receiptEmails = new ArrayList<>(set);
		inspectionEmailTemplateDto.setReceiptEmails(receiptEmails);
		return inspectionEmailTemplateDto;
	}

	private Collection<String> getRecrptLicensee(List<String> roles, String licenseeId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		for (String role : roles) {
			if (RECEIPT_ROLE_LICENSEE.equals(role)) {
				List<String> mails = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
				set.addAll(mails);
			} else if (RECEIPT_ROLE_AUTHORISED_PERSON.equals(role)) {
				List<LicenseeKeyApptPersonDto> pers = msgCommonUtil.getPersonById(licenseeId);
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
														InspectionEmailTemplateDto inspectionEmailTemplateDto,String recipientUserId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpById(appGrpId).getEntity();
		List<ApplicationDto> appList = hcsaAppClient.getAppsByGrpId(appGrpId).getEntity();
		for (ApplicationDto app : appList) {
			inspectionEmailTemplateDto = getAssignedOfficer(roles, app.getApplicationNo(), moduleType, inspectionEmailTemplateDto,recipientUserId);
		}
		set.addAll(getSubmitApplicant(roles, grpDto));
		inspectionEmailTemplateDto = getOfficer(roles, inspectionEmailTemplateDto,recipientUserId);
		List<String> receiptEmails = new ArrayList<>(set);
		inspectionEmailTemplateDto.setReceiptEmails(receiptEmails);
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getRecriptApp(List<String> roles, String appNo, String moduleType, InspectionEmailTemplateDto inspectionEmailTemplateDto,String recipientUserId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		ApplicationGroupDto grpDto = hcsaAppClient.getAppGrpByAppNo(appNo).getEntity();
		set.addAll(getSubmitApplicant(roles, grpDto));
		inspectionEmailTemplateDto = getAssignedOfficer(roles, appNo, moduleType, inspectionEmailTemplateDto,recipientUserId);
		inspectionEmailTemplateDto = getOfficer(roles, inspectionEmailTemplateDto,recipientUserId);
		List<String> receiptEmails = new ArrayList<>(set);
		inspectionEmailTemplateDto.setReceiptEmails(receiptEmails);
		return inspectionEmailTemplateDto;
	}

	private Collection<String> getSubmitApplicant(List<String> roles, ApplicationGroupDto applicantId) {
		Set<String> set = IaisCommonUtils.genNewHashSet();
		for (String role : roles) {
			if (RECEIPT_ROLE_LICENSEE.equals(role)) {
				OrgUserDto orgUserDto = taskOrganizationClient.getUserById(applicantId.getSubmitBy()).getEntity();
				if(orgUserDto != null && !StringUtil.isEmpty(orgUserDto.getEmail())){
					set.add(orgUserDto.getEmail());
				} else {
					log.info("orgUserDto is null, no applicant or no email");
				}
			}else if (RECEIPT_ROLE_TRANSFEREE_LICENSEE.equals(role)) {
				List<String> mails = IaisEGPHelper.getLicenseeEmailAddrs(applicantId.getNewLicenseeId());
				set.addAll(mails);
			}

			/* else if (RECEIPT_ROLE_AUTHORISED_PERSON.equals(role)) {
				List<LicenseeKeyApptPersonDto> pers = licenseeClient.getPersonByid(licenseeId).getEntity();
				if (!IaisCommonUtils.isEmpty(pers)) {
					pers.forEach(p -> {
						if (!StringUtil.isEmpty(p.getEmailAddr())) {
							set.add(p.getEmailAddr());
						}
					});
				}
			}*/
		}

		return set;
	}

	private InspectionEmailTemplateDto getAssignedOfficer(List<String> roles, String appNo, String moduleType, InspectionEmailTemplateDto inspectionEmailTemplateDto,String recipientUserId) {
		if (OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK.equals(moduleType)){
			inspectionEmailTemplateDto = getCurrentTaskAssignedInspector(inspectionEmailTemplateDto, appNo,recipientUserId);
		}else {
			//The default function
			inspectionEmailTemplateDto = getDefaultAssignedOfficer(roles, inspectionEmailTemplateDto, appNo,recipientUserId);
		}
		return inspectionEmailTemplateDto;
	}

	private List<OrgUserDto> receiveOrgUserByTaskInfo(String appNo, List<String> processUrl, List<String> taskStatus){
		Map<String, Object> params = IaisCommonUtils.genNewHashMap();
		params.put("appNo", appNo);
		params.put("processUrl", processUrl);
		params.put("taskStatus", taskStatus);
		if (AppConsts.DOMAIN_INTERNET.equalsIgnoreCase(currentDomain)) {
			HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
			HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
			String gatewayUrl = env.getProperty("iais.inter.gateway.url");
			return IaisEGPHelper.callEicGatewayWithBodyForList(gatewayUrl + "/v1/inspector-by-task", HttpMethod.POST, params,
					MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
					signature2.date(), signature2.authorization(), OrgUserDto.class).getEntity();
		}else {
			return taskOrganizationClient.getCurrentTaskAssignedInspectorInfo(params).getEntity();
		}
	}

	private InspectionEmailTemplateDto getCurrentTaskAssignedInspector(InspectionEmailTemplateDto inspectionEmailTemplateDto, String appNo,String recipientUserId) {
		if (StringUtil.isEmpty(appNo) || inspectionEmailTemplateDto == null) {
			return inspectionEmailTemplateDto;
		}

		String emtpId = inspectionEmailTemplateDto.getEmailTemplateId();
		List<OrgUserDto> orgUserList;
		List<String> processUrl;
		List<String> taskStatus;
		if (MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_EMAIL_INSPECTOR_EMAIL_CHM.equals(emtpId)){
			processUrl = new ArrayList<>(Arrays.asList(TaskConsts.TASK_PROCESS_URL_MAIN_FLOW, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE));
			taskStatus = new ArrayList<>(Arrays.asList(TaskConsts.TASK_STATUS_PENDING, TaskConsts.TASK_STATUS_READ));
			orgUserList = receiveOrgUserByTaskInfo(appNo, processUrl, taskStatus);
		}else {
			processUrl = new ArrayList<>(Arrays.asList(TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION, TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE));
			taskStatus = new ArrayList<>(Arrays.asList(TaskConsts.TASK_STATUS_PENDING, TaskConsts.TASK_STATUS_READ));
			orgUserList = receiveOrgUserByTaskInfo(appNo, processUrl, taskStatus);
		}

		if (IaisCommonUtils.isEmpty(orgUserList)){
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
			if(AppConsts.COMMON_STATUS_ACTIVE.equals(u.getStatus())&&u.getAvailable()) {
				if (!StringUtil.isEmpty(u.getEmail()) && (recipientUserId == null || u.getId().equals(recipientUserId))) {
					officerNameMap.put(String.valueOf(index), u.getDisplayName());
					emailAddressMap.put(String.valueOf(index), u.getEmail());
					index++;
				}
			}
		}

		inspectionEmailTemplateDto.setOfficerNameMap(officerNameMap);
		inspectionEmailTemplateDto.setEmailAddressMap(emailAddressMap);
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getDefaultAssignedOfficer(List<String> roles, InspectionEmailTemplateDto inspectionEmailTemplateDto, String appNo,String recipientUserId) {
		Set<String> userIds = IaisCommonUtils.genNewHashSet();
		List<AppPremisesRoutingHistoryDto> hisList;
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)) {
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
			if(!StringUtil.isEmpty(his.getProcessDecision())||RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN.equals(his.getRoleId())) {
				userMap.get(his.getRoleId()).add(his.getActionby());
			}
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
			} else if (RECEIPT_ROLE_SYSTEM_ADMIN.equals(role) && userMap.get(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN) != null) {
				userIds.addAll(userMap.get(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN));
			}
		}

		if (IaisCommonUtils.isEmpty(userIds)) {
			return inspectionEmailTemplateDto;
		}

		List<OrgUserDto> userList = null;
		if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)) {
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
				if(AppConsts.COMMON_STATUS_ACTIVE.equals(u.getStatus())&&u.getAvailable()) {
					if (!StringUtil.isEmpty(u.getEmail()) && (recipientUserId == null || u.getId().equals(recipientUserId))) {
						officerNameMap.put(index + "", u.getDisplayName());
						emailAddressMap.put(index + "", u.getEmail());
						index++;
					}
				}
			}
			inspectionEmailTemplateDto.setOfficerNameMap(officerNameMap);
			inspectionEmailTemplateDto.setEmailAddressMap(emailAddressMap);
		}
		return inspectionEmailTemplateDto;
	}

	private InspectionEmailTemplateDto getOfficer(List<String> roles, InspectionEmailTemplateDto inspectionEmailTemplateDto,String recipientUserId) {
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
			if (AppConsts.DOMAIN_INTRANET.equalsIgnoreCase(currentDomain)) {
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
					if(AppConsts.COMMON_STATUS_ACTIVE.equals(u.getStatus())&&u.getAvailable()) {
						if (!StringUtil.isEmpty(u.getEmail()) && (recipientUserId == null || u.getId().equals(recipientUserId))) {
							officerNameMap.put(index + "", u.getDisplayName());
							emailAddressMap.put(index + "", u.getEmail());
							index++;
						}
					}
				}
				inspectionEmailTemplateDto.setOfficerNameMap(officerNameMap);
				inspectionEmailTemplateDto.setEmailAddressMap(emailAddressMap);
			}
		}

		return inspectionEmailTemplateDto;
	}
}
