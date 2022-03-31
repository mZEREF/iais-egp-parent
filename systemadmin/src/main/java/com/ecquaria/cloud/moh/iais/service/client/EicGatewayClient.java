package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

/**
 * @author: yichen
 * @date time:2/25/2020 1:08 PM
 * @description:
 */
@Component
public class EicGatewayClient {
	@Value("${iais.intra.gateway.url}")
	private String gateWayUrl;

	@Value("${iais.hmac.keyId}")
	private String keyId;

	@Value("${iais.hmac.second.keyId}")
	private String secKeyId;

	@Value("${iais.hmac.secretKey}")
	private String secretKey;

	@Value("${iais.hmac.second.secretKey}")
	private String secSecretKey;

	@Autowired
	private EicRequestTrackingHelper requestTrackingHelper;

	@Value("${spring.application.name}")
	private String currentApp;

	@Value("${iais.current.domain}")
	private String currentDomain;

	public <T, R> FeignResponseEntity<R> callEicWithTrack(T obj, String clientMethod) {
		int client = EicClientConstant.SYSTEM_ADMIN_CLIENT;
		return callEicWithTrack(obj, clientMethod, client);
	}

	public <T, R> FeignResponseEntity<R> callEicWithTrack(T obj, String clientMethod, int client) {
		EicRequestTrackingDto track = requestTrackingHelper.clientSaveEicRequestTracking(client,
				this.getClass().getName(), clientMethod, currentApp + "-" + currentDomain,
				obj.getClass().getName(), JsonUtil.parseToJson(obj));
		track.setProcessNum(track.getProcessNum() + 1);
		Date now = new Date();
		track.setFirstActionAt(now);
		track.setLastActionAt(now);

		FeignResponseEntity<R> invoke = null;
		try {
			Method method = this.getClass().getMethod(clientMethod, obj.getClass());
			invoke = (FeignResponseEntity<R>) method.invoke(obj, this);
			track.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
		} catch (Exception e) {
			track.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
		}
		requestTrackingHelper.saveEicTrack(client, track);
		return invoke;
	}

	public <T, R> FeignResponseEntity<R> callEicWithTrack(List<T> objs, String actionMethod, Class<?> jsonClass, String jsonMethod) {
		int client = EicClientConstant.SYSTEM_ADMIN_CLIENT;
		EicRequestTrackingDto track = requestTrackingHelper.clientSaveEicRequestTracking(client,
				jsonClass.getName(), jsonMethod, currentApp + "-" + currentDomain,
				String.class.getName(), JsonUtil.parseToJson(objs));
		track.setProcessNum(track.getProcessNum() + 1);
		Date now = new Date();
		track.setFirstActionAt(now);
		track.setLastActionAt(now);

		FeignResponseEntity<R> invoke = null;
		try {
			Method method = this.getClass().getMethod(actionMethod, List.class);
			invoke = (FeignResponseEntity<R>) method.invoke(objs, this);
			track.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
		} catch (Exception e) {
			track.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
		}
		requestTrackingHelper.saveEicTrack(client, track);
		return invoke;
	}

	public FeignResponseEntity<String> saveSystemParameterFe(SystemParameterDto systemParameterDto) {
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/sys-params", HttpMethod.PUT, systemParameterDto,
				MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization(), String.class);
	}

	public FeignResponseEntity<MessageDto> syncMessageToFe(MessageDto messageDto, String date, String authorization,
												  String dateSec, String authorizationSec) {
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/message-configs", HttpMethod.POST, messageDto,
				MediaType.APPLICATION_JSON, date, authorization,
				dateSec, authorizationSec, MessageDto.class);
	}

	public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
																 String date, String authorization, String dateSec, String authorizationSec) {
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interInboxDto,
				MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InterMessageDto.class);
	}

	public FeignResponseEntity<Void> syncMasterCodeFe(List<MasterCodeDto> masterCodeDtoList) {
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/mastercode-sync", HttpMethod.POST, masterCodeDtoList,
				MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization(), Void.class);
	}

	public FeignResponseEntity<Void> syncTemplateFe(MsgTemplateDto msgTemplateDto) {
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/message-template", HttpMethod.PUT, msgTemplateDto,
				MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization(), Void.class);
	}

	public FeignResponseEntity<Void> syncFeUser(FeUserDto feUserDto) {
		HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
		HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
		return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/licensee-user-sync", HttpMethod.POST, feUserDto,
				MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
				signature2.date(), signature2.authorization(), Void.class);
	}

}
